package com.example.pjbookingsport.frag;

import android.icu.util.LocaleData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.pjbookingsport.adapter.DayAdapter;
import com.example.pjbookingsport.adapter.HourAdapter;
import com.example.pjbookingsport.adapter.SlotAdapter;
import com.example.pjbookingsport.adapter.SubFaAdapter;
import com.example.pjbookingsport.adapter.TypeBookAdapter;
import com.example.pjbookingsport.model.FacilityType;
import com.example.pjbookingsport.model.SportFacility;
import com.example.pjbookingsport.model.SubFacility;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
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
    private TextView tvThu;
    private TextView tvDayMonthYear;
    private DayAdapter dayAdapter;
    private TypeBookAdapter typeBookAdapter;
    private HourAdapter hourAdapter;
    private SubFaAdapter subFaAdapter;
    List<LocalDate> days;
    List<LocalTime> hours;
    List<SubFacility> subFacilities;
    List<SubFacility> subFacilitiesByType;
    LocalDate dateBook;
    LocalTime startHours;
    LocalTime endHours;

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

    }
    @Override
    public void onDayClick(LocalDate date, int position) {
        setDayView(days.get(position));
        setTypeView(facility.getPrices().get(0).getFacilityType());
    }
    @Override
    public void onTypeClick(FacilityType facilityType, int position) {
        Log.d("Type by click", facilityType.getName());
        setTypeView(facilityType);
    }

    private void setTypeView(FacilityType facilityType) {

        subFacilitiesByType = new ArrayList<>();
        for(SubFacility subFacility : subFacilities){

            if(subFacility.getFacilityType().getFacilityTypeId()==facilityType.getFacilityTypeId()){
                subFacilitiesByType.add(subFacility);
                Log.d("Type", subFacility.getFacilityType().getName());
            }
        }
        rvSubFa.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        subFaAdapter = new SubFaAdapter(subFacilitiesByType, hours, dateBook, new SlotAdapter.OnSlotClickListener() {
            @Override
            public void onSlotClick(LocalTime hourSlot, SubFacility subFacility) {
                Log.d("Type", subFacility.getName() + " "+hourSlot.toString());
            }
        });
        rvSubFa.setAdapter(subFaAdapter);
    }

    public void setDayView(LocalDate date){
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

}