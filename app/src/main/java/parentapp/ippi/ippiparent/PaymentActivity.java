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

public class PaymentActivity extends AppCompatActivity {

    private Button btnPaid;
    private TextView SitterName, ChargePerHour, startTime, endTime, reqTime, newTime, totalCharge;
    private DatabaseReference parentNameRef, sitterProfileRef, displayReceipt;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private ReceiptModel receipt;

    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";
    public  final static String RECEIPT_KEY = "parentapp.ippi.ippiparent.receipt_key";
    public  final static String BOOK_KEY = "parentapp.ippi.ippiparent.book_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnPaid = findViewById(R.id.btnPaid);
        SitterName = findViewById(R.id.tvSitterProfileName);
        ChargePerHour = findViewById(R.id.tvSitterCharge);
        startTime = findViewById(R.id.tvInitial);
        endTime = findViewById(R.id.tvFinal);
        reqTime = findViewById(R.id.tvRequest);
        newTime = findViewById(R.id.tvNewTime);
        totalCharge = findViewById(R.id.tvTotalCharge);
        receipt = new ReceiptModel();

        Intent intent = getIntent();
        final String Sitter = intent.getStringExtra(USERNAME_KEY);
        final String ReceiptID = intent.getStringExtra(RECEIPT_KEY);
        final String bookID = intent.getStringExtra(BOOK_KEY);


        mAuth = FirebaseAuth.getInstance();
        currentUserID =mAuth.getCurrentUser().getUid();

        sitterProfileRef = FirebaseDatabase.getInstance().getReference().child("BookingReceipt").child(currentUserID).child(ReceiptID);
        sitterProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String sitterName = dataSnapshot.child("sitterName").getValue().toString();
                    String start = dataSnapshot.child("startTime").getValue().toString();
                    String end = dataSnapshot.child("endTime").getValue().toString();
                    String req = dataSnapshot.child("reqTime").getValue().toString();
                    String timeNew = dataSnapshot.child("newEnd").getValue().toString();
                    String Total = dataSnapshot.child("TotalCharge").getValue().toString();
                    String newTotal = dataSnapshot.child("TotalNewCharge").getValue().toString();

                    SitterName.setText(sitterName);
                    startTime.setText(start);
                    endTime.setText(end);
                    reqTime.setText(req);
                    newTime.setText(timeNew);

                    if(!newTotal.equals("null")){
                        totalCharge.setText("RM"+newTotal);
                    }
                    if(newTotal.equals("null")){
                        totalCharge.setText("RM"+Total);
                    }
                    //totalCharge.setText("RM"+total);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });

        btnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, GiveRatingActivity.class);
                intent.putExtra(USERNAME_KEY, Sitter);
                intent.putExtra(BOOK_KEY, bookID);
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
