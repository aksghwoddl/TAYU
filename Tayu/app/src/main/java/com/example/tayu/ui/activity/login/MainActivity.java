/*
package com.example.tayu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //define view objects
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignup;
    Button textviewSingin;
    TextView textviewMessage;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;
    Button Google_Login;
    private static final int RC_SIGN_IN = 100;
    private GoogleApiClient mGoogleApiClient;

    public static class User {
        public String user_password;
        public String user_club;
        public String user_id;
        public Integer hour;
        public Integer min;
        public Integer sec;

        public float user_distance;

        public User( String id ,String password , String club , Integer hour , Integer min , Integer sec , float user_distance){
            this.user_id = id;
            this.user_password = password;
            this.user_club = club;
            this.hour = hour;
            this.min = min;
            this.sec = sec;
            this.user_distance = user_distance;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //구글로그인 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        //여기까지

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class)); //추가해 줄 ProfileActivity
        }
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textviewSingin= (Button) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        Google_Login = (Button) findViewById(R.id.Google_Login);
        progressDialog = new ProgressDialog(this);

        //button click event
        Google_Login.setOnClickListener(this);
        buttonSignup.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);
    }

    //Firebse creating a new user
    private void registerUser(){
        //사용자가 입력하는 email, password를 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        //email과 password가 비었는지 아닌지를 체크 한다.
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }

        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {




                        if(task.isSuccessful()){
                            finish();
                            writeNewUser(editTextEmail.getText().toString() , editTextPassword.getText().toString() , " " , 0 , 0 , 0 , 0);
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            //에러발생시
                            textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                            Toast.makeText(MainActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    //button click event
    @Override
    public void onClick(View view) {
        if(view == buttonSignup) {
            //TODO
            registerUser();
        }

        if(view == textviewSingin) {
            //TODO
            startActivity(new Intent(this, LoginActivity.class)); //추가해 줄 로그인 액티비티
        }

        if(view == Google_Login){
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent,RC_SIGN_IN);
        }
    }

    //구글 연동 코드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else{
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "구글 로그인 인증 성공", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                    }
                });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void writeNewUser(String Id, String password , String club , Integer hour , Integer min , Integer sec , float distance){
        User user = new User(Id , password , club , 0 , 0 , 0 , 0);
        DatabaseReference mDatabase;
        String uid = FirebaseAuth.getInstance().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(uid).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"회원가입이 완료 되었습니다!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"실패했습니다 다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //여기까지

}


*/
