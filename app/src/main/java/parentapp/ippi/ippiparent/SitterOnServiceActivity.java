package parentapp.ippi.ippiparent;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
    private DatabaseReference sitterProfileRef;
    private TextView SitterName, ChargePerHour;
    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";

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
        lyFinished = findViewById(R.id.btnServiceFinish);

        Intent intent = getIntent();
        final String Sitter = intent.getStringExtra(USERNAME_KEY);

        sitterProfileRef = FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();

        sitterProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

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
                startActivity(new Intent(SitterOnServiceActivity.this, RequestTimeActivity.class));
            }
        });

        lyFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SitterOnServiceActivity.this, PaymentActivity.class);
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
