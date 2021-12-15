package jaber.hussein.blogsemifinal;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;


public class SetuActivity extends AppCompatActivity {

    private CircleImageView proff;
    private Uri mainImage=null;
    private EditText Name;
    private boolean isChanged = false;
    private Button setup_btn;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private String phoneOnFireBase;

    private String phoneNumberrr=null;

    private EditText ForPhone;

    private String current_user_id;
    private String a;
    private String phonehere2;
    private String phoneHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setu);

//        toolbarforme= findViewById(R.id.Setuo_toolbar);
//        setSupportActionBar(toolbarforme);
//        getSupportActionBar().setTitle("account setupp");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ForPhone=findViewById(R.id.phoneNumber);

        progressBar= findViewById(R.id.setuo_pprog);
        proff=findViewById(R.id.prof);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        Name=findViewById(R.id.SetupName);
        setup_btn=findViewById(R.id.Setup_btn);
        setup_btn.setEnabled(false);
        user_id= firebaseAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);



        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        String name = task.getResult().getString("name");
                        String imagee= task.getResult().getString("image");
                        phoneOnFireBase=task.getResult().getString("phoneNumber");
                        mainImage=Uri.parse(imagee);
                        Name.setText(name);
                        ForPhone.setText(phoneOnFireBase);
                        setup_btn.setEnabled(true);

                        RequestOptions placeholder = new RequestOptions();
                        placeholder.placeholder(R.drawable.profile);
                        Glide.with(SetuActivity.this).applyDefaultRequestOptions(placeholder).load(imagee).into(proff);




                    }
                    else
                    {
//                        Toast.makeText(SetuActivity.this, "not exxx", Toast.LENGTH_SHORT).show();
                        setup_btn.setEnabled(true);
                    }
                    progressBar.setVisibility(View.INVISIBLE);

                }else
                {
                    Toast.makeText(SetuActivity.this, "ERROR:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }



            }
        });


        setup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name = Name.getText().toString();
                phoneHere=ForPhone.getText().toString();
                try {
                  a= Character.toString(phoneHere.charAt(0));
                    if(a.equals("0")&&phoneHere.length()==8)
                    {
                        phonehere2=phoneHere.substring(1);
                        phoneHere=phonehere2;
                        ForPhone.setText(phoneHere);
//                        Toast.makeText(SetuActivity.this, "done", Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){}





                String phoneandcode="+961"+phoneHere;
                try{phoneNumberrr=firebaseAuth.getCurrentUser().getPhoneNumber();}catch (Exception e){}

//                Toast.makeText(SetuActivity.this, ""+phoneNumberrr+"  "+phoneandcode, Toast.LENGTH_SHORT).show();


                progressBar.setVisibility(View.VISIBLE);
                setup_btn.setEnabled(false);


                if(!phoneHere.equals(phoneOnFireBase)&&!phoneandcode.equals(phoneNumberrr))
                {

                    if (phoneHere.length()==8||phoneHere.length()==7)
                    {
                        Intent newIntent= new Intent(SetuActivity.this,VerifyPhome.class);
                        newIntent.putExtra("phone","+961"+phoneHere);
                        startActivity(newIntent);
                        progressBar.setVisibility(View.INVISIBLE);
                        setup_btn.setEnabled(true);

                    }
                    else
                    {
                        Toast.makeText(SetuActivity.this, "check your info..", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        setup_btn.setEnabled(true);

                    }

                }else
                {
                    if(isChanged)
                    {

                        if (user_name.length()>4 && mainImage != null&&(phoneHere.length()==8||phoneHere.length()==7)) {


                            StorageReference image_path = storageReference.child("profile_image").child(user_id + ".jpg");
                            image_path.putFile(mainImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if (task.isSuccessful()) {

                                        storeFireStore(task, user_name,phoneHere);
                                        setup_btn.setEnabled(true);

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SetuActivity.this, "Error:" + error, Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        setup_btn.setEnabled(true);
                                    }


                                }
                            });
                        }else
                        {
                            Toast.makeText(SetuActivity.this, "UserName length must be greater than 4,and picture is not null. ", Toast.LENGTH_SHORT).show();
                            setup_btn.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);


                        }
                    }else
                    {

                        storeFireStore(null, user_name,phoneHere);
                        setup_btn.setClickable(true);

                    }
                }





            }
        });



        Toolbar setupToolbar= findViewById(R.id.Setuo_toolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account Setup");

        proff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
                {
                    if (ContextCompat.checkSelfPermission(SetuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(SetuActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(SetuActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


                    }else
                    {
                        BringImagePicker();
                    }





                }else
                {
                    BringImagePicker();
                }
            }
        });

    }

    private void storeFireStore(@NonNull Task<UploadTask.TaskSnapshot> task,String user_name,String phoneNumberHere) {
        try{

        Uri downloaded_uri;

        if(task != null)
       {

           downloaded_uri = task.getResult().getDownloadUrl();
       }else
       {
           downloaded_uri = mainImage;
       }



        final Map<String,String> userMap = new HashMap<>();
        userMap.put("name",user_name);
        userMap.put("uni","fot");
        userMap.put("phoneNumber",phoneNumberHere);

        userMap.put("image",downloaded_uri.toString());

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    firebaseFirestore.collection("uni").document("fot").collection("users")
                            .document(user_id)
                            .set(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(SetuActivity.this, "User settings are updated", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(SetuActivity.this,MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SetuActivity.this, "ERROR:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }




                        }
                    });

                }else
                {
                    Toast.makeText(SetuActivity.this, "ERROR:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }catch (Exception e)
        {
            Toast.makeText(this, "YOU MUST ENTER BOTH NAME AND PICTURE!", Toast.LENGTH_SHORT).show();
            setup_btn.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImage = result.getUri();
                proff.setImageURI(mainImage);
                isChanged=true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public void BringImagePicker()
    {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(SetuActivity.this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                current_user_id= firebaseAuth.getCurrentUser().getUid();
                firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().exists())
                            {
                                final String phoneHere=ForPhone.getText().toString();
                                try{phoneNumberrr=firebaseAuth.getCurrentUser().getPhoneNumber();}catch (Exception e){}

                                if(!phoneHere.equals(phoneNumberrr))
                                {
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which){
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    //Yes button clicked
                                                    Toast.makeText(SetuActivity.this, "Not Saved..", Toast.LENGTH_SHORT).show();
                                                    Intent setupIntent = new Intent(SetuActivity.this,MainActivity.class);
                                                    startActivity(setupIntent);
                                                    finish();

                                                    break;

                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    //No button clicked

                                                    break;
                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(SetuActivity.this);
                                    builder.setMessage("Are you sure you want to close without saving ?").setPositiveButton("Yes", dialogClickListener)
                                            .setNegativeButton("No", dialogClickListener).show();
                                }else
                                {
                                    Intent setupIntent = new Intent(SetuActivity.this,MainActivity.class);
                                    startActivity(setupIntent);
                                    finish();
                                }




                            }else
                            {
                                Toast.makeText(SetuActivity.this, "you must complete your account setup. ", Toast.LENGTH_SHORT).show();

                            }
                        }else
                        {
                            Toast.makeText(SetuActivity.this, "ERROR: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });

//                Intent intent = new Intent(SetuActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
