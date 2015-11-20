package project.dgubook.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import project.dgubook.R;
import project.dgubook.SplashActivity;
import project.dgubook.action_book.ReserveAtivity;
import project.dgubook.action_book.SearchAtivity;
import project.dgubook.action_book.UploadAtivity;
import project.dgubook.main.MainHomeAtivity;
import project.dgubook.setting.InfoAtivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class MessengerAtivity extends AppCompatActivity {

    Button home,  search, register, reserve, message, information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);

        home = (Button)findViewById(R.id.messenger_home_btn);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toHome();
            }
        });
        search= (Button)findViewById(R.id.messenger_button1);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSearch();
            }
        });
        register = (Button)findViewById(R.id.messenger_button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUpload();
            }
        });
        reserve = (Button)findViewById(R.id.messenger_button3);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReserve();
            }
        });
        message = (Button)findViewById(R.id.messenger_button4);
        information = (Button)findViewById(R.id.messenger_button5);
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

    private void toInformation() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, InfoAtivity.class.getName());
        startActivity(intent);
    }


}