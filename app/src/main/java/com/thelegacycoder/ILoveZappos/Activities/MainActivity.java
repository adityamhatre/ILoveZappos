package com.thelegacycoder.ILoveZappos.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.thelegacycoder.ILoveZappos.Adapters.ListViewAdapter;
import com.thelegacycoder.ILoveZappos.AppController.AppController;
import com.thelegacycoder.ILoveZappos.Interfaces.ZapposAPI;
import com.thelegacycoder.ILoveZappos.Models.ProductItem;
import com.thelegacycoder.ILoveZappos.Models.SearchAPIResponse;
import com.thelegacycoder.ILoveZappos.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<SearchAPIResponse> {
    private GridView gridView;
    private ZapposAPI zapposAPI;
    private ListViewAdapter listViewAdapter;
    private Call<SearchAPIResponse> call;
    private View searchView;
    private Boolean firstTime = true;
    private List<ProductItem> productItemList;
    private View listContainer;
    private SearchView searchProductView;
    private String searchQuery;
    private boolean sharing;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initActionListeners();
        initAPI();

        Uri data = getIntent().getData();
        if (data != null) {
            sharing = false;
            String url = data.toString();
            if (url.contains("share.zappos.com")) sharing = true;
            url = url.replace("http://share.zappos.com/zappos/", "");
            String searchQuery = url.substring(0, url.indexOf("&"));
            searchQuery = searchQuery.replace("search=", "");
            String index = url.substring(url.indexOf("&") + 1, url.length());
            index = index.replace("index=", "");
            try {
                if (sharing)
                    searchProduct(searchQuery);
                this.index = Integer.parseInt(index);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else sharing = false;


        if (savedInstanceState != null && savedInstanceState.containsKey("search")) {
            searchProduct(savedInstanceState.getString("search"));
        }
    }

    private void initViews() {
        gridView = (GridView) findViewById(R.id.grid_view);
        searchView = findViewById(R.id.search_view);
        listContainer = findViewById(R.id.list_container);

        searchProductView = new SearchView(this);
        searchProductView.setSubmitButtonEnabled(true);
        searchProductView.setQueryHint("Search");

        ((EditText) searchProductView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ((EditText) searchProductView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.GRAY);
        ImageView imageView = ((ImageView) searchProductView.findViewById(android.support.v7.appcompat.R.id.search_button));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.ic_search);

        hideContainer();
    }

    private void initAPI() {
        zapposAPI = AppController.getInstance().getRetrofit().create(ZapposAPI.class);
    }

    private void initActionListeners() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (sharing) {
                    startActivityForResult(new Intent(MainActivity.this, ProductViewActivity.class).putExtra("productIndex", i).putExtra("searchTerm", searchQuery).putExtra("sharing", sharing), 212);
                } else {
                    startActivity(new Intent(MainActivity.this, ProductViewActivity.class).putExtra("productIndex", i).putExtra("searchTerm", searchQuery).putExtra("sharing", sharing));
                }
                if (sharing) sharing = false;
            }
        });


        searchProductView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                searchProductView.clearFocus();
                hideKeyboard(searchProductView);
                searchProduct(query);
                searchProductView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void searchProduct(String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            call = zapposAPI.searchQuery(searchTerm, getString(R.string.zappos_key));
            AppController.getInstance().showLoading(this);
            call.enqueue(this);
        }
    }

    private void hideContainer() {
        listContainer.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
    }

    private void showContainer() {
        listContainer.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
    }

    private void hideKeyboard(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 212 && resultCode == 200) {
            hideContainer();
        }

    }

    @Override
    public void onResponse(Call<SearchAPIResponse> call, Response<SearchAPIResponse> response) {
        searchView.setVisibility(View.GONE);
        AppController.getInstance().setProducts(response.body().getResults());

        if (response.body().getResults().size() != 0) {
            if (firstTime) {
                productItemList = response.body().getResults();
                listViewAdapter = new ListViewAdapter(this, productItemList);
                gridView.setAdapter(listViewAdapter);
                firstTime = false;
            } else {
                productItemList.clear();
                productItemList.addAll(response.body().getResults());
                listViewAdapter.notifyDataSetChanged();
            }

            showContainer();
            if (sharing) {
                gridView.performItemClick(gridView.getChildAt(index), index, gridView.getAdapter().getItemId(index));
            }
        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
            if (productItemList != null)
                productItemList.clear();
            if (listViewAdapter != null) {
                listViewAdapter.notifyDataSetChanged();
            }

            hideContainer();
        }

        AppController.getInstance().dismissLoading();
    }

    @Override
    public void onFailure(Call<SearchAPIResponse> call, Throwable t) {
        AppController.getInstance().dismissLoading();
        Toast.makeText(this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Search").setActionView(searchProductView).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("search", searchQuery);
        super.onSaveInstanceState(outState);
    }
}
