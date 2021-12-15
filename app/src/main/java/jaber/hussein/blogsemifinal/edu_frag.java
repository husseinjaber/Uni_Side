package jaber.hussein.blogsemifinal;


import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class edu_frag extends Fragment {
    private BottomNavigationView edu_nav_bottom;
    private pdfsFragment pdfsFragmentt;
    private quesAndAns quesAndAnss;
    private ImageView refreshMe;




    public edu_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edu_frag, container, false);

        edu_nav_bottom=view.findViewById(R.id.eduNavBottom);
        pdfsFragmentt = new pdfsFragment();
        quesAndAnss = new quesAndAns();
        initializeFragment();
        refreshMe=view.findViewById(R.id.imageView3);



        refreshMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edu_nav_bottom.getSelectedItemId() == R.id.ques) {

                    try{

                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(quesAndAnss);
                        ft.attach(quesAndAnss);
                        ft.commit();
                    }catch (Exception e)
                    {
                        Toast.makeText(getContext(), "Error: please refresh the app", Toast.LENGTH_SHORT).show();
                    }
                }else if (edu_nav_bottom.getSelectedItemId() == R.id.files) {

                    try{

                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(pdfsFragmentt);
                        ft.attach(pdfsFragmentt);
                        ft.commit();
                    }catch (Exception e)
                    {
                        Toast.makeText(getContext(), "Error: please refresh the app", Toast.LENGTH_SHORT).show();
                    }



                }

            }
        });



        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

        try{

            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(quesAndAnss);
            ft.attach(quesAndAnss);
            ft.commit();

            FragmentTransaction ftt = getFragmentManager().beginTransaction();
            ftt.detach(pdfsFragmentt);
            ftt.attach(pdfsFragmentt);
            ftt.commit();






        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Error: please refresh the app", Toast.LENGTH_SHORT).show();
        }




        edu_nav_bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               switch (menuItem.getItemId())
               {
                   case R.id.files:
                       if (edu_nav_bottom.getSelectedItemId() == R.id.files) {

                           try{
                               pdfsFragmentt.scollUppp();
                               final FragmentTransaction ft = getFragmentManager().beginTransaction();
                               ft.detach(pdfsFragmentt);
                               ft.attach(pdfsFragmentt);
                               ft.commit();
                           }catch (Exception e)
                           {
                               Toast.makeText(getContext(), "Error: please refresh the app", Toast.LENGTH_SHORT).show();
                           }



                       } else
                       {
                           replaceFrag(pdfsFragmentt);
                       }
                       return true;
                   case R.id.ques:

                       if (edu_nav_bottom.getSelectedItemId() == R.id.ques) {

                                   try{
                                       quesAndAnss.scollUppp();
                                       final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                       ft.detach(quesAndAnss);
                                       ft.attach(quesAndAnss);
                                       ft.commit();
                                   }catch (Exception e)
                                   {
                                       Toast.makeText(getContext(), "Error: please refresh the app", Toast.LENGTH_SHORT).show();
                                   }



                       } else
                           {
                               replaceFrag(quesAndAnss);
                           }

                       return true;


               }

                return false;
            }
        });



    }





    private void replaceFrag(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (fragment == pdfsFragmentt) {
            fragmentTransaction.hide(quesAndAnss);
        }
        if (fragment == quesAndAnss) {
            fragmentTransaction.hide(pdfsFragmentt);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();

    }

    private void initializeFragment() {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();


        fragmentTransaction.add(R.id.edu_container, pdfsFragmentt);
        fragmentTransaction.add(R.id.edu_container,quesAndAnss);
        fragmentTransaction.commit();

        fragmentTransaction.hide(quesAndAnss);


        }
    public void refresh()
    {
        Toast.makeText(getContext(), "salndfmksaehdfjklsehfjkwef", Toast.LENGTH_SHORT).show();

    }


    }






