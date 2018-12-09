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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SitterProfileActivity extends AppCompatActivity {

    private Button goToContact, acceptRequest, seeLocation;
    private RatingBar star;
    private TextView SitterName, ChargePerHour, SitterAddress, SitterReview, SitterAge, SitterGender;
    private DatabaseReference sitterProfileRef, addBookingData;
    private FirebaseDatabase createBooking = FirebaseDatabase.getInstance();

    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";
    public  final static String BOOK_KEY = "parentapp.ippi.ippiparent.book_key";
    public  final static String RECEIPT_KEY = "parentapp.ippi.ippiparent.receipt_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_profile);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goToContact = findViewById(R.id.btnSitterContact);
        acceptRequest = findViewById(R.id.btnSitterAccept);
        seeLocation = findViewById(R.id.btnSeeLocation);
        SitterName = findViewById(R.id.tvSitterProfileName);
        ChargePerHour = findViewById(R.id.tvSitterCharge);
        SitterAddress = findViewById(R.id.tvSitterAddress);
        SitterReview = findViewById(R.id.tvCustReview);
        SitterAge = findViewById(R.id.tvSitterAge);
        SitterGender = findViewById(R.id.tvSitterGender);
        star = findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        final String message = intent.getStringExtra(USERNAME_KEY);
        final String bookID = intent.getStringExtra(BOOK_KEY);
        final String receiptID = intent.getStringExtra(RECEIPT_KEY);
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Toast.makeText(SitterProfileActivity.this,"key:"+message, Toast.LENGTH_SHORT).show();


        sitterProfileRef = FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();


        sitterProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    //String user =postSnapshot.getValue().toString();
                    String username = postSnapshot.child("username").getValue().toString();

                    if(username.equals(message)){

                    String age = postSnapshot.child("userAge").getValue().toString();
                    String gender = postSnapshot.child("userGender").getValue().toString();
                    String charge = postSnapshot.child("charge").getValue().toString();
                    String address = postSnapshot.child("userAddress").getValue().toString();
                    String review = postSnapshot.child("customerReview").getValue().toString();
                    String rating = postSnapshot.child("rating").getValue().toString();

                    Float r = Float.parseFloat(rating);


                    SitterName.setText(username);
                    SitterAge.setText(age+"y/o");
                    if(gender.equals("F")){
                        SitterGender.setText("Female");
                    }
                    if(gender.equals("M")){
                        SitterGender.setText("Male");
                    }
                    ChargePerHour.setText("RM"+charge+" per hour");
                    SitterAddress.setText(address);
                    SitterReview.setText(review);
                    star.setRating(r);
                    star.setFocusable(false);
                    //star.setNumStars(5);
                }
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
                Intent intent = new Intent(SitterProfileActivity.this, SitterContactActivity.class);

                intent.putExtra(USERNAME_KEY, message);
                startActivity(new Intent(intent));

            }
        });

        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference sitterProfile= FirebaseDatabase.getInstance().getReference("BabysitterLocation").getRef();
                sitterProfile.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String username = postSnapshot.child("username").getValue().toString();
                            if(username.equals(message)){
                                String key = postSnapshot.getKey().toString();
                                sitterProfile.child(key).child("status").setValue("BabySitting");

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("tag", "Failed to read value.", error.toException());
                    }
                });


                addBookingData = FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();


                addBookingData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            //String user =postSnapshot.getValue().toString();
                            String username = postSnapshot.child("username").getValue().toString();

                            if(username.equals(message)){

                                String phoneNumber = postSnapshot.child("userphonenumber").getValue().toString();
                                String location = postSnapshot.child("userAddress").getValue().toString();
                                final DatabaseReference createBooking= FirebaseDatabase.getInstance().getReference("BookingData").child(userID);
                                createBooking.child(bookID).child("SitterName").setValue(username);
                                createBooking.child(bookID).child("SitterLocation").setValue(location);
                                createBooking.child(bookID).child("SitterContact").setValue(phoneNumber);
                                createBooking.child(bookID).child("SitterRating").setValue("null");

                                final DatabaseReference createReceiptSitterName= FirebaseDatabase.getInstance().getReference("BookingReceipt").child(userID);
                                createReceiptSitterName.child(receiptID).child("sitterName").setValue(username);
                                generatePayment payment = new generatePayment();
                                payment.calculatePayment(bookID,receiptID);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("tag", "Failed to read value.", error.toException());
                    }
                });


                Intent intent = new Intent(SitterProfileActivity.this, NavigationToSitter.class);
                intent.putExtra(BOOK_KEY, bookID);
                intent.putExtra(RECEIPT_KEY,receiptID);
                intent.putExtra(USERNAME_KEY, message);
                startActivity(new Intent(intent));


            }
        });

        seeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SitterProfileActivity.this, NavigationToSitter.class);
                intent.putExtra(USERNAME_KEY, message);
                startActivity(new Intent(intent));
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
