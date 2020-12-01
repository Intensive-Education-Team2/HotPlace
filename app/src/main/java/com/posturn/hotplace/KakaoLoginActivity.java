package com.posturn.hotplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.Session;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.AgeRange;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KakaoLoginActivity extends AppCompatActivity {

    public static Context context;
    private Button kakaologin;
    public Long token;
    public String username;
    public String userimg;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);

        context=this;

        KakaoSdk.init(context, String.valueOf(R.string.appkey));

        getAppKeyHash();

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        getAppKeyHash();
        loadShared();

        kakaologin = findViewById(R.id.button_kakao_login);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(1000);
                kakaologin.setVisibility(Button.VISIBLE);
                kakaologin.setAnimation(animation);
            }
        }, 2000);




        kakaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.open(AuthType.KAKAO_LOGIN_ALL, KakaoLoginActivity.this);
            }
        });
    }





    //카카오 디벨로퍼에서 사용할 키값을 로그를 통해 알아낼 수 있다. (로그로 본 키 값을을 카카오 디벨로퍼에 등록해주면 된다.)
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }



    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
            //redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_kakao_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }


    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.getInstance()
                .me(new MeV2ResponseCallback() {

                    @Override
                    public void onSuccess(MeV2Response result) {
                        long userID = result.getId();
                        UserAccount kakaoAccount = result.getKakaoAccount();
                        userimg = result.getThumbnailImagePath();
                        username = result.getNickname();

                        Map<String, String> user = new HashMap<>();

                        user.put("token", Long.toString(userID));
                        user.put("name", username);
                        user.put("userimg", userimg);

                        db.collection("User")
                                .document(Long.toString(userID))
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        saveShared(userID, username, userimg);
                                    }
                                });

                        if (kakaoAccount == null) {
                            Toast.makeText(context, "아이디 정보를 제공해주셔야 합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        redirectHomeActivity(); // 로그인 성공시 MainActivity로

                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        redirectLoginActivity();
                    }
                });
    }

    private void redirectHomeActivity() {
        //Intent intent = new Intent(this, LoadingPageActivity.class);
        //startActivity(intent);
        startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, KakaoLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void logout(){
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        deleteShared();
                    }
                });
    }



    /*쉐어드에 입력값 저장*/
    private void saveShared(long id, String username, String userimg) {
        super.onStop();
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("token", id);
        editor.putString("name", username);
        editor.putString("userimg", userimg);
        editor.commit();
    }

    /*쉐어드값 불러오기*/
    private void loadShared() {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        token = pref.getLong("token", 0);
        username = pref.getString("name", "");
        userimg = pref.getString("userimg", "");
    }

    private void deleteShared(){
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}