package com.thelegacycoder.ILoveZappos.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
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
    private EditText searchBox;
    private View searchView;
    private Boolean firstTime = true;
    private List<ProductItem> productItemList;
    private View listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initActionListeners();
        initAPI();

        Uri data = getIntent().getData();
        if (data != null) {
            String url = data.toString();
            url = url.replace("http://www.zappos.com/zappos/", "");
            System.out.println(url);
            searchProduct(url);
        }


        if (savedInstanceState != null && savedInstanceState.containsKey("search")) {
            searchProduct(savedInstanceState.getString("search"));
        }
    }

    private void initActionListeners() {
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_SEARCH) {
                    searchProduct(searchBox.getText().toString());
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
                }


                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this, ProductViewActivity.class).putExtra("productIndex", i));
            }
        });

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(searchBox.getText().toString());
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    private void initAPI() {
        zapposAPI = AppController.getInstance().getRetrofit().create(ZapposAPI.class);
    }


    private void searchProduct(String searchTerm) {
        if (!searchTerm.isEmpty()) {
            call = zapposAPI.searchQuery(searchTerm, getString(R.string.zappos_key));
            AppController.getInstance().showLoading(this);
            call.enqueue(this);
        }
    }

    private void initViews() {
        gridView = (GridView) findViewById(R.id.grid_view);
        searchBox = (EditText) findViewById(R.id.search_box);
        searchView = findViewById(R.id.search_view);
        listContainer = findViewById(R.id.list_container);
        hideContainer();
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
        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
            searchView.setVisibility(View.VISIBLE);
            if (productItemList != null)
                productItemList.clear();
            if (listViewAdapter != null) {
                listViewAdapter.notifyDataSetChanged();
            }

            hideContainer();
        }

        AppController.getInstance().dismissLoading();
    }

    private void hideContainer() {
        listContainer.setVisibility(View.GONE);
    }

    private void showContainer() {
        listContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(Call<SearchAPIResponse> call, Throwable t) {
        AppController.getInstance().dismissLoading();
        Toast.makeText(this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("search", searchBox.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
