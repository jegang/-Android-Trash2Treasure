/**
 * @FileName HomeScreenRecyclerAdapter.java
 *
 * @author  Jegan Gopalakrishnan
 * @email   consultjegan@gmail.com
 * @version 1.0
 * @Date   12/9/2017
 */

package edu.syr.jgopalak.trashtotreasure;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class HomeScreenRecyclerAdapter extends RecyclerView.Adapter<HomeScreenRecyclerAdapter.MovieListViewHolder> {

    public interface OnItemClickListener{
        public void onItemClick(View v, int position);
        public  void onItemLongClick(View v, int position);
      //  public void onOverflowMenuClick(View v, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    private List<Map<String, ?>> myData;
    private Context myContext;
    OnItemClickListener myItemClickListener;
    public Map <String ,?> movie;
    StorageReference storageReference;


    public HomeScreenRecyclerAdapter(Context context, List<Map<String,?>> data)
    {
        myContext = context;
        myData = data;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public class MovieListViewHolder extends RecyclerView.ViewHolder{

        public ImageView iv_thumbnail;
        public TextView tv_title;
        public TextView tv_category;
        public ImageView iv_moreOptions;


        MovieListViewHolder(View view)
        {
            super(view);
            iv_thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
            tv_title = (TextView)view.findViewById(R.id.tv_adtitle);
            tv_category = (TextView)view.findViewById(R.id.category);
            iv_moreOptions = (ImageView) view.findViewById(R.id.overflow);
            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(myItemClickListener != null)
                    {
                        //myItemClickListener.onItemClick(v, getAdapterPosition());
                        Intent intent = new Intent(myContext, ContactSellerActivity.class);
                        movie = myData.get(getAdapterPosition());
                        AdDetailInfo.getInstance().setMyAd((HashMap)movie);
                        intent.putExtra("message", (String)movie.get("id"));
                        myContext.startActivity(intent);
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {
                    if(myItemClickListener != null)
                    {
                        myItemClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });

//            if(iv_moreOptions != null)
//            {
//                iv_moreOptions.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(myItemClickListener != null) {
//                            myItemClickListener.onOverflowMenuClick(v,getAdapterPosition());
//                        }
//                    }
//                });
//            }
        }

    }
    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;
        switch (viewType)
        {
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_cardview,parent,false);
                break;
        }


        MovieListViewHolder viewHolder= new MovieListViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int itemPosition)
    {
        if(itemPosition < 5)
        {
            return 1;
        }
        else if(itemPosition >= getItemCount()-5)
        {
            return 3;
        }
        else
        {
            return 2;
        }
    }
    @Override
    public void onBindViewHolder(MovieListViewHolder viewholder, int position) {

        movie = myData.get(position);
        String picture = movie.get("picture").toString();

        Glide.with(myContext)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(picture))
                .into(viewholder.iv_thumbnail);

        viewholder.tv_title.setText((String)movie.get("title"));
        viewholder.tv_category.setText((String) movie.get("category"));
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public void setOnClickListner( OnItemClickListener mItemClickListener){
        this.myItemClickListener = mItemClickListener;
    }
}
