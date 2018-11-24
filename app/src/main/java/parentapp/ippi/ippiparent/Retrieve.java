package parentapp.ippi.ippiparent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    //Stack<String> list2;
    ArrayAdapter<String> adapter;
    AvailableBabysitter babysitter;


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

        database=FirebaseDatabase.getInstance();
        ref = database.getReference("AvailableSitter");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    babysitter = ds.getValue(AvailableBabysitter.class);
                    //list2.push(babysitter.getUsername().toString()+"\nRating: "+babysitter.getRating().toString());
                    list.add(babysitter.getUsername().toString()+"\nRating: "+babysitter.getRating().toString());

                }

                //adapter
                lvBabysitter.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
