package jaber.hussein.blogsemifinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private EditText oldPass;
    private EditText newPass;
    private EditText  confrmNew;
    private Button ChangePassword1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar setupToolbar1= findViewById(R.id.toolbarForPassword);
        setSupportActionBar(setupToolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        newPass=findViewById(R.id.newPass);
        oldPass=findViewById(R.id.oldPass);
        confrmNew=findViewById(R.id.confirmNew);
        ChangePassword1=findViewById(R.id.button2);
        ChangePassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword1.setEnabled(false);
                if(!oldPass.getText().toString().equals("")&&!confrmNew.getText().toString().equals("")&&!newPass.getText().toString().equals(""))
                {
                    if(newPass.getText().toString().equals(confrmNew.getText().toString()))
                    {if(newPass.getText().toString().length()>5){
                        ChangePasswordHere();}
                        else
                    {
                        ChangePassword1.setEnabled(true);
                        Toast.makeText(ChangePassword.this, "Password must be more than 6 characters...", Toast.LENGTH_SHORT).show();
                    }
                    }
                    else
                    {
                        ChangePassword1.setEnabled(true);
                        Toast.makeText(ChangePassword.this, "Passwords must match..", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    ChangePassword1.setEnabled(true);
                    Toast.makeText(ChangePassword.this, "Please enter all data...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ChangePasswordHere() {try{

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPass.getText().toString());

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if(task.isSuccessful())
           {
               user.updatePassword(newPass.getText().toString())
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
//                                   Log.d(TAG, "User password updated.");
                                   Toast.makeText(ChangePassword.this, "Password Changed successfully  ", Toast.LENGTH_SHORT).show();
                                   ChangePassword1.setEnabled(true);
                                   finish();
                               }
                           }
                       });
           }
           else
           {
               ChangePassword1.setEnabled(true);
               Toast.makeText(ChangePassword.this, "old password in wrong..", Toast.LENGTH_SHORT).show();
           }
            }
        });}catch (Exception e)
    {
        ChangePassword1.setEnabled(true);
        Toast.makeText(this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home:
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
