package jaber.hussein.blogsemifinal;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class quesAndAns extends Fragment {
    private FloatingActionButton add_ques;
    private String depar = "CCNE";
    private String semester = "1";
    private String lang = "English";
    private EditText searchBox;



    private RecyclerView myRecForQues;
    private List<ques_post> ques_posts;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private ques_recy_adapter ques_recy_adapter1;


    private DocumentSnapshot LastVis;


    public quesAndAns() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ques_and_ans, container, false);
        searchBox=view.findViewById(R.id.search_box_ques);



//





        Spinner SpinnerYear = view.findViewById(R.id.ques_semes);
        Spinner SpinnerLang = view.findViewById(R.id.ques_Lang);
        Spinner SpinnerDep = view.findViewById(R.id.ques_depar);
//        refresh_questions=view.findViewById(R.id.refreshQuestions);
//        refresh_questions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = getChildFragmentManager().findFragmentById(R.id.ques);
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.detach(fragment);
//                fragmentTransaction.attach(fragment);
//                fragmentTransaction.commit();
//
//            }
//        });

        ArrayAdapter<CharSequence> adapterForLang = ArrayAdapter.createFromResource(getContext(), R.array.lang, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterForDep = ArrayAdapter.createFromResource(getContext(), R.array.department, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterForYear = ArrayAdapter.createFromResource(getContext(), R.array.semester, android.R.layout.simple_spinner_item);
        adapterForYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterForDep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterForLang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerYear.setAdapter(adapterForYear);
        SpinnerDep.setAdapter(adapterForDep);
        SpinnerLang.setAdapter(adapterForLang);


        SpinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester = parent.getItemAtPosition(position).toString();
//                Toast.makeText(AddQues.this, semester, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinnerDep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depar = parent.getItemAtPosition(position).toString();
//                Toast.makeText(AddQues.this, depar, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lang = parent.getItemAtPosition(position).toString();
//                Toast.makeText(AddQues.this, lang, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        add_ques = view.findViewById(R.id.addQUES);
        add_ques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent = new Intent(getActivity(), AddQues.class);
                startActivity(newintent);
            }
        });


        ques_posts = new ArrayList<>();
        myRecForQues = view.findViewById(R.id.QUES_rec);
        ques_recy_adapter1 = new ques_recy_adapter(ques_posts);
        myRecForQues.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecForQues.setAdapter(ques_recy_adapter1);


        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();
//            try {
//
//
//
//                myRecForQues.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                        super.onScrolled(recyclerView, dx, dy);
//                        Boolean reachefBottom = !myRecForQues.canScrollVertically(1);
//                        if (reachefBottom) {
////                        Toast.makeText(getContext(), "Reached bottom", Toast.LENGTH_SHORT).show();
//                            loadMoreQuestions();
//                        }
//
//
//                    }
//                });
//            } catch (Exception ee) {
//
//            }

            Query FirstQuery = firebaseFirestore.collection("uni").document("fot")
                    .collection("QuesAndAns")
                    .document(depar)
                    .collection(semester)
                    .orderBy("timestamp", Query.Direction.DESCENDING);
