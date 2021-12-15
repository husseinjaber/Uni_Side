package jaber.hussein.blogsemifinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText  loginEmailText;
    private EditText LoginPassText;
    private Button LoginBtn;
    private Button LoginRegBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loginProg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmailText =(EditText) findViewById(R.id.regEmailText);
        LoginPassText = (EditText) findViewById(R.id.regPassText2);
        LoginBtn = (Button) findViewById(R.id.Signup_btn);
        LoginRegBtn = (Button) findViewById(R.id.login_reg_btn);
        loginProg = (ProgressBar) findViewById(R.id.loginProg);
        mAuth = FirebaseAuth.getInstance();
        LoginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent= new Intent(LoginActivity.this,RegActivity.class);
                startActivity(regIntent);

            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail=loginEmailText.getText().toString();
                String loginPass=LoginPassText.getText().toString();
                if(!TextUtils.isEmpty(loginEmail)&& !TextUtils.isEmpty(loginPass))
                {
                    try{


                    loginProg.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                sendtoMain();

                            }
                            else
                            {
                                String error=task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error:"+error,Toast.LENGTH_LONG).show();
                            }
                            loginProg.setVisibility(View.INVISIBLE);
                        }
                    });

                }catch (Exception e)
                    {
                        Toast.makeText(LoginActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            sendtoMain();
        }




    }
    public void sendtoMain()
    {
        Intent mainIntent= new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
