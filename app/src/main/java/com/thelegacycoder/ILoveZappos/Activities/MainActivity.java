package com.thelegacycoder.ILoveZappos.Activities;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.thelegacycoder.ILoveZappos.Adapters.ListViewAdapter;
import com.thelegacycoder.ILoveZappos.Interfaces.ZapposAPI;
import com.thelegacycoder.ILoveZappos.Models.SearchResponse;
import com.thelegacycoder.ILoveZappos.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<SearchResponse> {
    public static final String BASE_URL = "https://api.zappos.com/";
    ListView listView;
    ZapposAPI zapposAPI;
    AlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        zapposAPI = retrofit.create(ZapposAPI.class);

        Uri data = getIntent().getData();
        if (data != null) {
            String url = data.toString();
            url=url.replace("http://www.zappos.com/zappos/", "");
            System.out.println(url);
            search(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Search").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                final EditText editText = new EditText(MainActivity.this);
                final LinearLayout linearLayout = new LinearLayout(MainActivity.this);

                linearLayout.addView(editText);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setView(linearLayout);
                builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        search(editText.getText().toString());
                        alertDialog.dismiss();
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();

                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void search(String s) {

        Call<SearchResponse> call = zapposAPI.searchQuery(s, getString(R.string.zappos_key));
        //asynchronous call
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
        ListViewAdapter listViewAdapter = new ListViewAdapter(this, response.body().getResults());
        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(listViewAdapter);
    }

    @Override
    public void onFailure(Call<SearchResponse> call, Throwable t) {

    }
}
