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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingHistoryActivity extends AppCompatActivity {

    private DatabaseReference MsgDB;
    public ArrayList<String> BookID=new ArrayList<>();
    public ArrayList<String> Dates=new ArrayList<>();
    public ArrayList<String> Time=new ArrayList<>();
    public ArrayList<String> SitterName=new ArrayList<>();

    MyAdapter myAdapter;
    private List<msg_data> MSGDATA= new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        MsgDB = FirebaseDatabase.getInstance().getReference().child("BookingData").child(userID);

//        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);

//        listView.setAdapter(adapter);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String BOOKINGID = ds.getKey().toString();
                    String Date = ds.child("BookDate").getValue(String.class);
                    String Times = ds.child("BookStart").getValue(String.class);
                    String Name = ds.child("SitterName").getValue(String.class);

                    list.add(BOOKINGID+ " / " + Date);
                    Log.d("TAG", BOOKINGID  );
                    System.out.println(list);
                    System.out.println("INI HEADER OI"+BOOKINGID);
                    BookID.add(BOOKINGID);
                    Dates.add(Date);
                    Time.add(Times);
                    SitterName.add(Name);

                    MSGDATA.add(new msg_data(BOOKINGID,Date,Times,Name));


                }

                System.out.println(BookID);
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
                        Toast.makeText(getApplicationContext(), BookID.get(position), Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(), BookingHistoryDetail.class);
                        intent.putExtra("BookingID", BookID.get(position).toString());
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
        String BookingID;
        String Date;
        String Time;
        String SitterName;


        public msg_data(String bookingID, String date, String time, String sitterName) {
            BookingID = bookingID;
            Date = date;
            Time = time;
            SitterName = sitterName;
        }


        public String getBookingID() {
            return BookingID;
        }

        public void setBookingID(String bookingID) {
            BookingID = bookingID;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String time) {
            Time = time;
        }

        public String getSitterName() {
            return SitterName;
        }

        public void setSitterName(String sitterName) {
            SitterName = sitterName;
        }


    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<BookingHistoryActivity.msg_data> msgdatalist;

        public MyAdapter(Context mContext, List<BookingHistoryActivity.msg_data> msgdatalist) {
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
            @SuppressLint("ViewHolder") View v=View.inflate(mContext,R.layout.historylistlayout, null);
            TextView BookingID=(TextView) v.findViewById(R.id.tvBookingID);
            TextView Date=(TextView) v.findViewById(R.id.tvBookDate);
            TextView Time=(TextView) v.findViewById(R.id.tvBookTime);
            TextView SitterName=(TextView) v.findViewById(R.id.tvBookSitter);


            //set data

            BookingID.setText(msgdatalist.get(position).getBookingID());
            Date.setText(msgdatalist.get(position).getDate());
            Time.setText(msgdatalist.get(position).getTime());
            SitterName.setText(msgdatalist.get(position).getSitterName());


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
