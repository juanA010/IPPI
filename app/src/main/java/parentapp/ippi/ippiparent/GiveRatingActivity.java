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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GiveRatingActivity extends AppCompatActivity {

    private Button btnDone;
    private TextView SitterName;
    private DatabaseReference sitterProfileRef;
    public  final static String USERNAME_KEY = "parentapp.ippi.ippiparent.message_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_rating);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDone = findViewById(R.id.buttonDone);
        SitterName = findViewById(R.id.tvSitterProfileName);
        Intent intent = getIntent();
        final String Sitter = intent.getStringExtra(USERNAME_KEY);

        sitterProfileRef = FirebaseDatabase.getInstance().getReference("BabysitterProfile").getRef();
        sitterProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    //String user =postSnapshot.getValue().toString();
                    String username = postSnapshot.child("username").getValue().toString();
                    if(username.equals(Sitter)){
                        SitterName.setText(username);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("tag", "Failed to read value.", error.toException());
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GiveRatingActivity.this, AppMenu.class));
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
