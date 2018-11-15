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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassActivity extends AppCompatActivity {

    private EditText ChangePassword, VerifyPassword;
    private Button UpdatePassword;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ChangePassword = findViewById(R.id.etChangePass);
        VerifyPassword = findViewById(R.id.etVerifyPass);
        UpdatePassword = findViewById(R.id.btUpdatePassword);

        //get firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        UpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String updt_changePass = ChangePassword.getText().toString();
                final String updt_verifyPass = VerifyPassword.getText().toString();

                if (updt_verifyPass.isEmpty() || updt_changePass.isEmpty()){
                    Toast.makeText(ChangePassActivity.this, "Empty field! Please enter your new Address", Toast.LENGTH_SHORT).show();

                }

                if(updt_verifyPass.equals(updt_changePass)) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(updt_verifyPass);

                    Toast.makeText(ChangePassActivity.this, "Successfully Updated! You can now Sign In using your new Password", Toast.LENGTH_LONG).show();
                    finish();

                }

                if (!updt_verifyPass.equals(updt_changePass)){
                    Toast.makeText(ChangePassActivity.this, "Password not matched. Please enter again! ", Toast.LENGTH_SHORT).show();

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
