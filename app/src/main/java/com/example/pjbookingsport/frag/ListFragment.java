package com.example.pjbookingsport.frag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.adapter.SportFacilityFieldAdapter;
import com.example.pjbookingsport.model.SportFacilityField;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private SportFacilityFieldAdapter adapter;
    private List<SportFacilityField> sportFieldList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.rv_sportFacility);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách sân thể thao
        sportFieldList = new ArrayList<>();
        sportFieldList.add(new SportFacilityField(R.drawable.caykeo,"Tennis Cây Lọc Vùng Thủ Đức",
                "1110 Phạm Văn Đồng, Linh Đông, Thủ Đức",
                4));
        sportFieldList.add(new SportFacilityField(R.drawable.galaxy,"Sân Cầu Lông Galaxy",
                "456 Điện Biên Phủ, Bình Thạnh", 3));
        sportFieldList.add(new SportFacilityField(R.drawable.caykeo,"Tennis Cây Lọc Vùng Thủ Đức",
                "1110 Phạm Văn Đồng, Linh Đông, Thủ Đức",
                4));
        sportFieldList.add(new SportFacilityField(R.drawable.galaxy,"Sân Cầu Lông Galaxy",
                "456 Điện Biên Phủ, Bình Thạnh", 3));
        sportFieldList.add(new SportFacilityField(R.drawable.caykeo,"Tennis Cây Lọc Vùng Thủ Đức",
                "1110 Phạm Văn Đồng, Linh Đông, Thủ Đức",
                4));
        sportFieldList.add(new SportFacilityField(R.drawable.galaxy,"Sân Cầu Lông Galaxy",
                "456 Điện Biên Phủ, Bình Thạnh", 3));
        sportFieldList.add(new SportFacilityField(R.drawable.caykeo,"Tennis Cây Lọc Vùng Thủ Đức",
                "1110 Phạm Văn Đồng, Linh Đông, Thủ Đức",
                4));
        sportFieldList.add(new SportFacilityField(R.drawable.galaxy,"Sân Cầu Lông Galaxy",
                "456 Điện Biên Phủ, Bình Thạnh", 3));

        // Thiết lập adapter
        adapter = new SportFacilityFieldAdapter(getContext(), sportFieldList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}