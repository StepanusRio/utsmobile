package com.example.utsaplikasimobiledatabuku.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.utsaplikasimobiledatabuku.R;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome To Easy Book Below is your Profile Details");
    }

    public LiveData<String> getText() {
        return mText;
    }
}