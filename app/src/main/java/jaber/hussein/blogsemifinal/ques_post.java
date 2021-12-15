package jaber.hussein.blogsemifinal;

import java.util.Date;

public class ques_post {
    public  String user;
    public String description;
    public Date timestamp;
    public String lang;
    public String  QuesPostId ;
    public String user_name_ques;
    public String user_image_url_ques;
    public String dep;
    public String sem;
    public String ques_post_course;
    public String getQues_post_course() {
        return ques_post_course;
    }

    public void setQues_post_course(String ques_post_course) {
        this.ques_post_course = ques_post_course;
    }



//    public String dontAddMee;


    public ques_post()
    {

    }

    public ques_post(String user, String description, Date timestamp, String lang) {
        this.user = user;
        this.description = description;
        this.timestamp = timestamp;
        this.lang = lang;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
