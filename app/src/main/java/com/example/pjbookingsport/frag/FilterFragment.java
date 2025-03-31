package com.example.pjbookingsport.frag;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pjbookingsport.R;

public class FilterFragment extends Fragment {

    private ImageButton btnBack;
    private TextView datetimepicker, cityPicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        datetimepicker = view.findViewById(R.id.datetime_picker);
        datetimepicker.setOnClickListener(v -> {
            DialogFragment newFragment = new DateTimePickerFragment(datetimepicker);
            newFragment.show(getParentFragmentManager(), "dateTimePicker");
        });

        cityPicker = view.findViewById(R.id.city_picker);
        cityPicker.setOnClickListener(v -> {
            showCityPickerDialog();
        });

        return view;
    }

    private void showCityPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn tỉnh/thành phố");

        // Lấy danh sách tỉnh/thành phố từ resources
        String[] provinces = getResources().getStringArray(R.array.vietnam_provinces);

        builder.setItems(provinces, (dialog, which) -> cityPicker.setText(provinces[which]));
        builder.show();
    }

}