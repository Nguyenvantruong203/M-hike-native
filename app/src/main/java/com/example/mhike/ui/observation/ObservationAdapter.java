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

import java.util.ArrayList;
import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {

    private List<Observation> observations = new ArrayList<>();
    private OnObservationClickListener listener;

    public interface OnObservationClickListener {
        void onEditClick(Observation observation);
        void onDeleteClick(Observation observation);
    }

    public ObservationAdapter(OnObservationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation observation = observations.get(position);
        holder.bind(observation);
    }

    @Override
    public int getItemCount() {
        return observations.size();
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
        notifyDataSetChanged();
    }

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

            // Bottom info could show observation summary or type
            tvBottomInfo.setText("Observation #" + observation.getId());

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