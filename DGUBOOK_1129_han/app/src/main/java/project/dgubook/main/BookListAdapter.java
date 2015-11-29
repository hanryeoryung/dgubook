package project.dgubook.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

import project.dgubook.R;

/**
 * Created by 지수 on 2015-06-30.
 */
public class BookListAdapter extends BaseAdapter {

    Context con;
    LayoutInflater inflacter;
    ArrayList<BookListItem> list;
    int layout;
    ImageView item_img;

    public BookListAdapter(Context context, int alayout, ArrayList<BookListItem> book_list){
        con = context;
        inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = book_list;
        layout = alayout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position).getbName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflacter.inflate(R.layout.main_list_item, parent, false);
        }
        TextView book_name = (TextView) convertView.findViewById(R.id.main_list_name);
        book_name.setText(stringReplace(list.get(position).getbName()));
        TextView book_author = (TextView) convertView.findViewById(R.id.main_list_author);
        book_author.setText(list.get(position).getbAuthor());
        TextView book_publish = (TextView) convertView.findViewById(R.id.main_list_publish);
        book_publish.setText(list.get(position).getbPublish());
        TextView book_p_price = (TextView) convertView.findViewById(R.id.main_list_p_price);
        book_p_price.setText(list.get(position).getbPrice_p());
        TextView book_price = (TextView) convertView.findViewById(R.id.main_list_price);
        book_price.setText(list.get(position).getbPrice());

        item_img = (ImageView) convertView.findViewById(R.id.main_list_img);
//        item_img.setImageResource(list.get(position).getbImg());
        if(list.get(position).getC_flag() == 0) {
            setImg(list.get(position).getbImg());
        }else{
//            item_img.setImageBitmap(list.get(position).getbImg());
        }

        return convertView;
    }

    private void setImg(String s) {
        Bitmap bm;
        try {
            URL url = new URL(s);
            // bm = BitmapFactory.decodeStream(url.openStream());
            BufferedInputStream bi = new BufferedInputStream(url.openStream(), 10240);
            //속도는 좀 느릴 수 있으나, 유실되는 자원이 없도록 사용하는 스트림 ! 안전하다.
            bm = BitmapFactory.decodeStream(bi);
            item_img.setImageBitmap(bm);
            bi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String stringReplace(String str){
            int str_length = str.length();
            String strlistchar   = "";
            String str_imsi   = "";
          //  String[] filter_word = {"","\\.","\\?","\\/","\\~","\\!","\\@","\\#","\\$","\\%","\\^","\\&","\\*","\\(","\\)","\\_","\\+","\\=","\\|","\\\\","\\}","\\]","\\{","\\[","\\\"","\\'","\\:","\\;","\\<","\\,","\\>","\\.","\\?","\\/"};
        String[] filter_word = {"", "<b>", "</b>"};
            for(int i=0;i<filter_word.length;i++){
                str_imsi = str.replaceAll(filter_word[i],"");
                str = str_imsi;
            }
            return str;
    }
}
