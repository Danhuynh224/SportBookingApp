package com.example.pjbookingsport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.BookingInfo;
import com.example.pjbookingsport.model.SubFacility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class SuccessActivity extends AppCompatActivity {
    private TextView tvMessage, tvBookingId, txtUserName, txtPrice, txtDate;
    private Button btnReturnHome;
    Booking booking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_success);
        tvMessage = findViewById(R.id.tvMessage);
        tvBookingId = findViewById(R.id.tvBookingId);
        txtUserName = findViewById(R.id.txtUserName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDate = findViewById(R.id.txtDate);
        btnReturnHome = findViewById(R.id.btnReturnHome);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        booking = (Booking) getIntent().getSerializableExtra("booking");




        List<BookingInfo> bookingInfos = booking.getBookingInfos();

        SubFacility subFacility = bookingInfos.get(0).getSubFacility();

        tvMessage.setText("Cảm ơn vì đã đặt\nBạn đã đặt sân thành công tại " + subFacility.getSportsFacility().getName() + ",\n" + booking.getBookingDate().format(formatter));

        txtUserName.setText(booking.getUser().getFullName());

        // Định dạng số với dấu chấm phân tách hàng nghìn
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm (.) để phân tách hàng nghìn
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        txtPrice.setText(decimalFormat.format(booking.getTotalPrice()) + " VND");
        txtDate.setText(booking.getBookingDate().format(formatter));

        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}