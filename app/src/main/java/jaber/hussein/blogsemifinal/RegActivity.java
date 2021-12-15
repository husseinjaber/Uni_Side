package jaber.hussein.blogsemifinal;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegActivity extends AppCompatActivity {
    private EditText reg_email;
    private EditText reg_pass;
    private  EditText reg_conf_pass;
    private Button reg_btn;
    private Button reg_login_button;
    private ProgressBar reg_prog;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        Spinner SpinnerUni= findViewById(R.id.yourUni);
        ArrayAdapter<CharSequence> adapterForUni= ArrayAdapter.createFromResource(this,R.array.unis,android.R.layout.simple_spinner_item);
        adapterForUni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerUni.setAdapter(adapterForUni);
        SpinnerUni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mAuth = FirebaseAuth.getInstance();
        reg_email=(EditText)findViewById(R.id.regEmailText);
        reg_pass=(EditText)findViewById(R.id.regPassText1);
        reg_conf_pass =(EditText)findViewById(R.id.regPassText2);
        reg_btn=(Button)findViewById(R.id.Signup_btn);
        reg_login_button = (Button)findViewById(R.id.login_reg);
        reg_prog = (ProgressBar) findViewById(R.id.signProg);
        reg_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= reg_email.getText().toString();
                String pas= reg_pass.getText().toString();
                String pass= reg_conf_pass.getText().toString();

                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pas)&&!TextUtils.isEmpty(pass))
                {
                    if(pas.equals(pass))
                    {try {
                        reg_prog.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent setuptt = new Intent(RegActivity.this, SetuActivity.class);
                                    startActivity(setuptt);
                                    finish();

                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(RegActivity.this, "ERROR..!! :+ " + error, Toast.LENGTH_LONG).show();

                                }


                                reg_prog.setVisibility(View.VISIBLE);
                            }

                        });

                    }catch (Exception e)
                    {
                        Toast.makeText(RegActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }else{
                        Toast.makeText(RegActivity.this,"passwords not matched",Toast.LENGTH_LONG).show();

                    }

                }

            }
        });

    }

    @Override
    protected void onStart() {


        super.onStart();
        FirebaseUser currentuser= mAuth.getCurrentUser();
        if(currentuser!=null)
        {
            sentomain();
        }


    }
    private void sentomain()
    {
        Intent mainIntent = new Intent(RegActivity.this,SetuActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
