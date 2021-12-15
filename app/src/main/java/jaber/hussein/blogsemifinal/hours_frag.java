package jaber.hussein.blogsemifinal;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class hours_frag extends Fragment {
    private RecyclerView myRec;
    private List<hours_post> hours_posts;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private hours_recy_adapter hours_recy_adapter1;
    private FloatingActionButton add_hours;
    private DocumentSnapshot LastVis;
    private String user_id_here;
    private StorageReference storageReference;
    private long timeNow;





    public hours_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hours_frag,container,false);

        add_hours=view.findViewById(R.id.addHours);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        try
        {
            user_id_here= firebaseAuth.getCurrentUser().getUid();
            Map<String, Object> TimeUserIn = new HashMap<>();
            TimeUserIn.put("TimeUserNow", FieldValue.serverTimestamp());
            firebaseFirestore.collection("Users")
                    .document(user_id_here).update(TimeUserIn).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    firebaseFirestore.collection("Users").document(user_id_here).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                if(task.getResult().exists())
                                {
                                    try {
                                        Date here = task.getResult().getDate("TimeUserNow");
                                        timeNow = here.getTime();
//                                        Toast.makeText(getContext(), "line 108 hours frag"+timeNow, Toast.LENGTH_SHORT).show();


                                    }catch (Exception e)
                                    {
//                                        Toast.makeText(getContext(), "Errorrrrrrrrrrrrrrrrrrrrrr:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
//                        Toast.makeText(SetuActivity.this, "not exxx", Toast.LENGTH_SHORT).show();
                                }

                            }else
                            {
                                Toast.makeText(getContext(), "ERROR:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }



                        }
                    });

                }
            });

        }catch (Exception e)
        {
            Toast.makeText(getActivity()    , "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        add_hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent= new Intent(getActivity(),hours.class);
                startActivity(newintent);
            }
        });
        hours_posts = new ArrayList<>();
        myRec=view.findViewById(R.id.my_rec);
        hours_recy_adapter1 = new hours_recy_adapter(hours_posts);
        myRec.setLayoutManager( new LinearLayoutManager(getActivity()));
        myRec.setAdapter(hours_recy_adapter1);



