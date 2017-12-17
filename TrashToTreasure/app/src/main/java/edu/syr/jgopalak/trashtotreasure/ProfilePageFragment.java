/**
 * @FileName ProfilePageFragment.java
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfilePageFragment extends Fragment {

    private OnFragmentInteractionListenerEditProfile mListener;

    TextView tv_profile_name;
    TextView tv_profile_emailid;
    TextView tv_profile_address;
    TextView tv_profile_phone_number;
    TextView tv_profile_gps_location;
    ImageView iv_displayPic;
    Button btn_editprofile;


    public ProfilePageFragment() {
        // Required empty public constructor
    }

    public static ProfilePageFragment newInstance() {
        ProfilePageFragment fragment = new ProfilePageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
        tv_profile_emailid = (TextView) view.findViewById(R.id.tv_profile_emailid);
        tv_profile_address = (TextView) view.findViewById(R.id.tv_profile_address);
        tv_profile_phone_number = (TextView) view.findViewById(R.id.tv_profile_phone_number);
        tv_profile_gps_location = (TextView) view.findViewById(R.id.tv_profile_gps_location);
        iv_displayPic = (ImageView) view.findViewById(R.id.iv_dp_profile);
        btn_editprofile = (Button) view.findViewById(R.id.btn_editprofile);

        tv_profile_name.setText(UserProfile.getInstance().profileInfo.get("fullName").toString());
        tv_profile_emailid.setText(UserProfile.getInstance().profileInfo.get("emailId").toString());
        tv_profile_address.setText(UserProfile.getInstance().profileInfo.get("address").toString());
        tv_profile_phone_number.setText(UserProfile.getInstance().profileInfo.get("phoneNumber").toString());

        List<String> yourposts = (List)UserProfile.getInstance().profileInfo.get("yourposts");

        if (yourposts != null) {

            Log.d("TrashToTreasure", "Number of items posted - Size" + yourposts.size());
            tv_profile_gps_location.setText("Wow!! You have donated " + Integer.toString(yourposts.size()) + " items");

        } else
        {
            tv_profile_gps_location.setText("OOPS !! You haven't donated any items");
        }
        //tv_profile_gps_location.setText(UserProfile.getInstance().profileInfo.get("Address").toString());


        String picturePath = UserProfile.getInstance().profileInfo.get("picture").toString();
        Log.d("picturePath", picturePath);
        if(UserProfile.getInstance().isGoogleAccount) {
            Glide.with(getContext())
                    .load(picturePath)
                    .into(iv_displayPic);
        }
        else {
            Glide.with(getContext())
                    .using(new FirebaseImageLoader())
                    .load(FirebaseStorage.getInstance().getReference().child(picturePath))
                    .into(iv_displayPic);
        }

        btn_editprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                onButtonPressed();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteractionEditProfile();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerEditProfile) {
            mListener = (OnFragmentInteractionListenerEditProfile) context;
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
    public interface OnFragmentInteractionListenerEditProfile {
        // TODO: Update argument type and name
        void onFragmentInteractionEditProfile();
    }
}
