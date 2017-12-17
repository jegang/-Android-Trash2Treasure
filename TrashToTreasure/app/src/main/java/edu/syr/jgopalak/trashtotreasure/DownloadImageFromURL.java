/**
 * @FileName DownloadImageFromURL.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */



package edu.syr.jgopalak.trashtotreasure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.utilities.Base64;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;

public class DownloadImageFromURL extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_page);


        String filename = "myfile";
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_WORLD_READABLE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("AdImages/ad_1.jpg");
//
//        final long ONE_MEGABYTE = 1024 * 1024;
//        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//
//
//
//                // Data for "images/island.jpg" is returns, use this as needed
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
//


//        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("AdImages/ad_1.jpg");
//
//        try {
//            File localFile = File.createTempFile("images", "jpg");
//
//            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    // Local temp file has been created
//
//
//
//
//                    Toast.makeText(DownloadImageFromURL.this, "Downloading Image Successfull", Toast.LENGTH_LONG).show();
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//                    Toast.makeText(DownloadImageFromURL.this, exception.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//
//        }
//        catch (Exception e)
//        {
//            Log.d("TrashToTreasure",e.getMessage());
//        }
//



    }
}
