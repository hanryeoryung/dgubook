package project.dgubook.join;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import project.dgubook.R;
import project.dgubook.SplashActivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        //조인 페이지 기입 내용들
        EditText email, name, student_id, password1, password2;
        email = (EditText)findViewById(R.id.join_email_id);
        name = (EditText)findViewById(R.id.join_user_name);
        student_id = (EditText)findViewById(R.id.join_user_student_id);
        password1 = (EditText)findViewById(R.id.join_password_1);
        password2 = (EditText)findViewById(R.id.join_password_2);

        Button join_and_confirm;  //마지막 가입하기 버튼 가입하기 버튼을 누르면 인증메일 발송 후 최종 가입결정
        join_and_confirm = (Button)findViewById(R.id.join_joinBtn);

    }

}