/**
 * @FileName PostDataFragment.java
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

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;


public class PostDataFragment extends Fragment {

    //private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_MOVIE_NUMBER = "movie_number";
    public static HashMap<String, ?> hmp;
    private static HashMap<String, ?> postInfo;
    private OnFragmentInteractionListener mListener;

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

    public static PostDataFragment newInstance(HashMap<String, ?> movie) {
        PostDataFragment fragment = new PostDataFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE_NUMBER, movie);
        fragment.setArguments(args);

        return fragment;
    }

    public PostDataFragment(){

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
        if(menu.findItem(R.id.icon_share) == null) {
            inflater.inflate(R.menu.menu_share, menu);
        }
        MenuItem shareItem = menu.findItem(R.id.icon_share);
        mshareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");

        String itemname = (String) postInfo.get("title");
        String location = (String) postInfo.get("location");

        intentShare.putExtra(Intent.EXTRA_TEXT, "Trash2Treasure : A free " + itemname +" is available for pick up at " + location + ". For more information download Trash2Treasure from playstore https://play.google.com/store/apps/details?id=edu.syr.jgopalak.trashtotreasure&hl=en");
        mshareActionProvider.setShareIntent(intentShare);

        super.onCreateOptionsMenu(menu,inflater);
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
        mainView = inflater.inflate(R.layout.fragment_post_detail,container,false);


        postInfo = (HashMap<String, ?>) getArguments().getSerializable(ARG_MOVIE_NUMBER);


        ivPoster = (ImageView) mainView.findViewById(R.id.iv_poster);
        tvTitle = (TextView) mainView.findViewById(R.id.tv_title);
        tvCategory = (TextView)mainView.findViewById(R.id.tv_category);
        btn_contact = (Button)mainView.findViewById(R.id.btn_contact);
        tvOverView = (TextView) mainView.findViewById(R.id.tv_description);
        tvAddress = (TextView) mainView.findViewById(R.id.tv_itemaddress);
        tvage = (TextView) mainView.findViewById(R.id.tv_itemage);
        tvcolor = (TextView) mainView.findViewById(R.id.tv_itemcolor);
        mListener = (OnFragmentInteractionListener) mainView.getContext();

        btn_contact = (Button) mainView.findViewById(R.id.btn_contact);

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickFragmentInteraction(postInfo.get("id").toString());
            }
        });

        tvTitle.setText( postInfo.get("title").toString());
        tvCategory.setText(postInfo.get("category").toString());
        tvOverView.setText(postInfo.get("description").toString());
        tvAddress.setText(postInfo.get("location").toString());
        tvage.setText(postInfo.get("Age").toString());
        tvcolor.setText(postInfo.get("color").toString());

        String url = postInfo.get("picture").toString();
        Glide.with(mainView.getContext())
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference().child(url))
                .into(ivPoster);

        return mainView;
    }

    public interface OnFragmentInteractionListener {
        void onClickFragmentInteraction(String id);
    }
}
