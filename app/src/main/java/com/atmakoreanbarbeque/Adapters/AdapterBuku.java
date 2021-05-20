package com.atmakoreanbarbeque.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atmakoreanbarbeque.API.BukuAPI;
import com.atmakoreanbarbeque.Models.Buku;
import com.atmakoreanbarbeque.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterBuku extends RecyclerView.Adapter<AdapterBuku.adapterBukuViewHolder> {

    private List<Buku> bukuList;
    private List<Buku> bukuListFiltered;
    private Context context;
    private View view;
    private AdapterBuku.deleteItemListener mListener;
    private String idMenu;

    public AdapterBuku(Context context, List<Buku> bukuList,
                       AdapterBuku.deleteItemListener mListener) {
        this.context            = context;
        this.bukuList           = bukuList;
        this.bukuListFiltered   = bukuList;
        this.mListener          = mListener;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public AdapterBuku.adapterBukuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_adapter_buku, parent, false);
        return new AdapterBuku.adapterBukuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBuku.adapterBukuViewHolder holder, int position) {
        final Buku buku = bukuListFiltered.get(position);

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.txtNama.setText(buku.getNamaMenu());
        holder.txtPengarang.setText(buku.getDeskripsiMenu());
        holder.txtHarga.setText("Rp "+ formatter.format(buku.getHarga()));
        Glide.with(context)
                .load(BukuAPI.ROOT_URL+buku.getPath())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivGambar);

    }

    @Override
    public int getItemCount() {
        return (bukuListFiltered != null) ? bukuListFiltered.size() : 0;
    }

    public class adapterBukuViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtPengarang, txtHarga, ivEdit, ivHapus;;
        private ImageView ivGambar;
        private CardView cardBuku;

        public adapterBukuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNama         = itemView.findViewById(R.id.tvNamaBuku);
            txtPengarang    = itemView.findViewById(R.id.tvPengarang);
            txtHarga        = itemView.findViewById(R.id.tvHarga);
            ivGambar        = itemView.findViewById(R.id.ivGambar);
            ivEdit          = (TextView) itemView.findViewById(R.id.ivEdit);
            ivHapus         = (TextView) itemView.findViewById(R.id.ivHapus);
            cardBuku        = itemView.findViewById(R.id.cardBuku);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString();
                if (userInput.isEmpty()) {
                    bukuListFiltered = bukuList;
                }
                else {
                    List<Buku> filteredList = new ArrayList<>();
                    for(Buku buku : bukuList) {
                        if(String.valueOf(buku.getNamaMenu()).toLowerCase().contains(userInput)) {
                            filteredList.add(buku);
                        }
                    }
                    bukuListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = bukuListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                bukuListFiltered = (ArrayList<Buku>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view_buku,fragment)
                .commit();
    }
}