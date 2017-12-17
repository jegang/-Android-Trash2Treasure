/**
 * @FileName UserProfile.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class UserProfile {
    HashMap profileInfo;
    String userId;
    List<String> myAds;
    List<Map<String,?>> AdsList;

    DatabaseReference myRef;
    StorageReference storageReference;
    boolean isGoogleAccount;
    String accountPath;
    final static String LOCAL_ACCOUNT = "UserProfile/LocalAccount";
    final static String GOOGLE_ACCOUNT = "UserProfile/GoogleAccount";

    private static UserProfile single_instance = null;

    public static UserProfile getInstance()
    {
        if (single_instance == null)
            single_instance = new UserProfile();

        return single_instance;
    }

    HashMap getProfileInfo()
    {
        return  profileInfo;
    }

    void setGoogleAccount()
    {
        isGoogleAccount = true;
        accountPath = GOOGLE_ACCOUNT;
    }

    void setLocalAccount()
    {
        isGoogleAccount = false;
        accountPath = LOCAL_ACCOUNT;
    }
    // Constructor gets the reference of firebase db
    public UserProfile(){
        profileInfo = new HashMap();
//        setLocalAccount();
        storageReference = FirebaseStorage.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference();
        AdsList = new ArrayList<Map<String ,?>>();
    }

    // Function creates the user profile for the first time
    void createUserProfile(String emailId, String fullName, String picture, String address, String phoneNumber)
    {
        profileInfo.put("emailId", emailId);
        profileInfo.put("fullName", fullName);
        profileInfo.put("picture", picture);
        profileInfo.put("address", address);
        profileInfo.put("phoneNumber", phoneNumber);
//        List<String> str = new ArrayList<String>();
//        str.add("");
//        profileInfo.put("yourposts", str);

        setLocalAccount();
        userId = emailId.replace(".","-");
        Log.d("User profile id ", userId);
        myRef.child(accountPath).child(userId).setValue(profileInfo);
    }

    public void updateProfile(String name, String address, String phonenumber)
    {
        myRef.child(accountPath).child(userId).child("fullName").setValue(name);
        myRef.child(accountPath).child(userId).child("phoneNumber").setValue(address);
        myRef.child(accountPath).child(userId).child("address").setValue(phonenumber);
    }


    public List< Map < String , ?>> getAdsList () {
        return AdsList ;
    }

    public HashMap getItem (int i){
        if (i >=0 && i < AdsList . size () ){
            return ( HashMap ) AdsList . get (i);
        }
        else
            return null ;
    }

    // Function creates the user profile for the first time
    void createGoogleUserProfile(String emailId, String fullName, String picture, String address, String phoneNumber)
    {
        userId = emailId.replace(".","-");
        Log.d("User profile id ", userId);
        final String final_emailId = emailId;
        final String final_fullName = fullName;
        final String final_picture = picture;
        final String final_address = address;
        final String final_phoneNumber = phoneNumber;
        setGoogleAccount();
        myRef.child(accountPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(userId).exists()) {
                    // run some code
                    if (snapshot.child(userId).child("yourposts").exists()) {
                        // run some code
                        GenericTypeIndicator<List<String>> genericTypeIndicator =new GenericTypeIndicator<List<String>>(){};

                        myAds=snapshot.child(userId).child("yourposts").getValue(genericTypeIndicator);

                        for(int i=0;i<myAds.size();i++){
                            Log.d("MyAds","TaskTitle = "+myAds.get(i));


                            final String postId = myAds.get(i);
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    AdsList.add((HashMap<String, String>) dataSnapshot.getValue());
                                   }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
                                }
                            });

                        }
                    }
                    else
                    {
                        myAds = null;
                    }
                }
                else
                {
                    profileInfo.put("emailId", final_emailId);
                    profileInfo.put("fullName", final_fullName);
                    profileInfo.put("picture", final_picture);
                    profileInfo.put("address", final_address);
                    profileInfo.put("phoneNumber", final_phoneNumber);
                    myRef.child(accountPath).child(userId).setValue(profileInfo);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
            }
        });
        //myRef.child(accountPath).child(userId).setValue(profileInfo);
    }

    public void refreshMyAds()
    {
        myRef.child(accountPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(userId).exists()) {
                    // run some code
                    if (snapshot.child(userId).child("yourposts").exists()) {
                        // run some code
                        GenericTypeIndicator<List<String>> genericTypeIndicator =new GenericTypeIndicator<List<String>>(){};

                        myAds=snapshot.child(userId).child("yourposts").getValue(genericTypeIndicator);

                        for(int i=0;i<myAds.size();i++){
                            Log.d("MyAds","TaskTitle = "+myAds.get(i));



                            AdsList = new ArrayList<Map<String ,?>>();

                            final String postId = myAds.get(i);
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    AdsList.add((HashMap<String, String>) dataSnapshot.getValue());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
                                }
                            });

                        }
                    }
                    else
                    {
                        myAds = null;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
            }
        });

    }


    public List getMyAds()
    {
//        myRef.child(accountPath).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.child("yourposts").exists()) {
//                    // run some code
//                    myAds = (ArrayList)snapshot.child("yourposts").getValue();
//                }
//                else
//                {
//                    myAds = null;
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
//            }
//        });
        return getAdsList();
    }

    void updateUserProfile()
    {
        FirebaseDatabase.getInstance().getReference().child(accountPath).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileInfo = (HashMap<String, String>) dataSnapshot.getValue();
                Log.d("Email Id", profileInfo.get("emailId").toString());
                Log.d("Full Name", profileInfo.get("fullName").toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
            }
        });

    }

    void updateNavHeader(final View headerView)
    {
        final TextView tv_name = (TextView) headerView.findViewById(R.id.nav_name);
        final TextView tv_email = (TextView) headerView.findViewById(R.id.nav_emailId);
        final ImageView iv_displayPic = (ImageView) headerView.findViewById(R.id.nav_profile);
        FirebaseDatabase.getInstance().getReference().child(accountPath).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileInfo = (HashMap<String, String>) dataSnapshot.getValue();
                tv_email.setText(profileInfo.get("emailId").toString());
                tv_name.setText(profileInfo.get("fullName").toString());
                String picturePath = profileInfo.get("picture").toString();
                Log.d("picturePath", picturePath);
                if(isGoogleAccount) {
                     Glide.with(headerView.getContext())
                        .load(picturePath)
                        .into(iv_displayPic);
                }
                else {

                    Glide.with(headerView.getContext())
                            .using(new FirebaseImageLoader())
                            .load(storageReference.child(picturePath))
                            .into(iv_displayPic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
            }
        });
    }



    void interestedDataUpdate (final View headerView, String adposter)
    {
        final TextView tv_number = (TextView) headerView.findViewById(R.id.phoneNumberID);
        final TextView tv_email = (TextView) headerView.findViewById(R.id.emailIDID);
        final ImageView iv_displayPic = (ImageView) headerView.findViewById(R.id.profile_image);
        boolean googleAccount = false;
        if (adposter.contains("GoogleAccount"))
        {
            googleAccount = true;
        }
        final boolean accountType = googleAccount;

        Log.d("TrashToTreasure", "UserProfile:interestedDataUpdate adposter : "+adposter);
        Log.d("TrashToTreasure", "UserProfile:interestedDataUpdate accountType : "+googleAccount);

        FirebaseDatabase.getInstance().getReference().child(adposter).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map <String, ?>  data = (HashMap<String, String>) dataSnapshot.getValue();
                tv_email.setText(data.get("emailId").toString());
                tv_number.setText(data.get("phoneNumber").toString());
                String picturePath = data.get("picture").toString();
                Log.d("picturePath", picturePath);
                if(accountType) {
                    Glide.with(headerView.getContext())
                            .load(picturePath)
                            .into(iv_displayPic);
                }
                else {
                    Glide.with(headerView.getContext())
                            .using(new FirebaseImageLoader())
                            .load(storageReference.child(picturePath))
                            .into(iv_displayPic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
            }
        });
    }


    public void deleteAdFromUserAccount(String id)
    {
        myAds.remove(id);
        for(int i=0; i < AdsList.size(); i++)
        {
            HashMap ToRemove = (HashMap)AdsList.get(i);
            String newID = (String)ToRemove.get("id");

            if( newID == id  )
            {

                AdsList.remove(ToRemove);
            }
        }
        myRef.child(accountPath).child(userId).child("yourposts").setValue(myAds);
    }
}

