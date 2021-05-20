package com.atmakoreanbarbeque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.atmakoreanbarbeque.Views.ScanQr;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.atmakoreanbarbeque.Views.ViewsBuku;
import com.atmakoreanbarbeque.Views.ViewsMahasiswa;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private NavigationView nv;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setAtribut();
    }

    private void init() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nv = (NavigationView) findViewById(R.id.NavigatioView);
        View view = nv.getHeaderView(0);

        ImageView gambar = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(this)
                .load("https://i.ytimg.com/vi/FMyEkfkYZ1U/maxresdefault.jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(gambar);
    }

    private void setAtribut() {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                closeDrawer();
                switch (id)
                {
//                    case R.id.mahasiswa :
//                        loadFragment(new ViewsMahasiswa());
//                        break;
                    case R.id.buku :
                        loadFragment(new ViewsBuku());
                        break;
                    case R.id.scanqr :
                        startActivity(new Intent(MainActivity.this, ScanQr.class));
                        break;
//                    case R.id.transaksi :
//                        loadFragment(new ViewsCart());
//                        break;
                    case R.id.keluar :
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Anda yakin ingin keluar ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    default :
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToogle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.mainFragment,fragment)
                .commit();
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() { }
}