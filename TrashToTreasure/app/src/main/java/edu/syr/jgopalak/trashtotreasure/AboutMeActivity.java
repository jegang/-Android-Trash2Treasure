/**
 * @FileName AboutMeActivity.java
 * @Functionality
 *          This file contains the info of the Developers of
 *          the application which is populated in one of activity
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   2017-November-10
 */


package edu.syr.jgopalak.trashtotreasure;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AboutMeActivity extends AppCompatActivity {

    Button btnDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        btnDownload = (Button) findViewById(R.id.uploadButton);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeExternalStorage(v);
            }
        });

    }


    public void writeExternalStorage(View view)
    {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath()+"/Trash2Treasure");
            if(!Dir.exists())
            {
                Dir.mkdir();

            }
            File file =  new File(Dir,"DeveloperInfo.txt");
            String output = "Developer 1:\n" +
                    "===================================\n" +
                    "Name : Jegan Gopalakrishnan\n" +
                    "Email ID : jgopalak@syr.edu\n" +
                    "Contact Number : +1-(315)-949-8571\n" +
                    "LinkedIn Prfoile Link: https://www.linkedin.com/in/jegan-gopalakrishnan/\n" +
                    "\n" +
                    "\n" +
                    "Developer 2:\n" +
                    "===================================\n" +
                    "Name : Sathesh Balakrishnan Manohar\n" +
                    "Email ID : sabalakr@syr.edu\n" +
                    "Contact Number : +1-(315)-949-8570\n" +
                    "LinkedIn Prfoile Link: https://www.linkedin.com/in/satheshbalakrishnanmanohar/";
            try
            {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(output.getBytes());
                fileOutputStream.close();
                Toast.makeText(getApplicationContext(),"File name DeveloperInfo.txt is stored in ExternalSD/Trash2Treasure",Toast.LENGTH_LONG).show();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Sd card not found",Toast.LENGTH_LONG).show();

        }
    }
}
