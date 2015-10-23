package com.mateoj.hack2help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mateoj.hack2help.R;
import com.mateoj.hack2help.data.model.Tour;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jose on 10/23/15.
 */
public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Tour> mTours;

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

    @Override
    public TourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_tour, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TourViewHolder holder, int position) {
        Tour tour = getItem(position);
        if (tour == null)
            return;

        holder.title.setText(tour.getTitle());
        holder.description.setText(tour.getDescription());
        holder.distance.setText(
                String.format(mContext.getString(R.string.distance),
                        NumberFormat.getInstance().format(tour.getDistance())));
        holder.duration.setText(mContext.getString(R.string.duration,
                NumberFormat.getInstance().format(tour.getDuration())));

        Picasso.with(mContext)
                .load(tour.getThumbUrl())
                .into(holder.thumb);
    }

    public Tour getItem(int pos)
    {
        return (pos < getItemCount()) ? mTours.get(pos) : null;
    }

    @Override
    public int getItemCount() {
        return (mTours == null) ? 0 : mTours.size();
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

        public TourViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
