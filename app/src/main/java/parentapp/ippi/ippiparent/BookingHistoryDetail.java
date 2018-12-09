package parentapp.ippi.ippiparent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingHistoryDetail extends AppCompatActivity {
    String a;
    private DatabaseReference BookingDB, ReceiptDB;

    private TextView txt_BookID, txt_name,txt_location, txt_contact, txt_date, txt_time, txt_charge, showReceipt;
    private RatingBar showRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history_detail);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String bookID = getIntent().getExtras().getString("BookingID");
        txt_BookID = findViewById(R.id.BookingID);
        txt_name =findViewById(R.id.SitterName);
        txt_location =findViewById(R.id.SitterLocation);
        txt_contact = findViewById(R.id.SitterPhone);
        txt_date = findViewById(R.id.tvBookDate);
        txt_time = findViewById(R.id.tvBookTime);
        txt_charge = findViewById(R.id.tvCharge);
        showRating = findViewById(R.id.SitterRating);
        showReceipt = findViewById(R.id.tvViewReceipt);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        BookingDB = FirebaseDatabase.getInstance().getReference().child("BookingData").child(userID).child(bookID);

        BookingDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String SitterName = dataSnapshot.child("SitterName").getValue().toString();
                    String SitterLocation = dataSnapshot.child("SitterLocation").getValue().toString();
                    String SitterContact = dataSnapshot.child("SitterContact").getValue().toString();
                    String BookDate = dataSnapshot.child("BookDate").getValue().toString();
                    String BookTime = dataSnapshot.child("BookStart").getValue().toString();
                    String TotalCharge = dataSnapshot.child("TotalCharge").getValue().toString();
                    String TotalNewCharge = dataSnapshot.child("TotalNewCharge").getValue().toString();
                    String SitterRating = dataSnapshot.child("SitterRating").getValue().toString();
                    final String ReceiptID = dataSnapshot.child("ReceiptID").getValue().toString();
                    Float r = Float.parseFloat(SitterRating);

                    txt_BookID.setText(bookID);
                    txt_name.setText(SitterName);
                    txt_location.setText(SitterLocation);
                    txt_contact.setText(SitterContact);
                    txt_date.setText(BookDate);
                    txt_time.setText(BookTime);

                    if(!TotalNewCharge.equals("null")){
                        txt_charge.setText("RM"+TotalNewCharge);
                    }
                    if(TotalNewCharge.equals("null")){
                        txt_charge.setText("RM"+TotalCharge);
                    }

                    showRating.setRating(r);
                    showRating.setFocusable(false);

                    showReceipt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent= new Intent(getApplicationContext(), ReciptActivity.class);
                            intent.putExtra("ReceiptID", ReceiptID);
                            startActivity(intent);
                        }
                    });


                }
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
