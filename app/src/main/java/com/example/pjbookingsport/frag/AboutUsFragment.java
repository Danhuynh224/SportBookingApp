package com.example.pjbookingsport.frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pjbookingsport.R;


public class AboutUsFragment extends Fragment {

    private TextView text1, text2, text3;
    private ImageButton btnBack;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);
        btnBack = view.findViewById(R.id.btn_back);

        String s1 = "<b>Sports Booking</b> là giải pháp toàn diện dành cho cả người chơi và người quản lý sân thể thao. Chúng tôi cung cấp nền tảng giúp người chơi dễ dàng tìm, đặt sân trực tuyến, thanh toán nhanh chóng, đồng thời tìm kiếm đối tác chơi thể thao một cách dễ dàng, giao tiếp trò chuyện tiện lợi, hiệu quả.";
        String s2 = "<b>Sports Booking</b> giúp người quản lý theo dõi lịch đặt sân, mua bán dịch vụ, quản lý thanh toán, và chăm sóc khách hàng hiệu quả.";
        String s3 = "<b>Với Sports Booking,</b> việc quản lý và trải nghiệm thể thao trở nên đơn giản và tiện lợi hơn bao giờ hết. Hãy tham gia ngay để tận hưởng sự tiện ích từ cả hai phía – người chơi và người quản lý!";

        text1.setText(Html.fromHtml(s1));
        text2.setText(Html.fromHtml(s2));
        text3.setText(Html.fromHtml(s3));

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }
}