package com.posturn.hotplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRegisterActivity extends AppCompatActivity {
    private EditText et_id, et_pass, et_name, et_age;
    private Button btn_register;

    private ObjectUser objectUser;

    final FirebaseFirestore user_r_db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.login_register_main );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("회원가입");



        //아이디값 찾아주기
        et_id = findViewById( R.id.et_regis_id );
        et_pass = findViewById( R.id.et_regis_pass );
        et_name = findViewById( R.id.et_regis_name );
        et_age = findViewById( R.id.et_regis_age );


        //회원가입 버튼 클릭 시 수행
        btn_register = findViewById( R.id.btn_register_register );
        btn_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                String userAge = et_age.getText().toString();

                objectUser = new ObjectUser();
                objectUser.setId(userID);
                objectUser.setPassword(userPass);
                objectUser.setName(userName);
                objectUser.setAge(userAge);
                objectUser.setImg("https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/user_img%2Fuser_male.png?alt=media&token=1b8290f1-97cb-4de4-aab8-f2d0e515281a");

                user_r_db.collection("User").document(objectUser.getId()).set(objectUser);

                Toast.makeText( getApplicationContext(), "성공", Toast.LENGTH_SHORT ).show();
                Intent intent = new Intent( LoginRegisterActivity.this, LoginActivity.class );
                startActivity( intent );

            }
        });
    }
}
