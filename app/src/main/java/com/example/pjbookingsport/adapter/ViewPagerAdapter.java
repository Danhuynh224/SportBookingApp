package com.example.pjbookingsport.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pjbookingsport.frag.PricingFragment;
import com.example.pjbookingsport.frag.InfoFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new InfoFragment();
        } else {
            return new PricingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
