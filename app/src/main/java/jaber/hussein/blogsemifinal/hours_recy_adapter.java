package jaber.hussein.blogsemifinal;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.os.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

public class hours_recy_adapter extends RecyclerView.Adapter<hours_recy_adapter.ViewHolder>{
    public List<hours_post > hours_posts;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private  boolean doubleClick = false;



    public hours_recy_adapter(List<hours_post> hours_posts){
        this.hours_posts=hours_posts;

    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return 1;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_hours,viewGroup,false  );
        context=viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();



        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.setIsRecyclable(false);


        final String currentUserId= firebaseAuth.getCurrentUser().getUid();
        final String hoursPID= hours_posts.get(i).hoursPostId;


        try {


            String desc_data = hours_posts.get(i).getDesc();
            viewHolder.setDesc(desc_data);
            String image_url = hours_posts.get(i).getImage_url();
            viewHolder.setHoursImag(image_url);
            Date date_data = hours_posts.get(i).getTimestamp();
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM HH:mm");
            String newFormatted = newFormat.format(date_data);
            viewHolder.set_time(newFormatted);
            String user_name_here = hours_posts.get(i).getUser_name();
            String user_image_here = hours_posts.get(i).getUser_image_url();
            viewHolder.setuserDATAA(user_name_here, user_image_here);
            String user_id = hours_posts.get(i).getUser();
            if (!user_id.equals(currentUserId)) {
                viewHolder.Delete.setEnabled(false);
                viewHolder.Delete.setVisibility(View.INVISIBLE);

            } else if (user_id.equals(currentUserId)) {
                viewHolder.Delete.setEnabled(true);
                viewHolder.Delete.setVisibility(View.VISIBLE);
            }
        }catch (Exception ee){}


        firebaseFirestore.collection("uni").document("fot")
                .collection("24hoursPosts/"+ hoursPID+ "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                try {
                    if (!documentSnapshots.isEmpty()) {
                        viewHolder.updateLikes(documentSnapshots.size());


                    } else {
                        viewHolder.updateLikes(0);

                    }
                }catch (Exception et)
                {


                }
            }
        });


        firebaseFirestore.collection("uni").document("fot")
                .collection("24hoursPosts/"+ hoursPID + "/Likes").document(currentUserId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        try {

                            if (documentSnapshot.exists()) {
                                viewHolder.HoursLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.second_heart));
                            } else {
                                viewHolder.HoursLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.first_heart));
                            }
                        } catch (Exception er)
                        {

                        }

                    }
                });



        viewHolder.HoursLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "+++"+hoursPID, Toast.LENGTH_SHORT).show();
                firebaseFirestore.collection("uni").document("fot")
                        .collection("24hoursPosts")
                        .document(hoursPID)
                        .collection("Likes")
                        .document(currentUserId)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists())
                        {
                            Map<String,Object> likesMap= new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("uni").document("fot")
                                    .collection("24hoursPosts/"+ hoursPID + "/Likes").document(currentUserId)
                                    .set(likesMap);
                        }else
                        {
                            firebaseFirestore.collection("uni").document("fot")
                                    .collection("24hoursPosts/"+ hoursPID + "/Likes").document(currentUserId)
                                    .delete();
                        }



                    }
                });


            }
        });
        viewHolder.comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Coming soon..", Toast.LENGTH_SHORT).show();
            }
        });



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

                                    firebaseFirestore.collection("uni").document("fot").collection("24hoursPosts")
                                            .document(hoursPID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            StorageReference pathTodelete = storageReference.child("fot/24hoursPosts/" + hours_posts.get(i).image_name);
                                            pathTodelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    hours_posts.remove(i);
                                                    viewHolder.Delete.setEnabled(false);
                                                    viewHolder.Delete.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(context, "Deleted !", Toast.LENGTH_SHORT).show();
                                                    notifyDataSetChanged();
                                                }
                                            });


                                        }
                                    });
                                }catch (Exception e ){
                                    Toast.makeText(context, "check your connection..", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this post?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });

        viewHolder.HoursImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{


                Runnable r = new Runnable() {
                    @Override
                    public void run() {

                        doubleClick = false;
                    }
                };

                if (doubleClick) {

                    //your logic for double click action
                    firebaseFirestore.collection("uni").document("fot")
                            .collection("24hoursPosts")
                            .document(hoursPID)
                            .collection("Likes")
                            .document(currentUserId)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if(!task.getResult().exists())
                            {try {
                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());
                                viewHolder.Liked_single.setVisibility(View.VISIBLE);
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewHolder.Liked_single.setVisibility(View.INVISIBLE);
                                    }
                                }, 500);

                                firebaseFirestore.collection("uni").document("fot")
                                        .collection("24hoursPosts/" + hoursPID + "/Likes").document(currentUserId)
                                        .set(likesMap);

                            }catch (Exception e)
                            {
                                Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            }else
                            {
                                viewHolder.Liked_single.setVisibility(View.VISIBLE);
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewHolder.Liked_single.setVisibility(View.INVISIBLE);
                                    }
                                }, 500);

