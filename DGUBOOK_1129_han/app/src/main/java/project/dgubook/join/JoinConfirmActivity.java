package project.dgubook.join;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import project.dgubook.R;
import project.dgubook.main.MainLoginActivity;
import project.dgubook.push.QuickstartPreferences;
import project.dgubook.push.RegistrationIntentService;

/**
 * Created by 지수 on 2015-11-05.
 */
public class JoinConfirmActivity extends AppCompatActivity {
    int confirm_num;
    EditText confirm_code;
    String uEmail, uName, uStudent, uPhone, uPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_confirm);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setValue();
        confirm_code = (EditText) findViewById(R.id.confirm_code);

        Button confirmBtn = (Button) findViewById(R.id.confirm_Btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinConfirmActivity.this);
                //Toast.makeText(JoinConfirmActivity.this, confirm_code.getText().toString(), Toast.LENGTH_SHORT).show();

                int code = Integer.valueOf(confirm_code.getText().toString());
                if (code == confirm_num) {
                    if (toAddUser() == 0) {
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.setMessage("회원가입 완료했습니다.").show();
                        toLogin();
                    } else {
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.setMessage("회원가입에 실패했습니다.(DB)").show();
                    }
                } else {
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.setMessage("인증코드가 정확하지 않습니다.").show();
                }

            }
        });
    }



    private void toLogin() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName(JoinConfirmActivity.this, MainLoginActivity.class.getCanonicalName());
        startActivity(intent);
        this.finish();
    }

    private void setValue() {
        Intent intent = getIntent();
        if (intent.hasExtra("email")) {

            uEmail = intent.getStringExtra("email");
//            Toast.makeText(CosmeticItemDetailActivity.this, Integer.toString(itemNum), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JoinConfirmActivity.this, "1데이터를 받아오지 못했습니다!", Toast.LENGTH_SHORT).show();
        }
        if (intent.hasExtra("name")) {

            uName = intent.getStringExtra("name");
//            Toast.makeText(CosmeticItemDetailActivity.this, Integer.toString(itemNum), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JoinConfirmActivity.this, "1데이터를 받아오지 못했습니다!", Toast.LENGTH_SHORT).show();
        }
        if (intent.hasExtra("s_id")) {

            uStudent = intent.getStringExtra("s_id");
//            Toast.makeText(CosmeticItemDetailActivity.this, Integer.toString(itemNum), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JoinConfirmActivity.this, "1데이터를 받아오지 못했습니다!", Toast.LENGTH_SHORT).show();
        }
        if (intent.hasExtra("phone")) {

            uPhone = intent.getStringExtra("phone");
//            Toast.makeText(CosmeticItemDetailActivity.this, Integer.toString(itemNum), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JoinConfirmActivity.this, "1데이터를 받아오지 못했습니다!", Toast.LENGTH_SHORT).show();
        }
        if (intent.hasExtra("email")) {

            uPassword = intent.getStringExtra("pass");
//            Toast.makeText(CosmeticItemDetailActivity.this, Integer.toString(itemNum), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JoinConfirmActivity.this, "1데이터를 받아오지 못했습니다!", Toast.LENGTH_SHORT).show();
        }

        if (intent.hasExtra("code")) {

            confirm_num = intent.getIntExtra("code", 0);
           Toast.makeText(JoinConfirmActivity.this, Integer.toString(confirm_num), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JoinConfirmActivity.this, "1데이터를 받아오지 못했습니다!", Toast.LENGTH_SHORT).show();
        }
    }

    private int toAddUser() {
        String tmp = "tmpData";
        try {

            URL mUrl = new URL("http://210.94.222.136/dgubook_join.php?email=" + URLEncoder.encode(uEmail, "UTF-8") + "&name=" +  URLEncoder.encode(uName, "EUC-KR") +
                    "&student_num=" + uStudent + "&phone=" + uPhone + "&password=" + URLEncoder.encode(uPassword, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            // conn.connect();
            if (conn != null) {

                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                // 연결되었음 코드가 리턴되면.
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String b_tmp = "";
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
                    Toast.makeText(JoinConfirmActivity.this, b_tmp, Toast.LENGTH_SHORT).show();
                }

            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
        return 0;
    }

}