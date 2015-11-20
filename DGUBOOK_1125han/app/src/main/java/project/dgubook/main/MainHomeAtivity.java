package project.dgubook.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import project.dgubook.BookVO;
import project.dgubook.R;
import project.dgubook.SplashActivity;
import project.dgubook.action_book.ReserveAtivity;
import project.dgubook.action_book.SearchAtivity;
import project.dgubook.action_book.UploadAtivity;
import project.dgubook.messenger.MessengerAtivity;
import project.dgubook.setting.InfoAtivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class MainHomeAtivity extends AppCompatActivity {


    Button home, noti, search, register, reserve, message, information;
    EditText mainSearch;
    TextView total_book_num;
    ListView main_list;
    ArrayList<BookListItem> bookList;
    int int_total_book_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home);
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
// 위에---------------
        home = (Button)findViewById(R.id.home_btn);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toHome();
            }
        });
        noti = (Button)findViewById(R.id.noti_btn);
        total_book_num = (TextView)findViewById(R.id.total_book_num);
        setTotal();

        setList();

//아래-------------
        search= (Button)findViewById(R.id.button1);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSearch();
            }
        });
        register = (Button)findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUpload();
            }
        });
        reserve = (Button)findViewById(R.id.button3);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReserve();
            }
        });
        message = (Button)findViewById(R.id.button4);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMessage();
            }
        });
        information = (Button)findViewById(R.id.button5);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInformation();
            }
        });



    }

    private void setList() {
        bookList = new ArrayList<BookListItem>();
        main_list = (ListView) findViewById(R.id.main_listView);
        new phpDown().execute("http://210.94.222.136/dgubook_main_list.php");

        BookListAdapter bookList_ad = new BookListAdapter(getApplicationContext(), R.layout.main_list_item, bookList);
        main_list.setAdapter(bookList_ad);
//        main_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }
    private class phpDown extends AsyncTask<String, Integer,String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
//                            Log.i("line: ", line);
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();

        }
        protected void onPostExecute(String str){
            int k=1;
            String name, img, author, publish, price, p_price;
            try{
                //Toast.makeText(getActivity(), "시작", Toast.LENGTH_SHORT).show();
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                for(int i=0; i<ja.length(); i++){
                    JSONObject jo = ja.getJSONObject(i);
//                    imgurl = jo.getString("cos_name");
                    name = jo.getString("book_name");
                    img = jo.getString("book_img");
                    author = jo.getString("book_author");
                    publish = jo.getString("book_publish");
                    price = jo.getString("book_price");
                    p_price = jo.getString("user_price");

                    BookListItem tmp = new BookListItem(name, img, author, publish, price, p_price);
                    bookList.add(tmp);

                    Log.d(Integer.toString(k)+"name : ", name);
                    k++;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            Log.i("list size: ", Integer.toString(bookList.size()));
        }
    }

    private int setTotal() {
        try {

            URL mUrl = new URL("http://210.94.222.136/dgubook_books_count.php");
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
//                    Toast.makeText(UploadAtivity.this, b_tmp, Toast.LENGTH_SHORT).show();
                    total_book_num.setText(b_tmp);
                    Log.d("-----", b_tmp);
                    if(!b_tmp.contains("added")){
                        return -3;
                    }
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