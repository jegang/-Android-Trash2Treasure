/**
 * @FileName PostAdInformationCollector.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.id;

public class PostAdInformationCollector {


    private String itemName;
    private String category;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getItemAge() {
        return itemAge;
    }

    public void setItemAge(String itemAge) {
        this.itemAge = itemAge;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    private String itemColor;
    private String itemAge;
    private String itemDescription;
    private String latitude;
    private String longitude;
    private String address;
    private String picture;




    private static PostAdInformationCollector single_instance = null;

    public static PostAdInformationCollector getInstance()
    {
        if (single_instance == null)
            single_instance = new PostAdInformationCollector();

        return single_instance;
    }


    private  PostAdInformationCollector()
    {
        initiateElements();
    }


    public void initiateElements()
    {
        itemName = "";
        category = "";
        itemColor = "";
        itemAge = "";
        itemDescription = "";
        latitude = "";
        longitude = "";
        address = "";
        picture = "";
    }


    void createHashMap()
    {
        final HashMap<String, String> newPost = new HashMap<String,String>();
        String accountPath = UserProfile.getInstance().accountPath +"/"+UserProfile.getInstance().userId;
        newPost.put("Age", itemAge);
        newPost.put("title", itemName);
        newPost.put("category", category);
        newPost.put("color",itemColor);
        newPost.put("description", itemDescription);


        newPost.put("location", address);
        newPost.put("picture", picture);
        newPost.put("adposter", accountPath);
        newPost.put("userLatitude", latitude );
        newPost.put("userLongitude",longitude );

        String key = FirebaseDatabase.getInstance().getReference("Posts").push().getKey();
        newPost.put("id",key);
        FirebaseDatabase.getInstance().getReference().child("Posts").child(key).setValue(newPost);

       // FirebaseDatabase.getInstance().getReference().child(accountPath).child("yourposts").setValue(id);

        final String postKey = key;
        final String accPath = accountPath;

        FirebaseDatabase.getInstance().getReference().child(accountPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,?> Info = (HashMap<String, ?>) dataSnapshot.getValue();

                List<String> yourposts = (List)Info.get("yourposts");
                if(yourposts == null)
                {
                    yourposts = new ArrayList<String>();
                }

                yourposts.add(postKey);

                FirebaseDatabase.getInstance().getReference().child(accPath).child("yourposts").setValue(yourposts);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
            }
        });
    }




}
