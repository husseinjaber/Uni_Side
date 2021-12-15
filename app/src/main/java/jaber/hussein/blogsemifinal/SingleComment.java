package jaber.hussein.blogsemifinal;

import java.util.Date;

public class SingleComment {
    private String CommentText;
    private String CommentUserName;
    private Date CommentDate;
    private String CommentUserImaege;
    private String CommentUserID;
    private String CommentID;
    private String depar;
    private String semester;
    private String questionID;

    public String getCommentID() {
        return CommentID;
    }

    public void setCommentID(String commentID) {
        CommentID = commentID;
    }

    public String getDepar() {
        return depar;
    }

    public void setDepar(String depar) {
        this.depar = depar;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getCommentUserID() {
        return CommentUserID;
    }

    public void setCommentUserID(String commentUserID) {
        CommentUserID = commentUserID;
    }



    public SingleComment()
    {

    }

    public String getCommentText() {
        return CommentText;
    }

    public void setCommentText(String commentText) {
        CommentText = commentText;
    }

    public String getCommentUserName() {
        return CommentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        CommentUserName = commentUserName;
    }

    public Date getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(Date commentDate) {
        CommentDate = commentDate;
    }

    public String getCommentUserImaege() {
        return CommentUserImaege;
    }

    public void setCommentUserImaege(String commentUserImaege) {
        CommentUserImaege = commentUserImaege;
    }

    public SingleComment(String commentText, String commentUserName, Date commentDate, String commentUserImaege) {
        CommentText = commentText;
        CommentUserName = commentUserName;
        CommentDate = commentDate;
        CommentUserImaege = commentUserImaege;
    }
}
