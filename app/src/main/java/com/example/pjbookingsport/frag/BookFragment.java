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

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.DayAdapter;
import com.example.pjbookingsport.adapter.TypeBookAdapter;
import com.example.pjbookingsport.model.FacilityType;
import com.example.pjbookingsport.model.SportFacility;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment implements DayAdapter.OnDayClickListener, TypeBookAdapter.OnDayClickListener {



    private RecyclerView rvDays ;
    private RecyclerView rvTypes ;
    private TextView tvThu;
    private TextView tvDayMonthYear;
    private DayAdapter adapter;
    private TypeBookAdapter typeBookAdapter;
    List<LocalDate> days;
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

        adapter = new DayAdapter(days,this); // Truyền this để bắt sự kiện
        rvDays.setAdapter(adapter);
        setDayView(days.get(0));
        //Chọn loại
        rvTypes = view.findViewById(R.id.rvTypes);
        rvTypes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        typeBookAdapter = new TypeBookAdapter(facility.getPrices(),this); // Truyền this để bắt sự kiện
        rvTypes.setAdapter(typeBookAdapter);
    }
    @Override
    public void onDayClick(LocalDate date, int position) {
        setDayView(days.get(position));
    }
    @Override
    public void onDayClick(FacilityType facilityType, int position) {
        setDayView(days.get(position));
    }
    public void setDayView(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
        String dateString = date.format(formatter);
        tvDayMonthYear.setText(dateString);
        tvThu.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("vi")));
    }

}