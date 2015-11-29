package project.dgubook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import project.dgubook.action_book.UploadAtivity;

/**
 * Created by 유건이 on 2015-11-21.
 */
//key:3ea9f5615aeec2baec03bc287fff70be 유건이로 인증받음

public class SearchBookUseNaverUploadActivity extends Activity {

    //레이아웃에 있는 ListView를 여기에 선언
    ListView ex1_list;
    EditText ex1_et;
    Button ex1_search_btn;

    private BookVO vo;

    int flag = 1;

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


    //--------------------------------------------------------//
    //서버 요청을 하기 위한 객체
    class NaverAsync extends AsyncTask<String,String,ArrayList<BookVO>>{
        @Override
        protected ArrayList<BookVO> doInBackground(String... strings) {

            return connectNaver();
            //Activity가 아니라 현재 Async 클래스 내에서 connectNaver()객체를 호출
            //현재 메서드는 외부에서 NaverAsync 객체를 생성한 후 execute()를 호출 하라 때
            //doInBackground()가 수행, 수행하자마자 반환형(여기서는 ArrayList<BookVO>)은
            //onPostExecute()함수를 자동으로 호출하면서 전달한다.
        }

        @Override
        protected void onPostExecute(ArrayList<BookVO> bookVOs) {
            //doInBackGround()이 수행한 후에 호출되는 곳!
            //인자는 doInBackGround에서 반환하는 ArrayList가 전달된다.
            //이 때 ListView에 적용될 Adapter클래스를 생성할 때 ArrayList를 넣어준다.
            ViewAdapter adapter1 = new ViewAdapter(SearchBookUseNaverUploadActivity.this, R.layout.ex1_book,bookVOs);

            //ListView에 Adapter클래스 설정
            ex1_list.setAdapter(adapter1);
        }


    }

    //--------------------------------------------------------//
    //ArrayList에 있는 VO들을 ex1_book.xml에 표현하도록 하는 Adapter 클래스
    class ViewAdapter extends ArrayAdapter<BookVO> {


        List<BookVO> list;

        public ViewAdapter(Context context, int resource, List<BookVO> objects) {
            //context는 Activity 정보
            //resource는 레이아웃(ex1_book.xml)
            //objects는 connectNaver에서 반환하는 ArrayList

            super(context, resource, objects);

            list = objects;

        }
        //현재 어댑터를 ListView에 적용시키면
        //ListView에서는 항목 하나하나를 구성하기 위해
        //아래의 getView() 메서드를 호출하게 된다.
        //즉, getView()는 행의 수만큼 호출한다.
        //(행의 수 : list.size())

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //ex1_book.xml을 View로 생성하여
            //반환하는 것이 현재 메서드의 목적이다.

            //View를 넘기기 위해 Layout파일을 초기화!
            //XML파일 있는 곳을 알려주면
            //inflater객체가 xml문서의 계층적 구조를 파악하여 객체화 시켜준다.
            //즉 inflater객체가 있어야한다.
            LayoutInflater linf = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //inflater 객체를 통하여 Layout의 xml을 객체화한다.
            convertView = linf.inflate(R.layout.ex1_book,null);

            //TextView또는 ImageView 등을 가지고 있는
            //Layout문서를 객체화 했다.
            //객체화된 convertView로부터 원하는 TextView 등을
            //검색하여 그곳의 vo에 있는 title,author등의 문자열을 가져와서 지정할 수 있다.

            //우선 ArrayList로부터 VO를 하나 가져오자!
            BookVO vo = list.get(position);