//                                firebaseFirestore.collection("uni").document("fot")
//                                        .collection("24hoursPosts/"+ hoursPID + "/Likes").document(currentUserId)
//                                        .delete();


//                                just nothing :)
                            }



                        }
                    });




                    doubleClick = false;

                }else {
                    doubleClick=true;
                    Handler handlerr=new Handler() ;
                    handlerr.postDelayed(r, 500);
                }

            }catch (Exception e)
                {
                    Toast.makeText(context, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
        }


        });





    }

    @Override
    public int getItemCount() {
//        return 1;
        return hours_posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView descView;
        private ImageView HoursImageView;
        private TextView hoursDate;
        private TextView userNamee;
        private CircleImageView userImagee;
        private ImageView HoursLikeBtn;
        private TextView HourslikeCount;
        private ImageView comment_btn;
        private ImageButton Delete;
        private ImageView Liked_single;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            HoursLikeBtn=mView.findViewById(R.id.hours_single_like);
            comment_btn=mView.findViewById(R.id.comment_single);
            Delete=mView.findViewById(R.id.hours_single_delete);
            Liked_single=mView.findViewById(R.id.liked_single);
        }
        public void setDesc(String descText)
        {
            descView = mView.findViewById(R.id.hours_single_desc);
            descView.setText(descText);
        }
        public void setHoursImag(String DownloadUri)
        {
            HoursImageView = mView.findViewById(R.id.hours_single_mage);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.imag_holder);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(DownloadUri).into(HoursImageView);
        }
        public void set_time(String date)
        {
            hoursDate=mView.findViewById(R.id.hours_single_time);
            hoursDate.setText(date);


        }
        //        public void serUserData(String name,String image)
//        {
//
//            userNamee=mView.findViewById(R.id.hours_single_username);
//            userImagee=mView.findViewById(R.id.hours_single_prof);
//            userNamee.setText(name);
//
//            RequestOptions placeHolder=new RequestOptions();
//            placeHolder.placeholder(R.drawable.profile);
//
//            Glide.with(context).applyDefaultRequestOptions(placeHolder).load(image).into(userImagee);
//        }
        public void setuserDATAA(String name, String image)
        {
            userNamee=mView.findViewById(R.id.hours_single_username);
            userImagee=mView.findViewById(R.id.hours_single_prof);
            userNamee.setText(name);
            RequestOptions placeHolder=new RequestOptions();
            placeHolder.placeholder(R.drawable.profile);
            Glide.with(context).applyDefaultRequestOptions(placeHolder).load(image).into(userImagee);

        }
        public void updateLikes(int count)
        {
            HourslikeCount = mView.findViewById(R.id.hours_single_likeCount);
            HourslikeCount.setText(count+" Likes");
        }


    }
}
