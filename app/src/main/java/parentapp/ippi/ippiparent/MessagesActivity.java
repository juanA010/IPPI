package parentapp.ippi.ippiparent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    //firbase var
    private DatabaseReference MsgDB;
    public ArrayList<String> Header=new ArrayList<>();

    public ArrayList<String> Dates=new ArrayList<>();

    MyAdapter myAdapter;
    private List<msg_data> MSGDATA= new ArrayList<>();
    private ListView listView;

    //arraylist var
//    private ArrayList<String> arrayList= new ArrayList<>();
//    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MsgDB = FirebaseDatabase.getInstance().getReference().child("Messages");

//        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);

//        listView.setAdapter(adapter);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String HEADER = ds.child("Header").getValue(String.class);
                    String Date = ds.child("Date").getValue(String.class);
                    list.add(HEADER+ " / " + Date);
                    Log.d("TAG", HEADER  );
                    System.out.println(list);
                    System.out.println("INI HEADER OI"+HEADER);
                    Header.add(HEADER);
                    Dates.add(Date);

                    MSGDATA.add(new msg_data(HEADER,Date));


                }

                System.out.println(Header);
                System.out.println(Dates);

//                System.out.println("this is array items list");
//                System.out.println();


                ListView listView = (ListView) findViewById(R.id.messageslistview);

                myAdapter = new MyAdapter(getApplicationContext(),MSGDATA);
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MessagesActivity.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(myAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(), Header.get(position), Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(), MessagesActivityDetails.class);
                        intent.putExtra("Header", Header.get(position).toString());
                        startActivity(intent);

                    }
                });




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        MsgDB.addListenerForSingleValueEvent(eventListener);



    }

    class msg_data{
        String Header;
        String Date;

        public msg_data(String header, String date) {
            Header = header;
            Date = date;
        }

        public String getHeader() {
            return Header;
        }

        public void setHeader(String header) {
            Header = header;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }
    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<msg_data> msgdatalist;

        public MyAdapter(Context mContext, List<msg_data> msgdatalist) {
            this.mContext = mContext;
            this.msgdatalist = msgdatalist;
        }

        @Override
        public int getCount() {
            return msgdatalist.size();
        }

        @Override
        public Object getItem(int position) {
            return msgdatalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") View v=View.inflate(mContext,R.layout.msglistlayout, null);
            TextView Header=(TextView) v.findViewById(R.id.Header);
            TextView Date=(TextView) v.findViewById(R.id.Dates);

            //set data

            Header.setText(msgdatalist.get(position).getHeader());
            Date.setText(msgdatalist.get(position).getDate());

            return v;
        }
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
