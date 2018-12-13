package parentapp.ippi.ippiparent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SitterOnServiceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button SitterContact, Emergency;
    private LinearLayout lyTracking, lyRequest, lyFinished;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private DatabaseReference sitterProfileRef, sitterLocationRef;
    private TextView SitterName, ChargePerHour, SitterLocation;
    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";
    public  final static String BOOK_KEY = "parentapp.ippi.ippiparent.book_key";
    public  final static String RECEIPT_KEY = "parentapp.ippi.ippiparent.receipt_key";
    public  final static String ID_KEY = "parentapp.ippi.ippiparent.user_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_on_service);
        drawerLayout = findViewById(R.id.drawer);


        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SitterContact = findViewById(R.id.btnSitterContact);
        Emergency = findViewById(R.id.btnEmergency);
        lyTracking = findViewById(R.id.btnTracker);
        lyRequest = findViewById(R.id.btnRequestTime);
        SitterName = findViewById(R.id.tvSitterProfileName);
        ChargePerHour = findViewById(R.id.tvSitterCharge);
        SitterLocation = findViewById(R.id.tvSitterLocation);
        lyFinished = findViewById(R.id.btnServiceFinish);

        Intent intent = getIntent();
        final String Sitter = intent.getStringExtra(USERNAME_KEY);
        final String bookID = intent.getStringExtra(BOOK_KEY);
        final String receiptID = intent.getStringExtra(RECEIPT_KEY);
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        sitterProfileRef = FirebaseDatabase.getInstance().getReference().child("BookingData").child(userID).child(bookID);

        sitterProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        String username = dataSnapshot.child("SitterName").getValue().toString();
                        SitterName.setText(username);
                        ChargePerHour.setText("RM5 / per hour");
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });

        sitterLocationRef = FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();


        sitterLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    //String user =postSnapshot.getValue().toString();
                    String username = postSnapshot.child("username").getValue().toString();

                    if(username.equals(Sitter)){
                        String address = postSnapshot.child("userAddress").getValue().toString();
                        SitterLocation.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });

        Emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent DIALIntent = new Intent(Intent.ACTION_DIAL);
                DIALIntent.setData(Uri.parse("tel:999"));

                if (ActivityCompat.checkSelfPermission(SitterOnServiceActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(DIALIntent);
            }
        });

        SitterContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SitterOnServiceActivity.this, SitterContactActivity.class);
                intent.putExtra(USERNAME_KEY, Sitter);
                startActivity(new Intent(intent));
            }
        });

        lyTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SitterOnServiceActivity.this, TrackingMapActivity.class));
            }
        });

        lyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getData = getIntent();
                final String bookID = getData.getStringExtra(BOOK_KEY);
                final String receiptID = getData.getStringExtra(RECEIPT_KEY);
                final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(SitterOnServiceActivity.this, RequestTimeActivity.class);
                intent.putExtra(ID_KEY,user );
                intent.putExtra(BOOK_KEY, bookID);
                intent.putExtra(RECEIPT_KEY, receiptID);

                //Toast.makeText(SitterOnServiceActivity.this, "data "+user+" "+bookID+" "+receiptID, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(intent));
            }
        });

        lyFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SitterOnServiceActivity.this, PaymentActivity.class);
                intent.putExtra(BOOK_KEY, bookID);
                intent.putExtra(RECEIPT_KEY, receiptID);
                intent.putExtra(USERNAME_KEY, Sitter);
                startActivity(new Intent(intent));

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;

        if (item.getItemId()==R.id.profileId){
            intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        }

        else if (item.getItemId()==R.id.historyId){
            intent = new Intent(this, BookingHistoryActivity.class);
            startActivity(intent);
            //hello
            //nadia

        }

        else if (item.getItemId()==R.id.helpId){
            intent = new Intent(this, HelpActivity.class);
            startActivity(intent);

        }

        else if (item.getItemId()==R.id.notificationID){
            intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);

        }

        else if (item.getItemId()==R.id.settingId){
            intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

        }
        return false;
    }
}
