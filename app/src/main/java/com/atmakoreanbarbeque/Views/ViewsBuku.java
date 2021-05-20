package com.atmakoreanbarbeque.Views;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atmakoreanbarbeque.API.BukuAPI;
import com.atmakoreanbarbeque.Adapters.AdapterBuku;
import com.atmakoreanbarbeque.Models.Buku;
import com.atmakoreanbarbeque.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class ViewsBuku extends Fragment{

    private RecyclerView recyclerView;
    private AdapterBuku adapter;
    private List<Buku> listBuku;
    private View view;
    private AdapterBuku.deleteItemListener mListener;
    private int orientation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_views_buku, container, false);

        loadDaftarBuku();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bar_buku, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.btnSearch);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.btnSearch).setVisible(true);
        menu.findItem(R.id.btnAdd).setVisible(false);
    }

    public void loadDaftarBuku(){
        setAdapter();
        getBuku();
    }

    public void setAdapter(){
        getActivity().setTitle("Data Menu");
        /*Buat tampilan untuk adapter jika potrait menampilkan 2 data dalam 1 baris,
        sedangakan untuk landscape 4 data dalam 1 baris*/
        listBuku = new ArrayList<Buku>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdapterBuku(view.getContext(), listBuku,
                new AdapterBuku.deleteItemListener() {
                    @Override
                    public void deleteItem(Boolean delete) {

                    }
                });

        orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            // set a GridLayoutManager with 3 number of columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4);
//            gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            // In portrait
            // set a GridLayoutManager with default vertical orientation and 2 number of columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
            recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getBuku() {
        //Tambahkan tampil buku disini
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //meminta tanggapan string daari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data menu");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, BukuAPI.URL_SELECT,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //bagian jika response berhasil
                progressDialog.dismiss();
                try {
                    //mengambil data response json object yang berupa data buku
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!listBuku.isEmpty())
                        listBuku.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //mengubah data jsonArray tertentu menjadi object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        Integer id_menu          = jsonObject.optInt("id_menu");
                        String nama_kategori = jsonObject.optString("nama_kategori");
                        String nama_bahan = jsonObject.optString("nama_bahan");
                        String unit_menu = jsonObject.optString("unit_menu");
                        String nama_menu         = jsonObject.optString("nama_menu");
                        String deskripsi_menu        = jsonObject.optString("deskripsi_menu");
                        String path        = jsonObject.optString("path");
                        Double harga_menu            = jsonObject.optDouble("harga_menu");

                        //membuat objek buku
                        Buku buku =
                                new Buku(id_menu, nama_kategori, nama_bahan, nama_menu, deskripsi_menu, unit_menu, harga_menu, path);

                        //menambahkan obejk user tadi ke list buku
                        listBuku.add(buku);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //bagian jika response jaringan terdapat gangguan/error
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }

}