package com.example.pjbookingsport.frag;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.BookedAdapter;
import com.example.pjbookingsport.adapter.ImageSliderPostAdapter;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.BookingInfo;
import com.example.pjbookingsport.model.Post;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.model.SubFacility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookedDetailFragment extends Fragment {

    private Booking booking;
    private ImageButton btnBack;

    private AppCompatButton btnBackMain, btnBookNew;

    private TextView tvTenSan, tvDiaChi, tvNgayGio, tvTongTien, tvBookingID, tvDateTitle;

    private LinearLayout layoutTimeSlots;
    private static final String ARG_BOOKING = "BOOKING";


    public BookedDetailFragment() {
        // Required empty public constructor
    }

    public static BookedDetailFragment newInstance(Booking booking) {
        BookedDetailFragment fragment = new BookedDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOKING, booking);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTenSan = view.findViewById(R.id.tvTenSan);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);
        tvNgayGio = view.findViewById(R.id.tvNgayGio);
        tvTongTien = view.findViewById(R.id.tvTongTien);
        layoutTimeSlots = view.findViewById(R.id.layoutTimeSlots);
        tvBookingID = view.findViewById(R.id.tvBookingID);
        tvDateTitle = view.findViewById(R.id.tvDateTitle);
        btnBack = view.findViewById(R.id.btn_back);
        btnBackMain = view.findViewById(R.id.btnBackMain);
        btnBookNew = view.findViewById(R.id.btnThanhToan);

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        btnBackMain .setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (getArguments() != null) {
            booking = (Booking) getArguments().getSerializable(ARG_BOOKING);
            if (booking != null) {
                displayBookingDetails(booking);

            }
        }
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void displayBookingDetails(Booking booking) {
        List<BookingInfo> bookingInfos = booking.getBookingInfos();

        if (bookingInfos == null || bookingInfos.isEmpty()) {
            return;
        }

        SubFacility subFacility = bookingInfos.get(0).getSubFacility();

        tvTenSan.setText(subFacility.getSportsFacility().getName());
        tvDiaChi.setText(subFacility.getSportsFacility().getAddress());

        layoutTimeSlots.removeAllViews();
        for (int i = 0; i < bookingInfos.size(); i++) {
            BookingInfo info = bookingInfos.get(i);
            SubFacility sub = info.getSubFacility();
            TextView timeSlotText = new TextView(getContext());
            timeSlotText.setText(sub.getName() + ": " + info.getStartTime() + " - " + info.getEndTime());
            timeSlotText.setTextColor(getResources().getColor(R.color.black));
            timeSlotText.setTypeface(null, Typeface.BOLD);
            layoutTimeSlots.addView(timeSlotText);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        tvNgayGio.setText("Ngày: " + booking.getBookingDate().format(formatter));
        tvDateTitle.setText(booking.getBookingDate().format(formatter));
        tvBookingID.setText(booking.getBookingId().toString());

        // Định dạng tiền
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm (.) để phân tách hàng nghìn
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);

        tvTongTien.setText("Tổng tiền: " + decimalFormat.format(booking.getTotalPrice())  + " VND");

    }
}