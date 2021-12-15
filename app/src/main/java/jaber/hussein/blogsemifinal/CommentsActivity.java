package jaber.hussein.blogsemifinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CommentsActivity extends AppCompatActivity {
    private String depar = "CCNE";
    private String semester = "S1";
    private String postID="#5MPLKP8CoiaXpcy4AYOo";
    private RecyclerView myRecForComment;
    private List<SingleComment> singleComments;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private CommentAdapter commentAdapter;
    private ImageButton close;
    private FloatingActionButton addComment;
    private String commentIDDD;
    private String UserOfQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        try{
        Bundle extras=getIntent().getExtras();
        depar=extras.getString("dep");
        semester=extras.getString("sem");
        postID=extras.getString("QuestionID") ;
        UserOfQuestion=extras.getString("userOfQuestion");


        }catch (Exception e){
            Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        addComment=findViewById(R.id.add_commentt);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(CommentsActivity.this,AddComment.class);
                newIntent.putExtra("dep",depar);
                newIntent.putExtra("sem",semester);
                newIntent.putExtra("QuestionID",postID);
                newIntent.putExtra("userQues",UserOfQuestion);

                startActivity(newIntent);
                finish();
            }
        });


        singleComments = new ArrayList<>();
        myRecForComment = findViewById(R.id.comment_recy);
        commentAdapter = new CommentAdapter(singleComments);
        myRecForComment.setLayoutManager(new LinearLayoutManager(this));
        myRecForComment.setAdapter(commentAdapter);
        close=findViewById(R.id.clsoeComments);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        if (firebaseAuth.getCurrentUser() != null) {


            firebaseFirestore = FirebaseFirestore.getInstance();
            Query FirstQuery = firebaseFirestore.collection("uni").document("fot")
                    .collection("QuesAndAns")
                    .document(depar)
                    .collection(semester)
                    .document(postID)
                    .collection("comments")
                    .orderBy("CommentDate", Query.Direction.DESCENDING);
            FirstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {

                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    try {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            commentIDDD = doc.getDocument().getId();


//                            Toast.makeText(CommentsActivity.this, "i am here", Toast.LENGTH_SHORT).show();

//                            final String pdfPostId = doc.getDocument().getId();
                            final SingleComment new_comment = doc.getDocument().toObject(SingleComment.class);

                            if (doc.getType() == DocumentChange.Type.ADDED) {


                                firebaseFirestore.collection("Users").document(new_comment.getCommentUserID())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String user_namee = task.getResult().getString("name");
                                                    String user_image = task.getResult().getString("image");


                                                    SingleComment aaaaa=new SingleComment();
                                                    aaaaa.setCommentUserImaege(user_image);
                                                    aaaaa.setCommentUserName(user_namee);
                                                    aaaaa.setCommentDate(new_comment.getCommentDate());
                                                    aaaaa.setCommentText(new_comment.getCommentText());
                                                    aaaaa.setCommentUserID(new_comment.getCommentUserID());
                                                    aaaaa.setCommentID(commentIDDD);
                                                    aaaaa.setDepar(depar);
                                                    aaaaa.setQuestionID(postID);
                                                    aaaaa.setSemester(semester);
                                                    singleComments.add( aaaaa);
                                                    commentAdapter.notifyDataSetChanged();

                                                } else {

                                                }


                                            }
                                        });


                            }


                        }


                    } catch (Exception eee) {

                    }
                }
            });
        }

    }


}
