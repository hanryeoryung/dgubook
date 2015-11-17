package project.dgubook.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import project.dgubook.R;
import project.dgubook.SplashActivity;
import project.dgubook.join.JoinActivity;

public class MainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {        


        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SplashActivity.class));
        setContentView(R.layout.main_login);

        Button loginBtn ,joinBtn;
        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 유효성 검사 추가
                AlertDialog dialog = loginDialogBox(); //로그인되었습니다 alert
                dialog.show();
            }
        });
        joinBtn = (Button)findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                toJoin();
            }
        });

    }


    private AlertDialog loginDialogBox() { //alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("알림");
        builder.setMessage("로그인 되었습니다!");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //로그인 후 추천창으로 이동
                toHome();
            }
        });

        AlertDialog dialog = builder.create();

        return dialog;

    }

    private void toHome() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, MainHomeAtivity.class.getCanonicalName());
        startActivity(intent);
    }
    private void toJoin() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, JoinActivity.class.getCanonicalName());
        startActivity(intent);
    }


}
