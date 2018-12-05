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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import parentapp.ippi.ippiparent.model.AvailableBabysitter;

public class SearchbabysitterActivity extends AppCompatActivity {

    private RadioButton GenderMale, GenderFemale, GenderAny, Age20, Age25, Age30;
    private RadioGroup GenderSelect, AgeSelect;
    private String selectedGender, selectedAge;
    private Button BtnSearchBs;
    private DatabaseReference sitterUserRef, sitterAvailable;
    private TimePickerDialog picker, picker2;
    private EditText etStartTime, etEndTime;
    private Button btnGet;

    public  final static String AGE_KEY = "parentapp.ippi.ippiparent.age_key";
    public  final static String GENDER_KEY = "parentapp.ippi.ippiparent.gender_key";
    public  final static String START_KEY = "parentapp.ippi.ippiparent.start_key";
    public  final static String END_KEY = "parentapp.ippi.ippiparent.end_key";


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
        etStartTime = findViewById(R.id.TimeStart);
        etEndTime = findViewById(R.id.TimeEnd);


        etStartTime.setInputType(InputType.TYPE_NULL);
        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SearchbabysitterActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                if(sMinute==00){
                                    etStartTime.setText(sHour + ":" + sMinute+"0");
                                }
                                else{
                                    etStartTime.setText(sHour + ":" + sMinute);
                                }
                            }
                        }, hour, minutes, true);
                picker.show();

            }
        });

        etEndTime.setInputType(InputType.TYPE_NULL);
        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr2 = Calendar.getInstance();
                int hour2 = cldr2.get(Calendar.HOUR_OF_DAY);
                int minutes2 = cldr2.get(Calendar.MINUTE);
                // time picker dialog
                picker2 = new TimePickerDialog(SearchbabysitterActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp2, int sHour2, int sMinute2) {
                                if(sMinute2==00){
                                    etEndTime.setText(sHour2 + ":" + sMinute2+"0");
                                }
                                else{
                                    etEndTime.setText(sHour2 + ":" + sMinute2);
                                }

                            }
                        }, hour2, minutes2, true);
                picker2.show();
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

               // String [] filter = {selectedGender,selectedAge};

                Intent intent = new Intent(SearchbabysitterActivity.this, Retrieve.class);
                intent.putExtra(AGE_KEY, selectedAge);
                intent.putExtra(GENDER_KEY, selectedGender);
                startActivity(new Intent(intent));




//                Retrieve retrieveList = new Retrieve();
//                retrieveList.setGender(selectedGender);
//                retrieveList.setAge(selectedAge);



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
