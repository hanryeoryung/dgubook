package project.dgubook.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.net.URL;

import project.dgubook.BookVO;
import project.dgubook.R;

/**
 * Created by 유건이 on 2015-11-26.
 */
public class BookDetailActivity extends AppCompatActivity {
    TextView book_title, book_author, book_publisher, book_public_price, book_personal_price;
    ImageView book_image;
    Button home, message;
    BookVO vo_tmp;
    String book_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail);


        book_title =(TextView)findViewById(R.id.detail_book_title);
        book_author=(TextView)findViewById(R.id.detail_book_author);
        book_publisher = (TextView)findViewById(R.id.detail_book_publisher);
        book_public_price = (TextView)findViewById(R.id.detail_public_price);
        book_personal_price =(TextView)findViewById(R.id.detail_personal_price);

        book_image = (ImageView)findViewById(R.id.detail_book_image);

        message = (Button)findViewById(R.id.detail_send_message);
        message.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //판매자에게메세지보내기!!------------------------------------------------
            }
        });

        home = (Button)findViewById(R.id.detail_home_btn);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toHome();
            }
        });

        setItem2();
    }

    private void setItem2() {
        //Intent intent = getIntent();
        String s1,s2,s3;
        Bundle bundle = getIntent().getExtras();
        BookListItem book = bundle.getParcelable("bookInformation");
        s1 = replace(book.getbName());
        s2 = replace(book.getbAuthor());
        s3 = replace(book.getbPublish());
        book_title.setText(s1);
        book_author.setText(s2);
        book_publisher.setText(s3);
        book_public_price.setText(book.getbPrice());
        book_personal_price.setText(book.getbPrice_p());
        book_img = book.getbImg();

            BookVO book2 = new BookVO();
            book2.setTitle(book_title.getText().toString());
            book2.setAuthor(book_author.getText().toString());
            book2.setPublisher(book_publisher.getText().toString());
            book2.setPrice(book_public_price.getText().toString());
            book2.setImage(book_img);
            //이미지 검색
            //이미지는 문자열을 지정하는 것이 아니라 실제 서버에 존재하는 이미지를 로드하여
            //Bitmap으로 만든 후 b_img에 설정해야한다. 따라서 그냥 set하면 안되고 이렇게 해줘야함.
            new ImageAsync(book_image, book2).execute(null, null);

    }

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
    private void toHome() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setClassName(this, MainHomeActivity.class.getName());
        startActivity(intent);
    }

    private String replace(String s){
        String s2 = "";
        String s3 = "";
        boolean contain1 = s.contains("<b>");
        boolean contain2 = s.contains("</b>");
        if(contain1 && contain2) {
            s2= s.replace("<b>", " ");
            s3 = s2.replace("</b>","");
            return s3;
        }
        else if(contain1){
            s2 = s.replace("<b>","");
            return s2;
        }
        else if(contain2){
            s2 = s.replace("</b>","");
            return s2;
        }
        else {
            return s;
        }
    }


}
