package com.mateoj.hack2help.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.mateoj.hack2help.R;
import com.mateoj.hack2help.TourDetailActivity;
import com.mateoj.hack2help.data.model.Tour;
import com.mateoj.hack2help.util.LocationUtils;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by jose on 10/23/15.
 */
public class TourAdapter extends RecyclerView.Adapter {
    private static final int VIEWTYPE_HEADER = 345;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Tour> mTours;
    private LatLng latLng;

    public TourAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setTours(List<Tour> tours)
    {
        if (mTours == null)
            mTours = new ArrayList<>();
        mTours.clear();
        mTours.addAll(tours);
        notifyDataSetChanged();
    }

    public String buildMapUlr()
    {
        if (latLng == null)
            return "https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318 &markers=color:red%7Clabel:C%7C40.718217,-73.998284&key=" + mContext.getString(R.string.mapsApiKey);
        else
            return "https://maps.googleapis.com/maps/api/staticmap?center=" + latLng.latitude + "," + latLng.longitude + "&zoom=13&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + latLng.latitude + "," + latLng.longitude + "&markers=color:green%7Clabel:G%7C40.711614,-74.012318 &markers=color:red%7Clabel:C%7C40.718217,-73.998284&key=" + mContext.getString(R.string.mapsApiKey);

    }

    public void setLatLng(LatLng latLng)
    {
        this.latLng = latLng;
        notifyItemChanged(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType != VIEWTYPE_HEADER) {
            View view = mInflater.inflate(R.layout.row_tour, parent, false);
            final TourViewHolder viewHolder = new TourViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().postSticky(getItem(viewHolder.getAdapterPosition() - 1));
                    Intent intent = new Intent(mContext, TourDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
            return viewHolder;
        }else {
            View view = mInflater.inflate(R.layout.main_list_header, parent, false);
            return new HeaderViewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            HeaderViewholder viewholder = (HeaderViewholder) holder;
            Picasso.with(mContext)
                    .load(buildMapUlr())
                    .noFade()
                    .into(viewholder.mapView);
            return;
        }


        Tour tour = getItem(position - 1);
        if (tour == null)
            return;
        TourViewHolder viewHolder = (TourViewHolder) holder;
        viewHolder.title.setText(tour.getTitle());
        viewHolder.description.setText(tour.getDescription());
//        viewHolder.distance.setText(
//                String.format(mContext.getString(R.string.distance),
//                        NumberFormat.getInstance().format(tour.getDistance())));
        viewHolder.distance.setText(String.format(mContext.getString(R.string.distance),
                NumberFormat.getInstance().format(tour.getLocation().distanceInMilesTo(LocationUtils.latLngToParseGeo(latLng)))));
        viewHolder.duration.setText(mContext.getString(R.string.duration,
                NumberFormat.getInstance().format(tour.getDuration())));
        if (!tour.getThumbUrl().equals(""))
            Picasso.with(mContext)
                    .load(tour.getThumbUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(viewHolder.thumb);
        else
            Picasso.with(mContext)
                .load(R.drawable.placeholder)
                .into(viewHolder.thumb);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEWTYPE_HEADER;

        return super.getItemViewType(position);
    }


    public Tour getItem(int pos)
    {
        return (pos < getItemCount()) ? mTours.get(pos)  : null;
    }

    @Override
    public int getItemCount() {
        return (mTours == null) ? 1 : mTours.size() + 1;
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        ImageView thumb;

        @Bind(R.id.title)
        TextView title;

        @Bind(R.id.description)
        TextView description;

        @Bind(R.id.duration)
        TextView duration;

        @Bind(R.id.distance)
        TextView distance;

        public TourViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class HeaderViewholder extends RecyclerView.ViewHolder {
        @Bind(R.id.mapView)
        ImageView mapView;

        public HeaderViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
