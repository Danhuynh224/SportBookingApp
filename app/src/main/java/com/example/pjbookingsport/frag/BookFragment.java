package com.example.pjbookingsport.frag;

import android.annotation.SuppressLint;
import android.icu.util.LocaleData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.BookInForAdapter;
import com.example.pjbookingsport.adapter.DayAdapter;
import com.example.pjbookingsport.adapter.HourAdapter;
import com.example.pjbookingsport.adapter.SlotAdapter;
import com.example.pjbookingsport.adapter.SubFaAdapter;
import com.example.pjbookingsport.adapter.TypeBookAdapter;
import com.example.pjbookingsport.model.Booking;
import com.example.pjbookingsport.model.BookingInfo;
import com.example.pjbookingsport.model.FacilityType;
import com.example.pjbookingsport.model.Price;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.model.SubFacility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment implements DayAdapter.OnDayClickListener, TypeBookAdapter.OnTypeClickListener {



    private RecyclerView rvDays ;
    private RecyclerView rvTypes ;
    private RecyclerView rvHours ;
    private RecyclerView rvSubFa;
    private RecyclerView rvBookInfo;
    private TextView tvThu;
    private TextView tvDayMonthYear;
    private TextView tvTotalHour;
    private TextView tvTotalPrice;
    private DayAdapter dayAdapter;
    private TypeBookAdapter typeBookAdapter;
    private HourAdapter hourAdapter;
    private SubFaAdapter subFaAdapter;
    private BookInForAdapter bookInForAdapter;
    List<LocalDate> days;
    List<LocalTime> hours;
    List<SubFacility> subFacilities;
    List<SubFacility> subFacilitiesByType;
    List<BookingInfo> bookingInfos = new ArrayList<>();
    LocalDate dateBook;
    Price price;
    Booking booking = new Booking();
    FacilityType prefacilityType;

    private ServiceAPI serviceAPI;
    private static final String ARG_FACILITY = "FACILITY";
    private SportFacility facility;

    public BookFragment() {
        // Required empty public constructor
    }

    public static BookFragment newInstance(SportFacility facility) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FACILITY, facility);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            facility = (SportFacility) getArguments().getSerializable(ARG_FACILITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvThu =view.findViewById(R.id.tvThu);
        tvDayMonthYear = view.findViewById(R.id.tvDayMonthYear);

        //Chọn ngày
        rvDays = view.findViewById(R.id.rvDays);
        rvDays.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        days = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i <= 30; i++) {
            days.add(today.plusDays(i));
        }

        dayAdapter = new DayAdapter(days,this); // Truyền this để bắt sự kiện
        rvDays.setAdapter(dayAdapter);

        //Chọn loại
        rvTypes = view.findViewById(R.id.rvTypes);
        rvTypes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        typeBookAdapter = new TypeBookAdapter(facility.getPrices(),this); // Truyền this để bắt sự kiện
        rvTypes.setAdapter(typeBookAdapter);

        setBookingFrame();
        rvHours=view.findViewById(R.id.rvHours);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvHours.setLayoutManager(layoutManager);
        hourAdapter = new HourAdapter(hours);
        rvHours.setAdapter(hourAdapter);

        rvSubFa=view.findViewById(R.id.rvSubFa);
        rvBookInfo=view.findViewById(R.id.inforRv);

        tvTotalHour = view.findViewById(R.id.tvTotalHour);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);

    }
    @Override
    public void onDayClick(LocalDate date, int position) {
        setDayView(days.get(position));
        if(prefacilityType!=null){
            setTypeView(prefacilityType);
        }
        else {
            setTypeView(facility.getPrices().get(0).getFacilityType());
        }
    }
    @Override
    public void onTypeClick(FacilityType facilityType, int position) {
        this.prefacilityType = facilityType;
        setTypeView(facilityType);
    }

    private void setTypeView(FacilityType facilityType) {
        List<Price> prices = facility.getPrices();
        for (Price tempPrice : prices){
            if(tempPrice.getFacilityType()==facilityType)
            {
                price =tempPrice;
                break;
            }
        }
        bookingInfos = new ArrayList<>();
        subFacilitiesByType = new ArrayList<>();
        for(SubFacility subFacility : subFacilities){

            if(subFacility.getFacilityType().getFacilityTypeId()==facilityType.getFacilityTypeId()){
                subFacilitiesByType.add(subFacility);
            }
        }
        rvSubFa.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        subFaAdapter = new SubFaAdapter(subFacilitiesByType, hours, dateBook, new SlotAdapter.OnSlotClickListener() {
            @Override
            public void onSlotClick(LocalTime startHour, LocalTime endHour, SubFacility subFacility) {
                setBookingInfos(startHour,endHour,subFacility);
            }
        });
        rvSubFa.setAdapter(subFaAdapter);

    }

    public void setDayView(LocalDate date){
        booking.setBookingDate(date);
        dateBook=date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
        String dateString = date.format(formatter);
        tvDayMonthYear.setText(dateString);
        tvThu.setText(date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("vi")));

    }

    public void setBookingFrame(){
        hours = new ArrayList<>();
        for(int i =3 ;i<23;i++){
            hours.add(LocalTime.of(i,0));
        }

        serviceAPI = RetrofitClient.getClient().create(ServiceAPI.class);
        serviceAPI.getSubFaByFaId(facility.getSportsFacilityId()).enqueue(new Callback<List<SubFacility>>() {
            @Override
            public void onResponse(Call<List<SubFacility>> call, Response<List<SubFacility>> response) {
                if(response.isSuccessful()){
                    subFacilities = response.body();
                    setDayView(days.get(0));
                    setTypeView(facility.getPrices().get(0).getFacilityType());

                }
                else {
                    Log.d("API ERROR", "Không thể lấy danh sách");
                }
            }

            @Override
            public void onFailure(Call<List<SubFacility>> call, Throwable t) {
                Log.d("API ERROR", "Không thể lấy danh mục" + t.getMessage());
            }
        });
    }
    @SuppressLint("SetTextI18n")
    public void setBookingInfos(LocalTime startHour, LocalTime endHour, SubFacility subFacility){
        int index = bookingInfos.stream()
                .filter(bookingInfo -> bookingInfo.getSubFacility().equals(subFacility))
                .map(bookingInfos::indexOf)
                .findFirst()
                .orElse(-1);
        if(index==-1){
            bookingInfos.add(new BookingInfo(subFacility,startHour,endHour,booking));
        } else if (startHour==null) {
            bookingInfos.remove(index);
        } else {
            bookingInfos.get(index).setStartTime(startHour);
            bookingInfos.get(index).setEndTime(endHour);
        }
        booking.setBookingInfos(bookingInfos);

        booking.Caculate(price);


        bookInForAdapter = new BookInForAdapter(bookingInfos);
        rvBookInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvBookInfo.addItemDecoration(dividerItemDecoration);
        rvBookInfo.setAdapter(bookInForAdapter);

        tvTotalHour.setText(String.valueOf(booking.getTotalHour())+" giờ");

        // Định dạng số với dấu chấm phân cách hàng nghìn
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm (.) để phân tách hàng nghìn

        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        tvTotalPrice.setText(decimalFormat.format(booking.getTotalPrice())+" VND");


    }

}