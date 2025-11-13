package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.chat.RoomMember;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * UC-04: Adapter for displaying room members in RecyclerView
 */
public class RoomMemberAdapter extends RecyclerView.Adapter<RoomMemberAdapter.MemberViewHolder> {
    
    private Context context;
    private List<RoomMember> members;
    private OnMemberActionListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    
    public interface OnMemberActionListener {
        void onRemoveMember(RoomMember member, int position);
    }
    
    public RoomMemberAdapter(Context context, List<RoomMember> members, OnMemberActionListener listener) {
        this.context = context;
        this.members = members;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_member, parent, false);
        return new MemberViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        RoomMember member = members.get(position);
        
        // Set member name
        holder.tvMemberName.setText(member.getUserName());
        
        // Set member email
        holder.tvMemberEmail.setText(member.getUserEmail());
        
        // Set user type with icon
        String userTypeText;
        int userTypeIcon;
        if (member.getUserType() == 2) {
            userTypeText = "Admin";
            userTypeIcon = R.drawable.ic_admin;
        } else {
            userTypeText = "Staff";
            userTypeIcon = R.drawable.ic_staff;
        }
        holder.tvUserType.setText(userTypeText);
        holder.ivUserTypeIcon.setImageResource(userTypeIcon);
        
        // Set joined date
        try {
            // Parse ISO 8601 date string
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date joinedDate = isoFormat.parse(member.getJoinedAt());
            holder.tvJoinedDate.setText("Joined: " + dateFormat.format(joinedDate));
        } catch (Exception e) {
            // Fallback to showing the raw string if parsing fails
            holder.tvJoinedDate.setText("Joined: " + member.getJoinedAt());
        }
        
        // Set remove button listener
        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveMember(member, holder.getAdapterPosition());
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return members.size();
    }
    
    static class MemberViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserTypeIcon;
        TextView tvMemberName;
        TextView tvMemberEmail;
        TextView tvUserType;
        TextView tvJoinedDate;
        ImageButton btnRemove;
        
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserTypeIcon = itemView.findViewById(R.id.ivUserTypeIcon);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            tvMemberEmail = itemView.findViewById(R.id.tvMemberEmail);
            tvUserType = itemView.findViewById(R.id.tvUserType);
            tvJoinedDate = itemView.findViewById(R.id.tvJoinedDate);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
