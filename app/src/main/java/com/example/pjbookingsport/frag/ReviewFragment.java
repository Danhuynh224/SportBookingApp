package com.example.pjbookingsport.frag;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.BookingInfo;
import com.example.pjbookingsport.model.JWT;
import com.example.pjbookingsport.model.ReviewRequest;
import com.example.pjbookingsport.model.SubFacility;
import com.example.pjbookingsport.model.User;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {
    private Booking booking;
    private RatingBar ratingBar;
    private EditText editTextComment;
    private Button btnSend;
    private ImageButton btnBack;
    private ImageView imgFacility;
    private TextView tvName, tvRating, tvTotalRating;
    private LinearLayout layoutSlot;
    private JWT jwt;
    private static final String ARG_BOOKING = "BOOKING";

    public ReviewFragment() {
        // Required empty public constructor
    }


    public static ReviewFragment newInstance(Booking booking) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOKING, booking);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgFacility = view.findViewById(R.id.img_facility);
        tvName = view.findViewById(R.id.tv_name);
        tvRating = view.findViewById(R.id.tvRating);
        tvTotalRating = view.findViewById(R.id.tvTotalRating);
        layoutSlot = view.findViewById(R.id.layoutSlot);
        ratingBar = view.findViewById(R.id.ratingBar);
        editTextComment = view.findViewById(R.id.editTextComment);
        btnSend = view.findViewById(R.id.btnSend);
        btnBack = view.findViewById(R.id.btn_back);
        jwt = SharedPreferencesHelper.getJWT(getContext());
        String imgUrl = getString(R.string.img_url);


        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable(ARG_BOOKING);
        }

        assert booking != null;
        List<BookingInfo> bookingInfos = booking.getBookingInfos();

        if (bookingInfos == null || bookingInfos.isEmpty()) {
            return;
        }

        SubFacility subFacility = bookingInfos.get(0).getSubFacility();

        Glide.with(this)
                .load(imgUrl + subFacility.getSportsFacility().getSportsFacilityId())
                .error(R.drawable.ic_launcher_foreground)
                .into(imgFacility);

        tvName.setText(subFacility.getSportsFacility().getName());

        tvRating.setText(subFacility.getSportsFacility().getAverageRating() + "/5");
        tvTotalRating.setText("(" + subFacility.getSportsFacility().getReviewCount() + ")");

        layoutSlot.removeAllViews();
        for (int i = 0; i < bookingInfos.size(); i++) {
            BookingInfo info = bookingInfos.get(i);
            SubFacility sub = info.getSubFacility();
            TextView timeSlotText = new TextView(getContext());
            timeSlotText.setText(sub.getName() + ": " + info.getStartTime() + " - " + info.getEndTime());
            timeSlotText.setTextColor(getResources().getColor(R.color.black));
            timeSlotText.setTypeface(null, Typeface.BOLD);
            layoutSlot.addView(timeSlotText);
        }

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editTextComment.getText().toString().trim();
                int rating = (int) ratingBar.getRating();

                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(getContext(), "Vui lòng nhập nhận xét!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rating == 0) {
                    Toast.makeText(getContext(), "Vui lòng chọn sao đánh giá", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = SharedPreferencesHelper.getUser(requireContext());

                assert user != null;
                ReviewRequest savedReview = new ReviewRequest(
                        user.getUserId(),
                        subFacility.getSportsFacility().getSportsFacilityId(),
                        rating,
                        comment
                );

                saveFeedback(savedReview);

            }
        });

    }


    private void saveFeedback(ReviewRequest request) {
        AlertDialog loadingDialog = new AlertDialog.Builder(requireContext())
                .setView(R.layout.dialog_loading)
                .setCancelable(false)
                .create();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.show();

        ServiceAPI serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
        serviceAPI.saveReview(jwt.getToken(), request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loadingDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    showSuccessDialog();
                } else {
                    Toast.makeText(getContext(), "Gửi đánh giá thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showSuccessDialog() {
        // Tạo dialog tùy chỉnh
        Dialog successDialog = new Dialog(requireContext());
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.dialog_success_feedback);

        // Thiết lập các thuộc tính cho cửa sổ dialog
        Window window = successDialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }

        // Tìm các view trong dialog
        ImageView imgSuccess = successDialog.findViewById(R.id.img_success);
        TextView tvSuccess = successDialog.findViewById(R.id.tv_success_message);
        Button btnOk = successDialog.findViewById(R.id.btn_ok);

        // Thiết lập animation cho hình ảnh thành công
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.success_animation);
        imgSuccess.startAnimation(animation);

        // Thiết lập nội dung và sự kiện click cho nút OK
        tvSuccess.setText("Cảm ơn bạn đã đánh giá!");
        btnOk.setOnClickListener(v -> {
            successDialog.dismiss();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Hiển thị dialog
        successDialog.setCancelable(false);
        successDialog.show();
    }





}