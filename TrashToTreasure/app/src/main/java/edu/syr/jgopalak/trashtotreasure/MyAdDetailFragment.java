/**
 * @FileName MyAdDetailFragment.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */


package edu.syr.jgopalak.trashtotreasure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;


public class MyAdDetailFragment extends Fragment {

    //private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_MOVIE_NUMBER = "movie_number";
    public static HashMap<String, ?> hmp;
    private static HashMap<String, ?> currentMovie;
    ImageView ivPoster;
    TextView tvTitle;
    TextView tvCategory;
    TextView tvOverView;
    TextView tvAddress;
    TextView tvcolor;
    TextView tvage;
    Button btn_contact;
    ViewGroup.LayoutParams initialParams;
    static LruCache<String, Bitmap> mImgMemoryCache;
    ShareActionProvider mshareActionProvider;

    public static MyAdDetailFragment newInstance(HashMap<String, ?> movie) {
        MyAdDetailFragment fragment = new MyAdDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE_NUMBER, movie);
        fragment.setArguments(args);

        return fragment;
    }

    public MyAdDetailFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // Adding action items
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
//        if(menu.findItem(R.id.icon_share) == null) {
//            inflater.inflate(R.menu.menu_share, menu);
//        }
//        MenuItem shareItem = menu.findItem(R.id.icon_share);
//        mshareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
//
//        Intent intentShare = new Intent(Intent.ACTION_SEND);
//        intentShare.setType("text/plain");
//
//        intentShare.putExtra(Intent.EXTRA_TEXT, (String) currentMovie.get(" title "));
//        mshareActionProvider.setShareIntent(intentShare);
//
//        super.onCreateOptionsMenu(menu,inflater);
    }

    //Handle menu clicks inside fragment
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id)
        {
            case R.id.icon_share:
                //Toast.makeText(getActivity(), "Clicked Search Button", Toast.LENGTH_LONG).show();

                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View mainView = null;
        mainView = inflater.inflate(R.layout.fragment_my_ad_detail,container,false);


        currentMovie = (HashMap<String, ?>) getArguments().getSerializable(ARG_MOVIE_NUMBER);

        //HashMap<String,?> currentMovie = hmp;//(HashMap<String, ?>) getArguments().getSerializable(ARG_MOVIE_NUMBER);
        ivPoster = (ImageView) mainView.findViewById(R.id.iv_poster);
//        initialParams= ivPoster.getLayoutParams();
//        initialParams.height = 500;
//        initialParams.width = 500;
//        ivPoster.setLayoutParams(initialParams);
        tvTitle = (TextView) mainView.findViewById(R.id.tv_title);
        tvCategory = (TextView)mainView.findViewById(R.id.tv_category);
        btn_contact = (Button)mainView.findViewById(R.id.btn_delete_the_ad);
        tvOverView = (TextView) mainView.findViewById(R.id.tv_description);
        tvAddress = (TextView) mainView.findViewById(R.id.tv_itemaddress);
        tvage = (TextView) mainView.findViewById(R.id.tv_itemage);
        tvcolor = (TextView) mainView.findViewById(R.id.tv_itemcolor);

        tvTitle.setText( currentMovie.get("title").toString());
        tvCategory.setText(currentMovie.get("category").toString());
        tvOverView.setText(currentMovie.get("description").toString());
        tvAddress.setText(currentMovie.get("location").toString());
        tvage.setText(currentMovie.get("Age").toString());
        tvcolor.setText(currentMovie.get("color").toString());

        String url = currentMovie.get("picture").toString();
        Glide.with(mainView.getContext())
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference().child(url))
                .into(ivPoster);

        final String idOfAd = currentMovie.get("id").toString();
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAdFromFirebase(idOfAd);
                UserProfile.getInstance().deleteAdFromUserAccount(idOfAd);
                Toast.makeText(getActivity(), "Thanks for donating your item",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), MyAdActivity.class);
                startActivity(intent); getActivity().finish();
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


            }
        });

        return mainView;
    }


    public void deleteAdFromFirebase(String adID)
    {
        FirebaseDatabase.getInstance().getReference().child("Posts").child(adID).removeValue();

    }
}
