package parentapp.ippi.ippiparent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import parentapp.ippi.ippiparent.model.ReceiptModel;

public class ReciptActivity extends AppCompatActivity {

    private TextView ReceiptID, SitterName, BookDate, ChargePerHour, startTime, endTime, reqTime, newTime, addCharge, totalCharge;
    private DatabaseReference parentNameRef, sitterProfileRef, displayReceipt;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private ReceiptModel receipt;

    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipt);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ReceiptID = findViewById(R.id.tvBookID);
        SitterName = findViewById(R.id.tvSitterProfileName);
        BookDate = findViewById(R.id.tvDate);
        ChargePerHour = findViewById(R.id.tvSitterCharge);
        startTime = findViewById(R.id.tvInitial);
        endTime = findViewById(R.id.tvFinal);
        reqTime = findViewById(R.id.tvRequest);
        newTime = findViewById(R.id.tvNewTime);
        totalCharge = findViewById(R.id.tvTotalCharge);
        receipt = new ReceiptModel();


        Intent intent = getIntent();
        final String Sitter = intent.getStringExtra(USERNAME_KEY);


        mAuth = FirebaseAuth.getInstance();
        currentUserID =mAuth.getCurrentUser().getUid();

        final String receiptID = getIntent().getExtras().getString("ReceiptID");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        displayReceipt = FirebaseDatabase.getInstance().getReference().child("BookingReceipt").child(userID);
        displayReceipt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String getID = postSnapshot.getKey().toString();
                    if (getID.equals(receiptID)) {

                        String sitterName = postSnapshot.child("sitterName").getValue().toString();
                        String bookDate =  postSnapshot.child("BookDate").getValue().toString();
                        String start = postSnapshot.child("startTime").getValue().toString();
                        String end = postSnapshot.child("endTime").getValue().toString();
                        String req = postSnapshot.child("reqTime").getValue().toString();
                        String timeNew = postSnapshot.child("newEnd").getValue().toString();
                        String newtotal = postSnapshot.child("TotalNewCharge").getValue().toString();
                        String total = postSnapshot.child("TotalCharge").getValue().toString();



                        ReceiptID.setText(getID);
                        SitterName.setText(sitterName);
                        BookDate.setText(bookDate);
                        startTime.setText(start);
                        endTime.setText(end);
                        reqTime.setText(req);
                        newTime.setText(timeNew);

                        if(!newtotal.equals("null")){
                            totalCharge.setText("RM"+newtotal);
                        }

                        if(newtotal.equals("null")){
                            totalCharge.setText("RM"+total);
                        }


                    }
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
