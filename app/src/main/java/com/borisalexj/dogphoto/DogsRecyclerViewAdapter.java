package com.borisalexj.dogphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user on 8/12/2017.
 */

class DogsRecyclerViewAdapter extends RecyclerView.Adapter<DogsRecyclerViewAdapter.DogsViewHolder> {
    ArrayList<DogModel> mDogsList;
    MapsActivity mMapsActivity;

    public DogsRecyclerViewAdapter(MapsActivity mapsActivity, ArrayList<DogModel> dogsList) {
        mDogsList = dogsList;
        mMapsActivity = mapsActivity;
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
        Button detailButton;

        private String TAG = Constants.TAG + this.getClass().getSimpleName() + " ";

        public DogsViewHolder(View itemView) {
            super(itemView);
//            Log.d(TAG, "TweetViewHolder: ");
            dogPhoto = (ImageView) itemView.findViewById(R.id.dog_item_photo);
            detailButton = (Button) itemView.findViewById(R.id.dog_item_details_button);

            dogPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mMapsActivity.addMarkerToMap(
                                Double.parseDouble(mDogsList.get(getAdapterPosition()).getLat()),
                                Double.parseDouble(mDogsList.get(getAdapterPosition()).getLng())
                        );
                    } catch (Exception e) {

                    }
                }
            });
            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mMapsActivity, DetailActivity.class);
                    intent.putExtra("dog_id", mDogsList.get(getAdapterPosition()).get_id());
                    mMapsActivity.startActivity(intent);
                }
            });
        }
    }
}