            if(vo!=null){
                //convertView로부터 원하는 TextView들 검색
                TextView title = (TextView)convertView.findViewById(R.id.ex1_book_title);
                TextView author = (TextView)convertView.findViewById(R.id.ex1_book_author);
                TextView publisher = (TextView)convertView.findViewById(R.id.ex1_book_publisher);
                TextView price = (TextView)convertView.findViewById(R.id.ex1_book_price);
                Button select = (Button)convertView.findViewById(R.id.ex1_select_button);

                if(title != null)
                    title.setText("Title : "+vo.getTitle());
                if(author != null)
                    author.setText("Author : "+vo.getAuthor());
                if(publisher != null)
                    publisher.setText("Publisher : "+vo.getPublisher());
                if(price != null)
                    price.setText("Price : "+vo.getPrice());

                //이미지 검색
                ImageView b_img = (ImageView)convertView.findViewById(R.id.ex1_book_img);

                //이미지는 문자열을 지정하는 것이 아니라 실제 서버에 존재하는 이미지를 로드하여
                //Bitmap으로 만든 후 b_img에 설정해야한다.

                new ImageAsync(b_img,vo).execute(null,null);

                select.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        toUpload(list.get(position));
                    }
                });

            }
            return convertView;

        }
    }

    //--------------------------------------------------------//
    /*서버와 연결하여 응답(XML)을 VO로 생성 한 후
      ArrayList에 모두 저장하여 반환하는 기능*/
    private ArrayList<BookVO> connectNaver(){

        ArrayList<BookVO> list = new ArrayList<BookVO>();

        try {

            String query = ex1_et.getText().toString();
            String mQuery = URLEncoder.encode(query, "UTF-8");

            String key = "3ea9f5615aeec2baec03bc287fff70be";
            URL url = new URL("http://openapi.naver.com/search?key="+key+"&query="
                    +mQuery+"&target=book&start=1&display=30&sort=sim");

            //위에서 생성된 URL을 통하여 서버에 요청하면 결과가 XML자원으로 응답이 온다.
            //그 XML자원을 파싱할 파서 객체가 필요하다.
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //보통 메소드들은 해당되는 객체를 생성하고 호출해야한다.
            //static으로 선언되있기 때문에 빠르게 인식, 객체가 만들어져도 하나만 만들어진다.
            //factory는 앱이 끝날 때까지 하나만! , 이미 만들어져 있는 factory를 사용하는것!

            //공장을 통해 파서 가져오기
            XmlPullParser parser = factory.newPullParser();
            //공장에서 새롭게 파서를 만든다.

            //파서를 통해 URL의 스트림을 얻어내어 호출한다.
            parser.setInput(url.openStream(),null);

            //이제 파서를 통하여 각 요소(Element)들을 이벤트성 처리를 반복수행한다.
            int parserEvent = parser.getEventType();
            while(parserEvent != XmlPullParser.END_DOCUMENT){
                //읽은 정보가 XML문서의 끝이 아닐 때 수행!
                if(parserEvent == XmlPullParser.START_TAG){
                    //시작태그들을 만났을 때만 수행

                    //시작태그의 이름을 알아낸다.
                    String tagName = parser.getName();

                    if(tagName.equals("title")){
                        vo = new BookVO();

                        //title태그의 문자열을 가져온다.
                        String title = parser.nextText();

                        //책 제목을 vo에 저장
                        vo.setTitle(title);

                    }else if(tagName.equals("image")){
                        vo.setImage(parser.nextText());
                    }else if(tagName.equals("author")){
                        vo.setAuthor(parser.nextText());
                    }else if(tagName.equals("publisher")){
                        vo.setPublisher(parser.nextText());
                    }else if(tagName.equals("price")){
                        vo.setPrice(parser.nextText());
                        //vo를 ArrayList에 저장
                        list.add(vo);
                    }
                }
                //다음 요소를 가져온다.
                parserEvent = parser.next();
            }





        }catch (Exception e){
            e.printStackTrace();
        }

        return list;

    }
    //-------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_ex1_naver);
        ex1_list = (ListView)findViewById(R.id.ex1_list);
        ex1_et = (EditText)findViewById(R.id.ex1_et);
        ex1_search_btn = (Button)findViewById(R.id.ex1_search_btn);

        ex1_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버에 요청을 실행하는 Async객체 생성
                new NaverAsync().execute(null,null); //NaverAsyn의 doInBackground()가 수행된다.
            }
        });




    }

    public void toUpload(BookVO item) {
        // Toast.makeText(RecommendActivity.this, "123", Toast.LENGTH_SHORT).show();
        String book_title = item.getTitle();
        String book_author=item.getAuthor();
        String book_publisher = item.getPublisher();
        String book_price = item.getPrice();
        String book_image = item.getImage();

        BookVO book = new BookVO();
        book.setTitle(book_title);
        book.setAuthor(book_author);
        book.setPublisher(book_publisher);
        book.setPrice(book_price);
        book.setImage(book_image);

//        String str = item.itemName;
//        Toast.makeText(RecommendActivity.this, str, Toast.LENGTH_SHORT).show();
        Intent dIntent = new Intent(Intent.ACTION_VIEW);
        // dIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        dIntent.setClassName(SearchBookUseNaverUploadActivity.this, UploadAtivity.class.getName());
        dIntent.putExtra("bookInfo",book); //객체를 업로드로 넘겨줌
        dIntent.putExtra("flag",flag); //객체를 받아왔는지 체크할 flag
       /* dIntent.putExtra("title", book_title);
        dIntent.putExtra("author", book_author);
        dIntent.putExtra("publisher",book_publisher);
        dIntent.putExtra("price",book_price);
        dIntent.putExtra("image",book_image);*/
        startActivity(dIntent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ex1_naver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

