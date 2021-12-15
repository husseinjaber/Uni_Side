package jaber.hussein.blogsemifinal;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    public List<SingleComment > singleComment;

    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;




    public CommentAdapter(List<SingleComment> singleComment){
        this.singleComment=singleComment;

    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return 1;
//    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_comment,viewGroup,false  );
        context=viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();



        return new CommentAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.setIsRecyclable(false);


        final String currentUserId= firebaseAuth.getCurrentUser().getUid();
//        final String hoursPID= hours_posts.get(i).hoursPostId;


        try {
            final String department =singleComment.get(i).getDepar();
            final String semester =singleComment.get(i).getSemester();
            final String questionID =singleComment.get(i).getQuestionID();
            final String commmentID =singleComment.get(i).getCommentID();


            final String comment_data=singleComment.get(i).getCommentText();
            viewHolder.setDesc(comment_data);
            Date date_data = singleComment.get(i).getCommentDate();
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM HH:mm");
            String newFormatted = newFormat.format(date_data);
            viewHolder.set_time(newFormatted);
            String user_name_here = singleComment.get(i).getCommentUserName();
            String user_image_here = singleComment.get(i).getCommentUserImaege();
            viewHolder.setuserDATAA(user_name_here, user_image_here);
            String user_id = singleComment.get(i).getCommentUserID();
            if (!user_id.equals(currentUserId)) {
                viewHolder.Delete.setEnabled(false);
                viewHolder.Delete.setVisibility(View.INVISIBLE);

            } else if (user_id.equals(currentUserId)) {
                viewHolder.Delete.setEnabled(true);
                viewHolder.Delete.setVisibility(View.VISIBLE);
            }
            viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
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
                                                .document(questionID)
                                                .collection("comments")
                                                .document(commmentID)
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                singleComment.remove(i);
                                                viewHolder.Delete.setEnabled(false);
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
                    builder.setMessage("Are you sure you want to delete this comment  ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });

        }catch (Exception ee){}


        


    }

    @Override
    public int getItemCount() {
//        return 1;
        return singleComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView descView;
        private TextView commentDate;
        private TextView userNamee;
        private CircleImageView userImagee;
        private ImageButton Delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            Delete=mView.findViewById(R.id.comment_single_delete);
        }
        public void setDesc(String descText)
        {
            descView = mView.findViewById(R.id.comment_details);
            descView.setText(descText);
        }
        public void set_time(String date)
        {
            commentDate=mView.findViewById(R.id.comment_single_time);
            commentDate.setText(date);


        }
        public void setuserDATAA(String name, String image)
        {
            userNamee=mView.findViewById(R.id.comment_single_username);
            userImagee=mView.findViewById(R.id.comment_single_prof);
            userNamee.setText(name);
            RequestOptions placeHolder=new RequestOptions();
            placeHolder.placeholder(R.drawable.profile);
            Glide.with(context).applyDefaultRequestOptions(placeHolder).load(image).into(userImagee);

        }



    }
}
