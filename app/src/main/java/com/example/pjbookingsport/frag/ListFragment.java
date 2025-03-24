package com.example.pjbookingsport.frag;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.SportFacilityFieldAdapter;
import com.example.pjbookingsport.model.SportFacility;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private SportFacilityFieldAdapter sportFacilityAdapter;
    private List<SportFacility> sportFacilityList = new ArrayList<>();
    private List<SportFacility> originalList = new ArrayList<>(); // Danh sách gốc
    private ServiceAPI apiService;
    private TextView tvNoResult;
    private EditText searchBar;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        tvNoResult = view.findViewById(R.id.tv_no_result);
        recyclerView = view.findViewById(R.id.rv_sportFacility);
        searchBar = view.findViewById(R.id.search_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String imgUrl = getString(R.string.img_url);
        sportFacilityAdapter = new SportFacilityFieldAdapter(getContext(), sportFacilityList, imgUrl);
        recyclerView.setAdapter(sportFacilityAdapter);

        GetSportFacilities(); // gọi API lấy danh sách sân

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void filterList(String query) {
        List<SportFacility> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            // Nếu EditText trống, hiển thị lại danh sách gốc
            sportFacilityAdapter.updateList(new ArrayList<>(originalList));
            tvNoResult.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            return;
        }

        for (SportFacility facility : originalList) {
            if (facility.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(facility);
            }
        }

        sportFacilityAdapter.updateList(filteredList);

        // Hiển thị thông báo nếu không có kết quả
        if (filteredList.isEmpty()) {
            tvNoResult.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoResult.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void GetSportFacilities() {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getAllSportFacility().enqueue(new Callback<List<SportFacility>>() {
            @Override
            public void onResponse(Call<List<SportFacility>> call, Response<List<SportFacility>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    originalList.clear();
                    originalList.addAll(response.body()); // Lưu danh sách gốc
                    sportFacilityList.clear();
                    sportFacilityList.addAll(originalList); // Hiển thị danh sách đầy đủ
                    sportFacilityAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API ERROR", "Không thể lấy danh sách sân thể thao");
                }
            }

            @Override
            public void onFailure(Call<List<SportFacility>> call, Throwable t) {
                Log.e("API ERROR", "Lỗi khi gọi API sân thể thao: " + t.getMessage());
            }
        });
    }
}
