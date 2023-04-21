/*
package com.example.tayu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tayu.augmentedimage.AugmentedImageActivity;
import com.example.tayu.augmentedimage.rendering.AugmentedImageRenderer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ProfileActivity";

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail , userDistView , userTimeView;
    private Button buttonLogout;
    private Button textivewDelete;
    private Button buttonMain;
    private Button testClub;
    private Button ar;
    private String uid = FirebaseAuth.getInstance().getUid();
    private int dist , timeH , timeM , timeS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        ar = (Button) findViewById(R.id.ar);
        buttonMain = (Button) findViewById(R.id.buttonMain);
        textivewDelete = (Button) findViewById(R.id.textviewDelete);
        testClub=(Button)findViewById(R.id.testClub);
        userDistView = findViewById(R.id.user_dist_view);
        userTimeView = findViewById(R.id.user_time_view);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //textViewUserEmail의 내용을 변경해 준다.
        textViewUserEmail.setText("반갑습니다. "+ user.getEmail()+"님!");

        //logout button event
        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
        buttonMain.setOnClickListener(this);
        testClub.setOnClickListener(this);
        ar.setOnClickListener(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef2 = database.getReference();

        dataRef2.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               dist = dataSnapshot.child("user_distance").getValue(Integer.class);
               timeH = dataSnapshot.child("hour").getValue(Integer.class);
               timeM = dataSnapshot.child("min").getValue(Integer.class);
               timeS = dataSnapshot.child("sec").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String convert_dist , convert_time = null;

        if(dist < 1000){
            convert_dist = dist + "m";
        }
        else
            convert_dist = dist + "Km";

        userDistView.setText("당신이 달린 거리는 " + convert_dist + " 입니다.");

        if(timeS < 60 && timeM == 0 && timeH == 0){
            convert_time = timeS + "초";
        }
        else if(timeM < 60 && timeH == 0){
            convert_time = timeM + "분" + timeS + "초입니다.";
        }

        else if(timeH > 1){
            convert_time = timeH + "시간" + timeM + "분" + timeS + "초";
        }

        userTimeView.setText("당신이 달린 시간은 " + convert_time + " 입니다.");
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        //회원탈퇴를 클릭하면 회원정보를 삭제한다. 삭제전에 컨펌창을 하나 띄워야 겠다.
        if(view == textivewDelete) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ProfileActivity.this);
            alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProfileActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }
                                    });
                        }
                    }
            );
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(ProfileActivity.this, "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        }
        if(view == buttonMain)
        {
            startActivity(new Intent(this,HomeActivity.class));
        }
        if(view==testClub){
            startActivity(new Intent(this,ClubActivity.class));
        }
        if(view==ar){
            startActivity(new Intent(this, AugmentedImageActivity.class));
        }
    }
}

*/
