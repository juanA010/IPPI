package parentapp.ippi.ippiparent;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import parentapp.ippi.ippiparent.model.AvailableBabysitter;
import parentapp.ippi.ippiparent.model.ReceiptModel;

public class SearchbabysitterActivity extends AppCompatActivity {

    private RadioButton GenderMale, GenderFemale, GenderAny, Age20, Age25, Age30;
    private RadioGroup GenderSelect, AgeSelect;
    private String selectedGender, selectedAge;
    private Button BtnSearchBs;
    private TimePicker timeStart, timeEnd;
    private Calendar calendar;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseDatabase createBooking = FirebaseDatabase.getInstance();
    private FirebaseDatabase createReceipt = FirebaseDatabase.getInstance();

    public  final static String AGE_KEY = "parentapp.ippi.ippiparent.age_key";
    public  final static String GENDER_KEY = "parentapp.ippi.ippiparent.gender_key";
    public  final static String BOOK_KEY = "parentapp.ippi.ippiparent.book_key";
    public  final static String RECEIPT_KEY = "parentapp.ippi.ippiparent.receipt_key";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbabysitter);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BtnSearchBs = findViewById(R.id.btSearchBs);
        GenderSelect = findViewById(R.id.SelectionGender);
        AgeSelect = findViewById(R.id.SelectionAge);
        GenderMale =findViewById(R.id.GenderMale);
        GenderFemale= findViewById(R.id.GenderFemale);
        GenderAny= findViewById(R.id.GenderAny);
        Age20 = findViewById(R.id.Age20);
        Age25= findViewById(R.id.Age25);
        Age30 = findViewById(R.id.Age30);
        timeStart = findViewById(R.id.TimeStart);
        timeEnd = findViewById(R.id.TimeEnd);
        calendar = Calendar.getInstance();
//        etStartTime = findViewById(R.id.TimeStart);
//        etEndTime = findViewById(R.id.TimeEnd);



        timeStart.setIs24HourView(false);
        timeEnd.setIs24HourView(false);
        mAuth = FirebaseAuth.getInstance();
        final String userID = mAuth.getCurrentUser().getUid();

        Random r = new Random();
        int randomID = r.nextInt((60000-1)+1) + 1;
        int randomID2 = r.nextInt((11-0)+0)+0;

        final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String bookDate = df.format(calendar.getTime()).toString();


        final String newBooked = new String("HN-"+randomID+"-"+randomID2);
        final String newReceipt = new String("BR-"+randomID+"-"+randomID2);



        timeStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int hour = hourOfDay;
                String format;
                String mins = null;
                String hours = null;
                if(hour == 0){
                    hour+=12;
                    format = "AM";
                }
                else if( hour == 12){
                    format ="PM";
                }
                else if(hour >12){
                    hour-= 12;
                    format = "PM";
                }
                else{
                    format = "AM";
                }

                if(minute <10){
                    mins = "0"+minute;
                }
                else{
                    mins = String.valueOf(minute);
                }
                if(hour<10){
                    hours = "0"+hour;
                }
                else{
                    hours = String.valueOf(hour);
                }

                String timestart= new String (hours+":"+mins+" "+format);
                createBooking.getReference("BookingData").child(userID).child(newBooked).child("BookDate").setValue(bookDate);
                createBooking.getReference("BookingData").child(userID).child(newBooked).child("BookStart").setValue(timestart);
                createReceipt.getReference("BookingReceipt").child(userID).child(newReceipt).child("BookDate").setValue(bookDate);
                createReceipt.getReference("BookingReceipt").child(userID).child(newReceipt).child("startTime").setValue(timestart);

                Toast.makeText(SearchbabysitterActivity.this, timestart,Toast.LENGTH_SHORT).show();
            }
        });

        timeEnd.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int hour = hourOfDay;
                String format;
                String mins = null;
                String hours = null;
                if(hour == 0){
                    hour+=12;
                    format = "AM";
                }
                else if( hour == 12){
                    format ="PM";
                }
                else if(hour >12){
                    hour-= 12;
                    format = "PM";

                }
                else{
                    format = "AM";
                }

                if(minute <10){
                    mins = "0"+minute;
                }
                else{
                    mins = String.valueOf(minute);
                }
                if(hour<10){
                    hours = "0"+hour;
                }
                else{
                    hours = String.valueOf(hour);
                }

                String timeend= new String (hours+":"+mins+" "+format);
                createBooking.getReference("BookingData").child(userID).child(newBooked).child("BookEnd").setValue(timeend);
                createBooking.getReference("BookingData").child(userID).child(newBooked).child("ReceiptID").setValue(newReceipt);
                createBooking.getReference("BookingData").child(userID).child(newBooked).child("TotalNewCharge").setValue("null");
                //ReceiptModel receiptData = new ReceiptModel("null","N/A","N/A","N/A","N/A");
                createReceipt.getReference("BookingReceipt").child(userID).child(newReceipt).child("endTime").setValue(timeend);
                createReceipt.getReference("BookingReceipt").child(userID).child(newReceipt).child("sitterName").setValue("null");
                createReceipt.getReference("BookingReceipt").child(userID).child(newReceipt).child("reqTime").setValue("null");
                createReceipt.getReference("BookingReceipt").child(userID).child(newReceipt).child("newEnd").setValue("null");
                createReceipt.getReference("BookingReceipt").child(userID).child(newReceipt).child("TotalCharge").setValue("null");
                createReceipt.getReference("BookingReceipt").child(userID).child(newReceipt).child("TotalNewCharge").setValue("null");

                Toast.makeText(SearchbabysitterActivity.this, timeend,Toast.LENGTH_SHORT).show();
            }
        });

        BtnSearchBs.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

               // GenderSelect.getCheckedRadioButtonId();
                if(GenderMale.isChecked()){
                    selectedGender = GenderMale.getText().toString();
                }
                if(GenderFemale.isChecked()){
                    selectedGender = GenderFemale.getText().toString();
                }
                if (GenderAny.isChecked()){
                    selectedGender = GenderAny.getText().toString();
                }

                if(Age20.isChecked()){
                    selectedAge = Age20.getText().toString();
                }
                if(Age25.isChecked()){
                    selectedAge = Age25.getText().toString();
                }
                if(Age30.isChecked()){
                    selectedAge = Age30.getText().toString();
                }

                Intent intent = new Intent(SearchbabysitterActivity.this, Retrieve.class);
                intent.putExtra(BOOK_KEY, newBooked);
                intent.putExtra(RECEIPT_KEY, newReceipt);
                intent.putExtra(AGE_KEY, selectedAge);
                intent.putExtra(GENDER_KEY, selectedGender);
                //Toast.makeText(SearchbabysitterActivity.this, "start "+start+" end: "+end, Toast.LENGTH_SHORT).show();
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
