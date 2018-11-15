package parentapp.ippi.ippiparent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchbabysitterActivity extends AppCompatActivity {

    private Button BtnSearchBs;
    private DatabaseReference sitterUserRef;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbabysitter);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BtnSearchBs = findViewById(R.id.btSearchBs);

        sitterUserRef = FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();

        BtnSearchBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SearchbabysitterActivity.this, AvailableSitterActivity.class));

                sitterUserRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                            String availability = postSnapshot.child("availibilty").getValue().toString();

                            if(availability.equals("false")){
                                startActivity(new Intent(SearchbabysitterActivity.this, NoAvailableSitter.class));
                            }

                            if(availability.equals("true")){
                                startActivity(new Intent(SearchbabysitterActivity.this, AvailableSitterActivity.class));
                            }


                            //Log.d(TAG, "======="+postSnapshot.child("username").getValue());
                            //Log.d(TAG, "======="+postSnapshot.child("rating").getValue());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("tag", "Failed to read value.", error.toException());
                    }
                });


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
