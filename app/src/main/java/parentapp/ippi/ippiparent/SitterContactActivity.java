package parentapp.ippi.ippiparent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;

public class SitterContactActivity extends AppCompatActivity {

    private TextView SitterContact, FamilyContact, NeighbourContact;
    private DatabaseReference SitterContactRef;

    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_contact);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SitterContact = findViewById(R.id.tvSitterPhone);
        FamilyContact = findViewById(R.id.tvFamilyPhone);
        NeighbourContact = findViewById(R.id.tvNeighbourPhone);

        Intent intent = getIntent();
        final String Sitter = intent.getStringExtra(USERNAME_KEY);

        SitterContactRef = FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();

        SitterContactRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String username = postSnapshot.child("username").getValue().toString();

                    if(username.equals(Sitter)){
                        final String sitterPhone = postSnapshot.child("userphonenumber").getValue().toString();
                        final String familyPhone = postSnapshot.child("familyphonenumber").getValue().toString();
                        final String neighbourPhone = postSnapshot.child("neighbourphone").getValue().toString();


                        SitterContact.setText(sitterPhone);
                        SitterContact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent DIALIntent = new Intent(Intent.ACTION_DIAL);
                                DIALIntent.setData(Uri.parse("tel:"+sitterPhone));

                                if (ActivityCompat.checkSelfPermission(SitterContactActivity.this,
                                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(DIALIntent);
                            }
                        });
                        FamilyContact.setText(familyPhone);
                        FamilyContact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent DIALIntent = new Intent(Intent.ACTION_DIAL);
                                DIALIntent.setData(Uri.parse("tel:"+familyPhone));

                                if (ActivityCompat.checkSelfPermission(SitterContactActivity.this,
                                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(DIALIntent);
                            }
                        });
                        NeighbourContact.setText(neighbourPhone);
                        NeighbourContact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent DIALIntent = new Intent(Intent.ACTION_DIAL);
                                DIALIntent.setData(Uri.parse("tel:"+neighbourPhone));

                                if (ActivityCompat.checkSelfPermission(SitterContactActivity.this,
                                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(DIALIntent);
                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w("tag", "Failed to read value.", error.toException());
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
