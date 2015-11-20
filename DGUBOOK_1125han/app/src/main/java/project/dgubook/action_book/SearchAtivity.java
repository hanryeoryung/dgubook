package project.dgubook.action_book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import project.dgubook.R;
import project.dgubook.SplashActivity;
import project.dgubook.main.MainHomeAtivity;
import project.dgubook.messenger.MessengerAtivity;
import project.dgubook.setting.InfoAtivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class SearchAtivity extends AppCompatActivity {

    Button home, search, register, reserve, message, information ,book_search;
    EditText book_title, book_author, book_publisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        book_title = (EditText)findViewById(R.id.search_book_title);
        book_author = (EditText)findViewById(R.id.search_book_author);
        book_publisher=(EditText)findViewById(R.id.search_book_publisher);

        book_search = (Button)findViewById(R.id.book_search);
        book_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //책 검색하기 버튼--------------------------------------------------
            }
        });


//밑에 버튼--------------------------------------------------
        home = (Button)findViewById(R.id.search_home_btn);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toHome();
            }
        });
        search= (Button)findViewById(R.id.search_button1);
        register = (Button)findViewById(R.id.search_button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUpload();
            }
        });
        reserve = (Button)findViewById(R.id.search_button3);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReserve();
            }
        });
        message = (Button)findViewById(R.id.search_button4);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMessage();
            }
        });
        information = (Button)findViewById(R.id.search_button5);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInformation();
            }
        });

    }

    private void toHome() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, MainHomeAtivity.class.getName());
        startActivity(intent);
    }
    private void toUpload() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, UploadAtivity.class.getName());
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


}