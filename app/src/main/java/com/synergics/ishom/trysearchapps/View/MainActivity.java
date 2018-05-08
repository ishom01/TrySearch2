package com.synergics.ishom.trysearchapps.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;
import com.synergics.ishom.trysearchapps.Model.ResultBarangObj;
import com.synergics.ishom.trysearchapps.Model.ResultQueryObj;
import com.synergics.ishom.trysearchapps.R;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //recycleview
    private SlimAdapter slimAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager grid;
    private ArrayList<Object> items = new ArrayList<>();

    private MaterialSearchView searchView;
    private TextView toolbarTitle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle);

        //setting grid layout
        grid = new GridLayoutManager(getApplicationContext(), 2);
        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return slimAdapter.getItem(position) instanceof ResultBarangObj ? 1 : 2;
            }
        });
        recyclerView.setLayoutManager(grid);

        slimAdapter = SlimAdapter.create()
                .register(R.layout.item_text, new SlimInjector<ResultQueryObj>() {
                    @Override
                    public void onInject(final ResultQueryObj data, IViewInjector injector) {
                        injector.with(R.id.item, new IViewInjector.Action() {
                            @Override
                            public void action(View view) {

                                TextView text = view.findViewById(R.id.text);
                                text.setText(data.getName());

                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        searchView.setQuery(data.getName(), true);
                                    }
                                });

                            }
                        });
                    }
                })
                .register(R.layout.item_barang, new SlimInjector<ResultBarangObj>() {
                    @Override
                    public void onInject(final ResultBarangObj data, IViewInjector injector) {
                        injector.with(R.id.item, new IViewInjector.Action() {
                            @Override
                            public void action(View view) {

                                ImageView image = view.findViewById(R.id.image);
                                Picasso.with(view.getContext())
                                        .load(data.getImage())
                                        .into(image);

                                TextView namaBarang = view.findViewById(R.id.namBarang);
                                namaBarang.setText(data.getName());

                                TextView hargaBarang = view.findViewById(R.id.hargaBarang);
                                hargaBarang.setText(data.getName());

                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(MainActivity.this, data.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                })
                .attachTo(recyclerView);

        setToolbar();


    }

    private void setToolbar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarTitle = findViewById(R.id.toolbarTittle);

        searchView = (MaterialSearchView) findViewById(R.id.searchView);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                toolbarTitle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                toolbarTitle.setVisibility(View.VISIBLE);

                slimAdapter.updateData(items);
                slimAdapter.notifyDataSetChanged();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //menampilkan barang hasil pencarian dan di post ke api
                displayBarang(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()){
                    //menampilkan keyword ketika diketik dan di post ke api
                    displayKeyword(newText);

                }else {
                    //menampilkan top search keyword dan di post ke api
                    displayTopKeyword(newText);
                }
                return true;
            }
        });
    }

    //display keyword berdasarkan yang diketik
    private void displayKeyword(String newText) {

        //tempat yand digunakan untuk menyimpan keyword
        ArrayList<Object> objResult = new ArrayList<>();

    }

    //display top keyword
    private void displayTopKeyword(String newText) {

        //tempat yang digunakan untuk menyimpan top keyword
        ArrayList<Object> topSearch = new ArrayList<>();


    }

    //display barang berdasarkan query
    private void displayBarang(String query) {

        //tempat yang digunakan unutk menyimpan barang hasil pencarian
        ArrayList<Object> newBarang = new ArrayList<>();

    }
}
