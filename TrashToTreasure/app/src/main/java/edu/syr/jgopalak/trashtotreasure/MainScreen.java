/**
 * @FileName MainScreen.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Method;
import java.util.HashMap;

public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeScreenFragment.RecycleViewListener{

    NavigationView navigationView;
    View headerView;
    private DatabaseReference mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
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


        initCollapsingToolbar();

        mDB = FirebaseDatabase.getInstance().getReference();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, HomeScreenFragment.newInstance()).commit();

    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
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
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int loadID = item.getItemId();


        switch (loadID) {

            case R.id.nav_logout:
                // loading the about me layout in this fragment
                Intent intent1 = new Intent(MainScreen.this, MainActivity.class);
                startActivity(intent1);
                finish();
                return true;

            case R.id.nav_home:

                intent1 = new Intent(MainScreen.this, MainScreen.class);
                startActivity(intent1);
                return true;
            case R.id.nav_post_ad:

                Intent itemIntent = new Intent(this, AdPostActivity.class);
                startActivity(itemIntent);
                return true;

            case R.id.nav_aboutme:
                itemIntent = new Intent(this, ProfilePageActivity.class);
                startActivity(itemIntent);
                return true;
            case R.id.nav_myads:
                itemIntent = new Intent(this, MyAdActivity.class);
                startActivity(itemIntent);
                return true;
            case R.id.nav_aboutDev:

                itemIntent = new Intent(this, AboutMeActivity.class);
                startActivity(itemIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it


                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_logout) {
           Intent intent1 = new Intent(MainScreen.this, MainActivity.class);
           startActivity(intent1);
           finish();
       }
       else if(id == R.id.nav_home)
       {
           Intent intent1 = new Intent(MainScreen.this, MainScreen.class);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public void onRecycleViewItemClicked(View v, HashMap<String, ?> movie) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, PostDataFragment.newInstance(movie)).addToBackStack(null).commit();
    }
}

