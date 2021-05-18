package com.atmakoreanbarbeque.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.atmakoreanbarbeque.API.MahasiswaAPI;
import com.atmakoreanbarbeque.Models.Mahasiswa;
import com.atmakoreanbarbeque.R;
import com.atmakoreanbarbeque.Views.TambahEditMahasiswa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class AdapterMahasiswa extends RecyclerView.Adapter<AdapterMahasiswa.adapterUserViewHolder> {

    private List<Mahasiswa> mahasiswaList;
    private List<Mahasiswa> mahasiswaListFiltered;
    private Context context;
    private View view;
    private AdapterMahasiswa.deleteItemListener mListener;

    public AdapterMahasiswa(Context context, List<Mahasiswa> mahasiswaList,
                            AdapterMahasiswa.deleteItemListener mListener) {
        this.context                =context;
        this.mahasiswaList          = mahasiswaList;
        this.mahasiswaListFiltered  = mahasiswaList;
        this.mListener              = mListener;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_mahasiswa, parent, false);
        return new adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterUserViewHolder holder, int position) {
        final Mahasiswa mahasiswa = mahasiswaListFiltered.get(position);

        holder.txtNama.setText(mahasiswa.getNamaMenu());
        holder.txtNpm.setText(mahasiswa.getIdMenu());
        holder.txtJenisKelamin.setText(mahasiswa.getDeskripsiMenu());
        holder.txtProdi.setText(mahasiswa.getIdMenu());
        Glide.with(context)
                .load("https://1080motion.com/wp-content/uploads/2018/06/NoImageFound.jpg.png")
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivGambar);
    }

    @Override
    public int getItemCount() {
        return (mahasiswaListFiltered != null) ? mahasiswaListFiltered.size() : 0;
    }

    public class adapterUserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtNpm, txtProdi, txtJenisKelamin, ivEdit, ivHapus;
        private ImageView ivGambar;

        public adapterUserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNama         = (TextView) itemView.findViewById(R.id.txtNama);
            txtNpm          = (TextView) itemView.findViewById(R.id.txtNpm);
            txtJenisKelamin = (TextView) itemView.findViewById(R.id.txtJenisKelamin);
            txtProdi        = (TextView) itemView.findViewById(R.id.txtProdi);
            ivGambar        = (ImageView) itemView.findViewById(R.id.ivGambar);
            ivEdit          = (TextView) itemView.findViewById(R.id.ivEdit);
            ivHapus         = (TextView) itemView.findViewById(R.id.ivHapus);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString().toLowerCase();
                if (userInput.isEmpty()) {
                    mahasiswaListFiltered = mahasiswaList;
                }
                else {
                    List<Mahasiswa> filteredList = new ArrayList<>();
                    for(Mahasiswa mahasiswa : mahasiswaList) {
                        if(mahasiswa.getNamaMenu().toLowerCase().contains(userInput)) {
                            filteredList.add(mahasiswa);
                        }
                    }
                    mahasiswaListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mahasiswaListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mahasiswaListFiltered = (ArrayList<Mahasiswa>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    //Fungsi menghapus data mahasiswa
    public void deleteMahasiswa(Integer idMenu){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data mahasiswa");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, MahasiswaAPI.URL_DELETE + idMenu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    mListener.deleteItem(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view_mahasiswa,fragment)
                .addToBackStack(null)
                .commit();
    }
}