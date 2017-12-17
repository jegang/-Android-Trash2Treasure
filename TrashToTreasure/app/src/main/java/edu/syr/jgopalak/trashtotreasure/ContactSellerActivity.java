/**
 * @FileName ContactSellerActivity.java
 * @Functionality
 *          Activity to load the information of
 *          the donator of the item
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ContactSellerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, InterestedFragment.OnFragmentInteractionListener, PostDataFragment.OnFragmentInteractionListener{

    NavigationView navigationView;
    View headerView;
    private DatabaseReference mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_post_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        UserProfile.getInstance().updateNavHeader(headerView);

        mDB = FirebaseDatabase.getInstance().getReference();
        Bundle bundle = getIntent().getExtras();
        String postId = bundle.getString("message");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, PostDataFragment.newInstance(AdDetailInfo.getInstance().getMyAd())).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            Intent intent1 = new Intent(this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
        else if(id == R.id.nav_home)
        {
            Intent intent1 = new Intent(this, MainScreen.class);
            startActivity(intent1);
        }
        else if (id == R.id.nav_post_ad)
        {
            Intent itemIntent = new Intent(this, AdPostActivity.class);
            startActivity(itemIntent);
            return true;
        }
        else if (id == R.id.nav_aboutme)
        {
            Intent itemIntent = new Intent(this, ProfilePageActivity.class);
            startActivity(itemIntent);
            return true;
        }
        else if (id == R.id.nav_myads)
        {
            Intent itemIntent = new Intent(this, MyAdActivity.class);
            startActivity(itemIntent);
            return true;
        }
        else if( id == R.id.nav_aboutDev)
        {
            Intent itemIntent = new Intent(this, AboutMeActivity.class);
            startActivity(itemIntent);
            return true;

        }

//        } else if (id == R.id.nav_task1) {
//            Intent intent1 = new Intent(this, Task1Activity.class);
//            startActivity(intent1);
//            return true;
//
//        } else if (id == R.id.nav_task2) {
//            Intent intent = new Intent(this, Task2Activity.class);
//            startActivity(intent);
//            return true;

        // }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCallFragmentInteraction(View v, String phoneNumber) {

        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                "tel", phoneNumber, null));
        startActivity(phoneIntent);

    }

    @Override
    public void onEmailFragmentInteraction(View v, String emailID) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:"+emailID+"?subject=" + "Is this item still available? " + "&body=" + "");
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onClickFragmentInteraction(String str) {
        FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();


        FirebaseDatabase.getInstance().getReference().child("Posts").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap hmp = (HashMap<String, String>) dataSnapshot.getValue();
                String adposter = hmp.get("adposter").toString();

                InterestedFragment frontFragment = new InterestedFragment();
                        ft.replace(R.id.main_frame,InterestedFragment.newInstance(adposter, adposter));
                        ft.commit();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
            }
        });


//        InterestedFragment frontFragment = new InterestedFragment();
//        ft.replace(R.id.main_frame,frontFragment);
//        ft.commit();
    }
}
