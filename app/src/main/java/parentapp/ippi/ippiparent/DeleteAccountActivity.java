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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteAccountActivity extends AppCompatActivity {

    private EditText VerifyEmail;
    private EditText VerifyPass;
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
        VerifyPass = findViewById(R.id.etInpPassword);
        DeleteAccount = findViewById(R.id.btDelete);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        user = mAuth.getCurrentUser();


        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_inpEmail = VerifyEmail.getText().toString();
                String txt_inpPass = VerifyPass.getText().toString();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(txt_inpEmail, txt_inpPass);

// Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("tag", "User re-authenticated.");

                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                database.getReference("Parents").child(user.getUid()).removeValue();
                                                Toast.makeText(DeleteAccountActivity.this, "Goodbye and have a nice day", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(DeleteAccountActivity.this, SignUpActivity.class));
                                            }

                                        }
                                    });
                                }else{
                                    Toast.makeText(DeleteAccountActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

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
