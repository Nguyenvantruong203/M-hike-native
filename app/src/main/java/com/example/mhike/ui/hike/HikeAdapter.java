package com.example.mhike.ui.hike;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.R;
import com.example.mhike.data.model.Hike;

import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private List<Hike> hikes = new ArrayList<>();
    private final OnHikeClickListener listener;
    private final boolean isHorizontal;

    public interface OnHikeClickListener {
        void onHikeClick(Hike hike);
        void onEditClick(Hike hike);
        void onDeleteClick(Hike hike);
    }

    public HikeAdapter(OnHikeClickListener listener, boolean isHorizontal) {
        this.listener = listener;
        this.isHorizontal = isHorizontal;
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isHorizontal ? R.layout.item_hike_horizontal : R.layout.item_hike_vertical;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        holder.bind(hikes.get(position));
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }

    public void setHikes(List<Hike> hikes) {
        this.hikes = hikes;
        notifyDataSetChanged();
    }

    class HikeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivHikeImage;
        private final TextView tvHikeName, tvHikeLocation, tvHikeType;
        private ImageButton btnEdit, btnDelete;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHikeImage = itemView.findViewById(R.id.ivHikeImage);
            tvHikeName = itemView.findViewById(R.id.tvHikeName);
            tvHikeLocation = itemView.findViewById(R.id.tvHikeLocation);

            // Chỉ layout dọc mới có thêm Type + các nút
            if (!isHorizontal) {
                tvHikeType = itemView.findViewById(R.id.tvHikeType);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            } else {
                tvHikeType = null;
            }
        }

        public void bind(Hike hike) {
            tvHikeName.setText(hike.getName());
            tvHikeLocation.setText(hike.getLocation());

            if (tvHikeType != null) {
                tvHikeType.setText(hike.getType());
            }

            // Load ảnh
            if (hike.getImageUri() != null && !hike.getImageUri().isEmpty()) {
                try {
                    ivHikeImage.setImageURI(Uri.parse(hike.getImageUri()));
                } catch (Exception e) {
                    ivHikeImage.setImageResource(R.drawable.image_hike_1);
                }
            } else {
                ivHikeImage.setImageResource(R.drawable.image_hike_1);
            }

            // Click vào toàn bộ item để xem chi tiết
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onHikeClick(hike);
            });

            // Click riêng từng nút
            if (btnEdit != null) {
                btnEdit.setOnClickListener(v -> {
                    if (listener != null) listener.onEditClick(hike);
                });
            }

            if (btnDelete != null) {
                btnDelete.setOnClickListener(v -> {
                    if (listener != null) listener.onDeleteClick(hike);
                });
            }
        }
    }
}
