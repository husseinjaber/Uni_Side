package jaber.hussein.blogsemifinal;

import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar settings_toolBar;
    private Button AboutMeee;
    private Button ChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ChangePassword=findViewById(R.id.ChangePassword);
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToChange=new Intent(SettingsActivity.this,ChangePassword.class);
                startActivity(ToChange);

            }
        });

        settings_toolBar= findViewById(R.id.Settings_toolbar);
        setSupportActionBar(settings_toolBar);
        try {
            getSupportActionBar().setTitle("Settings");
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AboutMeee= findViewById(R.id.aboutUS);
        AboutMeee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(SettingsActivity.this,AboutMe.class);
                startActivity(newIntent);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {


            case android.R.id.home:
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;


            default: return super.onOptionsItemSelected(item);
        }




    }
}
