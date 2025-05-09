package com.example.pjbookingsport.frag;
import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.pjbookingsport.model.Review;
import com.example.pjbookingsport.model.ReviewRequest;
import com.example.pjbookingsport.model.SubFacility;
import com.example.pjbookingsport.model.User;
import com.example.pjbookingsport.sharedPreferences.SharedPreferencesHelper;

import java.time.LocalDateTime;
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

        ServiceAPI serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
        serviceAPI.saveReview(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    requireActivity().getSupportFragmentManager().popBackStack();
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
}