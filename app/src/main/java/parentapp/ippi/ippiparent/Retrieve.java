package parentapp.ippi.ippiparent;

import android.app.Activity;
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



    public Retrieve( ) {


    }

    public void setGender(String SelectedGender){
        this.gender = SelectedGender;
    }
    public String getGender(){
        return gender;
    }

    public void setAge(String SelectedAge){
        this.age = SelectedAge;
    }

    public String getAge(){
        return age;
    }



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
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    babysitter = ds.getValue(AvailableBabysitter.class);
                    //list2.push(babysitter.getUsername().toString()+"\nRating: "+babysitter.getRating().toString());
                    list.add(babysitter.getUsername().toString()+"\nRating: "+babysitter.getRating().toString());
//                    ds.getKey();
                    lvBabysitter.setAdapter(adapter);
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
                startActivity(new Intent(Retrieve.this, SitterProfileActivity.class));



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
