package project.dgubook.messenger;

/**
 * Created by 지수 on 2015-11-25.
 */
public class MessageListItem {
    private String c_id, b_id, bName, bimg, state, s_email, s_name, r_email, r_name, f_date;
    public MessageListItem(String c_id, String b_id, String name, String b_img, String st, String s, String sname, String r, String rname, String fdate){
        this.c_id = c_id;
        this.b_id = b_id;
        bName = name;
        bimg = b_img;
        state = st;
        s_email = s;
        s_name = sname;
        r_email = r;
        r_name = rname;
        f_date = fdate;
    }


    public String getbName() {
        return bName;
    }

    public String getB_id() {
        return b_id;
    }

    public String getState() {
        return state;
    }

    public String getR_email() {
        return r_email;
    }

    public String getS_email() {
        return s_email;
    }

    public String getF_date() {
        return f_date;
    }

    public String getBimg() {
        return bimg;
    }
}

