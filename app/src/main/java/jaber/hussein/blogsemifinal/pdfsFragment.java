package jaber.hussein.blogsemifinal;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.List;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class pdfsFragment extends Fragment {
    private FloatingActionButton add_pdf;

    private String depar = "CCNE";
    private String semester = "1";
    private String lang = "English";
    private RecyclerView myRecForPdf;
    private List<pdf_post> pdf_posts;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private pdf_recy_adater pdf_recy_adapter;
//    private DocumentSnapshot LastVis;
    private EditText searchBox;


    public pdfsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pdfs, container, false);


        Spinner SpinnerYear = view.findViewById(R.id.pdf_semes);
        Spinner SpinnerLang = view.findViewById(R.id.pdf_Lang);
        Spinner SpinnerDep = view.findViewById(R.id.pdf_depar);
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


        add_pdf=view.findViewById(R.id.addPDF);
        add_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent= new Intent(getActivity(),AddPdf.class);
                startActivity(newintent);
            }
        });



        pdf_posts = new ArrayList<>();
        myRecForPdf = view.findViewById(R.id.PDFrec);
        pdf_recy_adapter = new pdf_recy_adater(pdf_posts);
        myRecForPdf.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecForPdf.setAdapter(pdf_recy_adapter);

        searchBox=view.findViewById(R.id.search_box);


        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();
//            try {
//
//
//
//                myRecForPdf.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                        super.onScrolled(recyclerView, dx, dy);
//                        Boolean reachefBottom = !myRecForPdf.canScrollVertically(1);
//                        if (reachefBottom) {
////                        Toast.makeText(getContext(), "Reached bottom", Toast.LENGTH_SHORT).show();
////                            loadMorepdf();
//                        }
//
//
//
//                    }
//                });
//            } catch (Exception ee) {
//
//            }

            Query FirstQuery = firebaseFirestore.collection("uni").document("fot")
                    .collection("pdfFiles")
                    .document(depar)
                    .collection(semester)
                    .orderBy("pdf_post_timestamp", Query.Direction.DESCENDING);
