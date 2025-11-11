package com.example.mhike.ui.observation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.R;
import com.example.mhike.data.model.Observation;
import com.example.mhike.data.model.ObservationWithHike;

import java.util.ArrayList;
import java.util.List;

public class GroupedObservationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<Object> items = new ArrayList<>();
    private OnObservationClickListener listener;

    public interface OnObservationClickListener {
        void onEditClick(Observation observation);
        void onDeleteClick(Observation observation);
    }

    public GroupedObservationAdapter(OnObservationClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_observation_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_observation, parent, false);
            return new ObservationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((String) items.get(position));
        } else if (holder instanceof ObservationViewHolder) {
            ((ObservationViewHolder) holder).bind((Observation) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<ObservationWithHike> data) {
        items.clear();
        for (ObservationWithHike item : data) {
            // Add header with hike name
            items.add(item.getHike().getName());
            // Add all observations for this hike
            items.addAll(item.getObservations());
        }
        notifyDataSetChanged();
    }

    // Header ViewHolder
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHikeName;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHikeName = itemView.findViewById(R.id.tvHikeName);
        }

        public void bind(String hikeName) {
            tvHikeName.setText(hikeName);
        }
    }

    // Observation ViewHolder
    class ObservationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime, tvObservation, tvComment, tvBottomInfo;
        private ImageButton btnEdit, btnDelete;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvObservation = itemView.findViewById(R.id.tvObservation);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvBottomInfo = itemView.findViewById(R.id.tvBottomInfo);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Observation observation) {
            tvTime.setText(observation.getTime());
            tvObservation.setText(observation.getObservation());

            // Show comment if exists
            if (observation.getComment() != null && !observation.getComment().isEmpty()) {
                tvComment.setText(observation.getComment());
                tvComment.setVisibility(View.VISIBLE);
            } else {
                tvComment.setVisibility(View.GONE);
            }

            tvBottomInfo.setVisibility(View.GONE);

            // Click listeners
            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(observation);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(observation);
                }
            });
        }
    }
}