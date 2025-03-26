package com.example.pjbookingsport.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pjbookingsport.frag.FacilityDetailFragment;
import com.example.pjbookingsport.frag.FacilityPriceFragment;
import com.example.pjbookingsport.frag.FacilityInfoFragment;
import com.example.pjbookingsport.model.SportFacility;

public class FacilityPagerAdapter extends FragmentStateAdapter {

    private final SportFacility facility;

    public FacilityPagerAdapter(@NonNull FacilityDetailFragment fragmentActivity, SportFacility facility) {
        super(fragmentActivity);
        this.facility = facility;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return FacilityInfoFragment.newInstance(facility);
        } else {
            return new FacilityPriceFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
