package parentapp.ippi.ippiparent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SitterProfileActivity extends AppCompatActivity {

    private Button goToContact, acceptRequest;
    private RatingBar star;
    private TextView SitterName, ChargePerHour, SitterAddress, SitterReview;
    private DatabaseReference sitterProfileRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_profile);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goToContact = findViewById(R.id.btnSitterContact);
        acceptRequest = findViewById(R.id.btnSitterAccept);
        SitterName = findViewById(R.id.tvSitterProfileName);
        ChargePerHour = findViewById(R.id.tvSitterCharge);
        SitterAddress = findViewById(R.id.tvSitterAddress);
        SitterReview = findViewById(R.id.tvCustReview);
        star = findViewById(R.id.ratingBar);


        sitterProfileRef = FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();

        sitterProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String username = postSnapshot.child("username").getValue().toString();
                    String charge = postSnapshot.child("charge").getValue().toString();
                    String address = postSnapshot.child("userAddress").getValue().toString();
                    String review = postSnapshot.child("customerReview").getValue().toString();
                    long rating = (long) postSnapshot.child("rating").getValue();


                    SitterName.setText(username);
                    ChargePerHour.setText("RM"+charge+" per hour");
                    SitterAddress.setText(address);
                    SitterReview.setText(review);
                    star.setRating((float) rating);
                    star.setFocusable(false);
                    //star.setNumStars(5);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });
        goToContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SitterProfileActivity.this, SitterContactActivity.class));
            }
        });

        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SitterProfileActivity.this, NavigationToSitter.class));
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
