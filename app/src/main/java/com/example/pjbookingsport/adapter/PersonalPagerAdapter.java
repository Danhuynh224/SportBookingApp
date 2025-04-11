package com.example.pjbookingsport.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pjbookingsport.frag.PersonalInfoFragment;

public class PersonalPagerAdapter extends FragmentStateAdapter {

    public PersonalPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new PersonalInfoFragment();
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
