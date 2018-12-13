package parentapp.ippi.ippiparent;

import android.content.Intent;
import android.support.annotation.DrawableRes;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private TextView txt_username, txt_userphone, txt_useremail, txt_userlocation;
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUserID =mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Parents").child(currentUserID);
        txt_username = findViewById(R.id.tvProfileName);
        txt_userphone = findViewById(R.id.tvProfilePhone);
        txt_useremail = findViewById(R.id.tvProfileEmail);
        txt_userlocation = findViewById(R.id.tvProfileLocation);
        logout = (Button)findViewById(R.id.btnLogout);

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    if (dataSnapshot.exists()) {
                        String myProfileName = dataSnapshot.child("username").getValue().toString();
                        String myProfilePhone = dataSnapshot.child("userphonenumber").getValue().toString();
                        String myProfileEmail = dataSnapshot.child("useremailAddress").getValue().toString();
                        String myProfileLocation = dataSnapshot.child("userAddress").getValue().toString();

                        txt_username.setText("Username: " + myProfileName);
                        txt_userphone.setText("Phone Number: " + myProfilePhone);
                        txt_useremail.setText("Email: " + myProfileEmail);
                        txt_userlocation.setText("Location: " + myProfileLocation);
                    }
                }
                catch (Exception e){
                    Toast.makeText(ProfileActivity.this, "There is no internet connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

    }


    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
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
