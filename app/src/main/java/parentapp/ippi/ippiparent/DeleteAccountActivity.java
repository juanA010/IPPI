package parentapp.ippi.ippiparent;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteAccountActivity extends AppCompatActivity {

    private EditText VerifyEmail;
    private Button DeleteAccount;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        VerifyEmail = findViewById(R.id.etInpEmail);
        DeleteAccount = findViewById(R.id.btDelete);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        user = mAuth.getCurrentUser();


        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_inpEmail = VerifyEmail.getText().toString();
                if(txt_inpEmail.equals(user.getEmail())){
                    database.getReference("Parents").child(user.getUid()).removeValue();
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DeleteAccountActivity.this, "Your account has been deleted! Thanks for using Happy Nanny :)", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(DeleteAccountActivity.this, LoginActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(DeleteAccountActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                if(!txt_inpEmail.equals(user.getEmail())){
                    Toast.makeText(DeleteAccountActivity.this, "Please enter valid email to proceed", Toast.LENGTH_SHORT).show();
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
