package com.example.pjbookingsport.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Price;
import com.example.pjbookingsport.model.Review;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportFacilityAdapter extends RecyclerView.Adapter<SportFacilityAdapter.ViewHolder> {
    private Context context;
    private List<SportFacility> sportFacilityList;
    private String imgUrl;
    private OnItemClickListener listener;

    public SportFacilityAdapter(Context context, List<SportFacility> sportFacilityList, String imgUrl, OnItemClickListener listener) {
        this.context = context;
        this.sportFacilityList = sportFacilityList;
        this.imgUrl = imgUrl;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_facility, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SportFacility sportFacility = sportFacilityList.get(position);
        holder.title.setText(sportFacility.getName());
        holder.address.setText(sportFacility.getAddress());

        Glide.with(context)
                .load(imgUrl + sportFacility.getSportsFacilityId())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.image);

        holder.rating_value.setText(sportFacility.getAverageRating() + "/5");
        holder.rating_sum.setText("(" + sportFacility.getReviewCount() + ")");

        holder.iconContainer.removeAllViews();

        // Lấy loại sân thể thao
        List<Price> prices = sportFacility.getPrices();
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
            ImageView iconView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(68, 68);
            params.setMargins(0, 0, 10, 0);
            iconView.setLayoutParams(params);
            iconView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iconView.setImageResource(iconRes);
            holder.iconContainer.addView(iconView);
        }


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(sportFacility);
            }
        });
        holder.bookButton.setOnClickListener(v -> {
            if(SharedPreferencesHelper.checkUserIsSave(holder.itemView.getContext())) {
                if (listener != null) {
                    listener.onBookClick(sportFacility);
                }
            }
            else {
                showLogoutDialog();
            }
        });






    }

    @Override
    public int getItemCount() {
        return sportFacilityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, address, rating_value, rating_sum;
        Button bookButton;
        LinearLayout iconContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
            bookButton = itemView.findViewById(R.id.book_button);
            iconContainer = itemView.findViewById(R.id.icon_sport_container);
            rating_sum = itemView.findViewById(R.id.rating_sum);
            rating_value = itemView.findViewById(R.id.rating_value);
        }
    }

    public void updateList(List<SportFacility> newList) {
        DiffUtil.Callback diffCallback = new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return sportFacilityList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return sportFacilityList.get(oldItemPosition).getSportsFacilityId() ==
                        newList.get(newItemPosition).getSportsFacilityId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return sportFacilityList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        };

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        sportFacilityList.clear();
        sportFacilityList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public interface OnItemClickListener {
        void onItemClick(SportFacility facility);
        void onBookClick(SportFacility facility);

    }

    public void showLogoutDialog() {
        android.app.Dialog dialog = new android.app.Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_confirm_login);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.5f);

        android.view.Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout((int) (context.getResources().getDisplayMetrics().widthPixels * 0.9),
                    android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }

        androidx.appcompat.widget.AppCompatButton btnHuy = dialog.findViewById(R.id.huyBtn);
        androidx.appcompat.widget.AppCompatButton btnLogout = dialog.findViewById(R.id.loginButton);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLogout.setOnClickListener(v -> {
            com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper.clearAccount(context);
            com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper.clearUser(context);
            android.content.Intent intent = new android.content.Intent(context, com.example.pjbookingsport.activity.LoginActivity.class);
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

            dialog.dismiss();
        });

        dialog.show();
    }


}
