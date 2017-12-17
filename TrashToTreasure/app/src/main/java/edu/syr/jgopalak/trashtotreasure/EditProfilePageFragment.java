/**
 * @FileName EditProfilePageFragment.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EditProfilePageFragment extends Fragment {

    private OnFragmentInteractionListenerProfile mListener;

    EditText tv_profile_name;
    TextView tv_profile_emailid;
    EditText tv_profile_address;
    EditText tv_profile_phone_number;
    TextView tv_profile_gps_location;
    ImageView iv_displayPic;
    Button btn_editprofile, changePicture;
    ImageButton btn_mailMePassword;


    public EditProfilePageFragment() {
        // Required empty public constructor
    }

    public static EditProfilePageFragment newInstance() {
        EditProfilePageFragment fragment = new EditProfilePageFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile , container , false);
        final View currentview = view;

        mListener = (OnFragmentInteractionListenerProfile) view.getContext();

        tv_profile_name = (EditText) view.findViewById(R.id.et_editprofile_name);
        tv_profile_address = (EditText) view.findViewById(R.id.et_editprofile_address);
        tv_profile_phone_number = (EditText) view.findViewById(R.id.et_editprofile_tvphonenumber);
        changePicture = (Button) view.findViewById(R.id.changePicture);
        btn_editprofile = (Button) view.findViewById(R.id.btn_editprofile_save);
        btn_mailMePassword = (ImageButton) view.findViewById(R.id.mailMePassword);




        btn_mailMePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UserProfile.getInstance().isGoogleAccount) {

                    Toast.makeText(getContext(), "Looks like you have logged in via gmail account. Change your google password", Toast.LENGTH_SHORT).show();

                } else {


                    FirebaseAuth.getInstance().sendPasswordResetEmail(UserProfile.getInstance().profileInfo.get("emailId").toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "We have sent you instructions to change your password!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (Exception e) {
                                            Log.e("T2T", e.getMessage());
                                        }
                                        Toast.makeText(getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                }

        });


        btn_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveProfileInfo();
                mListener.onSaveButtonClick();
            }
        });

        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.imgUploadClickedFromFragment(currentview);
            }
        });

        return view;
    }

    public void saveProfileInfo()
    {

        String profile_name = tv_profile_name.getText().toString();
        String profile_address = tv_profile_address.getText().toString();
        String phone_number = tv_profile_phone_number.getText().toString();

        if(TextUtils.isEmpty(profile_name)){
            Toast.makeText(getActivity(), "Please enter your name !!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            UserProfile.getInstance().profileInfo.put("fullName",tv_profile_name.getText());
        }

        if(TextUtils.isEmpty(profile_address)){
            Toast.makeText(getActivity(), "Please enter your address !!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            UserProfile.getInstance().profileInfo.put("address",tv_profile_address.getText());
        }

        if(TextUtils.isEmpty(phone_number)){
            Toast.makeText(getActivity(), "Please enter your mobile number !!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            UserProfile.getInstance().profileInfo.put("phoneNumber",tv_profile_phone_number.getText());
        }

        UserProfile.getInstance().updateProfile(tv_profile_name.getText().toString(), tv_profile_address.getText().toString(), tv_profile_phone_number.getText().toString());

        mListener.onSaveButtonClick();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
           // mListener.onFragmentInteractionProfile();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerProfile) {
            mListener = (OnFragmentInteractionListenerProfile) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListenerProfile {
        // TODO: Update argument type and name
        void onSaveButtonClick();
        void imgUploadClickedFromFragment(View v);
    }
}
