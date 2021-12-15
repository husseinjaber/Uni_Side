package jaber.hussein.blogsemifinal;


import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton addPost;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    //    private boolean aaaa=false;
    private BottomNavigationView mainNav;


    private hours_frag hours_fragg;
    private edu_frag edu_fragg;
    private notif_frag notif_fragg;
    private spons_frag spons_fragg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainNav = (BottomNavigationView) findViewById(R.id.main_nav);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("UniSide");


        //Fragments here
        if (mAuth.getCurrentUser() != null) {


            mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.edu_id:
                            replaceFrag(edu_fragg);
                            return true;
                        case R.id.hours_id:
                            if (mainNav.getSelectedItemId() == R.id.hours_id) {


                                hours_fragg.scollUppp();

                                new CountDownTimer(1500, 1000) {
                                    public void onFinish() {
                                        // When timer is finished
                                        // Execute your code here
                                        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                        ft.detach(hours_fragg);
                                        ft.attach(hours_fragg);
                                        ft.commit();

                                    }

                                    public void onTick(long millisUntilFinished) {
                                        // millisUntilFinished    The amount of time until finished.
                                    }
                                }.start();


                            } else {
                                replaceFrag(hours_fragg);
                            }
                            return true;
                        case R.id.notf_id:
                            replaceFrag(notif_fragg);
                            return true;

                        case R.id.spons_id:
                            replaceFrag(spons_fragg);
                            return true;
                        default:
                            return false;
                    }


                }
            });

            notif_fragg = new notif_frag();
            edu_fragg = new edu_frag();
            hours_fragg = new hours_frag();

            spons_fragg = new spons_frag();
            initializeFragment();


            //till here frag


//        addPost=findViewById(R.id.addPost);
//        addPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent new_post= new Intent(MainActivity.this,Post.class);
//                startActivity(new_post);
//
//            }
//        });


        }

    }



    @Override
    protected void onStart() {
        super.onStart();
//        addPost.setEnabled(aaaa);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendtoLogin();
        } else {
            current_user_id = mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            Intent setupIntent = new Intent(MainActivity.this, SetuActivity.class);
                            startActivity(setupIntent);
                            finish();
                        } else {
//                            aaaa=true;
//                            addPost.setEnabled(aaaa);
                            reIntialize();


                        }
                    } else {
                        String errorMess = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "ERROR: " + errorMess, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut();
                return true;
            case R.id.action_settings_acc:
                Intent intentt = new Intent(MainActivity.this, SetuActivity.class);
                startActivity(intentt);

                return true;
            case R.id.settings:
                Intent  intenttt= new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intenttt);

                return true;
            default:
                return false;

        }


    }

    public void logOut() {
        mAuth.signOut();
        sendtoLogin();


    }

    public void sendtoLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void replaceFrag(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == hours_fragg) {
            fragmentTransaction.hide(spons_fragg);

            fragmentTransaction.hide(edu_fragg);
            fragmentTransaction.hide(notif_fragg);
        }
        if (fragment == edu_fragg) {
            fragmentTransaction.hide(hours_fragg);
            fragmentTransaction.hide(spons_fragg);

            fragmentTransaction.hide(notif_fragg);

        }
        if (fragment == spons_fragg) {
            fragmentTransaction.hide(hours_fragg);
            fragmentTransaction.hide(edu_fragg);
            fragmentTransaction.hide(notif_fragg);


        }
        if (fragment == notif_fragg) {
            fragmentTransaction.hide(hours_fragg);
            fragmentTransaction.hide(edu_fragg);
            fragmentTransaction.hide(spons_fragg);


        }

        fragmentTransaction.show(fragment);

        fragmentTransaction.commit();

    }

    private void initializeFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_container, hours_fragg);
        fragmentTransaction.add(R.id.main_container, edu_fragg);
        fragmentTransaction.add(R.id.main_container, spons_fragg);
        fragmentTransaction.add(R.id.main_container, notif_fragg);


        fragmentTransaction.show(hours_fragg);


        fragmentTransaction.hide(notif_fragg);

        fragmentTransaction.hide(spons_fragg);
        fragmentTransaction.hide(edu_fragg);

        fragmentTransaction.commit();

        new CountDownTimer(300, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(hours_fragg);
                ft.attach(hours_fragg);
                ft.commit();



            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();



    }

    private void reIntialize() {
        if(mainNav.getSelectedItemId() == R.id.hours_id){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(notif_fragg);
        fragmentTransaction.hide(spons_fragg);
        fragmentTransaction.hide(edu_fragg);
        fragmentTransaction.commit();


        new CountDownTimer(300, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(hours_fragg);
                ft.attach(hours_fragg);
                ft.commit();

            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

    }
    }

}
