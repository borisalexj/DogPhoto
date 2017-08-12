package com.borisalexj.dogphoto.ui.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.borisalexj.dogphoto.R;
import com.borisalexj.dogphoto.models.DogModel;
import com.borisalexj.dogphoto.ui.DetailActivity;
import com.borisalexj.dogphoto.ui.MapsActivity;
import com.borisalexj.dogphoto.util.Constants;
import com.borisalexj.dogphoto.util.Utils;

import java.util.ArrayList;

/**
 * Created by user on 8/12/2017.
 */

public class DogsRecyclerViewAdapter extends RecyclerView.Adapter<DogsRecyclerViewAdapter.DogsViewHolder> {
    private String TAG = Constants.TAG + this.getClass().getSimpleName() + " ";

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
        Utils.setImageViewFromFile(holder.mDogPhoto, mDogsList.get(position).getPhoto());

    }

    @Override
    public int getItemCount() {
        return mDogsList.size();
    }

    public class DogsViewHolder extends RecyclerView.ViewHolder {
        ImageView mDogPhoto;
        Button mDetailButton;

        private String TAG = Constants.TAG + this.getClass().getSimpleName() + " ";

        public DogsViewHolder(View itemView) {
            super(itemView);
            mDogPhoto = (ImageView) itemView.findViewById(R.id.dog_item_photo);
            mDetailButton = (Button) itemView.findViewById(R.id.dog_item_details_button);

            mDogPhoto.setOnClickListener(new View.OnClickListener() {
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
            mDetailButton.setOnClickListener(new View.OnClickListener() {
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
