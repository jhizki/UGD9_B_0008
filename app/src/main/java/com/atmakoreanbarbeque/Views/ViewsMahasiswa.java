package com.atmakoreanbarbeque.Views;

import android.app.ProgressDialog;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atmakoreanbarbeque.API.MahasiswaAPI;
import com.atmakoreanbarbeque.Adapters.AdapterMahasiswa;
import com.atmakoreanbarbeque.Models.Buku;
import com.atmakoreanbarbeque.Models.Mahasiswa;
import com.atmakoreanbarbeque.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;


public class ViewsMahasiswa extends Fragment {
    private RecyclerView recyclerView;
    private AdapterMahasiswa adapter;
    private List<Mahasiswa> listMahasiswa;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_views_mahasiswa, container, false);

        setAdapter();
        getMahasiswa();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bar_mahasiswa, menu);
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
        menu.findItem(R.id.btnAdd).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnAdd) {
            Bundle data = new Bundle();
            data.putString("status", "tambah");
            TambahEditMahasiswa tambahEditMahasiswa = new TambahEditMahasiswa();
            tambahEditMahasiswa.setArguments(data);

            loadFragment(tambahEditMahasiswa);
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadDaftarMahasiswa(){
        setAdapter();
        getMahasiswa();
    }

    public void setAdapter(){
        getActivity().setTitle("Data Mahasiswa");
        listMahasiswa = new ArrayList<Mahasiswa>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdapterMahasiswa(view.getContext(), listMahasiswa, new AdapterMahasiswa.deleteItemListener() {
            @Override
            public void deleteItem(Boolean delete) {
                if(delete){
                    loadDaftarMahasiswa();
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view_mahasiswa,fragment)
                .addToBackStack(null)
                .commit();
    }

    //Fungsi menampilkan data mahasiswa
    public void getMahasiswa() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data mahasiswa");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MahasiswaAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("mahasiswa");

                    if(!listMahasiswa.isEmpty())
                        listMahasiswa.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        Integer id_menu          = jsonObject.optInt("id_menu");
                        String nama_kategori = jsonObject.optString("nama_kategori");
                        String nama_bahan = jsonObject.optString("nama_bahan");
                        String unit_menu = jsonObject.optString("unit_menu");
                        String nama_menu         = jsonObject.optString("nama_menu");
                        String deskripsi_menu        = jsonObject.optString("deskripsi_menu");
                        Double harga_menu            = jsonObject.optDouble("harga_menu");
                        String path         = jsonObject.optString("path");

                        //membuat objek buku
                        Mahasiswa mahasiswa =
                                new Mahasiswa(id_menu, nama_kategori, nama_bahan, nama_menu, deskripsi_menu, unit_menu, harga_menu, path);


                        //Menambahkan objek user tadi ke list user
                        listMahasiswa.add(mahasiswa);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}