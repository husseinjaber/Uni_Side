package jaber.hussein.blogsemifinal;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddQues extends AppCompatActivity {
    private String semester;
    private String depar;
    private String lang;
    private EditText descrip;

    private Toolbar add_ques_toolbarr;
    private ProgressBar quesProgress;

    private Button add_ques;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private EditText myCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ques);
        descrip=findViewById(R.id.ques_desc);
        add_ques=findViewById(R.id.ques_add_btn);
        quesProgress=findViewById(R.id.ques_prog);
        myCourse=findViewById(R.id.quesCourseTitle);

            try
            {
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
            }catch (Exception e)
            {
                    Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        add_ques_toolbarr=findViewById(R.id.new_ques_toolbar);
        setSupportActionBar(add_ques_toolbarr);
        getSupportActionBar().setTitle("Add New Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        descrip.setMovementMethod(new ScrollingMovementMethod());

        Spinner SpinnerYear = findViewById(R.id.spinner_year2);
        Spinner SpinnerLang = findViewById(R.id.spinner_lang2);
        Spinner SpinnerDep=findViewById(R.id.spinner_dep2);

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
//                Toast.makeText(AddQues.this, semester, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinnerDep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depar=parent.getItemAtPosition(position).toString();
//                Toast.makeText(AddQues.this, depar, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lang=parent.getItemAtPosition(position).toString();
//                Toast.makeText(AddQues.this, lang, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add_ques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_ques.setEnabled(false);
                final String hi = descrip.getText().toString();
                final String course124321=myCourse.getText().toString();

//                final String mine = UUID.randomUUID().toString();

                if(!TextUtils.isEmpty(semester)&&!TextUtils.isEmpty(lang)&&!TextUtils.isEmpty(depar)&&!TextUtils.isEmpty(hi)&&!TextUtils.isEmpty(course124321))
                {
                    try {


                        quesProgress.setVisibility(View.VISIBLE);
                        Map<String, Object> quesMap = new HashMap<>();
                        quesMap.put("user", firebaseAuth.getCurrentUser().getUid());
                        quesMap.put("timestamp", FieldValue.serverTimestamp());
                        quesMap.put("description", hi);
                        quesMap.put("lang", lang);
                        quesMap.put("dep", depar);
                        quesMap.put("sem", semester);
                        quesMap.put("ques_post_course",course124321);
//                    quesMap.put("dontAddMe", mine);



                    firebaseFirestore.collection("uni").document("fot")
                            .collection("QuesAndAns")
                            .document(depar)
                            .collection(semester)
                            .add(quesMap)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(AddQues.this, "Question Added", Toast.LENGTH_SHORT).show();
                                        quesProgress.setVisibility(View.INVISIBLE);

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
                                        quesProgress.setVisibility(View.INVISIBLE);

                                        Toast.makeText(AddQues.this, "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });







                }catch (Exception eee){
                        Toast.makeText(AddQues.this, "Error"+eee.toString(), Toast.LENGTH_LONG).show();
                        quesProgress.setVisibility(View.INVISIBLE);
                        add_ques.setEnabled(true);

                    }
            }else
                {
                    Toast.makeText(AddQues.this, "Please Enter all data..", Toast.LENGTH_SHORT).show();
                    add_ques.setEnabled(true);
                }


            }
        });


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
