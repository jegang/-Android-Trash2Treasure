/**
 * @FileName InterestedFragment.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;


public class InterestedFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static String mailId;
    private static String phonenumber;

    public interface OnFragmentInteractionListener {
        void onCallFragmentInteraction(View v,String phoneNumber);
        void onEmailFragmentInteraction(View v, String emailID);
    }

    public InterestedFragment() {
    }

    public static InterestedFragment newInstance(String emailId, String phoneNumber) {
        InterestedFragment fragment = new InterestedFragment();
        Bundle args = new Bundle();
        mailId = emailId;
        phonenumber = phoneNumber;
        args.putString("email", emailId);
        args.putString("phone", phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_seller,container,false);

        UserProfile.getInstance().interestedDataUpdate(rootView, mailId);

        final TextView callText = (TextView)rootView.findViewById(R.id.phoneNumberID);
        final TextView emailText = (TextView) rootView.findViewById(R.id.emailIDID);
        Button call = (Button)rootView.findViewById(R.id.callButton);
        Button email = (Button) rootView.findViewById(R.id.emailButton);
        ImageView iv_displayPic = (ImageView) rootView.findViewById(R.id.profile_image);


//        String id = getArguments().getString("email").toString();
//        String phone = getArguments().getString("phone").toString();

        callText.setText(phonenumber);
        emailText.setText(mailId);


//        callText.setText(UserProfile.getInstance().profileInfo.get("phoneNumber").toString());
//        emailText.setText(UserProfile.getInstance().profileInfo.get("emailId").toString());
        mListener = (OnFragmentInteractionListener) rootView.getContext();


//        String picturePath = UserProfile.getInstance().profileInfo.get("picture").toString();
//        Log.d("picturePath", picturePath);
//        if(UserProfile.getInstance().isGoogleAccount) {
//            Glide.with(getContext())
//                    .load(picturePath)
//                    .into(iv_displayPic);
//        }
//        else {
//            Glide.with(getContext())
//                    .using(new FirebaseImageLoader())
//                    .load(FirebaseStorage.getInstance().getReference().child(picturePath))
//                    .into(iv_displayPic);
//        }




        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCallFragmentInteraction(v, (String) callText.getText());
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEmailFragmentInteraction(v, (String) emailText.getText());
            }
        });

        return rootView;
    }



}