/**
 * @FileName AdDetailInfo.java
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

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class AdDetailInfo {
    HashMap<String,?> myAd;
    private static AdDetailInfo single_instance = null;

    public static AdDetailInfo getInstance()
    {
        if (single_instance == null)
            single_instance = new AdDetailInfo();
        return single_instance;
    }


    void setMyAd(HashMap<String,?> myad)
    {
        myAd = myad;
    }

    HashMap getMyAd()
    {
        return myAd;
    }
   }