//        myRec.setItemViewCacheSize(100);
//        myRec.setHasFixedSize(true);
//        myRec.setDrawingCacheEnabled(true);
//        myRec.getRecycledViewPool().setMaxRecycledViews(1,11);





        if(firebaseAuth.getCurrentUser() != null) {


            try {


            myRec.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean reachefBottom = ! myRec.canScrollVertically(1);
                    if(reachefBottom)
                    {
//                        Toast.makeText(container.getContext(), "Reached bottom", Toast.LENGTH_SHORT).show();
                        loadMoreHours();
                    }


                }
            });
            }
            catch (Exception ee)
            {

            }

            Query FirstQuery = firebaseFirestore.collection("uni").document("fot")
                    .collection("24hoursPosts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(3);
            FirstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    try{



                            LastVis = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);





                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            final String hoursPostId=doc.getDocument().getId();
//                        Toast.makeText(getActivity()   , "+++ "+hoursPostId, Toast.LENGTH_SHORT).show();
                            final hours_post hours_post_new = doc.getDocument().toObject(hours_post.class);

//
//                            Calendar cal= Calendar.getInstance();
//                            final Date dateHere=cal.getTime();
//                        Toast.makeText(getActivity(), "+++++"+((hours_post_new.timestamp).getTime()-dateHere.getTime()), Toast.LENGTH_SHORT).show();

//
//
//
                            if (timeNow-((hours_post_new.timestamp).getTime())<24*60*60*1000) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {


                                    firebaseFirestore.collection("Users").document(hours_post_new.user)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
//                                        Toast.makeText(getActivity(), "i am here", Toast.LENGTH_SHORT).show();

                                                        DocumentSnapshot documentHere=task.getResult();
                                                        if(documentHere.exists())
                                                        {
                                                            String user_namee = task.getResult().getString("name");
                                                            String user_image = task.getResult().getString("image");


                                                            hours_post aaaaa = new hours_post();
                                                            aaaaa.setDesc(hours_post_new.desc);
                                                            aaaaa.setImage_name(hours_post_new.image_name);
                                                            aaaaa.setImage_url(hours_post_new.image_url);
                                                            aaaaa.setTimestamp(hours_post_new.timestamp);
                                                            aaaaa.setUser(hours_post_new.user);
                                                            aaaaa.setUser_image_url(user_image);
                                                            aaaaa.hoursPostId = hoursPostId;
                                                            aaaaa.setUser_name(user_namee);
                                                            boolean umm= false;
                                                            for(hours_post a:hours_posts)
                                                            {
                                                                if(a.image_name==hours_post_new.image_name)
                                                                    umm=true;
                                                            }


                                                            if (!umm) {
                                                                hours_posts.add( aaaaa);
                                                            }


                                                            hours_recy_adapter1.notifyDataSetChanged();
                                                        }else
                                                        {
//                                                            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                                                        }


                                                    } else {
                                                        Toast.makeText(getActivity(), "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                    }


                                                }
                                            });


                                }
                            }else if(timeNow-((hours_post_new.timestamp).getTime())>24*60*60*1000)
                            {
//                                Toast.makeText(getActivity(), "i am here", Toast.LENGTH_SHORT).show();

                                try {

                                    firebaseFirestore.collection("uni").document("fot").collection("24hoursPosts")
                                            .document(hoursPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            StorageReference pathTodelete = storageReference.child("fot/24hoursPosts/" +hours_post_new.image_name);
                                            pathTodelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

//                                                    Toast.makeText(getActivity(), "Deleted heree!", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        }
                                    });
                                }catch (Exception eee ){
                                    Toast.makeText(getActivity(), "check your connection..", Toast.LENGTH_SHORT).show();
                                }

                            }


                        }


                    }
                    catch (Exception eee)
                    {
//                        Toast.makeText(getActivity()   , "Error"+eee.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }


                }
            });
        }


        // Inflate the layout for this fragment
        return view;
    }
    public void  loadMoreHours() {
        try
        {
        Query NextQuery = firebaseFirestore.collection("uni").document("fot")
                .collection("24hoursPosts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(LastVis)
                .limit(3);
        NextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if (!documentSnapshots.isEmpty()) {


                    LastVis = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        final String hoursPostId = doc.getDocument().getId();
                        final hours_post hours_post_new = doc.getDocument().toObject(hours_post.class);

//                        Calendar cal = Calendar.getInstance();
//                        Date dateHere = cal.getTime();
//                        Toast.makeText(getActivity(), "+++++"+((hours_post_new.timestamp).getTime()-dateHere.getTime()), Toast.LENGTH_SHORT).show();

//
//
//

                        long aa = (timeNow - (hours_post_new.timestamp).getTime());
                        long bb = 24 * 60 * 60 * 1000;
                        if (aa < bb) {
//                            Toast.makeText(getActivity(), "++++"+aa, Toast.LENGTH_SHORT).show();

                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                try{


                                firebaseFirestore.collection("Users").document(hours_post_new.user)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String user_namee = task.getResult().getString("name");
                                                    String user_image = task.getResult().getString("image");


                                                    hours_post aaaaa = new hours_post();
                                                    aaaaa.setDesc(hours_post_new.desc);
                                                    aaaaa.setImage_name(hours_post_new.image_name);
                                                    aaaaa.setImage_url(hours_post_new.image_url);
                                                    aaaaa.setTimestamp(hours_post_new.timestamp);
                                                    aaaaa.setUser(hours_post_new.user);
                                                    aaaaa.setUser_image_url(user_image);
                                                    aaaaa.setUser_name(user_namee);
                                                    aaaaa.hoursPostId = hoursPostId;
                                                    boolean umm = false;
                                                    for (hours_post a : hours_posts) {
                                                        if (a.image_name == hours_post_new.image_name)
                                                            umm = true;
                                                    }

                                                    if (!umm) {
                                                        hours_posts.add(aaaaa);
                                                    }
                                                    hours_recy_adapter1.notifyDataSetChanged();

                                                } else {
                                                    Toast.makeText(getActivity(), "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        });


                            }catch (Exception ew)
                                {
                                    Toast.makeText(getContext(), "Error: "+ew.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else if (aa > bb) {
                            try {

                                firebaseFirestore.collection("uni").document("fot").collection("24hoursPosts")
                                        .document(hoursPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        StorageReference pathTodelete = storageReference.child("fot/24hoursPosts/" + hours_post_new.image_name);
                                        pathTodelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

//                                                Toast.makeText(getActivity(), "Deleted heree!", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                });
                            } catch (Exception eee) {
                                Toast.makeText(getActivity(), "check your connection..", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }

                }

            }
        });
    }
    catch (Exception ee)
        {

        }

    }
    public void scollUppp()
    {
        myRec.smoothScrollToPosition(0);

    }


}



