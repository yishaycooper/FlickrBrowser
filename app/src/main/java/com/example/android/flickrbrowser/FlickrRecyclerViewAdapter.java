package com.example.android.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

// the Adapter ust packages viewholders and passes them to the recyclerview
class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    // MainActivity is going to provide a list of photos
    private List<Photo> mPhotoList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(Context context, List<Photo> photoList) {
        mContext = context;
        mPhotoList = photoList;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        called by the layout manager when it needs a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent,false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        if ((mPhotoList == null) || (mPhotoList.size() == 0)) {
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(R.string.empty_photo);
        } else {
            Photo photoItem = mPhotoList.get(position);// retrieve current photo item using using recylers position
            Picasso.with(mContext).load(photoItem.getImage())// Picasso is a singalton so we use static whith method that makes sure only one object is created
                    // load then loads a image from a URL and is stored in the image field of the photo class
                    .error(R.drawable.placeholder)// set error icon
                    .placeholder(R.drawable.placeholder)// set placeholder icon
                    .into(holder.thumbnail);// put the downloaded image into the imageview widget in the view holder
            holder.title.setText(photoItem.getTitle());// just put title into textview
            // the recyclerview can then display the holder because it still has a reference to the view holder
        }

    }

    @Override
    public int getItemCount() {
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.size() : 1);// return 1 to display message for no photos match
    }

    void loadNewData(List<Photo> newPhotos) {
        mPhotoList =newPhotos;
//        Notify any registered observers that the data set has changed in this case the recyclerview
//        this is the line that loads and refreshes the data in the recyclerview
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.get(position) : null);
    }

    // static just means top level class
// class must be declared package private because its passed back and forth to RecyclerView within a separate package
    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
    ImageView thumbnail = null;
    TextView title = null;

    // no need for pasing fields because only itenVie is passed in from the RecyclerView// constructor must be declared package private because its passed back and forth to the
    public FlickrImageViewHolder(View itemVie) {
        super(itemVie);
        this.thumbnail = (ImageView) itemVie.findViewById(R.id.thumbnail);
        this.title = (TextView) itemVie.findViewById(R.id.title);
    }
}
}
