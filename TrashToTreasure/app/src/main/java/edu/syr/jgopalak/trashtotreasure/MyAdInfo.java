/**
 * @FileName MyAdInfo.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyAdInfo {
    List<Map<String,?>> AdsList;
    DatabaseReference myRef;
    HomeScreenRecyclerAdapter myRecyclerAdapter;
    Context context;

    public int getSize(){
        return AdsList.size();
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

    public MyAdInfo(){

        final List<String> myAdsKey = UserProfile.getInstance().getMyAds();
        AdsList = new ArrayList<Map<String ,?>>();
        myRef = FirebaseDatabase.getInstance().getReference();

        for(int i=0; i< myAdsKey.size(); i++)
        {
            final String postId = myAdsKey.get(i);
            myRef.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child(postId).exists()) {
                        // run some code
                        HashMap<String, String> hmp = (HashMap) snapshot.child(postId).getValue();
                        AdsList.add(hmp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
                }
            });

        }
    }
}
