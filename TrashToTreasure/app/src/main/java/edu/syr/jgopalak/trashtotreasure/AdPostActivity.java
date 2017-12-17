/**
 * @FileName AdPostActivity.java
 * @Functionality
 *          To send the hashmap across the activity,
 *          created this singleton class
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   11/26/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdPostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PostAdInfoFragment.OnFragmentPostAdInfoInteractionListener,
        PostAdLocationFragment.OnFragmentPostAdLocationInteractionListener{

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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, PostAdInfoFragment.newInstance()).addToBackStack(null).commit();

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
            Intent intent1 = new Intent(AdPostActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
        else if(id == R.id.nav_home)
        {
            Intent intent1 = new Intent(AdPostActivity.this, MainScreen.class);
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
    public void onFragmentInteractionPostAdInfo(HashMap<String, String> info) {

        PostAdInformationCollector.getInstance().initiateElements();
        PostAdInformationCollector.getInstance().setItemName(info.get("title"));
        PostAdInformationCollector.getInstance().setItemAge(info.get("Age"));
        PostAdInformationCollector.getInstance().setCategory(info.get("category"));
        PostAdInformationCollector.getInstance().setItemDescription(info.get("description"));
        PostAdInformationCollector.getInstance().setItemColor(info.get("color"));

        //Intent
        Intent additionalItemIntent = new Intent(AdPostActivity.this,MapsActivity.class);
        startActivity(additionalItemIntent);
    }

    @Override
    public void onFragmentInteractionPostAdLocation(HashMap<String, String> info) {

    }
}
