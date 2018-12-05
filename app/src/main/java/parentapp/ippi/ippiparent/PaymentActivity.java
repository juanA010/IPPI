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
    private TextView SitterName, ChargePerHour, startTime, endTime, reqTime, newTime, addCharge, totalCharge;
    private DatabaseReference parentNameRef, sitterProfileRef, displayReceipt;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private ReceiptModel receipt;

    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";

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
        addCharge = findViewById(R.id.tvExtraCharge);
        totalCharge = findViewById(R.id.tvTotalCharge);
        receipt = new ReceiptModel();

        Intent intent = getIntent();
        final String Sitter = intent.getStringExtra(USERNAME_KEY);


        mAuth = FirebaseAuth.getInstance();
        currentUserID =mAuth.getCurrentUser().getUid();
        parentNameRef = FirebaseDatabase.getInstance().getReference().child("Parents").child(currentUserID);
        parentNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String parentName= dataSnapshot.child("username").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //parentNameRef = FirebaseDatabase.getInstance().getReference("Parents").child();
        sitterProfileRef = FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();
        sitterProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    //String user =postSnapshot.getValue().toString();
                    String username = postSnapshot.child("username").getValue().toString();
                    if(username.equals(Sitter)){
                        String charge = postSnapshot.child("charge").getValue().toString();
                        SitterName.setText(username);
                        ChargePerHour.setText("RM"+charge+" per hour");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });

        displayReceipt = FirebaseDatabase.getInstance().getReference("BookingReceipt").getRef();
        displayReceipt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String sitterName = postSnapshot.child("SitterName").getValue().toString();
                    if (sitterName.equals(Sitter)) {

                        String start = postSnapshot.child("startTime").getValue().toString();
                        String end = postSnapshot.child("endTime").getValue().toString();
                        String req = postSnapshot.child("reqTime").getValue().toString();
                        String timeNew = postSnapshot.child("newEnd").getValue().toString();
                        String add = postSnapshot.child("addCharge").getValue().toString();
                        String total = postSnapshot.child("totalCharge").getValue().toString();

                        startTime.setText(start);
                        endTime.setText(end);
                        reqTime.setText(req);
                        newTime.setText(timeNew);
                        addCharge.setText("RM"+add);
                        totalCharge.setText("RM"+total);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PaymentActivity.this, GiveRatingActivity.class);
                intent.putExtra(USERNAME_KEY, Sitter);
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
