package com.example.pjbookingsport.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.activity.LoginActivity;
import com.example.pjbookingsport.frag.HomeFragment;
import com.example.pjbookingsport.model.Price;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private String imgUrl;
    private List<SportFacility> mListFacility;
    private static HomeFragment context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SportFacility facility);
        void onBookClick(SportFacility facility);
    }

    public PhotoAdapter(HomeFragment context, List<SportFacility> mListPhoto, OnItemClickListener listener) {
        this.mListFacility = mListPhoto;
        this.context = context;
        this.imgUrl = context.getString(R.string.img_url);
        this.listener = listener;
    }

    public void updateData(List<SportFacility> newData) {
        this.mListFacility = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        SportFacility sportFacility = mListFacility.get(position);
        if (sportFacility == null) {
            return;
        }
        holder.bind(sportFacility, listener, imgUrl);
    }

    @Override
    public int getItemCount() {
        return mListFacility != null ? mListFacility.size() : 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        private TextView txtName;
        private TextView txtAddress;
        private Button btnBook;
        private LinearLayout containerType;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            txtName = itemView.findViewById(R.id.name);
            txtAddress = itemView.findViewById(R.id.address);
            btnBook = itemView.findViewById(R.id.book_button);
            containerType = itemView.findViewById(R.id.containerType);
        }

        public void bind(SportFacility facility, OnItemClickListener listener, String imgUrl) {
            txtName.setText(facility.getName());
            txtAddress.setText(facility.getAddress());

            Glide.with(itemView.getContext())
                    .load(imgUrl + facility.getSportsFacilityId())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imgPhoto);

            containerType.removeAllViews();

            // Lấy loại sân thể thao
            List<Price> prices = facility.getPrices();
            List<String> types= new ArrayList<>();
            if (prices != null && !prices.isEmpty()) {
                for(Price price : prices){
                    types.add(price.getFacilityType().getName());
                }
            }

            // Danh sách icon phù hợp với loại sân thể thao
            List<Integer> iconList = new ArrayList<>();

            for (String type: types) {
                if (type.equals("Cầu lông")) {
                    iconList.add(R.drawable.badminton);
                } else if (type.equals("Bóng đá")) {
                    iconList.add(R.drawable.football);
                } else if (type.equals("Tennis")) {
                    iconList.add(R.drawable.tennis);
                } else if (type.equals("Pickleball")) {
                    iconList.add(R.drawable.pickle);
                } else if (type.equals("Bóng rổ")) {
                    iconList.add(R.drawable.basketball);
                } else {
                    iconList.add(R.drawable.volleyball);
                }
            }

            // Thêm icon vào LinearLayout
            for (int iconRes : iconList) {
                ImageView iconView = new ImageView(itemView.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(58, 58);
                params.setMargins(10, 0, 0, 0);
                iconView.setLayoutParams(params);
                iconView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iconView.setImageResource(iconRes);
                containerType.addView(iconView);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(facility);
                }
            });

            btnBook.setOnClickListener(v -> {
                if(SharedPreferencesHelper.checkUserIsSave(itemView.getContext())) {
                    if (listener != null) {
                        listener.onBookClick(facility);
                    }
                }
                else {
                    showLogoutDialog();
                }
            });


        }

        private void showLogoutDialog() {
            Dialog dialog = new Dialog(context.requireContext(), R.style.CustomDialog);
            dialog.setContentView(R.layout.dialog_confirm_login);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0.5f);

            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout((int) (context.getResources().getDisplayMetrics().widthPixels * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
            }

            AppCompatButton btnHuy = dialog.findViewById(R.id.huyBtn);
            AppCompatButton btnLogout = dialog.findViewById(R.id.loginButton);

            btnHuy.setOnClickListener(v -> dialog.dismiss());

            btnLogout.setOnClickListener(v -> {
                SharedPreferencesHelper.clearAccount(context.requireContext());
                SharedPreferencesHelper.clearUser(context.requireContext());
                Intent intent = new Intent(context.getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                dialog.dismiss();
            });

            dialog.show();
        }


    }
}
