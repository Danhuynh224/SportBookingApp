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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pjbookingsport.API.RetrofitClient;
import com.example.pjbookingsport.API.ServiceAPI;
import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.SportFacilityFieldAdapter;
import com.example.pjbookingsport.model.SportFacility;

import java.io.Serializable;
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
    private ImageButton btnFilter;;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        btnFilter = view.findViewById(R.id.filter_btn);
        tvNoResult = view.findViewById(R.id.tv_no_result);
        recyclerView = view.findViewById(R.id.rv_sportFacility);
        searchBar = view.findViewById(R.id.search_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String imgUrl = getString(R.string.img_url);
        sportFacilityAdapter = new SportFacilityFieldAdapter(getContext(), sportFacilityList, imgUrl, new SportFacilityFieldAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SportFacility facility) {
                FacilityDetailFragment detailFragment = FacilityDetailFragment.newInstance(facility);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
            @Override
            public void onBookClick(SportFacility facility) {
                BookFragment bookFragment = BookFragment.newInstance(facility);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragMain, bookFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
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

        btnFilter.setOnClickListener(v -> {
            FilterFragment filterFragment = new FilterFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, filterFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void filterList(String query) {
        if (query.isEmpty()) {
            sportFacilityAdapter.updateList(new ArrayList<>(originalList));
            tvNoResult.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            return;
        }

        apiService.getSportsFacilities(query).enqueue(new Callback<List<SportFacility>>() {
            @Override
            public void onResponse(Call<List<SportFacility>> call, Response<List<SportFacility>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SportFacility> filteredList = response.body();
                    sportFacilityAdapter.updateList(filteredList);

                    if (filteredList.isEmpty()) {
                        tvNoResult.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvNoResult.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    showNoResults();
                    Log.e("API ERROR", "Không thể tìm kiếm sân thể thao");
                }
            }

            @Override
            public void onFailure(Call<List<SportFacility>> call, Throwable t) {
                showNoResults();
                Log.e("API ERROR", "Lỗi khi tìm kiếm: " + t.getMessage());
            }
        });
    }

    private void showNoResults() {
        tvNoResult.setText("Không có sân nào");
        tvNoResult.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
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
