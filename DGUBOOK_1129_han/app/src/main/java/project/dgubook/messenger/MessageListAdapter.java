package project.dgubook.messenger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

import project.dgubook.R;
import project.dgubook.main.BookListItem;

/**
 * Created by 지수 on 2015-06-30.
 */
public class MessageListAdapter extends BaseAdapter {

    Context con;
    LayoutInflater inflacter;
    ArrayList<MessageListItem> list;
    int layout;
    ImageView item_img;

    public MessageListAdapter(Context context, int alayout, ArrayList<MessageListItem> book_list){
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
            convertView = inflacter.inflate(R.layout.message_list_item, parent, false);
        }
        TextView book_name = (TextView) convertView.findViewById(R.id.message_list_name);
        book_name.setText(list.get(position).getbName());
        TextView state = (TextView) convertView.findViewById(R.id.message_list_state);
        state.setText(list.get(position).getState());
        TextView f_date = (TextView) convertView.findViewById(R.id.messege_list_time);
        state.setText(list.get(position).getF_date());
        item_img = (ImageView) convertView.findViewById(R.id.main_list_img);
//        item_img.setImageResource(list.get(position).getbImg());
        setImg(list.get(position).getBimg());
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
}