//                    .limit(3);


            FirstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {

                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    try {
//                    Toast.makeText(getActivity(), "11", Toast.LENGTH_SHORT).show();


                        LastVis = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);


                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            final String quesPostId = doc.getDocument().getId();
                            final ques_post ques_post_new = doc.getDocument().toObject(ques_post.class);

                            if (doc.getType() == DocumentChange.Type.ADDED) {


                                firebaseFirestore.collection("Users").document(ques_post_new.user)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
//                                                       Toast.makeText(getActivity(), "i am here", Toast.LENGTH_SHORT).show();
                                                    String user_namee = task.getResult().getString("name");
                                                    String user_image = task.getResult().getString("image");


                                                    ques_post aaaaa = new ques_post();
                                                    aaaaa.setDescription(ques_post_new.description);
                                                    aaaaa.setTimestamp(ques_post_new.timestamp);
                                                    aaaaa.setUser(ques_post_new.user);
                                                    aaaaa.user_image_url_ques = user_image;
                                                    aaaaa.QuesPostId = quesPostId;
                                                    aaaaa.user_name_ques = user_namee;
                                                    aaaaa.dep=ques_post_new.dep;
                                                    aaaaa.sem=ques_post_new.sem;
                                                    aaaaa.ques_post_course=ques_post_new.ques_post_course;
                                                        boolean umm= false;



                                                    String SearchText=searchBox.getText().toString();
                                                    boolean searchIsEm=false;
                                                    if(SearchText.equals(""))
                                                    {
                                                        searchIsEm=true;
                                                    }
//                                                        for(ques_post a:ques_posts)
//                                                        {
//                                                            if()
//                                                                umm=true;
//                                                        }


//                                                        if (!umm) {

                                                    if(searchIsEm)
                                                    {
                                                        if (ques_post_new.lang.equals(lang)) {
                                                            try {


                                                                for (ques_post a : ques_posts) {
//
                                                                    if (a.QuesPostId.equals(quesPostId)) {
                                                                        umm = true;
                                                                    }
                                                                }
                                                                if (!umm) {
                                                                    ques_posts.add(aaaaa);
                                                                    ques_recy_adapter1.notifyDataSetChanged();

                                                                }
                                                            }catch (Exception e){}

                                                        }
                                                    }
                                                    else
                                                    {
                                                        Boolean asss= Pattern.compile(Pattern.quote(SearchText), Pattern.CASE_INSENSITIVE).matcher(ques_post_new.ques_post_course).find();
                                                        if (ques_post_new.lang.equals(lang)&&asss) {
                                                            try {


                                                                for (ques_post a : ques_posts) {
//
                                                                    if (a.QuesPostId.equals(quesPostId)) {
                                                                        umm = true;
                                                                    }
                                                                }
                                                                if (!umm) {
                                                                    ques_posts.add(aaaaa);
                                                                    ques_recy_adapter1.notifyDataSetChanged();

                                                                }
                                                            }catch (Exception e){}

                                                        }
                                                    }



                                                    ques_recy_adapter1.notifyDataSetChanged();

                                                } else {
                                                    Toast.makeText(getActivity(), "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        });


                            }


                        }


                    } catch (Exception eee) {
//                    Toast.makeText(getActivity()   , "Error"+eee.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
        return view;
    }

//    public void loadMoreQuestions() {
//        Query NextQuery = firebaseFirestore.collection("uni").document("fot")
//                .collection("QuesAndAns")
//                .document(depar)
//                .collection(semester)
//                .orderBy("timestamp", Query.Direction.DESCENDING)
//                .startAfter(LastVis)
//                .limit(3);
//
//
//        NextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                try {
////                    Toast.makeText(getActivity(), "11", Toast.LENGTH_SHORT).show();
//
//
//                    LastVis = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
//
//
//                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
//
//                        final String quesPostId = doc.getDocument().getId();
//                        final ques_post ques_post_new = doc.getDocument().toObject(ques_post.class);
//
//                        if (doc.getType() == DocumentChange.Type.ADDED) {
//
//
//                            firebaseFirestore.collection("Users").document(ques_post_new.user)
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                            if (task.isSuccessful()) {
////                                                Toast.makeText(getActivity(), "i am here", Toast.LENGTH_SHORT).show();
//                                                String user_namee = task.getResult().getString("name");
//                                                String user_image = task.getResult().getString("image");
//
//
//                                                ques_post aaaaa = new ques_post();
//                                                aaaaa.setDescription(ques_post_new.description);
//                                                aaaaa.setTimestamp(ques_post_new.timestamp);
//                                                aaaaa.setUser(ques_post_new.user);
//                                                aaaaa.user_image_url_ques = user_image;
//                                                aaaaa.QuesPostId = quesPostId;
//                                                aaaaa.user_name_ques = user_namee;
//                                                aaaaa.dep=ques_post_new.dep;
//                                                aaaaa.sem=ques_post_new.sem;
//
//                                                        boolean umm= false;
//
//
//                                                if (ques_post_new.lang.equals(lang)) {
//                                                    try {
////                                                        Toast.makeText(getActivity()   ,quesPostId , Toast.LENGTH_SHORT).show();
//
//
//                                                        for (ques_post a : ques_posts) {
//                                                            if (a.QuesPostId.equals(quesPostId)) {
//                                                                umm = true;
//                                                            }
//                                                        }
//                                                        if (!umm) {
//                                                            ques_posts.add(aaaaa);
//                                                            ques_recy_adapter1.notifyDataSetChanged();
//
//                                                        }
//                                                    }catch (Exception e){
//                                                        Toast.makeText(getActivity(), ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                    ques_recy_adapter1.notifyDataSetChanged();
//
//
//                                                }
//
//                                        } else
//
//                                        {
//                                            Toast.makeText(getActivity(), "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                                        }
//
//
//                                    }
//                        });
//
//
//                    }
//
//
//                }
//
//
//            }  catch(
//            Exception eee)
//
//            {
////                        Toast.makeText(getActivity()   , "Error"+eee.getMessage().toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    });
//}

    void scollUppp()
    {
        myRecForQues.smoothScrollToPosition(0);

    }



    }


