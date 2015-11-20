package project.dgubook.main;

/**
 * Created by 지수 on 2015-11-25.
 */
public class BookListItem {
    private String bName, bAuthor, bPublish, bPrice, bPrice_p, bImg;
    public BookListItem(String name,String img, String author, String publish, String price, String p_price){
        bName = name;
        bImg = img;
        bAuthor = author;
        bPublish = publish;
        bPrice = price;
        bPrice_p = p_price;
    }

    public String getbName() {
        return bName;
    }

    public String getbImg() {
        return bImg;
    }

    public String getbAuthor() {
        return bAuthor;
    }

    public String getbPublish() {
        return bPublish;
    }

    public String getbPrice() {
        return bPrice;
    }

    public String getbPrice_p() {
        return bPrice_p;
    }

}

