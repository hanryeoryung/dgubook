package project.dgubook.action_book;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import project.dgubook.BookVO;
import project.dgubook.R;
import project.dgubook.SearchBookUseNaverActivity;
import project.dgubook.main.MainHomeAtivity;
import project.dgubook.messenger.MessengerAtivity;
import project.dgubook.setting.InfoAtivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class UploadAtivity extends AppCompatActivity {

    Button search_book_information, upload;
    EditText book_title, book_author, book_publisher, book_public_price, book_personal_price, book_sale_or_lend;
    Button home, search, register, reserve, message, information;
    ImageView book_image;
    BookVO vo_tmp;
    int flag=0;
    int s_way=0; // 0 -> 판매, 1->대여, 2->둘다
    String user_id;

    //서버의 이미지를 Bitmap으로 가져오는 객체 -------------------//
    class ImageAsync extends AsyncTask<String,String,ImageView> {
        ImageView img;
        //ex1_book.xml에 있는 ImageView이다.

        Bitmap bm;
        //서버로부터 받아서 위의 ImageView에 설정할 객체
        BookVO vo;
        public ImageAsync(ImageView img,BookVO vo){
            this.img = img; //ex1_book.xml에 있는 ImageView
            this.vo = vo; //해당 vo 객체
            vo_tmp = vo;
        }


        @Override
        protected ImageView doInBackground(String... strings) {
            //URL을 연결하여 bm을 생성하는 것이 목적이다.
            try {
                URL url = new URL(vo.getImage());
                // bm = BitmapFactory.decodeStream(url.openStream());
                BufferedInputStream bi = new BufferedInputStream(url.openStream(),10240);
                //속도는 좀 느릴 수 있으나, 유실되는 자원이 없도록 사용하는 스트림 ! 안전하다.
                bm = BitmapFactory.decodeStream(bi);
                bi.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ImageView imageView) {
            img.setImageBitmap(bm);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //shared preference를 불러옴 (자동로그인 사용을 위해)
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        user_id  = pref.getString("editText", "");

        search_book_information = (Button) findViewById(R.id.search_book_info);
        search_book_information.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toSearchBookUseNaver();
            }
        });
        upload = (Button)findViewById(R.id.book_upload); // 책등록하기 버튼!!!********************************************************
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //디비에 저장하기===========================================================================================================
                if(!book_title.getText().toString().equals("")&& !book_author.getText().toString().equals("")&&!book_publisher.getText().toString().equals("")&&!book_public_price.getText().toString().equals("")&&!book_personal_price.getText().toString().equals("")&&!book_sale_or_lend.getText().toString().equals("")){
                    //빈칸이 아닐때
                    boolean contain = book_sale_or_lend.getText().toString().contains("판매");
                    boolean contain2 = book_sale_or_lend.getText().toString().contains("대여");
                    if(!(contain||contain2)){
                        //빈칸이 아니지만 판매나 대여에 다른 말을 쓴경우
                        Toast.makeText(UploadAtivity.this, "판매나 대여로 입력해주세요!!!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //이때 디비에 값들 저장---------------------------------------------------------------------------------------------------------------
                        if(contain&&contain2) {
                            s_way = 2;
                        }else {
                            if (contain) s_way = 0;
                            if (contain2) s_way = 1;
                        }
                        if(addBooks() == 0){
                            Toast.makeText(UploadAtivity.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UploadAtivity.this, "오류가 발생하였습니다..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(UploadAtivity.this, "빈칸을 모두 채워 주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        book_title= (EditText)findViewById(R.id.book_title);
        book_author = (EditText)findViewById(R.id.book_author);
        book_publisher = (EditText)findViewById(R.id.book_publisher);
        book_public_price=(EditText)findViewById(R.id.public_price);
        book_personal_price=(EditText)findViewById(R.id.personal_price);
        book_sale_or_lend =(EditText)findViewById(R.id.sale_or_lend);

        book_image = (ImageView)findViewById(R.id.book_image);

        setItem();

        //밑에 버튼----------------------------------------------
        home = (Button) findViewById(R.id.upload_home_btn);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toHome();
            }
        });
        search = (Button) findViewById(R.id.upload_button1);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSearch();
            }
        });
        register = (Button) findViewById(R.id.upload_button2);
        reserve = (Button) findViewById(R.id.upload_button3);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReserve();
            }
        });
        message = (Button) findViewById(R.id.upload_button4);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMessage();
            }
        });
        information = (Button) findViewById(R.id.upload_button5);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInformation();
            }
        });
    }

    private int addBooks() {
        try {

            URL mUrl = new URL("http://210.94.222.136/dgubook_add_book.php?name=" + URLEncoder.encode(book_title.getText().toString(), "EUC-KR") + "&img=" +  URLEncoder.encode(vo_tmp.getImage().toString(), "EUC-KR")
                    + "&author=" +  URLEncoder.encode(book_author.getText().toString(), "EUC-KR") + "&publish=" + URLEncoder.encode(book_publisher.getText().toString(), "EUC-KR") + "&price=" + URLEncoder.encode(book_public_price.getText().toString(), "EUC-KR")
                    + "&u_price=" + URLEncoder.encode(book_personal_price.getText().toString(), "EUC-KR")+ "&s_way=" + URLEncoder.encode(Integer.toString(s_way), "EUC-KR")
                    + "&s_email=" + URLEncoder.encode(user_id, "EUC-KR"));
//            URL mUrl = new URL("http://210.94.222.136/dgubook_add_book.php?name=" + URLEncoder.encode("두비두밥", "EUC-KR") + "&img=" +  URLEncoder.encode("https://fbcdn-photos-e-a.akamaihd.net/hphotos-ak-xaf1/v/t1.0-0/p173x172/1514944_955109231189474_8658401992889956792_n.jpg?oh=ae0dad9e1bdc7a1997c09284774037d9&oe=56EC59B8&__gda__=1457693405_f0c33d89cf2fa6ca37fa58d4e2d1457e", "EUC-KR")
//                    + "&author=" +  URLEncoder.encode("려령", "EUC-KR") + "&publish=" + URLEncoder.encode("려령", "EUC-KR") + "&price=" + URLEncoder.encode("160000", "EUC-KR")
//                    + "&u_price=" + URLEncoder.encode("120000", "EUC-KR")+ "&s_way=" + URLEncoder.encode("2", "EUC-KR")
//                    + "&s_email=" + URLEncoder.encode("wbwlwb@naver.com"            , "EUC-KR") );
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
                    Log.d("-----",b_tmp);
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

    private void setItem() {
        Intent intent = getIntent();
        if(intent.hasExtra("flag")){ //객체를 받아왔는지 체크하여 flag에 표시
            flag = intent.getIntExtra("flag",1);
        }
        if(flag ==1) { //객체 받아온 경우 Edittext에 가져온 값들 넣기

            Bundle bundle = getIntent().getExtras();
            BookVO book = bundle.getParcelable("bookInfo");
            book_title.setText(book.getTitle());
            book_author.setText(book.getAuthor());
            book_publisher.setText(book.getPublisher());
            book_public_price.setText(book.getPrice());
            //이미지 검색
            //이미지는 문자열을 지정하는 것이 아니라 실제 서버에 존재하는 이미지를 로드하여
            //Bitmap으로 만든 후 b_img에 설정해야한다. 따라서 그냥 set하면 안되고 이렇게 해줘야함.
            new ImageAsync(book_image, book).execute(null, null);
        }

        /*
        if (intent.hasExtra("title")) {
            book_title.setText(intent.getStringExtra("title"));
        }
        if(intent.hasExtra("author")){
            book_author.setText(intent.getStringExtra("author"));
        }
        if(intent.hasExtra("publisher")){
            book_publisher.setText(intent.getStringExtra("publisher"));
        }
        if(intent.hasExtra("price")){
            book_public_price.setText(intent.getStringExtra("price"));
        }
        if(intent.hasExtra("image")){
            image = intent.getStringExtra("image");
            //이미지 검색
            new ImageAsync(book_image,image).execute(null, null);
        }*/

    }


    private void toHome() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, MainHomeAtivity.class.getName());
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
    private void toSearchBookUseNaver() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, SearchBookUseNaverActivity.class.getName());
        startActivity(intent);
    }
}