package project.dgubook;

import android.os.Parcel;
import android.os.Parcelable;

public class BookVO implements Parcelable {
    private String title;
    private String author;
    private String publisher;
    private String price;
    private String image;

    public BookVO(){

    }
    public BookVO(Parcel in){
        readFromParcel(in);
    }
    public BookVO(String _title, String _author, String _publisher, String _price, String _image){
        this.title = _title;
        this.author = _author;
        this.publisher = _publisher;
        this.price = _price;
        this.image = _image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(publisher);
        dest.writeString(price);
        dest.writeString(image);
    }

    public void readFromParcel(Parcel in){
        title = in.readString();
        author = in.readString();
        publisher = in.readString();
        price = in.readString();
        image = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public BookVO createFromParcel(Parcel in){
            return new BookVO(in);
        }
        public BookVO[] newArray(int size){
            return new BookVO[size];
        }
    };

}