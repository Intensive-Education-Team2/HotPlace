package com.posturn.hotplace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;

public class PostingActivity extends AppCompatActivity implements View.OnClickListener {
    private final int GET_GALLERY_IMAGE = 200;
    private SharedPreferences pref;

    private String userimg;
    private String username;
    private String place;
    private Timestamp date;
    private String img;
    private String content;
    private int hot;
    private List hotuser;

    private Button selectPlace;
    private ImageButton selectImg;
    private EditText editContent;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;
    private Uri selectedImageUri;

    private int allpostNum;
    private int placepostNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        storage = FirebaseStorage.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("게시글 작성");

        selectPlace = findViewById(R.id.btn_place);
        selectImg = findViewById(R.id.btn_image);
        editContent = findViewById(R.id.edittext_content);

        selectPlace.setOnClickListener(this);
        selectImg.setOnClickListener(this);

        pref = getSharedPreferences("profile", MODE_PRIVATE);
    }

    //툴바 테마
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_board_menu, menu);
        return true;
    }

    //툴바 기능
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기
                onBackPressed();
                return true;
            }
            case R.id.write: {//등록버튼
                posting();
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setResult(2);
                        finish();

                    }
                }, 4000);
                return false;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_place:
                intent = new Intent(getApplicationContext(), FilterPageActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_image:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) { // 정상 반환일 경우에만 동작하겠다
            String btnText = data.getExtras().getString("placeName");
            selectPlace.setText(btnText);
            selectPlace.setTextSize(16);
            selectPlace.setTextColor(Color.parseColor("#ffffff"));
            selectPlace.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_select_radius));
            //selectPlace.setBackgroundColor(Color.parseColor("#EE6C81"));
            placepostNumber();
        }

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            selectImg.setImageURI(selectedImageUri);
            selectImg.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }
    }

    public void posting() {
        if (selectPlace.getText().equals("Place")) {
            //위치태그를 선택해주세요
        } else if (selectedImageUri == null) {
            //이미지를 선택해주세요
        } else {
            uploadImg(getPath(selectedImageUri));
        }
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    public void uploadImg(String uri) {
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://hotplaceserver.appspot.com");
        Uri file = Uri.fromFile(new File(uri));

        StorageReference riversRef = storageRef.child("images/" + allpostNumber());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            uploadAllPost(downloadUri.toString(), allpostNum);
                            uploadPlacePost(downloadUri.toString(), placepostNumber());
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });
    }

    public int allpostNumber(){
        db.collection("AllPost")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                allpostNum=document.getData().size();
                                Log.d("size", String.valueOf(allpostNum));
                            }
                        } else {
                            Log.d("failure", "failure");
                        }
                    }
                });

        return allpostNum;
    };

    public int placepostNumber(){
        DocumentReference docRef = db.collection("PlacePost").document(selectPlace.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        placepostNum=document.getData().size();
                        Log.v("size", String.valueOf(placepostNum));
                    } else {
                    }
                } else {
                }
            }
        });
        return placepostNum;
    };

    public void uploadAllPost(String imgurl, int postnum){
        ObjectPost objectPost;
        Timestamp ts = new Timestamp(new Date());
        List hotuser = Arrays.asList("");

        objectPost=new ObjectPost(pref.getString("userimg", ""),
                pref.getString("name", ""),
                selectPlace.getText().toString(),
                ts,
                imgurl,
                editContent.getText().toString(),
                0,
                hotuser);

        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put(Integer.toString(postnum), objectPost);
        db.collection("AllPost").document("post").update(nestedData);
    }

    public void uploadPlacePost(String imgurl, int postnum){
        ObjectPost objectPost;
        Timestamp ts = new Timestamp(new Date());
        List hotuser = Arrays.asList("");

        objectPost = new ObjectPost(pref.getString("userimg", ""),
                pref.getString("name", ""),
                selectPlace.getText().toString(),
                ts,
                imgurl,
                editContent.getText().toString(),
                0,
                hotuser);

        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put(Integer.toString(postnum), objectPost);
        db.collection("PlacePost").document(selectPlace.getText().toString()).update(nestedData);
    }
}
