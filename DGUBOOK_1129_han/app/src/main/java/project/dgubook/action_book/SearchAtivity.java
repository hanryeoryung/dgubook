package project.dgubook.action_book;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import project.dgubook.R;
import project.dgubook.main.BookDetailActivity;
import project.dgubook.main.BookListAdapter;
import project.dgubook.main.BookListItem;
import project.dgubook.main.MainHomeActivity;
import project.dgubook.messenger.MessengerAtivity;
import project.dgubook.setting.InfoAtivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class SearchAtivity extends AppCompatActivity {

    Button home, search, register, reserve, message, information ,book_search;
    EditText book_title, book_author, book_publisher;
    ListView search_list;
    ArrayList<BookListItem> bookList;
    BookListAdapter bookList_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        book_title = (EditText)findViewById(R.id.search_book_title);
        book_author = (EditText)findViewById(R.id.search_book_author);
        book_publisher=(EditText)findViewById(R.id.search_book_publisher);
        search_list = (ListView) findViewById(R.id.search_listView);
        book_search = (Button)findViewById(R.id.book_search);
        book_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //책 검색하기 버튼--------------------------------------------------
//                search_list.
                setSearchItem();
//                search_list.refreshDrawableState();
                setList();
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

    private void setList() {

    }

    private void setSearchItem() {
        bookList = new ArrayList<BookListItem>();
        String name, author, publish;
        try {
            if(book_title.getText().toString().isEmpty()){
                Log.i("line: ", URLEncoder.encode(book_title.getText().toString(), "EUC-KR"));
                name = "null";
            }else{
                name = book_title.getText().toString();
            }
            if(book_author.getText().toString().isEmpty()){
                author = "null";
            }else{
                author = book_author.getText().toString();
            }
            if(book_publisher.getText().toString().isEmpty()){
                publish = "null";
            }else{
                publish = book_publisher.getText().toString();
            }
            new phpDown().execute("http://210.94.222.136/dgubook_search_list.php?name="
                    + URLEncoder.encode(name, "EUC-KR")
                    + "&author=" +  URLEncoder.encode(author, "EUC-KR")
                    + "&publish=" + URLEncoder.encode(publish, "EUC-KR"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       bookList_ad = new BookListAdapter(getApplicationContext(), R.layout.main_list_item, bookList);
//        this.
        search_list.setAdapter(bookList_ad);

        // main_list.setOnClickListener(new View.OnClickListener() {

        //public void onClick(View v) {
        //toDetail(bookList.get(position));
        //}
        // });
        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //list 클릭시 디테일 페이지로 이동

                toDetail(bookList.get(position));
            }
        });

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
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "EUC-KR"));
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
            int c_flag;
            try{
                //Toast.makeText(getActivity(), "시작", Toast.LENGTH_SHORT).show();
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                bookList.clear();
                for(int i=0; i<ja.length(); i++){
                    JSONObject jo = ja.getJSONObject(i);
//                    imgurl = jo.getString("cos_name");
                    name = jo.getString("book_name");
                    img = jo.getString("book_img");
                    author = jo.getString("book_author");
                    publish = jo.getString("book_publish");
                    price = jo.getString("book_price");
                    p_price = jo.getString("user_price");
                    c_flag = jo.getInt("camera_flag");

                    BookListItem tmp = new BookListItem(name, img, author, publish, price, p_price, c_flag);
                    bookList.add(tmp);

                    Log.d(Integer.toString(k) + "----- ", name);
                    k++;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            Log.i("list size: ", Integer.toString(bookList.size()));

        }
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
    public void toDetail(BookListItem item) {
        String book_title = item.getbName();
        String book_author=item.getbAuthor();
        String book_publisher = item.getbPublish();
        String book_price = item.getbPrice();
        String book_personal_price = item.getbPrice_p();
        String book_image = item.getbImg();

        BookListItem book = new BookListItem();
        book.setbName(book_title);
        book.setbImg(book_image);
        book.setbAuthor(book_author);
        book.setbPublish(book_publisher);
        book.setbPrice(book_price);
        book.setbPrice_p(book_personal_price);

//        String str = item.itemName;
//        Toast.makeText(RecommendActivity.this, str, Toast.LENGTH_SHORT).show();
        Intent dIntent = new Intent(Intent.ACTION_VIEW);
        // dIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        dIntent.setClassName(this, BookDetailActivity.class.getName());
        dIntent.putExtra("bookInformation", book); //객체를 디테일로로 넘겨줌
        startActivity(dIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}