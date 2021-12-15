package jaber.hussein.blogsemifinal;


import java.util.Date;

public class hours_post {
    public String user;
    public String image_url;
    public String desc;
    public String user_name;
    public String user_image_url;
    public String image_name;
    public Date timestamp;
    public String hoursPostId;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image_url() {
        return user_image_url;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }



    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }




    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }




    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }





    public hours_post(String user, String image_url, String desc,Date timestamp,String image_name) {
        this.user = user;
        this.image_url = image_url;
        this.desc = desc;
        this.timestamp = timestamp;



    }

    public hours_post() { }
}
