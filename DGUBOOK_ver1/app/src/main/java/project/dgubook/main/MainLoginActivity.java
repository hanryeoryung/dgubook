package project.dgubook.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import project.dgubook.R;
import project.dgubook.SplashActivity;
import project.dgubook.join.JoinActivity;

public class MainLoginActivity extends AppCompatActivity {
    EditText email, pass;
    String uEmail, uPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SplashActivity.class));
        setContentView(R.layout.main_login);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);

        Button loginBtn ,joinBtn;
        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 유효성 검사 추가
                if(loginVerify() == 0){
                    AlertDialog dialog = loginDialogBox(0); //로그인되었습니다 alert
                    dialog.show();
                }else if(loginVerify() == -1){//연결실패
                    AlertDialog dialog = loginDialogBox(-1); //로그인되었습니다 alert
                    dialog.show();
                }
                else if(loginVerify() == -2){//디비에러
                    AlertDialog dialog = loginDialogBox(-1); //로그인되었습니다 alert
                    dialog.show();
                }
                else if(loginVerify() == -3){
                    //아이디 틀린메세지 확인
                    AlertDialog dialog = loginDialogBox(-1); //로그인되었습니다 alert
                    dialog.show();
                }
                else if(loginVerify() == -4){
                    //비번 틀린메세지 확인
                    AlertDialog dialog = loginDialogBox(-1); //로그인되었습니다 alert
                    dialog.show();
                }
            }
        });
        joinBtn = (Button)findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                toJoin();
            }
        });

    }

    private int loginVerify() {
        uEmail = email.getText().toString();
        uPassword = pass.getText().toString();
        String b_tmp = "";
        try {

            URL mUrl = new URL("http://210.94.222.136/dgubook_loginverify.php?id=" + URLEncoder.encode(uEmail, "UTF-8")  + "&pass=" + URLEncoder.encode(uPassword, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            // conn.connect();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                // 연결되었음 코드가 리턴되면.
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "EUC-KR"));
                    for (; ; ) {
                        // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                        String line = br.readLine();
                        if (line == null) break;
                        b_tmp += line;
                        // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                        //jsonHtml.append(line + "\n");
                    }
                    br.close();
                    // join_back_btn.setText(b_tmp);
                    Toast.makeText(MainLoginActivity.this, b_tmp, Toast.LENGTH_SHORT).show();
                }

            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
        if(b_tmp.equals("No Such User Found")){
            return -3;
        }else if(b_tmp.equals("false pass")){
            return -4;
        }
        return 0;
    }


    private AlertDialog loginDialogBox(int flag) { //alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("알림");

        if(flag == 0){
            builder.setMessage("로그인 되었습니다!");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    toHome();
                }
            });
        }else if(flag == -1){ //연결실패
            builder.setMessage("네트워크 문제로 로그인 실패 하였습니다.!");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }else if(flag == -2){ //디비에러
            builder.setMessage("일시적인 에러가 발생하였습니다.!");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }else if(flag == -3){ //아이디 틀렸을때
            builder.setMessage("없는 이메일 입니다.!");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }else if(flag == -4){ //비번 틀렸을때
            builder.setMessage("비밀번호가 틀렸습니다. 다시 확인해 주세요!");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }

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
