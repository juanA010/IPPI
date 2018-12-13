package parentapp.ippi.ippiparent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import parentapp.ippi.ippiparent.model.AvailableBabysitter;

public class AvailableSitterActivity extends AppCompatActivity {

    private TextView goToSitterProfile;
    private TextView txt_username, txt_rating;
    private DatabaseReference sitterUserRef;
    private Button AcceptingRequest;
    AvailableBabysitter babysitter;
    String TAG="tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_sitter);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goToSitterProfile = findViewById(R.id.tvViewSitterProfile);
        txt_username = findViewById(R.id.tvSitterName);
        txt_rating = findViewById(R.id.tvRating);
        AcceptingRequest = findViewById(R.id.btnAccept);

        ///sitterUserRef = FirebaseDatabase.getInstance().getReference("AvailableSitter").getRef();
        sitterUserRef = FirebaseDatabase.getInstance().getReference("AvailableSitter").getRef();

        sitterUserRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        babysitter = postSnapshot.getValue(AvailableBabysitter.class);

//                        String username = postSnapshot.child("username").getValue().toString();
//                        String rating = postSnapshot.child("rating").getValue().toString();
                        txt_username.setText(babysitter.getUsername());
                        txt_rating.setText(babysitter.getRating());






                        //Log.d(TAG, "======="+postSnapshot.child("username").getValue());
                        //Log.d(TAG, "======="+postSnapshot.child("rating").getValue());
                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        AcceptingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AvailableSitterActivity.this, NavigationToSitter.class));
            }
        });

        goToSitterProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity (new Intent(AvailableSitterActivity.this, SitterProfileActivity.class));

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
