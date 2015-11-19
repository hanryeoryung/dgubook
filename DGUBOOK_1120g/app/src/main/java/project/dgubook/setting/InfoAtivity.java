package project.dgubook.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import project.dgubook.R;
import project.dgubook.SplashActivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class InfoAtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SplashActivity.class));
        setContentView(R.layout.information);
    }


}