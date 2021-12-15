package jaber.hussein.blogsemifinal;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class hours extends AppCompatActivity {
    private Toolbar newPostToolbar;


    private ImageView hoursImage;
    private EditText hoursText;
    private Button hoursBtn;
    private ProgressBar hoursProg;
    private Uri myNewFuckinUri=null;

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours);

        newPostToolbar = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add 24Hours-Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        hoursImage=(ImageView) findViewById(R.id.new_post_image);
        hoursText=(EditText)findViewById(R.id.new_post_desc);
        hoursBtn=(Button)findViewById(R.id.post_btn);
        hoursProg=(ProgressBar)findViewById(R.id.hours_prog);

        try{storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();}catch (Exception e)
        {
            Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        hoursImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(hours.this);
            }
        });
        hoursBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoursBtn.setEnabled(false);
                final String desc = hoursText.getText().toString();
                if(!TextUtils.isEmpty(desc)&&myNewFuckinUri!=null)
                {
                    hoursProg.setVisibility(View.VISIBLE);
                    String a="24hoursPosts";
                    final String hello= UUID.randomUUID().toString();
                    StorageReference filePath = storageReference.child("fot").child(a).child(hello+".jpg");
                    filePath.putFile(myNewFuckinUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful())
                            {
                                try{
                                    String downloadURI = task.getResult().getDownloadUrl().toString();
                                    Map<String,Object> hoursmap = new HashMap<>();
                                    hoursmap.put("image_url",downloadURI);
                                    hoursmap.put("desc",desc);
                                    hoursmap.put("user",firebaseAuth.getCurrentUser().getUid());
                                    hoursmap.put("timestamp", FieldValue.serverTimestamp());
                                    hoursmap.put("image_name",hello+".jpg");






                                    firebaseFirestore.collection("uni").document("fot").collection("24hoursPosts").add(hoursmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(hours.this, "Posted !", Toast.LENGTH_SHORT).show();
                                                Intent mainIntent= new Intent(hours.this,MainActivity.class);
                                                hoursBtn.setEnabled(true);
                                                startActivity(mainIntent);
                                                finish();
                                            }else
                                            {
                                                Toast.makeText(hours.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                            hoursProg.setVisibility(View.INVISIBLE);
                                            hoursBtn.setEnabled(true);



                                        }
                                    });



                                }catch (Exception e)
                                {
                                    Toast.makeText(hours.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else
                            {
                                String errorr=task.getException().getMessage();
                                Toast.makeText(hours.this, "Error:"+errorr, Toast.LENGTH_SHORT).show();
                                hoursBtn.setEnabled(true);

                                hoursProg.setVisibility(View.INVISIBLE);
                            }


                        }
                    });


                }else{
                    Toast.makeText(hours.this, "Please enter the image and description", Toast.LENGTH_SHORT).show();
                    hoursBtn.setEnabled(true);

                }

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                myNewFuckinUri= result.getUri();
                hoursImage.setImageURI(myNewFuckinUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(hours.this,MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
//    public static String random() {
//        Random generator = new Random();
//        StringBuilder randomStringBuilder = new StringBuilder();
//        int randomLength = generator.nextInt(MAX_LENGTH);
//        char tempChar;
//        for (int i = 0; i < randomLength; i++){
//            tempChar = (char) (generator.nextInt(96) + 32);
//            randomStringBuilder.append(tempChar);
//        }
//        return randomStringBuilder.toString();
//    }
}
