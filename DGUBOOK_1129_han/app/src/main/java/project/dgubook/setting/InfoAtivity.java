package project.dgubook.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import project.dgubook.R;
import project.dgubook.action_book.LendBookActivity;
import project.dgubook.action_book.PurchaseBookActivity;
import project.dgubook.action_book.ReserveAtivity;
import project.dgubook.action_book.SearchAtivity;
import project.dgubook.action_book.UploadAtivity;
import project.dgubook.main.MainHomeActivity;
import project.dgubook.messenger.MessengerAtivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class InfoAtivity extends AppCompatActivity {

    Button home,  search, register, reserve, message, information;
    TextView user_id, user_name;
    Button change_pw, log_out, bye_bye, uploaded_book, buy_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);


        // 위아래-----------
        home = (Button)findViewById(R.id.information_home_btn);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toHome();
            }
        });
        search= (Button)findViewById(R.id.information_button1);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSearch();
            }
        });
        register = (Button)findViewById(R.id.information_button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUpload();
            }
        });
        reserve = (Button)findViewById(R.id.information_button3);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReserve();
            }
        });
        message = (Button)findViewById(R.id.information_button4);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMessage();
            }
        });
        information = (Button)findViewById(R.id.information_button5);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInformation();
            }
        });

        //-----------------------유저 아이디랑 이름----------------------
        user_id = (TextView)findViewById(R.id.setting_user_id);
        user_name=(TextView)findViewById(R.id.setting_user_name);
        //-------------------------------------------------------------

        change_pw = (Button)findViewById(R.id.setting_change_pw);
        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChangePassWord();
            }
        });

        log_out = (Button)findViewById(R.id.setting_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = logoutDialogBox();
                dialog.show(); //정말로 로그아웃 할 것인지 묻기!
            }
        });
        bye_bye = (Button)findViewById(R.id.setting_good_bye);
        bye_bye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = withdrawDialogBox();
                dialog.show(); //정말 탈퇴할 것인지 묻기!!
            }
        });
        uploaded_book = (Button)findViewById(R.id.setting_purchase);
        uploaded_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPurchaseBook(); //업로드한 책 보는 버튼
            }
        });
        buy_book = (Button)findViewById(R.id.setting_buy);
        buy_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLendBook(); //구매하거나 빌린 책 보는 버튼

            }
        });

    }
    private void toHome() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, MainHomeActivity.class.getName());
        startActivity(intent);
    }
    private void toUpload() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, UploadAtivity.class.getName());
        startActivity(intent);
    }
    private void toSearch() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, SearchAtivity.class.getName());
        startActivity(intent);
    }
    private void toReserve() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, ReserveAtivity.class.getName());
        startActivity(intent);
    }
    private void toMessage() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, MessengerAtivity.class.getName());
        startActivity(intent);
    }

    private void toInformation() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, InfoAtivity.class.getName());
        startActivity(intent);
    }
    private void toChangePassWord(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, ChangePasswordActivity.class.getName());
        startActivity(intent);
    }
    private void toPurchaseBook(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, PurchaseBookActivity.class.getName());
        startActivity(intent);
    }
    private void toLendBook(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, LendBookActivity.class.getName());
        startActivity(intent);
    }
    private AlertDialog logoutDialogBox() { //alert

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("DGUBOOK 알림");
        builder.setMessage("정말로 로그아웃 하시겠습니까?");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();

        return dialog;

    }
    private AlertDialog withdrawDialogBox() { //alert

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("DGUBOOK 알림");
        builder.setMessage("정말로 탈퇴하시겠습니까? ㅠㅡㅠ");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();

        return dialog;

    }

}