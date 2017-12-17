/**
 * @FileName UploadImageActivity.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;



import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;

import static java.security.AccessController.getContext;


public class UploadImageActivity extends AppCompatActivity {
    Button chooseImg, uploadImg;
    ImageView imgView, cameraImg, chooseImgCamera;
    int PICK_IMAGE_REQUEST = 111;
    int CAMERA_IMAGE_REQUEST = 20;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1000;
    Uri filePath;
    ProgressDialog pd;
    Uri uri;

    private StorageReference mstorage;

    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://trashtotreasure-a797f.appspot.com/AdImages");    //change the url according to your firebase app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ad_post_camera);

        chooseImgCamera = (ImageView) findViewById(R.id.browseCameraOrGallery);
        chooseImg = (Button) findViewById(R.id.galleryselect);
        uploadImg = (Button) findViewById(R.id.finalpostbuton);
        imgView = (ImageView) findViewById(R.id.uploadedimage);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");


        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

            }
        });

        chooseImgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(UploadImageActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_IMAGE_REQUEST);
                }
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, CAMERA_IMAGE_REQUEST);
                //uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());


                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission - ", String.valueOf(shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)));
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Log.d("Requesting Permission", "Persmission");
                    }
                    ActivityCompat.requestPermissions(UploadImageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
//
//                Uri mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        new ContentValues());
//                Log.d("URI",mPhotoUri.toString());
//                photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri.toString());
//                startActivityForResult(photoCaptureIntent, CAMERA_IMAGE_REQUEST);


            }
        });


        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    pd.show();

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String imagename = timestamp.getTime() + ".jpg";
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://trashtotreasure-a797f.appspot.com/AdImages");
                    StorageReference childRef = storageRef.child(imagename);

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);


                    PostAdInformationCollector.getInstance().setPicture("AdImages/" + imagename);
//                    PostAdInformationCollector.getInstance().createHashMap();

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            UserProfile.getInstance().getMyAds();

                            FingerprintManager fingerprintManager = (FingerprintManager) getApplicationContext().getSystemService(Context.FINGERPRINT_SERVICE);
                            if (ActivityCompat.checkSelfPermission(UploadImageActivity.this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            if (!fingerprintManager.isHardwareDetected()) {
                                // Device doesn't support fingerprint authentication
                                PostAdInformationCollector.getInstance().createHashMap();
                                UserProfile.getInstance().refreshMyAds();
                                Toast.makeText(UploadImageActivity.this, "Ad Posted Successfully", Toast.LENGTH_SHORT).show();
                                Intent itemIntent = new Intent(UploadImageActivity.this, SuccessActivity.class);
                                startActivity(itemIntent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();

                            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                                PostAdInformationCollector.getInstance().createHashMap();
                                UserProfile.getInstance().refreshMyAds();
                                Toast.makeText(UploadImageActivity.this, "Ad Posted Successfully", Toast.LENGTH_SHORT).show();
                                // User hasn't enrolled any fingerprints to authenticate with
                                Intent itemIntent = new Intent(UploadImageActivity.this, SuccessActivity.class);
                                startActivity(itemIntent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();

                            } else {
                                // Everything is ready for fingerprint authentication
                                Intent itemIntent = new Intent(UploadImageActivity.this, FingerprintActivity.class);
                                startActivity(itemIntent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }


//                            Intent itemIntent = new Intent(UploadImageActivity.this, SuccessActivity.class);
//                            startActivity(itemIntent);
//                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(UploadImageActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(UploadImageActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Setting image to ImageView
            imgView.setImageBitmap(bitmap);
        }
        else if(requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Log.d("Inside Camera", "Printing" + CAMERA_IMAGE_REQUEST);

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(bitmap);
            filePath = data.getData();

            Log.d("Inside Camera", "FilePath before" + filePath);

            Uri tempUri = getImageUri(getApplicationContext(), bitmap);
            filePath = tempUri;
            Log.d("Inside Camera", "FilePath after" + filePath);


//            Bundle bundle = data.getExtras();
//            if (bundle != null) {
//                Log.d("Bundle ","details="+bundle2string(bundle));
//                String filepath = data.getStringExtra(MediaStore.EXTRA_OUTPUT);
//
//                Log.d("Inside Camera", "FilePath after" + filepath);
//            }
        }

    }


    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    }

