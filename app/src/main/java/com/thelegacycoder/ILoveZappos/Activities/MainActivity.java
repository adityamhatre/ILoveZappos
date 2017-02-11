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

import com.thelegacycoder.ILoveZappos.Adapters.ProductsAdapter;
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
    private ProductsAdapter productsAdapter;
    private Call<SearchAPIResponse> call;
    private View menuView;
    private Boolean firstTime = true;
    private List<ProductItem> productItemList;
    private View listContainer;
    private SearchView searchProductView;
    private String searchQuery;
    private boolean sharing;
    private boolean splash = true;
    private int index;

    private ImageView shoes, shorts, shirts, tee, blazer, jeans, pajama, jacket;
    private Menu menu;
    private boolean listShowing = false;

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
            if (listShowing = savedInstanceState.getBoolean("listShowing"))
                searchProduct(searchQuery = savedInstanceState.getString("search"));
        }
    }


    private void initViews() {
        gridView = (GridView) findViewById(R.id.grid_view);
        menuView = findViewById(R.id.menu_view);
        listContainer = findViewById(R.id.list_container);


        shoes = (ImageView) findViewById(R.id.shoes);
        shorts = (ImageView) findViewById(R.id.shorts);
        shirts = (ImageView) findViewById(R.id.shirt);
        tee = (ImageView) findViewById(R.id.tee);
        blazer = (ImageView) findViewById(R.id.blazer);
        jeans = (ImageView) findViewById(R.id.jeans);
        pajama = (ImageView) findViewById(R.id.pajama);
        jacket = (ImageView) findViewById(R.id.jacket);

        searchProductView = new SearchView(this);
        searchProductView.setSubmitButtonEnabled(true);
        searchProductView.setQueryHint("shoes, winter wear,  etc");

        ((EditText) searchProductView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ((EditText) searchProductView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.LTGRAY);
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


        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(searchQuery = "shoes");
            }
        });
        shorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(searchQuery = "shorts");
            }
        });
        shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(searchQuery = "shirts");
            }
        });
        tee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(searchQuery = "tee");
            }
        });
        blazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(searchQuery = "blazer");
            }
        });
        jeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(searchQuery = "jeans");
            }
        });
        pajama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(searchQuery = "pajama");
            }
        });
        jacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(searchQuery = "jacket");
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
        splash = false;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            call = zapposAPI.searchQuery(searchTerm, getString(R.string.zappos_key));
            AppController.getInstance().showLoading(this);
            call.enqueue(this);
        }
    }

    private void hideContainer() {
        listContainer.setVisibility(View.GONE);
        listShowing = false;
        menuView.setVisibility(View.VISIBLE);
    }

    private void showContainer() {
        listContainer.setVisibility(View.VISIBLE);
        listShowing = true;
        menuView.setVisibility(View.GONE);
    }

    private void hideKeyboard(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 212 && resultCode == 200) {
            onBackPressed();
        }

    }

    @Override
    public void onResponse(Call<SearchAPIResponse> call, Response<SearchAPIResponse> response) {
        menuView.setVisibility(View.GONE);
        AppController.getInstance().setProducts(response.body().getResults());

        if (response.body().getResults().size() != 0) {
            if (firstTime) {
                productItemList = response.body().getResults();
                productsAdapter = new ProductsAdapter(this, productItemList);
                gridView.setAdapter(productsAdapter);
                firstTime = false;
            } else {
                productItemList.clear();
                productItemList.addAll(response.body().getResults());
                productsAdapter.notifyDataSetChanged();
            }

            showContainer();
            if (sharing) {
                gridView.performItemClick(gridView.getChildAt(index), index, gridView.getAdapter().getItemId(index));
            }
        } else {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
            if (productItemList != null)
                productItemList.clear();
            if (productsAdapter != null) {
                productsAdapter.notifyDataSetChanged();
            }

            hideContainer();
        }

        AppController.getInstance().dismissLoading();
    }

    @Override
    public void onBackPressed() {
        if (!splash) {
            splash = true;
            hideContainer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFailure(Call<SearchAPIResponse> call, Throwable t) {
        AppController.getInstance().dismissLoading();
        Toast.makeText(this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("Cart").setIcon(AppController.getInstance().getProductsInCart() != null && AppController.getInstance().getProductsInCart().size() != 0 ? R.drawable.ic_cart_full : R.drawable.ic_cart).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (AppController.getInstance().getProductsInCart().size() != 0) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "No items in cart", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add("Search").setActionView(searchProductView).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        if (menu != null && menu.getItem(0) != null) {
            if (AppController.getInstance().getProductsInCart().size() != 0)
                menu.getItem(0).setIcon(R.drawable.ic_cart_full);
            else menu.getItem(0).setIcon(R.drawable.ic_cart);
        }
        super.onResume();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("search", searchQuery);
        outState.putBoolean("listShowing", listShowing);
        super.onSaveInstanceState(outState);
    }
}
