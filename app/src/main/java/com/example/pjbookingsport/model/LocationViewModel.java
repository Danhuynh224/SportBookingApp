package com.example.pjbookingsport.model;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {
    private final MutableLiveData<Location> locationLiveData = new MutableLiveData<>();

    public void setLocation(Location location) {
        locationLiveData.setValue(location);
    }

    public LiveData<Location> getLocation() {
        return locationLiveData;
    }
}
