package jaber.hussein.blogsemifinal;

import android.arch.core.executor.TaskExecutor;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.security.spec.ECField;
import java.util.concurrent.TimeUnit;

public class VerifyPhome extends AppCompatActivity {
    private Button verify;
    private EditText code;
    private String VerificationCode;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phome);
        verify=findViewById(R.id.verify_btn);
        code=findViewById(R.id.phone_txt);
        mAuth=FirebaseAuth.getInstance();
        String phoneNumber=getIntent().getStringExtra("phone");

        sendVerificationCode(""+phoneNumber);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String CodeHereToo=code.getText().toString();
                if(!CodeHereToo.isEmpty()&&code.length()>5){
                verifyCode(CodeHereToo);}


            }
        });


    }
    private void verifyCode(String code) {
        try {
//            Toast.makeText(this, "12321354523456246546546", Toast.LENGTH_SHORT).show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationCode, code);


            mAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Snackbar.make(findViewById(android.R.id.content), "Mobile Verified Successfully.",
                                Snackbar.LENGTH_SHORT).show();

                        finish();

                    } else {

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            //mVerificationField.setError("Invalid code.");
                            Snackbar.make(findViewById(android.R.id.content), "Invalid Code.",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerifyPhome.this,"signInWithCredential:failure"+task.getException(),
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationCode(String phone)
    {try{
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBacks
        );}catch (Exception e){
        Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            VerificationCode=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String CodeHere=phoneAuthCredential.getSmsCode();
            if(CodeHere!=null)
            {
                verifyCode(CodeHere);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhome.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };
}
