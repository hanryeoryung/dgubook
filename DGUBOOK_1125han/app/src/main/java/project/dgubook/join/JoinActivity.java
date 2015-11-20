package project.dgubook.join;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import project.dgubook.R;
import project.dgubook.SplashActivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class JoinActivity extends AppCompatActivity {
    Editable mail;
    String body;
    Random confirm_;
    int confirm_num;
    GMailSender sender;
    ProgressDialog dialog;
    EditText email, name, student_id, password1, password2, confirm_code;
    String uEmail, uName, uStudent, uPassword;
    int email_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //조인 페이지 기입 내용들

        email = (EditText)findViewById(R.id.join_email_id);
        name = (EditText)findViewById(R.id.join_user_name);
        student_id = (EditText)findViewById(R.id.join_user_student_id);
        password1 = (EditText)findViewById(R.id.join_password_1);
        password2 = (EditText)findViewById(R.id.join_password_2);

        Button join_and_confirm;  //마지막 가입하기 버튼 가입하기 버튼을 누르면 인증메일 발송 후 최종 가입결정
        join_and_confirm = (Button)findViewById(R.id.join_joinBtn);
        join_and_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_flag == 1) {
                    if (!email.getText().toString().equals("") && !student_id.getText().toString().equals("") && !name.getText().toString().equals("") && !password1.getText().toString().equals("") && !password2.getText().toString().equals("")) {
                        //빈칸없는지 확인
                        boolean contains = email.getText().toString().contains("@dongguk.edu");
                        if (contains) { //동국웹메일인지 확인
                            if (password1.getText().toString().equals(password2.getText().toString())) {
                                mail = email.getText();
                                uEmail = email.getText().toString();
                                uName = name.getText().toString();
                                uStudent = student_id.getText().toString();
                                uPassword = password1.getText().toString();
//                            AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                if (toConfirmCodeSend() == 0) { //인증 코드 메일 발송

                                } else {
//                                builder.setMessage("오류가 있어 회원가입에 실패했습니다.(code)").show();
                                    Toast.makeText(JoinActivity.this, "오류가 있어 회원가입에 실패했습니다.(code)", Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
//                            builder.setMessage("비밀번호가 일치하지 않습니다").show();
                                Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
//                        builder.setMessage("동국대학교 웹메일을 사용해주세요!").show();
                            Toast.makeText(JoinActivity.this, "동국대학교 웹메일을 사용해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
//                    builder.setMessage("빈칸없이 기입해 주세요!").show();
                        Toast.makeText(JoinActivity.this, "빈칸없이 기입해 주세요!", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(JoinActivity.this, "이메일 중복체크를 해주십시오", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button email_duplication_btn;
        email_duplication_btn = (Button)findViewById(R.id.email_duplication); //이메일 중복확인 버튼
        email_duplication_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(JoinActivity.this);
                mbuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {            }    });
                if(emailDuplicationCheck(email.getText().toString()) == -3){
                    mbuilder.setMessage("이미 가입된 이메일입니다.").show();
                    email.setText("");
                }else {
                    mbuilder.setMessage("사용 가능한 이메일입니다.").show();
                    email_flag = 1;
                }
            }
        });


    }

    private int emailDuplicationCheck(String mail) {
        String b_tmp = "";
        try {
            URL mUrl = new URL("http://210.94.222.136/dgubook_emailverify.php?id='" + URLEncoder.encode(mail, "UTF-8")+"'");

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
//                    Toast.makeText(JoinActivity.this, b_tmp, Toast.LENGTH_SHORT).show();
                }
            } else {
                 return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
                    return -2;
        }
        if(b_tmp.equals("no")) {
            return -3;
        }
        return 0;
    }


    private void toConfirm() {
        Intent tmp = new Intent(Intent.ACTION_VIEW);
        tmp.setClassName(JoinActivity.this, JoinConfirmActivity.class.getName());
        tmp.putExtra("email", uEmail);
        tmp.putExtra("name", uName);
        tmp.putExtra("s_id", uStudent);
        tmp.putExtra("pass", uPassword);
        tmp.putExtra("code", confirm_num);
        startActivity(tmp);
    }

    private int toConfirmCodeSend() {
        confirm_num = getRandom(100000,10000);
        body = "\t\t\t\t\t\t 인증번호는 아래와 같습니다.\n" +
                "\t\t\t\t\t\t 회원가입 요청에 의해 발송된 이메일입니다.\n" +
                "\t\t\t\t\t\t* 인증번호 : "+String.valueOf(confirm_num)+"\n" +
                "\t\t\t\t\t\t 회원가입 시 이용하세요.";
//        Intent msIntent = new Intent(Intent.ACTION_SEND);
//        msIntent.putExtra(Intent.EXTRA_SUBJECT, "DGUBOOK에서 인증 요청 메일 보냅니다.");
//        msIntent.setType("text/csv");
//        msIntent.putExtra(Intent.EXTRA_EMAIL, String.valueOf(mail));
//        msIntent.putExtra(Intent.EXTRA_TEXT, body);
//        startActivity(Intent.createChooser(msIntent, "메일을 발송해줘~"));
        sender = new GMailSender("hanryeoryung@gmail.com", "pxlrrxiaqtinlgzb"); // SUBSTITUTE ID PASSWORD
        timeThread();
        return 0;
    }

    public int getRandom(int max, int offset) {
        confirm_ = new Random();

        int nResult = confirm_.nextInt(max) + offset;


        return nResult;


    }


    public void timeThread() {

        dialog = new ProgressDialog(this);
        dialog.setTitle("Wait...");
        dialog.setMessage("메일을 보내는 중입니다.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                try {

                    sender.sendMail("인증 코드 발송", // subject.getText().toString(),
                            body, // body.getText().toString(),
                            "hanryeoryung@gmail.com", // from.getText().toString(),
                            uEmail // to.getText().toString()
                    );
                    toConfirm();
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(JoinActivity.this, "발송 실패", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            private void sleep(int i) {
                // TODO Auto-generated method stub

            }

        }).start();
    }
}