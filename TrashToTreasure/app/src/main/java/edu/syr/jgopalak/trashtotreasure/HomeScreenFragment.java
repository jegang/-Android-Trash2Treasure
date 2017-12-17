/**
 * @FileName HomeScreenFragment.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;


public class HomeScreenFragment extends Fragment {

    RecyclerView myRecyclerView;
    LinearLayoutManager myLayoutManager;
    HomeScreenRecyclerAdapter myAdapter;
    //MovieInfo mData = new MovieInfo();
    static int taskId;
    TextView textViewToolbar;


    PostInfo postData;

//    public void sortByReleaseYear() {
//        mData.sortByYear(mData.getMoviesList());
//        myAdapter.notifyDataSetChanged();
//    }

    public interface RecycleViewListener {
        public void onRecycleViewItemClicked(View v, HashMap<String, ?> movie);
    }
    private RecycleViewListener customOnClickRvListener;

    private static final String ARG_SECTION_NUMBER = "sectionNumber";

    public static  HomeScreenFragment newInstance(){
        HomeScreenFragment fragment = new HomeScreenFragment();
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        postData = new PostInfo();
    }


    public HomeScreenFragment(){
    }

    public void itemClicked(View view,int position) {
        final View v = view;
        if (position < 0) {
            Log.d("MY DEBUG", "Activity -onItemClicked -No Data");
            return;
        }
        final HashMap<String, ?> movie = (HashMap<String, ?>) postData.getItem(position);
        //customOnClickRvListener.onRecycleViewItemClicked(v, movie);
        String id = String.valueOf(movie.get("id"));
        FirebaseDatabase.getInstance().getReference().child("Posts").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> post = (HashMap<String, String>) dataSnapshot.getValue();
                customOnClickRvListener.onRecycleViewItemClicked(v, post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FBTest: ", "Cannot read movie detail " + databaseError.getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        final View rootView = inflater.inflate(R.layout.content_main,container,false);

        customOnClickRvListener = (RecycleViewListener) rootView.getContext();

        myRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
//        myRecyclerView.setHasFixedSize(true);
        //Implement myRecyclerView

       // myLayoutManager = new LinearLayoutManager(rootView.getContext());
        //   Implement myLayoutManager

        //myRecyclerView.setLayoutManager(myLayoutManager);

         RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(rootView.getContext(), 2);
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        myAdapter = new HomeScreenRecyclerAdapter(getContext(), postData.getAdsList());
        myRecyclerView.setAdapter(myAdapter);
        if(postData.getSize() == 0)
        {
            postData.setMyRecyclerAdapter(myAdapter);
            postData.setContext(getActivity());
            postData.initializeDataFromCloud();
        }

        myAdapter.setOnClickListner(new HomeScreenRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view , int position)
            {
                Toast.makeText(getContext(), "Move "+ (position+1)+" Selected",Toast.LENGTH_SHORT).show();
                itemClicked(view, position);
            }
            @Override
            public void onItemLongClick(View view , int position)
            {
                // getActivity().startActionMode(new ActionBarCallBack(position));
            }
        });

                itemAnimation();
                adapterAnimator();
                return rootView;
                }



    private  void defaultAnim(){
        DefaultItemAnimator anim = new DefaultItemAnimator();
        anim.setAddDuration(5000);
        anim.setRemoveDuration(5000);
        myRecyclerView.setItemAnimator(anim);
    }


    private void itemAnimation(){
        ScaleInAnimator anim = new ScaleInAnimator();
        anim.setAddDuration(500);
        anim.setRemoveDuration(500);
        myRecyclerView.setItemAnimator(anim);
    }

    private void adapterAnimator(){
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(myAdapter);
        ScaleInAnimationAdapter scale = new ScaleInAnimationAdapter(alphaAdapter);
        myRecyclerView.setAdapter(scale);
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
