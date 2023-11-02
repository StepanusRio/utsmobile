package com.example.utsaplikasimobiledatabuku.ui.daftar_buku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utsaplikasimobiledatabuku.API.DeleteBookAPI;
import com.example.utsaplikasimobiledatabuku.API.ServerAPI;
import com.example.utsaplikasimobiledatabuku.Model.DataBuku;

import com.example.utsaplikasimobiledatabuku.Model.Value;
import com.example.utsaplikasimobiledatabuku.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.ViewHolder> {
    private Context context;
    private List<DataBuku> results;
    public BukuAdapter(Context context, List<DataBuku> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataBuku result = results.get(position);
        holder.isbn.setText(result.getIsbn());
        holder.judul.setText(result.getJudul());
        holder.penulis.setText(result.getPenulis());
        holder.penerbit.setText(result.getPenerbit());
        holder.tahun.setText(result.getTahun());
        holder.jmlhalaman.setText(result.getJmlhalaman());
        holder.harga.setText(result.getHarga());
        holder.BtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DELETE BOOK");
                builder.setMessage("Confirm to Delete"+result.getJudul());
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteAPI(result.getIsbn());
                        Delete(holder.getAdapterPosition());
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView isbn,judul,penulis,penerbit,tahun,jmlhalaman,harga;
        public ConstraintLayout layout;
        public ImageButton BtnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.isbn = (TextView) itemView.findViewById(R.id.TvISBN);
            this.judul = (TextView) itemView.findViewById(R.id.TvJudul);
            this.penulis = (TextView) itemView.findViewById(R.id.TvPenulis);
            this.penerbit = (TextView) itemView.findViewById(R.id.TvPenerbit);
            this.tahun = (TextView) itemView.findViewById(R.id.TvTahun);
            this.jmlhalaman = (TextView) itemView.findViewById(R.id.TvJlmHal);
            this.harga = (TextView) itemView.findViewById(R.id.TvHarga);
            this.layout = (ConstraintLayout) itemView.findViewById(R.id.layoutitem);
            this.BtnDelete = (ImageButton) itemView.findViewById(R.id.BtnDelete);
        }
    }
    public void Delete(int item){
        results.remove(item);
        notifyItemRemoved(item);
    }
    public void DeleteAPI(String tisbn){
        ServerAPI urlapi = new ServerAPI();
        String URL = urlapi.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DeleteBookAPI api = retrofit.create(DeleteBookAPI.class);
        api.delete(tisbn).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context, "[SUCCESS]", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "[FAILURE]", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
