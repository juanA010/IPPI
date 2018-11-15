package parentapp.ippi.ippiparent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeEmailActivity extends AppCompatActivity {

    private EditText ChangeEmail, VerifyEmail;
    private Button UpdateEmail;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ChangeEmail = findViewById(R.id.etChangeEmail);
        VerifyEmail = findViewById(R.id.etVerifyEmail);
        UpdateEmail = findViewById(R.id.btUpdateEmail);

        //get firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        UpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String updt_changeEmail = ChangeEmail.getText().toString();
                final String updt_verifyEmail = VerifyEmail.getText().toString();


                if (updt_verifyEmail.isEmpty() || updt_changeEmail.isEmpty()){
                    Toast.makeText(ChangeEmailActivity.this, "Empty field! Please enter your email", Toast.LENGTH_SHORT).show();

                }

                if(updt_verifyEmail.equals(updt_changeEmail)){

//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    user.updateEmail(updt_verifyEmail.trim());
//                    Toast.makeText(ChangeEmailActivity.this, "Successfully Updated! You can now sign in using your new Email", Toast.LENGTH_LONG).show();
//                    Log.d("tag", "email: "+user.getEmail());
//                    finish();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updateEmail(updt_verifyEmail);

                    System.out.println("email: "+user.getEmail());

                    database.getReference("Parents").child(mAuth.getCurrentUser().getUid()).child("useremailAddress").setValue(updt_verifyEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                    Toast.makeText(ChangeEmailActivity.this, "Successfully Updated! You can now sign in using your new Email", Toast.LENGTH_LONG).show();
                                    finish();


                                    }

                             else {
                                Toast.makeText(ChangeEmailActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                            }


                        }



                    });

//                    user.sendEmailVerification();
//                    Toast.makeText(ChangeEmailActivity.this,"email: "+user.getEmail(), Toast.LENGTH_SHORT).show();


//

                }

                if (!updt_verifyEmail.equals(updt_changeEmail)){
                    Toast.makeText(ChangeEmailActivity.this, "Email not matched. Please enter again! ", Toast.LENGTH_SHORT).show();

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
