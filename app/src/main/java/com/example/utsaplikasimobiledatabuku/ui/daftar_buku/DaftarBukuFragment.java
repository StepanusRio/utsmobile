package com.example.utsaplikasimobiledatabuku.ui.daftar_buku;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utsaplikasimobiledatabuku.API.GetBookAPI;
import com.example.utsaplikasimobiledatabuku.API.ServerAPI;
import com.example.utsaplikasimobiledatabuku.Model.DataBuku;
import com.example.utsaplikasimobiledatabuku.Model.Value;
import com.example.utsaplikasimobiledatabuku.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DaftarBukuFragment extends Fragment {
    private List<DataBuku> results = new ArrayList<>();
    private BukuAdapter viewAdapter;
    RecyclerView recyclerView;
    ImageButton btndelete;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_buku,container,false);
        btndelete = view.findViewById(R.id.BtnDelete);
        viewAdapter = new BukuAdapter(getContext(),results);
        recyclerView = view.findViewById(R.id.rvBookList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);
        loadBook();
        return view;
    }
    private void loadBook() {
        ServerAPI urlapi = new ServerAPI();
        String URL = urlapi.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetBookAPI api = retrofit.create(GetBookAPI.class);
        Call<Value> call = api.get();
        Log.i("[BOOK LOADED}", "loadBook: Load Book Accessed");
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                results = response.body().getResult();
                Log.i("Info Load", "onResponse: Response body Masuk Adapter");
                viewAdapter = new BukuAdapter(getContext(),results);
                recyclerView.setAdapter(viewAdapter);
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Log.i("Info Load", "onFailure: Load Failed");
            }
        });
    }
}