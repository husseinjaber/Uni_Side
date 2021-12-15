package jaber.hussein.blogsemifinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class pdf_recy_adater extends RecyclerView.Adapter<pdf_recy_adater.ViewHolder> {
    public List<pdf_post > pdf_posts;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    String downloadURLforBtn="";
    public pdf_recy_adater(List<pdf_post> pdf_posts){
        this.pdf_posts=pdf_posts;

    }

    @NonNull
    @Override
    public pdf_recy_adater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_pdf,viewGroup,false  );
        context=viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();



        return new pdf_recy_adater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final pdf_recy_adater.ViewHolder viewHolder, final int i) {
        viewHolder.setIsRecyclable(false);


        final String currentUserIdForPdf= firebaseAuth.getCurrentUser().getUid();

        try {



            downloadURLforBtn=pdf_posts.get(i).pdf_post_URL;
            String desc_data = pdf_posts.get(i).pdf_post_description;
            viewHolder.setpdfDescHere(desc_data);
            String course_name="CourseName";
            course_name=pdf_posts.get(i).pdf_post_course;
            viewHolder.setCourseName(course_name);

            Date date_data = pdf_posts.get(i).pdf_post_timestamp;
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM HH:mm");
            String newFormatted = newFormat.format(date_data);
            viewHolder.set_time(newFormatted);
            String user_name_here = pdf_posts.get(i).pdf_post_user_name;
            String user_image_here = pdf_posts.get(i).pdf_post_user_image_url;
            viewHolder.setuserDATA(user_name_here, user_image_here);
            String user_id = pdf_posts.get(i).pdf_post_user;
            if (!user_id.equals(currentUserIdForPdf)) {
                viewHolder.DeletePdf.setEnabled(false);
                viewHolder.DeletePdf.setVisibility(View.INVISIBLE);

            } else if (user_id.equals(currentUserIdForPdf)) {
                viewHolder.DeletePdf.setEnabled(true);
                viewHolder.DeletePdf.setVisibility(View.VISIBLE);
            }
        }catch (Exception ee){
            Toast.makeText(context, "Please refresh the page", Toast.LENGTH_LONG).show();
        }
        final String department =pdf_posts.get(i).pdf_post_dep;
        final String semester = pdf_posts.get(i).pdf_post_sem;
        final String pdfID=pdf_posts.get(i).pdfPostId;


        viewHolder.DeletePdf.setOnClickListener(new View.OnClickListener() {
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
                                            .collection("pdfFiles")
                                            .document(department)
                                            .collection(semester)
                                            .document(pdfID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            StorageReference pathTodelete = storageReference.child("/fot/eduPosts/pdfFiles/" + pdf_posts.get(i).getPdf_post_dep()+"/"+pdf_posts.get(i).getPdf_post_sem()+"/"+pdf_posts.get(i).pdf_name);
                                            pathTodelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    pdf_posts.remove(i);
                                                    viewHolder.DeletePdf.setEnabled(false);
                                                    viewHolder.DeletePdf.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(context, "Deleted !", Toast.LENGTH_SHORT).show();
                                                    notifyDataSetChanged();
                                                }
                                            });





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
                builder.setMessage("Are you sure you want to delete this File?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


        viewHolder.DownloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadURLforBtn));
                context.startActivity(browserIntent);}catch(Exception e)
                {
                    Toast.makeText(context, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });







    }

    @Override
    public int getItemCount() {
        return pdf_posts.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView desc_pdf;
        private CircleImageView userImage;
        private ImageButton DeletePdf;
        private TextView userName;
        private TextView PdfDate;
        private Button DownloadPdf;
        private TextView courseName;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;


            DeletePdf=mView.findViewById(R.id.pdf_single_delete);
            DownloadPdf=mView.findViewById(R.id.downloadPDF);




        }
        public void setCourseName(String Name)
        {
            courseName=mView.findViewById(R.id.pdf_single_course);
            courseName.setText(Name);
        }


        public void setpdfDescHere(String pdf)
        {
            desc_pdf=mView.findViewById(R.id.pdf_single_desc);

            desc_pdf.setText(pdf);
        }
        public void set_time(String date)
        {
            PdfDate=mView.findViewById(R.id.pdf_single_time);
            PdfDate.setText(date);
        }
        public void setuserDATA(String name, String image)
        {
            userName=mView.findViewById(R.id.pdf_single_username);
            userImage=mView.findViewById(R.id.pdf_single_prof);
            userName.setText(name);
            RequestOptions placeHolder=new RequestOptions();
            placeHolder.placeholder(R.drawable.profile);
            Glide.with(context).applyDefaultRequestOptions(placeHolder).load(image).into(userImage);

        }





    }


}