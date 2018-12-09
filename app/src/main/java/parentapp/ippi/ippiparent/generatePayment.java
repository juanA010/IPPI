package parentapp.ippi.ippiparent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class generatePayment extends AppCompatActivity {

    String receiptID, bookID;
    String chargeNew, timeNew;
    final SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    public generatePayment() {


    }

    public void calculatePayment(final String bookID, final String ReceiptID){
        this.receiptID = ReceiptID;
        this.bookID = bookID;
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference receiptData = FirebaseDatabase.getInstance().getReference("BookingReceipt").child(userID).child(receiptID);
        receiptData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String getStartTime = dataSnapshot.child("startTime").getValue().toString();
                    String getEndTime = dataSnapshot.child("endTime").getValue().toString();

                    try {
                        Date startTime = df.parse(getStartTime);
                        Date endTime = df.parse(getEndTime);

                        long difference = endTime.getTime() - startTime.getTime();

                        if(difference<0){
                            Date dateMax = df.parse("24:00");
                            Date dateMin = df.parse("00:00");
                            difference = (dateMax.getTime()-startTime.getTime())+(endTime.getTime()-dateMin.getTime());
                        }

                        int days = (int) (difference / (1000*60*60*24));
                        int hours = (int) ((difference - (1000*60*60*24*days))/(1000*60*60));
                        int min = (int)(difference - (1000*60*60*24*days) - (1000*60*60*hours))/ (1000*60);

                        totalCharge(hours, min, bookID, receiptID, userID);

                        String h =  Integer.toString(hours);
                        String m = String.valueOf(min);


                        //return h+" "+m;
                        Log.d("tag", "duration time: "+h+":"+m);
                       // db.getReference("BookingReceipt").child(userID).child(receiptID).child("totalCharge").setValue(h+"+"+m);
                        //Toast.makeText(generatePayment.this, "hour"+h+" minutes: "+m,Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void totalCharge (int hour, int mins, final String bID,  final String rID, final String uID){
        int rateHour = 10;
        float rateMin = (float) 0.2;


        final float totalCharge = (rateHour*hour)+(rateMin*mins);
        final String total = String.format("%.2f",totalCharge);


        final DatabaseReference receiptData = FirebaseDatabase.getInstance().getReference("BookingReceipt").child(uID).child(rID);
        receiptData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    receiptData.child("TotalCharge").setValue(total);
                    db.getReference("BookingData").child(uID).child(bID).child("TotalCharge").setValue(total);
                }
                else{
                    Toast.makeText(generatePayment.this, "Receipt not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public float requestExtra(String ori, String hour, String min){


        float originalCharge = Float.parseFloat(ori);
        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(min);
        int rateHour = 10;
        float rateMin = (float) 0.2;

        int calculateHourExtra = rateHour*h;
        float calculateMinExtra = rateMin * m;

        float extraCharge = calculateHourExtra + calculateMinExtra;

        float newCharge =(originalCharge + extraCharge);

        return  newCharge;


    }

    public float getAddCharge(String hour, String min){

        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(min);
        int rateHour = 10;
        float rateMin = (float) 0.2;

        int calculateHourExtra = rateHour*h;
        float calculateMinExtra = rateMin * m;

        float extraCharge = calculateHourExtra + calculateMinExtra;

        return  extraCharge;


    }

    public String NewEndTime (String end, String hour, String min ){

        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(min);
        String newTime = "null";

        try {
            Date endTime = df.parse(end);
            Calendar cal = Calendar.getInstance();
            cal.setTime(endTime);
            cal.add(Calendar.HOUR, h);
            cal.add(Calendar.MINUTE, m);


            newTime = df.format(cal.getTime());
           //newTime = String.valueOf(cal.getTime());



        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  newTime;
    }



}
