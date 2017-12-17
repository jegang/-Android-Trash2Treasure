/**
 * @FileName ProfilePageActivity.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;

public class ProfilePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProfilePageFragment.OnFragmentInteractionListenerEditProfile, EditProfilePageFragment.OnFragmentInteractionListenerProfile{

    NavigationView navigationView;
    View headerView;
    private DatabaseReference mDB;
    Menu menu;
    int PICK_IMAGE_REQUEST = 111;
    ImageView imgview;
    Uri filePath;
    ProgressDialog pd;

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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, ProfilePageFragment.newInstance()).addToBackStack(null).commit();

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
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        menu.setGroupVisible(R.id.menu_profilepage,true);
        menu.setGroupVisible(R.id.menu_editprofile, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.menu_user_editprofile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, EditProfilePageFragment.newInstance()).addToBackStack(null).commit();
                menu.setGroupVisible(R.id.menu_profilepage,false);
                menu.setGroupVisible(R.id.menu_editprofile, true);
                break;
            case R.id.menu_user_logout:

                Intent intent1 = new Intent(ProfilePageActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.menu_gotoprofile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, ProfilePageFragment.newInstance()).addToBackStack(null).commit();
                menu.setGroupVisible(R.id.menu_profilepage,true);
                menu.setGroupVisible(R.id.menu_editprofile, false);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Inside on Activity  ","Req Code"+requestCode);
        Log.d("Inside on Activity ","Result"+RESULT_OK);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            Log.d("Inside Gallery","Printing"+filePath);

            //getting image from gallery
            Bitmap bitmap = null;
            try {
                ContentResolver contentResolver = getContentResolver();
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath);
                //Setting image to ImageView
                imgview.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteractionEditProfile() {
        menu.setGroupVisible(R.id.menu_profilepage,false);
        menu.setGroupVisible(R.id.menu_editprofile, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, EditProfilePageFragment.newInstance()).addToBackStack(null).commit();

    }

    public void imgUploadClickedFromFragment(View view)
    {
        imgview = (ImageView)view.findViewById(R.id.iv_editprofile_displaypic);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

    }

    public void uploadImage()
    {
        if(filePath != null) {


//            String imagename = UserProfile.getInstance().userId+".jpg";
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            String imagename = timestamp.getTime()+".jpg";

            StorageReference photoRef = FirebaseStorage.getInstance().getReference().child(UserProfile.getInstance().profileInfo.get("picture").toString());

            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Log.d("Trash2Treasure", "onSuccess: deleted file");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.d("Trash2Treasure", "onFailure: did not delete file");
                }
            });




            StorageReference childRef = FirebaseStorage.getInstance().getReference().child("UserProfilePictures").child(imagename);

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            UserProfile.getInstance().profileInfo.put("picture", imagename);
            UserProfile.getInstance().myRef.child(UserProfile.getInstance().accountPath).child(UserProfile.getInstance().userId).child("picture").setValue("UserProfilePictures/"+imagename);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(ProfilePageActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ProfilePageActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(ProfilePageActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSaveButtonClick(){

        UserProfile.getInstance().updateUserProfile();
        Toast.makeText(ProfilePageActivity.this, "Profile Information Updated", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(ProfilePageActivity.this, MainScreen.class);
        startActivity(intent1);
        finish();
    }
}
