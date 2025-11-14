package com.example.prm392_mobile_carlinker.ui.garagestaff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffDto;

import java.util.ArrayList;
import java.util.List;

public class GarageStaffAdapter extends RecyclerView.Adapter<GarageStaffAdapter.ViewHolder> {

    private List<GarageStaffDto> staffList = new ArrayList<>();
    private OnStaffClickListener clickListener;
    private OnStaffEditListener editListener;
    private OnStaffDeleteListener deleteListener;

    public interface OnStaffClickListener {
        void onStaffClick(GarageStaffDto staff);
    }

    public interface OnStaffEditListener {
        void onStaffEdit(GarageStaffDto staff);
    }

    public interface OnStaffDeleteListener {
        void onStaffDelete(GarageStaffDto staff);
    }

    public GarageStaffAdapter(OnStaffClickListener clickListener,
                             OnStaffEditListener editListener,
                             OnStaffDeleteListener deleteListener) {
        this.clickListener = clickListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    public void setStaffList(List<GarageStaffDto> staffList) {
        this.staffList = staffList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_garage_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GarageStaffDto staff = staffList.get(position);
        holder.bind(staff);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivStaffImage;
        private TextView tvStaffName;
        private TextView tvStaffEmail;
        private TextView tvStaffPhone;
        private TextView tvStaffRole;
        private TextView tvStaffStatus;
        private TextView tvCreatedAt;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStaffImage = itemView.findViewById(R.id.ivStaffImage);
            tvStaffName = itemView.findViewById(R.id.tvStaffName);
            tvStaffEmail = itemView.findViewById(R.id.tvStaffEmail);
            tvStaffPhone = itemView.findViewById(R.id.tvStaffPhone);
            tvStaffRole = itemView.findViewById(R.id.tvStaffRole);
            tvStaffStatus = itemView.findViewById(R.id.tvStaffStatus);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(GarageStaffDto staff) {
            tvStaffName.setText(staff.getFullName());
            tvStaffEmail.setText(staff.getEmail());
            tvStaffPhone.setText(staff.getPhoneNumber() != null ? staff.getPhoneNumber() : "Chưa có SĐT");
            tvStaffRole.setText(staff.getGarageRoleString());
            tvStaffStatus.setText(staff.getUserStatusString());
            tvCreatedAt.setText("Tạo: " + staff.getFormattedCreatedAt());

            // Load image
            if (staff.getImage() != null && !staff.getImage().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(staff.getImage())
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .circleCrop()
                        .into(ivStaffImage);
            } else {
                ivStaffImage.setImageResource(R.drawable.ic_person);
            }

            // Set status color
            int statusColor;
            switch (staff.getUserStatus()) {
                case 0: // ACTIVE
                    statusColor = itemView.getContext().getColor(R.color.green);
                    break;
                case 1: // INACTIVE
                    statusColor = itemView.getContext().getColor(R.color.gray);
                    break;
                case 2: // SUSPENDED
                    statusColor = itemView.getContext().getColor(R.color.red);
                    break;
                default:
                    statusColor = itemView.getContext().getColor(R.color.gray);
            }
            tvStaffStatus.setTextColor(statusColor);

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onStaffClick(staff);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (editListener != null) {
                    editListener.onStaffEdit(staff);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onStaffDelete(staff);
                }
            });
        }
    }
}
