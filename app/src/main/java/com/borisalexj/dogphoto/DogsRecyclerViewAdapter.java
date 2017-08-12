package com.borisalexj.dogphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user on 8/12/2017.
 */

class DogsRecyclerViewAdapter extends RecyclerView.Adapter<DogsRecyclerViewAdapter.DogsViewHolder> {
    ArrayList<DogModel> mDogsList;

    public DogsRecyclerViewAdapter(MapsActivity mapsActivity, ArrayList<DogModel> dogsList) {
        mDogsList = dogsList;
    }

    @Override
    public DogsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_item, parent, false);
        DogsViewHolder viewHolder = new DogsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DogsViewHolder holder, int position) {
        String filename = mDogsList.get(position).getPhoto();
        if (!TextUtils.isEmpty(filename)) {
            File imgFile = new File(filename);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                holder.dogPhoto.setImageBitmap(myBitmap);

            }
        }

    }

    @Override
    public int getItemCount() {
        return mDogsList.size();
    }

    public class DogsViewHolder extends RecyclerView.ViewHolder {
        ImageView dogPhoto;
        TextView tweetAuthor;
        TextView tweetDateTime;
        TextView tweetText;
        private String TAG = Constants.TAG + this.getClass().getSimpleName() + " ";

        public DogsViewHolder(View itemView) {
            super(itemView);
//            Log.d(TAG, "TweetViewHolder: ");
            dogPhoto = (ImageView) itemView.findViewById(R.id.dog_item_photo);
        }
    }
}
