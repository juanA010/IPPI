package parentapp.ippi.ippiparent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.view.View;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class HelpActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition),
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition),
//                        Toast.LENGTH_SHORT).show();

            }
        });

//        // Listview on child click listener
//        expListView.setOnChildClickListener(new OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//                // TODO Auto-generated method stub
//                Toast.makeText(
//                       getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                               + " : "
//                               + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                               childPosition), Toast.LENGTH_SHORT)
//                        .show();
//                return false;
//            }
//        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("About Happy Nanny");
        listDataHeader.add("How safety is the App?");
        listDataHeader.add("How to book BabySitter?");
        listDataHeader.add("What is the Payment Method?");
        listDataHeader.add("What is the rate charge for the Babysitter?");

        // Adding child data
        List<String> about = new ArrayList<String>();
        about.add("Happy Nanny is the application that provide a platform for a parent to book a babysitter for their kids." +
                " As nowday it is hard for the parent to find available and trusted babysitter around their area, so by using this application " +
                "they can search for available babysitter easily.");

        List<String> safety = new ArrayList<String>();
        safety.add("The Babysitter must undergo a few verification process to ensure they are qualified to be a babysitter. The verification steps is as follow:");
        safety.add("1)Document Submission: Babysitter must go to the company by submitting their personal detail and document such as photocopy of IC, health examination result " +
                "and a verification form that provide by the company signed or stamped by local authorities. (Village Committee, District Officer ,etc ");
        safety.add("2)Document Screening: To verify either the documents are fake or not and confirmation by the local authorities");
        safety.add("3)Interview Session : If the babysitter passed the screening they will be informed to come for an interview at the company");
        safety.add("4)Registration Session: After success the interview the babysitter will be called to come to the company and register their detail into the system");
        safety.add("By following this steps, we ensure that all babysitter are qualified to use the Babysitter Application and make their services.");

        List<String> booking = new ArrayList<String>();
        booking.add("1) Click on the START button to start booking the babysitter");
        booking.add("2) Click on the filter bar to filter your babysitter preference");
        booking.add("3) Click the Search Button to start search for available babysitter");
        booking.add("4) If we cannot retrieve the available babysitter for a few minutes, you need to choose other preferences");
        booking.add("5) Choose the babysitter from the list and view their profile");
        booking.add("6) Click the accept button to start their service and once you arrived to babysitter house click the Arrived Button");
        booking.add("7) While babysitter on service you can track your child location as our babysitter are equipped with tracking device and will be put on your child");
        booking.add("8) You can request extra time if you need more time to picked up your child and extra charge will be added");
        booking.add("9) Once you finished picked up your child, the receipt for the payment will be display for you.");
        booking.add("10) You need to pay at that time once you picked up your child");

        List<String> payment = new ArrayList<String>();
        payment.add("The payment method provide for this app at this time only cash payment. We will try to serve you better by providing online" +
                "transaction in the future");

        List<String> rateCharge = new ArrayList<String>();
        rateCharge.add("The rate for babysitter services has been set for RM5 per hour");

        listDataChild.put(listDataHeader.get(0), about); // Header, Child data
        listDataChild.put(listDataHeader.get(1), safety);
        listDataChild.put(listDataHeader.get(2), booking);
        listDataChild.put(listDataHeader.get(3), payment);
        listDataChild.put(listDataHeader.get(4), rateCharge);
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