//                    .limit(3);


            FirstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {

                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                    try {
//                        LastVis = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
//                        numberLoop++;
//                    }catch (Exception eeee){}
                    try {
//                        Toast.makeText(getActivity(), ""+depar+semester, Toast.LENGTH_SHORT).show();
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {


                            final String pdfPostId = doc.getDocument().getId();
                            final pdf_post pdf_post_new = doc.getDocument().toObject(pdf_post.class);

                            if (doc.getType() == DocumentChange.Type.ADDED) {


                                firebaseFirestore.collection("Users").document(pdf_post_new.pdf_post_user)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
//                                                       Toast.makeText(getActivity(), "i am here", Toast.LENGTH_SHORT).show();
                                                    String user_namee = task.getResult().getString("name");
                                                    String user_image = task.getResult().getString("image");


                                                    pdf_post aaaaa = new pdf_post();
                                                    aaaaa.setPdf_post_description(pdf_post_new.pdf_post_description);
                                                    aaaaa.setPdf_post_timestamp(pdf_post_new.pdf_post_timestamp);
                                                    aaaaa.setPdf_post_user(pdf_post_new.pdf_post_user);
                                                    aaaaa.setPdf_post_user_image_url(user_image);
                                                    aaaaa.setPdfPostId(pdfPostId);
                                                    aaaaa.setPdf_post_user_name(user_namee);
                                                    aaaaa.setPdf_post_dep(pdf_post_new.pdf_post_dep);
                                                    aaaaa.setPdf_post_sem(pdf_post_new.pdf_post_sem);
                                                    aaaaa.setPdf_post_URL(pdf_post_new.pdf_post_URL);
                                                    aaaaa.pdf_name=pdf_post_new.pdf_name;
                                                    aaaaa.pdf_post_course=pdf_post_new.pdf_post_course;

                                                    boolean umm= false;

                                                    String SearchText=searchBox.getText().toString();
                                                    boolean searchIsEm=false;
                                                    if(SearchText.equals(""))
                                                    {
                                                        searchIsEm=true;
                                                    }


                                                    if(searchIsEm)
                                                    {
                                                        if (pdf_post_new.pdf_post_lang.equals(lang)) {
                                                        try {
//                                                        Toast.makeText(getActivity()   ,quesPostId , Toast.LENGTH_SHORT).show();


                                                            for (pdf_post a : pdf_posts) {
                                                                if (a.pdfPostId.equals(pdfPostId)) {
                                                                    umm = true;
                                                                }
                                                            }
                                                            if (!umm) {
                                                                pdf_posts.add(aaaaa);

                                                                pdf_recy_adapter.notifyDataSetChanged();

                                                            }
                                                        }catch (Exception e){}

                                                    }
                                                    }else
                                                    {
                                                        Boolean asss= Pattern.compile(Pattern.quote(SearchText), Pattern.CASE_INSENSITIVE).matcher(pdf_post_new.pdf_post_course).find();

                                                        if (pdf_post_new.pdf_post_lang.equals(lang)&&asss) {
                                                            try {
//                                                        Toast.makeText(getActivity()   ,quesPostId , Toast.LENGTH_SHORT).show();


                                                                for (pdf_post a : pdf_posts) {
                                                                    if (a.pdfPostId.equals(pdfPostId)) {
                                                                        umm = true;
                                                                    }
                                                                }
                                                                if (!umm) {
                                                                    pdf_posts.add(aaaaa);
                                                                    pdf_recy_adapter.notifyDataSetChanged();

                                                                }
                                                            }catch (Exception e){}

                                                        }
                                                    }


                                                    pdf_recy_adapter.notifyDataSetChanged();

                                                } else {
                                                    Toast.makeText(getActivity(), "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        });


                            }


                        }


                    } catch (Exception eee) {
                    Toast.makeText(getActivity()   , "Error"+eee.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
        return view;
    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        for(int i=0;i<5;i++){
////            Toast.makeText(getContext(), "aSDsad", Toast.LENGTH_SHORT).show();
//
//            new CountDownTimer(1000, 1000) {
//                public void onFinish() {
//                    // When timer is finished
//                    // Execute your code here
//                    if(pdf_posts.size()==0&&numberLoop>2)
//                    {
////                        loadMorepdf();
//
//                    }
//
//                }
//
//                public void onTick(long millisUntilFinished) {
//                    // millisUntilFinished    The amount of time until finished.
//                }
//            }.start();}
//
//    }

//    public void loadMorepdf() {
//        Query NextQuery = firebaseFirestore.collection("uni").document("fot")
//                .collection("pdfFiles")
//                .document(depar)
//                .collection(semester)
//                .orderBy("pdf_post_description", Query.Direction.DESCENDING)
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
//                        final String pdfPostId = doc.getDocument().getId();
//                        final pdf_post pdf_post_new = doc.getDocument().toObject(pdf_post.class);
//
//                        if (doc.getType() == DocumentChange.Type.ADDED) {
//
//
//                            firebaseFirestore.collection("Users").document(pdf_post_new.pdf_post_user)
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
//                                                pdf_post aaaaa = new pdf_post();
//                                                aaaaa.setPdf_post_description(pdf_post_new.pdf_post_description);
//                                                aaaaa.setPdf_post_timestamp(pdf_post_new.pdf_post_timestamp);
//                                                aaaaa.setPdf_post_user(pdf_post_new.pdf_post_user);
//                                                aaaaa.setPdf_post_user_image_url(user_image);
//                                                aaaaa.setPdfPostId(pdfPostId);
//                                                aaaaa.setPdf_post_user_name(user_namee);
//                                                aaaaa.setPdf_post_dep(pdf_post_new.pdf_post_dep);
//                                                aaaaa.setPdf_post_sem(pdf_post_new.pdf_post_sem);
//                                                aaaaa.setPdf_post_URL(pdf_post_new.pdf_post_URL);
//                                                aaaaa.pdf_name=pdf_post_new.pdf_name;
//                                                aaaaa.pdf_post_course=pdf_post_new.pdf_post_course;
//
//
//
//                                                boolean umm= false;
//
//                                                String SearchText=searchBox.getText().toString();
//                                                boolean searchIsEm=false;
//                                                if(SearchText.equals(""))
//                                                {
//                                                    searchIsEm=true;
//                                                }
//
//
//                                                if(searchIsEm)
//                                                {
//                                                    if (pdf_post_new.pdf_post_lang.equals(lang)) {
//                                                        try {
////                                                        Toast.makeText(getActivity()   ,quesPostId , Toast.LENGTH_SHORT).show();
//
//
//                                                            for (pdf_post a : pdf_posts) {
//                                                                if (a.pdfPostId.equals(pdfPostId)) {
//                                                                    umm = true;
//                                                                }
//                                                            }
//                                                            if (!umm) {
//                                                                pdf_posts.add(aaaaa);
//                                                                pdf_recy_adapter.notifyDataSetChanged();
//
//                                                            }
//                                                        }catch (Exception e){}
//
//                                                    }
//                                                }else
//                                                {
//                                                    Boolean asss= Pattern.compile(Pattern.quote(SearchText), Pattern.CASE_INSENSITIVE).matcher(pdf_post_new.pdf_post_course).find();
//
//                                                    if (pdf_post_new.pdf_post_lang.equals(lang)&&asss) {
//                                                        try {
////                                                        Toast.makeText(getActivity()   ,"rgfxdcgfdgf" , Toast.LENGTH_SHORT).show();
//
//
//                                                            for (pdf_post a : pdf_posts) {
//                                                                if (a.pdfPostId.equals(pdfPostId)) {
//                                                                    umm = true;
//                                                                }
//                                                            }
//                                                            if (!umm) {
//                                                                pdf_posts.add(aaaaa);
//                                                                pdf_recy_adapter.notifyDataSetChanged();
//
//                                                            }
//                                                        }catch (Exception e){}
//
//                                                    }
//                                                }
//
//
//                                            } else
//
//                                            {
//                                                Toast.makeText(getActivity(), "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                                            }
//
//
//                                        }
//                                    });
//
//
//                        }
//
//
//                    }
//
//
//                }  catch(
//                        Exception eee)
//
//                {
////                        Toast.makeText(getActivity()   , "Error"+eee.getMessage().toString(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//    }
//
    void scollUppp()
    {
//        myRecForQues.smoothScrollToPosition(0);

    }



}



