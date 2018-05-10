package com.synergics.ishom.trysearchapps.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;
import com.synergics.ishom.trysearchapps.Controller.ApiConfig.ApiClient;
import com.synergics.ishom.trysearchapps.Controller.ApiConfig.ApiInterface;
import com.synergics.ishom.trysearchapps.Model.ResultBarangObj;
import com.synergics.ishom.trysearchapps.Model.ResultTitleObj;
import com.synergics.ishom.trysearchapps.Model.RetrofitResponse.ResponseCategory;
import com.synergics.ishom.trysearchapps.R;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //recycleview
    private SlimAdapter slimAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager grid;
    private ArrayList<Object> items = new ArrayList<>();

    private MaterialSearchView searchView;
    private TextView toolbarTitle;
    private Toolbar toolbar;

    private ArrayList<ResponseCategory.Category> categorys = new ArrayList<>();
    private ArrayList<ResponseCategory.Category> childCategory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycle);

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
                .register(R.layout.item_text, new SlimInjector<ResponseCategory.Category>() {
                    @Override
                    public void onInject(final ResponseCategory.Category data, IViewInjector injector) {
                        injector.with(R.id.item, new IViewInjector.Action() {
                            @Override
                            public void action(View view) {

                                TextView text = view.findViewById(R.id.text);
                                text.setText(data.name);

                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        searchView.setQuery(data.name, true);
                                    }
                                });

                            }
                        });
                    }
                })
                .register(R.layout.item_title, new SlimInjector<ResultTitleObj>() {
                    @Override
                    public void onInject(final ResultTitleObj data, IViewInjector injector) {
                        injector.with(R.id.item, new IViewInjector.Action() {
                            @Override
                            public void action(View view) {

                                TextView text = view.findViewById(R.id.text);
                                text.setText(data.getName());

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

                                TextView namaBarang = view.findViewById(R.id.namaBarang);
                                namaBarang.setText(data.getName());

                                TextView hargaBarang = view.findViewById(R.id.hargaBarang);
                                hargaBarang.setText("Rp. " + money(data.getHarga()));

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

        fetchData();

    }

    private void category(){

        ApiInterface apiInterface = ApiClient.EnglishApi().create(ApiInterface.class);
        Call call = apiInterface.getCategory();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response.isSuccessful()){
                    ResponseCategory object = (ResponseCategory) response.body();

                    if (object.categories.size() != 0){

                        for (ResponseCategory.Category category : object.categories){

                            categorys.add(category);
                            if (category.children.size() != 0){

                                for (ResponseCategory.Category catChild1 : category.children){

                                    childCategory.add(catChild1);
                                    if (catChild1.children.size() != 0){

                                        for (ResponseCategory.Category catChild2 : catChild1.children){

                                            childCategory.add(catChild2);

                                        }

                                    }

                                }

                            }

                        }

                    }else {
                        Toast.makeText(MainActivity.this, "Category not found", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Failed to access server", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    private void fetchData() {

        items.add(new ResultBarangObj(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 1", 10000));
        items.add(new ResultBarangObj(2, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 2", 10000));
        items.add(new ResultBarangObj(3, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 3", 10000));
        items.add(new ResultBarangObj(4, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 1", 10000));
        items.add(new ResultBarangObj(5, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 2", 10000));
        items.add(new ResultBarangObj(6, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 3", 10000));items.add(new ResultBarangObj(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 1", 10000));
        items.add(new ResultBarangObj(7, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 2", 10000));
        items.add(new ResultBarangObj(8, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 3", 10000));items.add(new ResultBarangObj(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 1", 10000));
        items.add(new ResultBarangObj(9, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 2", 10000));
        items.add(new ResultBarangObj(10, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 3", 10000));items.add(new ResultBarangObj(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 1", 10000));
        items.add(new ResultBarangObj(11, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 2", 10000));
        items.add(new ResultBarangObj(12, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", "Barang 3", 10000));

        slimAdapter.updateData(items);
        slimAdapter.notifyDataSetChanged();

    }

    private void setToolbar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        toolbarTitle = (TextView) findViewById(R.id.toolbarTittle);

        searchView = (MaterialSearchView) findViewById(R.id.searchView);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //menampilkan top keyword get dari api
                toolbarTitle.setVisibility(View.INVISIBLE);

                displayTopKeyword();
            }

            @Override
            public void onSearchViewClosed() {
                //menmapilkan data sebelumnya ketika di back
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
                    displayTopKeyword();
                }
                return true;
            }
        });
    }

    //display keyword berdasarkan yang diketik
    private void displayKeyword(String newText) {

        //tempat yand digunakan untuk menyimpan keyword
        ArrayList<Object> objResult = new ArrayList<>();

        objResult.add(new ResultTitleObj(1, "Kategori"));

        for (int i = 0; i < categorys.size(); i++) {
            if (categorys.get(i).name.contains(newText)){
                objResult.add(categorys.get(i));
            }
        }

        objResult.add(new ResultTitleObj(1, "Result"));

        for (int i = 0; i < childCategory.size(); i++) {
            if (childCategory.get(i).name.contains(newText)){
                objResult.add(childCategory.get(i));
            }
        }

        slimAdapter.updateData(objResult);
        slimAdapter.notifyDataSetChanged();

    }

    //display top keyword
    private void displayTopKeyword() {

        //tempat yang digunakan untuk menyimpan top keyword
        ArrayList<Object> topSearch = new ArrayList<>();

        topSearch.add(new ResultTitleObj(1, "Kategori"));

        for (int i = 0; i < categorys.size(); i++) {
            topSearch.add(categorys.get(i));
        }

//        topSearch.add(new ResultQueryObj(1, "Top 1"));
//        topSearch.add(new ResultQueryObj(1, "Top 2"));
//        topSearch.add(new ResultQueryObj(1, "Top 3"));
//        topSearch.add(new ResultQueryObj(1, "Top 4"));
//        topSearch.add(new ResultQueryObj(1, "Top 5"));
//        topSearch.add(new ResultQueryObj(1, "Top 6"));
//        topSearch.add(new ResultQueryObj(1, "Top 7"));
//        topSearch.add(new ResultQueryObj(1, "Top 8"));
//        topSearch.add(new ResultQueryObj(1, "Top 9"));

        slimAdapter.updateData(topSearch);
        slimAdapter.notifyDataSetChanged();

    }

    //display barang berdasarkan query
    private void displayBarang(String query) {

        //tempat yang digunakan unutk menyimpan barang hasil pencarian
        ArrayList<Object> newBarang = new ArrayList<>();

        newBarang.add(new ResultBarangObj(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 1", 10000));
        newBarang.add(new ResultBarangObj(2, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 2", 10000));
        newBarang.add(new ResultBarangObj(3, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 3", 10000));
        newBarang.add(new ResultBarangObj(4, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 1", 10000));
        newBarang.add(new ResultBarangObj(5, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 2", 10000));
        newBarang.add(new ResultBarangObj(6, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 3", 10000));
        newBarang.add(new ResultBarangObj(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 1", 10000));
        newBarang.add(new ResultBarangObj(7, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 2", 10000));
        newBarang.add(new ResultBarangObj(8, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 3", 10000));
        newBarang.add(new ResultBarangObj(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 1", 10000));
        newBarang.add(new ResultBarangObj(9, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 2", 10000));
        newBarang.add(new ResultBarangObj(10, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 3", 10000));
        newBarang.add(new ResultBarangObj(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 1", 10000));
        newBarang.add(new ResultBarangObj(11, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 2", 10000));
        newBarang.add(new ResultBarangObj(12, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfSNIZg2a79s1bttC1WH_86kf0zHzR5qdb_jIcUI1wWbRQpv0d", query + "Barang 3", 10000));

        slimAdapter.updateData(newBarang);
        slimAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.actionSerch);
        searchView.setMenuItem(item);
        return true;
    }


    private String money(int d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.UK);;
        formatter .applyPattern("#,###");
        return formatter.format(d);
    }
}
