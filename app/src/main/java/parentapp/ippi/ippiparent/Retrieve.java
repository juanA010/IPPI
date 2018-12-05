package parentapp.ippi.ippiparent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

import parentapp.ippi.ippiparent.model.AvailableBabysitter;

public class Retrieve extends AppCompatActivity {
    ListView lvBabysitter;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    String gender, age;
    //Stack<String> list2;
    ArrayAdapter<String> adapter;
    AvailableBabysitter babysitter;
    TextView viewProfile;
    Button accept, decline;
    String username_text;

    Dialog notAvailable;

    public  final static String AGE_KEY = "parentapp.ippi.ippiparent.age_key";
    public  final static String GENDER_KEY = "parentapp.ippi.ippiparent.gender_key";
    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        babysitter = new AvailableBabysitter();
        lvBabysitter = findViewById(R.id.babysitterlist);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.babysitter_info, R.id.tvSitterName, list);

        //final LinearLayout L1 = findViewById(R.id.ListLayout);
        final View vi = getLayoutInflater().inflate(R.layout.babysitter_info, null);


        viewProfile = vi.findViewById(R.id.tvViewSitterProfile);
        accept = vi.findViewById(R.id.btnAccept);

        //Log.d("tag", "detail: "+getAge()+" "+getGender());

        database=FirebaseDatabase.getInstance();
        ref = database.getReference("AvailableSitter");




        ref.addValueEventListener(new ValueEventListener() {
            Intent intent2 = getIntent();
            final String age = intent2.getStringExtra(AGE_KEY);
            final String gender = intent2.getStringExtra(GENDER_KEY);

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {

                    String sitterage = ds.child("userAge").getValue().toString();
                    String sittergender = ds.child("userGender").getValue().toString();

                    int r = Integer.parseInt(sitterage);

                    if (age.equals("20-24") && gender.equals("Male")) {
                        if ((r >= 20 && r <= 24) && sittergender.equals("M")) {
                            babysitter = ds.getValue(AvailableBabysitter.class);
                            String SitterUsername = babysitter.getUsername().toString();
                            list.add(SitterUsername);
                            lvBabysitter.setAdapter(adapter);

                        }

                    }
                    if (age.equals("20-24") && gender.equals("Female")) {
                        if ((r >= 20 && r <= 24) && sittergender.equals("F")) {
                            babysitter = ds.getValue(AvailableBabysitter.class);
                            String SitterUsername = babysitter.getUsername().toString();
                            list.add(SitterUsername);
                            lvBabysitter.setAdapter(adapter);

                        }
                    }

                    if (age.equals("20-24") && gender.equals("Any")) {
                        if ((r >= 20 && r <= 24)) {
                            babysitter = ds.getValue(AvailableBabysitter.class);
                            String SitterUsername = babysitter.getUsername().toString();
                            list.add(SitterUsername);
                            lvBabysitter.setAdapter(adapter);

                        }
                    }


                    if (age.equals("25-29") && gender.equals("Male")) {
                        if ((r >= 25 && r <= 29) && sittergender.equals("M")) {
                            babysitter = ds.getValue(AvailableBabysitter.class);
                            String SitterUsername = babysitter.getUsername().toString();
                            list.add(SitterUsername);
                            lvBabysitter.setAdapter(adapter);

                        }
                    }

                    if (age.equals("25-29") && gender.equals("Female")) {
                        if (((r >= 25 && r <= 29) && sittergender.equals("F"))) {
                                babysitter = ds.getValue(AvailableBabysitter.class);
                                String SitterUsername = babysitter.getUsername().toString();
                                list.add(SitterUsername);
                                lvBabysitter.setAdapter(adapter);

                        }

                    }


                    if (age.equals("25-29") && gender.equals("Any")) {
                        if ((r >= 25 && r <= 29)) {
                            babysitter = ds.getValue(AvailableBabysitter.class);
                            String SitterUsername = babysitter.getUsername().toString();
                            list.add(SitterUsername);
                            lvBabysitter.setAdapter(adapter);

                        }

                    }

                    if (age.equals("30 above") && gender.equals("Male")) {
                        if ((r >=30) && sittergender.equals("M")) {
                            babysitter = ds.getValue(AvailableBabysitter.class);
                            String SitterUsername = babysitter.getUsername().toString();
                            list.add(SitterUsername);
                            lvBabysitter.setAdapter(adapter);

                        }

                    }

                    if (age.equals("30 above") && gender.equals("Female")) {
                        if ((r >=30) && sittergender.equals("F")) {
                            babysitter = ds.getValue(AvailableBabysitter.class);
                            String SitterUsername = babysitter.getUsername().toString();
                            list.add(SitterUsername);
                            lvBabysitter.setAdapter(adapter);

                        }

                    }

                    if (age.equals("30 above") && gender.equals("Any")) {
                        if ((r >=30)) {
                            babysitter = ds.getValue(AvailableBabysitter.class);
                            String SitterUsername = babysitter.getUsername().toString();
                            list.add(SitterUsername);
                            lvBabysitter.setAdapter(adapter);

                        }

                    }

                    else if(list.isEmpty()){
                        Toast.makeText(Retrieve.this,"There is no available babysitter at this moment. We apologies for the inconvenient occur. Please choose another preference or wat for a while for the babysitter to be available. Thank you!", Toast.LENGTH_LONG).show();
                    }
                }


                //adapter

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lvBabysitter.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {


                //Log.d("log", "age: "+age+" gender "+gender);

               final String message = (String) parent.getItemAtPosition(position);
               //String message =   babysitter.getUsername().toString();

                Intent intent = new Intent(Retrieve.this, SitterProfileActivity.class);
                intent.putExtra(USERNAME_KEY, message);


                startActivity(new Intent(intent));

//Toast.makeText(Retrieve.this,"key:"+item, Toast.LENGTH_SHORT).show();

                //final String selectedFromList = (String) list.get(position);



//
//                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
////                                String key = dataSnapshot.getChildren().toString();
////                                Toast.makeText(Retrieve.this,"key:"+key, Toast.LENGTH_LONG).show();
//                                for(DataSnapshot ds: dataSnapshot.getChildren()){
//                                    //String value = ds.
//
//                                    String key = ds.getKey();
//                                    Toast.makeText(Retrieve.this,"key:"+key, Toast.LENGTH_LONG).show();
//
//
//                                    //startActivity(new Intent(Retrieve.this, SitterProfileActivity.class));
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });


            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
