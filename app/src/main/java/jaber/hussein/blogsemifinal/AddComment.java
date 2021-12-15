package jaber.hussein.blogsemifinal;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddComment extends AppCompatActivity {
    private String depar="CCNE";
    private String semester="S1";
    private String postID="";
    private ImageView close;
    private Button addComment;
    private EditText commentTexttt;
    private ProgressBar mmyProg;
    private String textOfComment="";
    private String userOFQuestion;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        mmyProg=findViewById(R.id.commentProg);
        try
        {
            firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseAuth=FirebaseAuth.getInstance();
        }catch (Exception e)
        {
            Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        try{
            Bundle extras=getIntent().getExtras();
            depar=extras.getString("dep");
            semester=extras.getString("sem");
            postID=extras.getString("QuestionID");
            userOFQuestion=extras.getString("userQues");
        }catch (Exception e){
            Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        close=findViewById(R.id.closeAddComment);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commentTexttt=findViewById(R.id.commentTextHere);

        addComment=findViewById(R.id.add_comment);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textOfComment=commentTexttt.getText().toString();
//                Toast.makeText(AddComment.this, ""+commentTexttt.getText().toString(), Toast.LENGTH_SHORT).show();
                if(!TextUtils.isEmpty(textOfComment))
                {
                    addComment.setEnabled(false);
                    try {
                        mmyProg.setVisibility(View.VISIBLE);
                        Map<String, Object> commentMap = new HashMap<>();
                        commentMap.put("CommentUserID", firebaseAuth.getCurrentUser().getUid());
                        commentMap.put("CommentDate", FieldValue.serverTimestamp());
                        commentMap.put("CommentText", textOfComment);
                        final Map<String, Object> NotifMap = new HashMap<>();
                        NotifMap.put("textOfNotification","you have new Comment..");



                        firebaseFirestore.collection("uni").document("fot")
                                .collection("QuesAndAns")
                                .document(depar)
                                .collection(semester)
                                .document(postID)
                                .collection("comments")
                                .add(commentMap)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful())
                                        {
                                            mmyProg.setVisibility(View.INVISIBLE);

                                            new CountDownTimer(300, 1000) {
                                                public void onFinish() {
                                                    // When timer is finished
                                                    // Execute your code here
                                                    finish();


                                                }

                                                public void onTick(long millisUntilFinished) {
                                                    // millisUntilFinished    The amount of time until finished.
                                                }
                                            }.start();



                                        }
                                        else
                                        {
                                            mmyProg.setVisibility(View.INVISIBLE);
                                            addComment.setEnabled(true);

                                            Toast.makeText(AddComment.this, "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });







                    }catch (Exception eee){
                        Toast.makeText(AddComment.this, "Error"+eee.toString(), Toast.LENGTH_LONG).show();
                        mmyProg.setVisibility(View.INVISIBLE);
//                        add_ques.setEnabled(true);
                          addComment.setEnabled(true);

                    }
                    addComment.setEnabled(true);

                }
                else
                {
                    Toast.makeText(AddComment.this, "Please enter comment text..", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
