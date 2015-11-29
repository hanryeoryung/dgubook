package project.dgubook.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private static final int MSG_LOGINOK = 0;
    private static final int MSG_INTERNETERROR = -1;
    private static final int MSG_DBERROR = -2;
    private static final int MSG_IDFALSE = -3;
    private static final int MSG_PASSFALSE = -4;
    EditText email, pass;
    String uEmail, uPassword;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SplashActivity.class));
        setContentView(R.layout.main_login);
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        CheckBox auto_login_cb = (CheckBox)findViewById(R.id.auto_login);

        //shared preference를 불러옴 (자동로그인 사용을 위해)
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);

        //저장된 값 불러오기 (자동로그인을 위해 아이디와 비밀번호 자동로그인 체크박스를 불러옴)
        //a_가 붇은 변수들은 자동로그인을 위한 변수
        String a_user_id = pref.getString("editText", "");
        String a_user_pw = pref.getString("editText2", "");
        Boolean a_login_cb = pref.getBoolean("check1",false);
        //저장값 불러오기
        auto_login_cb.setChecked(a_login_cb);
        if(auto_login_cb.isChecked()){
            email.setText(a_user_id);
            pass.setText(a_user_pw);
            loginVerify();
        }
        Button loginBtn ,joinBtn;
        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 유효성 검사 추가
                loginVerify();
            }
        });
        joinBtn = (Button)findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                toJoin();
            }
        });

    }
    //자동로그인 기능 구현을 위한 함수---------------------------------------------------------------
    public void onStop(){ //어플이 화면에서 사라질때
        super.onStop();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        //ui상태 저장
        SharedPreferences.Editor editor = pref.edit();
        //editor를 불러옴
        EditText a_user_id = (EditText)findViewById(R.id.email);
        EditText a_user_pw = (EditText)findViewById(R.id.password);
        CheckBox a_login_cb = (CheckBox)findViewById(R.id.auto_login);
        //저장할 값 입력
        editor.putString("editText",a_user_id.getText().toString());
        editor.putString("editText2", a_user_pw.getText().toString());
        editor.putBoolean("check1",a_login_cb.isChecked());

        editor.commit(); //저장
    }
    private int loginVerify() {
        uEmail = email.getText().toString();
        uPassword = pass.getText().toString();
        String b_tmp = "";
        try {
            URL mUrl = new URL("http://210.94.222.136/dgubook_loginverify.php?id='" + URLEncoder.encode(uEmail, "UTF-8") +
                    "'&pass=" + uPassword);//URLEncoder.encode(uPassword, "UTF-8"));
//                    URL mUrl= new URL("http://210.94.222.136/dgubook_loginverify.php?id='wbwlwb@gmail.com'&pass=ccv");
//                    Toast.makeText(MainLoginActivity.this, URLEncoder.encode(uPassword, "UTF-8"), Toast.LENGTH_SHORT).show();
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
                    }
                    br.close();
                    // join_back_btn.setText(b_tmp);
                    Toast.makeText(MainLoginActivity.this, b_tmp, Toast.LENGTH_SHORT).show();
                }
            } else {
                // return -1;

                handler.sendMessage(handler.obtainMessage(MSG_INTERNETERROR));
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
//                    return -2;
            handler.sendMessage(handler.obtainMessage(MSG_DBERROR));
            return 0;
        }
        if (b_tmp.equals("No Such User Found")) {
//                    return -3;
            handler.sendMessage(handler.obtainMessage(MSG_IDFALSE));
            return 0;
        }
        if (b_tmp.equals("false pass")) {
//                    return -4;
            handler.sendMessage(handler.obtainMessage(MSG_PASSFALSE));
            return 0;
        }//                return 0;
        handler.sendMessage(handler.obtainMessage(MSG_LOGINOK));
        return 0;
    }
    public Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_LOGINOK:
                    dialog = loginDialogBox(0);
                    dialog.show();
                    break;
                case MSG_INTERNETERROR:
                    dialog = loginDialogBox(-1);
                    dialog.show();
                    break;
                case MSG_DBERROR:
                    dialog = loginDialogBox(-2);
                    dialog.show();
                    break;
                case MSG_IDFALSE:
                    //아이디 틀린메세지 확인
                    dialog = loginDialogBox(-3);
                    dialog.show();
                    break;
                case MSG_PASSFALSE:
                    //비번 틀린메세지 확인
                    dialog = loginDialogBox(-4);
                    dialog.show();
                    break;
            }
        }
    };

    private AlertDialog loginDialogBox(int flag) { //alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        if(flag == 0){
            builder.setMessage("로그인 되었습니다!");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    toHome();
                    dialog.cancel();
                }
            });
        }else if(flag == -1){ //연결실패
            builder.setMessage("네트워크 문제로 로그인 실패 하였습니다.!");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.cancel();
                }
            });
        }else if(flag == -2){ //디비에러
            builder.setMessage("일시적인 에러가 발생하였습니다.!");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.cancel();
                }
            });
        }else if(flag == -3){ //아이디 틀렸을때
            builder.setMessage("없는 이메일 입니다.!");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.cancel();
                }
            });
        }else if(flag == -4){ //비번 틀렸을때
            builder.setMessage("비밀번호가 틀렸습니다. 다시 확인해 주세요!");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.cancel();
                }
            });
        }

        AlertDialog dialog = builder.create();

        return dialog;

    }
    private void toHome() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, MainHomeActivity.class.getCanonicalName());
        startActivity(intent);
        this.finish();
    }
    private void toJoin() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, JoinActivity.class.getCanonicalName());
        startActivity(intent);
    }


}
