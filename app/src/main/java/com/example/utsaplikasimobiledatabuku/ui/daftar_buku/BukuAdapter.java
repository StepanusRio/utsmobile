package com.example.utsaplikasimobiledatabuku.ui.daftar_buku;

import android.content.Context;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.ViewHolder> {
    private Context context;
    private List<DataBuku> results;

    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    public BukuAdapter(Context context, List<DataBuku> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(v,listener);
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
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView isbn,judul,penulis,penerbit,tahun,jmlhalaman,harga;
        public ConstraintLayout layout;
        public ImageButton BtnDelete;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
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
            BtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    listener.onItemClick(getAdapterPosition());
                    delete(String.valueOf(isbn));
                }

                private void delete(String tisbn) {
                    ServerAPI urlapi = new ServerAPI();
                    String URL = urlapi.BASE_URL;
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    DeleteBookAPI api = retrofit.create(DeleteBookAPI.class);
                    api.delete(tisbn).enqueue(new Callback<Value>() {
                        @Override
                        public void onResponse(Call<Value> call, Response<Value> response) {
                            Toast.makeText(context, "[Success]", Toast.LENGTH_SHORT).show();
//                            try{
//                                JSONObject json = new JSONObject(response.body().toString());
//                                if (json.getString("result").toString().equals("1")){
//                                    
//                                }
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
                        }

                        @Override
                        public void onFailure(Call<Value> call, Throwable t) {
                            Toast.makeText(context, "[Failure]", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }
}
