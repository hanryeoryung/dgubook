package project.dgubook.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 지수 on 2015-11-25.
 */
public class BookListItem implements Parcelable {
    private String bName, bAuthor, bPublish, bPrice, bPrice_p, bImg;
    int c_flag;
    public BookListItem(){

    }
    public BookListItem(Parcel in){
        readFromParcel(in);
    }
    public BookListItem(String name,String img, String author, String publish, String price, String p_price, int c_flag){
        bName = name;
        bImg = img;
        bAuthor = author;
        bPublish = publish;
        bPrice = price;
        bPrice_p = p_price;
        this.c_flag = c_flag;
    }

    public String getbName() {
        return bName;
    }
    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbImg() {
        return bImg;
    }
    public void setbImg(String bImg) {
        this.bImg = bImg;
    }
    public String getbAuthor() {
        return bAuthor;
    }
    public void setbAuthor(String bAuthor) {
        this.bAuthor = bAuthor;
    }
    public String getbPublish() {
        return bPublish;
    }
    public void setbPublish(String bPublish) {this.bPublish = bPublish;}
    public String getbPrice() {
        return bPrice;
    }
    public void setbPrice(String bPrice) {
        this.bPrice = bPrice;
    }
    public String getbPrice_p() {
        return bPrice_p;
    }
    public void setbPrice_p(String bPrice_p) {
        this.bPrice_p = bPrice_p;
    }

    public int getC_flag() {
        return c_flag;
    }

    // 객체를 intent안에 넣기 위한 함수들 paracable 포함
    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(bName);
        dest.writeString(bImg);
        dest.writeString(bAuthor);
        dest.writeString(bPublish);
        dest.writeString(bPrice);
        dest.writeString(bPrice_p);
    }

    public void readFromParcel(Parcel in){
        bName = in.readString();
        bImg = in.readString();
        bAuthor = in.readString();
        bPublish = in.readString();
        bPrice= in.readString();
        bPrice_p = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public BookListItem createFromParcel(Parcel in){
            return new BookListItem(in);
        }
        public BookListItem[] newArray(int size){
            return new BookListItem[size];
        }
    };

}

