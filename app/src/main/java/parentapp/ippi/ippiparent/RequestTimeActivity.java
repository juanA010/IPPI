package parentapp.ippi.ippiparent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;

public class RequestTimeActivity extends AppCompatActivity {

    private Button RequestTime, CalculateCharge;
    private TextView InitialTime, FinalTime, newEndTime, OriginalCharge, NewCharge;
    private EditText reqHour, reqMin;
    private DatabaseReference receiptRetrieve;
    private String TimeNew;
    private String newCharge;
    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";
    public  final static String BOOK_KEY = "parentapp.ippi.ippiparent.book_key";
    public  final static String RECEIPT_KEY = "parentapp.ippi.ippiparent.receipt_key";
    public  final static String ID_KEY = "parentapp.ippi.ippiparent.user_key";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_time);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent sendUser = getIntent();
        final String Sitter = sendUser.getStringExtra(USERNAME_KEY);
        final String bookID = sendUser.getStringExtra(BOOK_KEY);
        final String receiptID = sendUser.getStringExtra(RECEIPT_KEY);
        final String userID = sendUser.getStringExtra(ID_KEY);

        InitialTime = findViewById(R.id.tvInitial);
        FinalTime = findViewById(R.id.tvFinal);
        newEndTime = findViewById(R.id.tvNewEnd);
        OriginalCharge = findViewById(R.id.tvOriCharge);
        NewCharge = findViewById(R.id.tvNewCharge);
        reqHour = findViewById(R.id.etReqHour);
        reqMin = findViewById(R.id.etReqMin);
        RequestTime = findViewById(R.id.btnRequest);
        CalculateCharge = findViewById(R.id.btnCalculateNew);

        Log.d("tag", "id "+userID+" "+receiptID);
        receiptRetrieve = FirebaseDatabase.getInstance().getReference().child("BookingReceipt").child(userID).child(receiptID);

        receiptRetrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String getStartTime = dataSnapshot.child("startTime").getValue().toString();
                    final String getEndTime = dataSnapshot.child("endTime").getValue().toString();
                    final String getOriCharge = dataSnapshot.child("TotalCharge").getValue().toString();
                    //String getNewEndTime = dataSnapshot.child("newEnd").getValue().toString();

                    InitialTime.setText(getStartTime);
                    FinalTime.setText(getEndTime);
                    OriginalCharge.setText("RM"+getOriCharge);
                    //newEndTime.setText(getNewEndTime);


                    CalculateCharge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String h = reqHour.getText().toString();
                            String m = reqMin.getText().toString();
                            //int hours = Integer.parseInt(h);
                            //int mins = Integer.parseInt(m);

                            generatePayment newReq = new generatePayment();
                            float getNewCharge = newReq.requestExtra(getOriCharge,h,m);
                            String getNewTime = newReq.NewEndTime(getEndTime,h,m);
                            newCharge = String.format("%.2f",getNewCharge);

                            TimeNew = getNewTime;
                            //ChargeNew = newCharge;

                            NewCharge.setText("RM"+newCharge);
                            newEndTime.setText(TimeNew);
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        RequestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference setRequestData = FirebaseDatabase.getInstance().getReference().child("BookingReceipt").child(userID).child(receiptID);

                setRequestData.addValueEventListener(new ValueEventListener() {
                    String h = reqHour.getText().toString();
                    String m = reqMin.getText().toString();



                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            setRequestData.child("reqTime").setValue(h+"h "+m+"m");
                            setRequestData.child("newEnd").setValue(TimeNew);
                            setRequestData.child("TotalNewCharge").setValue(newCharge);

                            final DatabaseReference setNewBookingData = FirebaseDatabase.getInstance().getReference().child("BookingData").child(userID).child(bookID);
                            setNewBookingData.child("BookEnd").setValue(TimeNew);
                            setNewBookingData.child("TotalNewCharge").setValue(newCharge);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(RequestTimeActivity.this, SitterOnServiceActivity.class);
                intent.putExtra(BOOK_KEY, bookID);
                intent.putExtra(RECEIPT_KEY, receiptID);
                intent.putExtra(USERNAME_KEY, Sitter );
                startActivity(new Intent(intent));

                //startActivity(new Intent(RequestTimeActivity.this, SitterOnServiceActivity.class));
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
