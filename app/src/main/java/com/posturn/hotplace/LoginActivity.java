package com.posturn.hotplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login, btn_register;
    private ArrayList<ObjectUser> objectUsers = new ArrayList<ObjectUser>();
    private ObjectUser objectUser;

    final FirebaseFirestore user_db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.login_main );

        et_id = findViewById( R.id.et_login_id );
        et_pass = findViewById( R.id.et_login_pass );

        btn_register = findViewById( R.id.btn_register );
        btn_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LoginActivity.this, LoginRegisterActivity.class );
                startActivity( intent );
            }
        });


        btn_login = findViewById( R.id.btn_login );
        btn_login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                user_db.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map map = document.getData();
                                if (userID.equals(map.get("id").toString()) && userPass.equals(map.get("password").toString())) {
                                    objectUser = new ObjectUser();
                                    objectUser.id = map.get("id").toString();
                                    objectUser.password = map.get("password").toString();
                                    objectUser.name = map.get("name").toString();
                                    objectUser.age = map.get("age").toString();
                                    objectUser.img = map.get("img").toString();

                                    Toast.makeText( getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT ).show();
                                    Intent intent = new Intent( LoginActivity.this, MainPageActivity.class );
                                    intent.putExtra("userName", objectUser.getName());
                                    intent.putExtra("userImg",objectUser.getImg());
                                    LoginActivity.this.startActivity(intent);
                                }else{
                                    Toast.makeText( getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT ).show();
                                    return;
                                }
                            }
                        }
                    }
                });


            }
        });
    }

}
