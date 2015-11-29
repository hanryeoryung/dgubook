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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import project.dgubook.BookVO;
import project.dgubook.R;
import project.dgubook.SearchBookUseNaverReserveActivity;
import project.dgubook.main.MainHomeActivity;
import project.dgubook.messenger.MessengerAtivity;
import project.dgubook.setting.InfoAtivity;

/**
 * Created by 지수 on 2015-11-05.
 */
public class ReserveAtivity extends AppCompatActivity {

    Button home, search, register, reserve, message, information , book_reserve, book_search;
    EditText book_title, book_author, book_publisher, book_public_price;
    ImageView book_image;
    String user_id;
    BookVO vo_tmp;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //shared preference를 불러옴 (자동로그인 사용을 위해)
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        user_id  = pref.getString("editText", "");
        //위-----------------------------
        home = (Button)findViewById(R.id.reserve_home_btn);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toHome();
            }
        });
        //중간-------------------
        book_title = (EditText)findViewById(R.id.book_title_for_reserve);
        book_author = (EditText)findViewById(R.id.book_author_for_reserve);
        book_publisher = (EditText)findViewById(R.id.book_publisher_for_reserve);
        book_public_price = (EditText)findViewById(R.id.public_price_for_reserve);

        book_image = (ImageView)findViewById(R.id.book_image_for_reserve);

        book_search = (Button)findViewById(R.id.search_book_for_reserve);
        book_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
             //책검색버튼
                toSearchBookUseNaver();
            }
        });

        setItem();

        book_reserve = (Button)findViewById(R.id.book_reserve);
        book_reserve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //예약하기 버튼----------------------------------------------------------------
                if(!book_title.getText().toString().equals("")&& !book_author.getText().toString().equals("")&&!book_publisher.getText().toString().equals("")&&!book_public_price.getText().toString().equals(""))
                {
                    //빈칸이 아닐때 디비에 저장---------------------------------------------------
                    if(addReserveBooks() == 0){
                        Toast.makeText(ReserveAtivity.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        initial(book_title, book_author, book_publisher, book_public_price, book_image);
                        toHome();
                    } else {
                        Toast.makeText(ReserveAtivity.this, "오류가 발생하였습니다..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReserveAtivity.this, "빈칸을 모두 채워 주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //아래----------------------
        search= (Button)findViewById(R.id.reserve_button1);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSearch();
            }
        });
        register = (Button)findViewById(R.id.reserve_button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUpload();
            }
        });
        reserve = (Button)findViewById(R.id.reserve_button3);
        message = (Button)findViewById(R.id.reserve_button4);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMessage();
            }
        });
        information = (Button)findViewById(R.id.reserve_button5);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInformation();
            }
        });



    }

    private int addReserveBooks() {
        try {

            URL mUrl = new URL("http://210.94.222.136/dgubook_add_reserve.php?name=" + URLEncoder.encode(book_title.getText().toString(), "EUC-KR") + "&img=" +  URLEncoder.encode(vo_tmp.getImage().toString(), "EUC-KR")
                    + "&author=" +  URLEncoder.encode(book_author.getText().toString(), "EUC-KR") + "&publish=" + URLEncoder.encode(book_publisher.getText().toString(), "EUC-KR") + "&price=" + URLEncoder.encode(book_public_price.getText().toString(), "EUC-KR")
                    + "&r_email=" + URLEncoder.encode(user_id, "EUC-KR"));
            //카메라사진 앨범에서 가져올경우 URi를 디비에 저장함 따라서 저기 vo_tmp대신에 mImageCaptureUri.toString()을 넣으면 업로드가 됨 불러올때도 바꿔야함 이미지를 불러올때 imageview.setImageBitmap(mImageCaptureUri)이걸 해야 한다고 함 수정좀 ㅠㅠ
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

    private void initial(EditText s1,EditText s2,EditText s3,EditText s4,ImageView img ){
        s1.setText("");
        s2.setText("");
        s3.setText("");
        s4.setText("");
        img.setImageDrawable(null);
    }

    //서버의 이미지를 Bitmap으로 가져오는 객체 -------------------//
    class ImageAsync extends AsyncTask<String,String,ImageView> {
        ImageView img;
        //ex1_book.xml에 있는 ImageView이다.

        Bitmap bm;
        //서버로부터 받아서 위의 ImageView에 설정할 객체
        BookVO vo;

        public ImageAsync(ImageView img, BookVO vo) {
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
                BufferedInputStream bi = new BufferedInputStream(url.openStream(), 10240);
                //속도는 좀 느릴 수 있으나, 유실되는 자원이 없도록 사용하는 스트림 ! 안전하다.
                bm = BitmapFactory.decodeStream(bi);
                bi.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(ImageView imageView) {
            img.setImageBitmap(bm);
        }
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
        intent.setClassName(this, SearchBookUseNaverReserveActivity.class.getName());
        startActivity(intent);
    }
}