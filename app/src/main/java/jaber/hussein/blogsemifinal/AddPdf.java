package jaber.hussein.blogsemifinal;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddPdf extends AppCompatActivity {
    private String semester;
    private String depar;
    private String lang;


    private Toolbar add_pdf_toolbarr;
    private Button importFile;
    private Button addFile;


    private ProgressBar myfuckingProgressbar;

    Uri pdfURI;
    private TextView UriHere;

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    private EditText description;
    private EditText CourseHere;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pdf);
        add_pdf_toolbarr=findViewById(R.id.new_pdf_toolbar);
        setSupportActionBar(add_pdf_toolbarr);
        getSupportActionBar().setTitle("Add New Files");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        description=findViewById(R.id.pdf_desc);
        myfuckingProgressbar=findViewById(R.id.pdf_prog);
        CourseHere=findViewById(R.id.pdfCourseTitle);

        addFile=findViewById(R.id.pfd_add_btn2);
        importFile=findViewById(R.id.pfd_import_file_btn);
        UriHere=findViewById(R.id.pdf_file_name);

        try{storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();}catch
                (Exception e)
        {
            Toast.makeText(this, "Error:"+e.getMessage()    , Toast.LENGTH_SHORT).show();
        }


        Spinner SpinnerYear = findViewById(R.id.spinner_year);
        Spinner SpinnerLang = findViewById(R.id.spinner_lang);
        Spinner SpinnerDep=findViewById(R.id.spinner_dep);

        ArrayAdapter<CharSequence> adapterForLang= ArrayAdapter.createFromResource(this,R.array.lang,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterForDep= ArrayAdapter.createFromResource(this,R.array.department,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterForYear= ArrayAdapter.createFromResource(this,R.array.semester,android.R.layout.simple_spinner_item);
        adapterForYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterForDep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterForLang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerYear.setAdapter(adapterForYear);
        SpinnerDep.setAdapter(adapterForDep);
        SpinnerLang.setAdapter(adapterForLang);

        SpinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester=parent.getItemAtPosition(position).toString();
//                Toast.makeText(AddPdf.this, semester, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinnerDep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depar=parent.getItemAtPosition(position).toString();
//                Toast.makeText(AddPdf.this, depar, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lang=parent.getItemAtPosition(position).toString();
//                Toast.makeText(AddPdf.this, lang, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String desc =description.getText().toString();
                final String course=CourseHere.getText().toString();
                if(!TextUtils.isEmpty(desc)&&pdfURI!=null&&!TextUtils.isEmpty(course))
                {
                    addFile.setEnabled(false);
                    myfuckingProgressbar.setVisibility(View.VISIBLE);
//                    hoursProg.setVisibility(View.VISIBLE);
                    String a="pdfFiles";
                    final String hello= UUID.randomUUID().toString();
                    StorageReference filePath = storageReference.child("fot").child("eduPosts").child(a).child(depar).child(semester).child(hello+".pdf");
                    filePath.putFile(pdfURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful())
                            {try{
                                String downloadURI = task.getResult().getDownloadUrl().toString();
                                Map<String,Object> pdfMap = new HashMap<>();
                                pdfMap.put("pdf_post_URL",downloadURI);
                                pdfMap.put("pdf_post_description",desc);
                                pdfMap.put("pdf_post_user",firebaseAuth.getCurrentUser().getUid());
                                pdfMap.put("pdf_post_timestamp", FieldValue.serverTimestamp());
                                pdfMap.put("pdf_name",hello+".pdf");
                                pdfMap.put("pdf_post_lang",lang);
                                pdfMap.put("pdf_post_dep",depar);
                                pdfMap.put("pdf_post_sem",semester);
                                pdfMap.put("pdf_post_course",course);

                                firebaseFirestore.collection("uni")
                                        .document("fot")
                                        .collection("pdfFiles")
                                        .document(depar)
                                        .collection(semester)
                                        .add(pdfMap)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(AddPdf.this, "Added !", Toast.LENGTH_SHORT).show();
//                                          hoursBtn.setEnabled(true);
                                          finish();
                                            myfuckingProgressbar.setVisibility(View.INVISIBLE);
                                            addFile.setEnabled(true);
                                        }else
                                        {
                                            Toast.makeText(AddPdf.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            myfuckingProgressbar.setVisibility(View.INVISIBLE);
                                            addFile.setEnabled(true);
                                        }
//                                        hoursProg.setVisibility(View.INVISIBLE);
//                                        hoursBtn.setEnabled(true);



                                    }
                                });



                            }catch
                            (Exception e)
                            {
                                Toast.makeText(AddPdf.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            }else
                            {
                                String errorr=task.getException().getMessage();
                                Toast.makeText(AddPdf.this, "Error:"+errorr, Toast.LENGTH_SHORT).show();
//                                hoursBtn.setEnabled(true);
                                myfuckingProgressbar.setVisibility(View.INVISIBLE);
                                addFile.setEnabled(true);

//                                hoursProg.setVisibility(View.INVISIBLE);
                            }


                        }
                    });


                }else{
                    Toast.makeText(AddPdf.this, "Please enter all info", Toast.LENGTH_SHORT).show();
//                    hoursBtn.setEnabled(true);

                }


            }
        });


        importFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });








    }
    private void selectPDF()
    {
        try
        {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"open with"),86);

        }catch(Exception e)
        {
            Toast.makeText(this, "You need to install a pdf viewer Play Store", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==86&&resultCode==RESULT_OK&&data!=null)
        {
            pdfURI=data.getData();
            UriHere.setText(pdfURI.toString());

        }else
        {
            Toast.makeText(this, "Please Select a File", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

//                Intent intent = new Intent(AddPdf.this,MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




//!TextUtils.isEmpty(desc)
