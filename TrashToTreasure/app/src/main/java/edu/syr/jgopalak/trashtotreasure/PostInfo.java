/**
 * @FileName PostInfo.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PostInfo {

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

    public PostInfo(){ AdsList = new ArrayList<Map<String ,?>>();
        myRef = FirebaseDatabase.getInstance().getReference();
       /// addNewItemToFirebase();
    }

    public void initializeDataFromCloud(){
        AdsList.clear();
        myRef.child("Posts").addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot , String s)
            {
                Log.d("FBTest: OnChildAdded", dataSnapshot.toString());
                HashMap<String , String > post = (HashMap <String , String >) dataSnapshot.getValue();
                onItemAddedToCloud(post);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot , String s)
            {
                Log.d("FBTest: OnChildUpdated", dataSnapshot.toString());
                HashMap <String , String > post = (HashMap <String , String >) dataSnapshot.getValue();
                onItemUpdatedInCloud(post);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                Log.d("FBTest: OnChildDeleted", dataSnapshot.toString());
                HashMap <String , String > post = (HashMap <String , String >) dataSnapshot.getValue();
                onItemDeletedFromCloud(post);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot , String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void onItemAddedToCloud(HashMap item)
    {
        String id = String.valueOf(item.get("original_title"));
        int insertPos = 0;
        for( int i = 0; i < AdsList.size(); i++ )
        {
            HashMap post = (HashMap) AdsList.get(i);
            String movieId = String.valueOf(post.get("title"));
            if(movieId.equals(id))
            {
                break;
            }
            else if(movieId.compareTo(id) < 0)
            {
                insertPos = i+1;
            }
            else
                break;
        }
        AdsList.add(insertPos , item);
        Log.d("FBTest: notifyInserted ", id);
        if(myRecyclerAdapter != null)
        {
            myRecyclerAdapter.notifyItemInserted(insertPos);
            //myRecyclerAdapter.notifyDataSetChanged();
        }
    }
    private void onItemUpdatedInCloud(HashMap item)
    {
        String id = String.valueOf(item.get("title"));
        for (int i = 0; i < AdsList.size(); i++)
        {
            HashMap movie = (HashMap) AdsList.get(i);
            String movieId = (String) movie.get("title");
            if (movieId.equals(id))
            {
                AdsList.remove(i);
                AdsList.add(i, item);
                Toast.makeText(context, "Item Updated: " + id, Toast.LENGTH_SHORT).show();
                Log.d("FBTest: notifyUpdated ", id);
                if (myRecyclerAdapter != null)
                {
                    myRecyclerAdapter.notifyItemChanged(i);
                }
                break;
            }
        }
    }
    private void onItemDeletedFromCloud(HashMap item) {
        int pos = -1;
        String id = (String) item.get("title");
        for (int i = 0; i < AdsList.size(); i++) {
            HashMap movie = (HashMap) AdsList.get(i);
            String movieId = String.valueOf(movie.get("title"));
            if (movieId.equals(id)) {
                pos = i;
                break;
            }
        }
        if (pos != -1) {
            AdsList.remove(pos);
            Log.d("FBTest: notifyDeleted ", id);
            Toast.makeText(context, "Item Deleted: " + id, Toast.LENGTH_SHORT).show();
            if (myRecyclerAdapter != null) {
                myRecyclerAdapter.notifyItemRemoved(pos);
                myRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    public void deleteItemFromFirebase(Map<String , ?> movie)
    {
        if( movie != null )
        {
            String id = (String) movie.get("title");
            myRef.child("Posts").child(id).removeValue();
        }
    }
    public void addItemToFirebase(Map<String ,?> movie)
    {
        if( movie != null )
        {
            String id = String.valueOf(movie.get("title"));
            myRef.child("Posts").child(id).setValue(movie);
        }
    }

    public void addNewItemToFirebase()
    {
        Map<String ,String> hmp = new HashMap<String, String>();
        hmp.put("title", "Pens");
        hmp.put("category","Stationary");
        hmp.put("location", "216 Westcott Street");
        hmp.put("description", "10 colour pens contains multiple colors");
        hmp.put("picture", "AdImages/ad_1.jpg");

        String key = FirebaseDatabase.getInstance().getReference("Posts").push().getKey();
        hmp.put("id",key);
        myRef.child("Posts").child(key).setValue(hmp);

    }

    public void setMyRecyclerAdapter(HomeScreenRecyclerAdapter myRecyclerAdapter) {
        this.myRecyclerAdapter = myRecyclerAdapter;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
