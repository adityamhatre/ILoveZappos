package com.thelegacycoder.ILoveZappos.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thelegacycoder.ILoveZappos.Adapters.ListViewAdapter;
import com.thelegacycoder.ILoveZappos.AppController.AppController;
import com.thelegacycoder.ILoveZappos.Interfaces.ZapposAPI;
import com.thelegacycoder.ILoveZappos.Models.SearchResponse;
import com.thelegacycoder.ILoveZappos.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<SearchResponse> {
    private ListView listView;
    private ZapposAPI zapposAPI;
    private ListViewAdapter listViewAdapter;
    private Call<SearchResponse> call;
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initAPI();

        Uri data = getIntent().getData();
        if (data != null) {
            String url = data.toString();
            url = url.replace("http://www.zappos.com/zappos/", "");
            System.out.println(url);
            search(url);
        }
    }

    private void initAPI() {
        zapposAPI = AppController.getInstance().getRetrofit().create(ZapposAPI.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Cart").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                return false;
            }
        }).setIcon(android.R.drawable.btn_star).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
       /* menu.add("Search").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MainActivity.this, ProductViewActivity.class));
                return false;
            }
        }).setIcon(android.R.drawable.ic_menu_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/
        return super.onCreateOptionsMenu(menu);
    }

    private void search(String s) {
        call = zapposAPI.searchQuery(s, getString(R.string.zappos_key));
        AppController.getInstance().showLoading(this);
        call.enqueue(this);
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        searchBox = (EditText) findViewById(R.id.search_box);

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_SEARCH) {
                    search(searchBox.getText().toString());
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this, ProductViewActivity.class).putExtra("productIndex", i));
            }
        });

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(searchBox.getText().toString());
            }
        });
    }

    @Override
    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
        listViewAdapter = new ListViewAdapter(this, response.body().getResults());
        AppController.getInstance().setProducts(response.body().getResults());
        if (response.body().getTotalResultCount() == 0) {
            Toast.makeText(this, "No results", Toast.LENGTH_SHORT).show();
        } else {
            listView.setAdapter(listViewAdapter);
        }
        AppController.getInstance().dismissLoading();
    }

    @Override
    public void onFailure(Call<SearchResponse> call, Throwable t) {
        AppController.getInstance().dismissLoading();
        Toast.makeText(this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
    }
}
