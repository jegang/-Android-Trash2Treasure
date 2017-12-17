/**
 * @FileName MyAdsFragment.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */


package edu.syr.jgopalak.trashtotreasure;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static java.nio.file.Paths.get;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyAdsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyAdsFragment extends android.support.v4.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private List<Map<String,?>> myAdlist;
    private List<String> myAd;
    private FragmentActivity myContext;
    MyFragmentPagerAdapter myPagerAdapter;
    ViewPager mViewPager;
    private PostAdInfoFragment.OnFragmentPostAdInfoInteractionListener mListener;

    public MyAdsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostAdInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAdsFragment newInstance() {
        MyAdsFragment fragment = new MyAdsFragment();

//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_my_ads, container, false);
        myAdlist = UserProfile.getInstance().getAdsList();

        if(myAdlist == null)
        {
            return view;
        }

        myPagerAdapter = new MyFragmentPagerAdapter(myContext.getSupportFragmentManager(), myAdlist.size());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(myPagerAdapter);
        // mViewPager.setCurrentItem(3);
        //customizeViewPager();
        mViewPager.setPageTransformer(false, new FlipPageViewTransformer());
//        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View page, float postition){
//                page.setRotationY(postition * -40);
//            }
//        });



        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabSwipe);
        tabLayout.setupWithViewPager(mViewPager);


        return view;
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        int count;

        public MyFragmentPagerAdapter(FragmentManager fm, int size) {
            super(fm);
            count = size;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Log.d("getItem", Integer.toString(position));

            return MyAdDetailFragment.newInstance((HashMap<String, String>) UserProfile.getInstance().getItem(position));
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.d("getPageTitle", Integer.toString(position));
            Locale l = Locale.getDefault();
            HashMap<String, ?> movie = (HashMap<String,String>) UserProfile.getInstance().getItem(position);
            if(movie != null) {
                String name = ((HashMap<String, String>) UserProfile.getInstance().getItem(position)).get("title");
                return name.toUpperCase(l);
            }
            return null;
        }
    }

    @Override
    public void onAttach(Context context)
    {
        myContext = (FragmentActivity)context;
        super.onAttach(context);
    }

    @Override public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

}