package jaber.hussein.blogsemifinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ques_recy_adapter extends RecyclerView.Adapter<ques_recy_adapter.ViewHolder> {
    public List<ques_post > ques_posts;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    public ques_recy_adapter(List<ques_post> ques_posts){
        this.ques_posts=ques_posts;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_ques,viewGroup,false  );
        context=viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.setIsRecyclable(false);


        final String currentUserIdForQues= firebaseAuth.getCurrentUser().getUid();

        try {


            String desc_data = ques_posts.get(i).getDescription();
            viewHolder.setQuestionHere(desc_data);

            String course_name11=ques_posts.get(i).getQues_post_course();
//            Toast.makeText(context, course_name11, Toast.LENGTH_SHORT).show();
            viewHolder.setCourseName2(course_name11);


            Date date_data = ques_posts.get(i).getTimestamp();
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM HH:mm");
            String newFormatted = newFormat.format(date_data);
            viewHolder.set_time(newFormatted);
            String user_name_here = ques_posts.get(i).user_name_ques;
            String user_image_here = ques_posts.get(i).user_image_url_ques;

            viewHolder.setuserDATA(user_name_here, user_image_here);
            String user_id = ques_posts.get(i).getUser();
            if (!user_id.equals(currentUserIdForQues)) {
                viewHolder.DeleteQues.setEnabled(false);
                viewHolder.DeleteQues.setVisibility(View.INVISIBLE);

            } else if (user_id.equals(currentUserIdForQues)) {
                viewHolder.DeleteQues.setEnabled(true);
                viewHolder.DeleteQues.setVisibility(View.VISIBLE);
            }
        }catch (Exception ee){
            Toast.makeText(context, "Please refresh the page", Toast.LENGTH_LONG).show();
        }
        final String department =ques_posts.get(i).dep;
        final String semester = ques_posts.get(i).sem;
        final String questionID=ques_posts.get(i).QuesPostId;
        final String userHere=ques_posts.get(i).getUser();



        viewHolder.DeleteQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                try {
//                    Toast.makeText(context, ""+questionID+department+semester, Toast.LENGTH_SHORT).show();

                                    firebaseFirestore.collection("uni").document("fot")
                                            .collection("QuesAndAns")
                                            .document(department)
                                            .collection(semester)
                                            .document(questionID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            ques_posts.remove(i);
                                            viewHolder.DeleteQues.setEnabled(false);
                                            viewHolder.DeleteQues.setVisibility(View.INVISIBLE);
                                            Toast.makeText(context, "Deleted !", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();


                                        }
                                    });
                                }catch (Exception e ){
                                    Toast.makeText(context, "check your connection.."+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this question?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
        viewHolder.Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context,CommentsActivity.class);
                commentIntent.putExtra("dep",department);
                commentIntent.putExtra("sem",semester);
                commentIntent.putExtra("QuestionID",questionID);
                commentIntent.putExtra("userOfQuestion",userHere);
                context.startActivity(commentIntent);
            }
        });







    }

    @Override
    public int getItemCount() {
        return ques_posts.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView Question;
        private CircleImageView userImage;
        private ImageButton DeleteQues;
        private ImageView Comment;
        private TextView userName;
        private TextView QuesDate;
        private TextView courseName2;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;


            DeleteQues=mView.findViewById(R.id.ques_single_delete);
            Comment=mView.findViewById(R.id.ques_reply);




        }


        public void setQuestionHere(String question)
        {
            Question=mView.findViewById(R.id.ques_single_desc);

            Question.setText(question);
        }
        public void setCourseName2(String Name)
        {
            courseName2=mView.findViewById(R.id.ques_single_course);
            courseName2.setText(Name);
        }

        public void set_time(String date)
        {
            QuesDate=mView.findViewById(R.id.ques_single_time);
            QuesDate.setText(date);
        }
        public void setuserDATA(String name, String image)
        {
            userName=mView.findViewById(R.id.ques_single_username);
            userImage=mView.findViewById(R.id.ques_single_prof);
            userName.setText(name);
            RequestOptions placeHolder=new RequestOptions();
            placeHolder.placeholder(R.drawable.profile);
            Glide.with(context).applyDefaultRequestOptions(placeHolder).load(image).into(userImage);

        }



    }


}
