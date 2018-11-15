package parentapp.ippi.ippiparent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import parentapp.ippi.ippiparent.model.SignUpModel;


public class SignUpActivity extends AppCompatActivity{

    private EditText userEmail, userPassword, userName, userPhone, userAddress, userAge, userGender;
    private Button SignUp;
    private TextView GoToLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        userEmail = (EditText) findViewById((R.id.etUserEmail));
        userPassword = (EditText) findViewById(R.id.etUserPassword);
        userName = (EditText) findViewById(R.id.etUsername);
        userPhone = findViewById(R.id.etUserPhone);
        userAddress = findViewById(R.id.etUserAddress);
        userAge = findViewById(R.id.etUserAge);
        userGender = findViewById(R.id.etUserGender);
        SignUp = (Button) findViewById(R.id.btnRegister);
        GoToLogin = (TextView) findViewById(R.id.goToLogin);

        mAuth = FirebaseAuth.getInstance();

        GoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txt_username = userName.getText().toString();
                final String txt_useremail = userEmail.getText().toString();
                final String txt_userpassword = userPassword.getText().toString();
                final String txt_userphone = userPhone.getText().toString();
                final String txt_userAddress = userAddress.getText().toString();
                final String txt_userAge = userAge.getText().toString();
                final String txt_userGender = userGender.getText().toString();

                if(txt_username.isEmpty() || txt_useremail.isEmpty() || txt_userpassword.isEmpty()||txt_userphone.isEmpty()||txt_userAddress.isEmpty()||txt_userAge.isEmpty()||txt_userGender.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }
                if(txt_userpassword.length() < 6){
                    userPassword.setError(getString(R.string.minimum_password));
                }
                else{

                    //create user

                    mAuth.createUserWithEmailAndPassword(txt_useremail, txt_userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Registration Failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            SignUpModel register = new SignUpModel(txt_useremail, txt_username, txt_userphone, txt_userAddress, txt_userAge, txt_userGender );
                                            database.getReference("Parents").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(register).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                        mAuth.signOut();
                                                        finish();}

                                                    else {
                                                        Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            Toast.makeText(SignUpActivity.this, "Verification email not sent!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
//                                SignUpModel register = new SignUpModel(txt_useremail, txt_username, txt_userphone, txt_userAddress, txt_userAge, txt_userGender );
//                                database.getReference("Parents").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(register).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(SignUpActivity.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
//                                            mAuth.signOut();
//                                            finish();}
//
//                                            else {
//                                            Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    }
//                                });

                            }

                        }
                    });

                }
            }
        });



    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(SignUpActivity.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//        }
    }


//
//    private void register(final String username, String email, String password, String phone, String address, String age, String gender){
//
//        final SignUpModel register = new SignUpModel(username, email, password,phone,address,age,gender);
//
//        reference = database.getReference("user");
//
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(register.getUsername()).exists()){
//                    Toast.makeText(getApplicationContext(), "Username already exist", Toast.LENGTH_SHORT).show();
//                } else
//                {
//                    reference.child(register.getUsername()).setValue(register);
//                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }




}
