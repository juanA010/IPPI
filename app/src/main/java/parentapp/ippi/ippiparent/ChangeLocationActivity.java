package parentapp.ippi.ippiparent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeLocationActivity extends AppCompatActivity {

    private EditText ChangeLocation, VerifyLocation;
    private Button UpdateLocation;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ChangeLocation = findViewById(R.id.etChangeLocation);
        VerifyLocation = findViewById(R.id.etVerifyLocation);
        UpdateLocation = findViewById(R.id.btUpdateLocation);

        //get firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        UpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String updt_changeLocation = ChangeLocation.getText().toString();
                final String updt_verifyLocation = VerifyLocation.getText().toString();

                if (updt_verifyLocation.isEmpty() || updt_changeLocation.isEmpty()){
                    Toast.makeText(ChangeLocationActivity.this, "Empty field! Please enter your new Address", Toast.LENGTH_SHORT).show();

                }

                if(updt_verifyLocation.equals(updt_changeLocation)){
                    database.getReference("Parents").child(mAuth.getCurrentUser().getUid()).child("userAddress").setValue(updt_verifyLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(ChangeLocationActivity.this, "Successfully Updated! ", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangeLocationActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                if (!updt_verifyLocation.equals(updt_changeLocation)){
                    Toast.makeText(ChangeLocationActivity.this, "Address not matched. Please enter again! ", Toast.LENGTH_SHORT).show();

                }

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
