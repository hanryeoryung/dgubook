package project.dgubook.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import project.dgubook.R;

/**
 * Created by 유건이 on 2015-11-26.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    EditText old_pw, new_pw1, new_pw2;
    Button change , back_setting;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_change_pw);

        old_pw = (EditText)findViewById(R.id.last_pw);
        new_pw1=(EditText)findViewById(R.id.new_pw);
        new_pw2 = (EditText)findViewById(R.id.new_pw2);

        change = (Button)findViewById(R.id.change_new_password);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //비밀번호 변경 버튼--------------------------------------------------------------------------
            }
        });
        back_setting = (Button)findViewById(R.id.back_to_setting);
        back_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSetting();
            }
        });
    }
    private void toSetting() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, InfoAtivity.class.getName());
        startActivity(intent);
    }
}
