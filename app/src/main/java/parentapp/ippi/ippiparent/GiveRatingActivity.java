package parentapp.ippi.ippiparent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GiveRatingActivity extends AppCompatActivity {

    private Button btnDone;
    private TextView SitterName;
    private EditText SitterReview;
    private RatingBar SitterRating;
    private DatabaseReference setSitterRating;
    private FirebaseAuth mAuth;
    private String currentUserID;
    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";
    public  final static String BOOK_KEY = "parentapp.ippi.ippiparent.book_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_rating);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDone = findViewById(R.id.buttonDone);
        SitterName = findViewById(R.id.tvSitterProfileName);
        SitterRating = findViewById(R.id.rbGiveRating);
        SitterReview = findViewById(R.id.etGiveReview);

        mAuth = FirebaseAuth.getInstance();
        currentUserID =mAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        final String Sitter = intent.getStringExtra(USERNAME_KEY);
        final String bookID = intent.getStringExtra(BOOK_KEY);

        setSitterRating = FirebaseDatabase.getInstance().getReference().child("BookingData").child(currentUserID).child(bookID);
        setSitterRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String Sitter = dataSnapshot.child("SitterName").getValue().toString();
                    SitterName.setText(Sitter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ratingForSitter = String.valueOf(SitterRating.getRating());
                final String reviewForSitter = SitterReview.getText().toString();

                if(!ratingForSitter.equals("0.0")){
                    final DatabaseReference sendRatingData= FirebaseDatabase.getInstance().getReference("BookingData").child(currentUserID).child(bookID);

                    sendRatingData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                sendRatingData.child("SitterRating").setValue(ratingForSitter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    final DatabaseReference editSitterProfile= FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();

                    editSitterProfile.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String username = postSnapshot.child("username").getValue().toString();
                                if(username.equals(Sitter)){
                                    String getUserKey = postSnapshot.getKey().toString();
                                    editSitterProfile.child(getUserKey).child("rating").setValue(ratingForSitter);
                                    editSitterProfile.child(getUserKey).child("customerReview").setValue(reviewForSitter);
                                    //DatabaseReference setRatingReview = FirebaseDatabase.getInstance().getReference("BabysitterProfile").child(getUserKey);

                                    //setRatingReview.child("rating").setValue(ratingForSitter);
                                    //setRatingReview.child("customerReview").setValue(reviewForSitter);
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    startActivity(new Intent(GiveRatingActivity.this, AppMenu.class));
                }

                if(ratingForSitter.equals("0.0")){
                    Toast.makeText(GiveRatingActivity.this, "Please give the rating for babysitter", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
