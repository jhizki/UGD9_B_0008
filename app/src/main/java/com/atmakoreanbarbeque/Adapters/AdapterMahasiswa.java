package com.atmakoreanbarbeque.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.atmakoreanbarbeque.API.MahasiswaAPI;
import com.atmakoreanbarbeque.Models.Mahasiswa;
import com.atmakoreanbarbeque.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view_mahasiswa,fragment)
                .addToBackStack(null)
                .commit();
    }
}