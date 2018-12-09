package parentapp.ippi.ippiparent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MessagesActivityDetails extends AppCompatActivity {
    String a;
    private DatabaseReference MsgDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        MsgDB = FirebaseDatabase.getInstance().getReference().child("Messages");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_details);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



//        output.setText(getIntent().getExtras().getString("Header"));
        String a = getIntent().getExtras().getString("Header");
        System.out.println("THSI IS A"+a);
        String b;
        Query query = MsgDB.orderByChild("Header").equalTo(a);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView output = (TextView) findViewById(R.id.text1);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String b=ds.child("Description").getValue(String.class);
                    System.out.println("this is description"+b);
                    output.setText(b);
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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