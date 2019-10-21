package com.bombadu.aprikot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsProActivity  extends AppCompatActivity implements NewsAdapter.ItemClickCallback {
    private RecyclerView recyclerView;
    private OkHttpClient client = new OkHttpClient();
    private JsonObject jsonObject;
    private String source = "";
    private ArrayList<String> titleArrayList;
    private ArrayList<String> descriptionArrayList;
    private ArrayList<String> authorArrayList;
    private ArrayList<String> imageUrlArrayList;
    private ArrayList<String> spinnerArrayList;
    private ArrayList<String> webUrlArrayList;
    private ArrayList listData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_pro_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        recyclerView = findViewById(R.id.recyclerview);

        populateSpinnerList();


        source = "ars-technica";

        //fetchData();


    }

    private void populateSpinnerList() {
        spinnerArrayList = new ArrayList<>();
        spinnerArrayList.add("ars-technica");
        spinnerArrayList.add("engadget");
        spinnerArrayList.add("recode");
        spinnerArrayList.add("the-next-web");
        spinnerArrayList.add("the-verge");
        spinnerArrayList.add("the-wall-street-journal");
    }

    private void fetchData() {

        titleArrayList = new ArrayList<>();
        descriptionArrayList = new ArrayList<>();
        authorArrayList = new ArrayList<>();
        imageUrlArrayList = new ArrayList<>();
        webUrlArrayList = new ArrayList<>();



        String url = "https://newsapi.org/v1/articles?source="+ source + "&apiKey=6b9392d84b4b4f2983e2b3c9d8f2c090";
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //Do Nothing
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String myResponse = response.body().string();
                Log.d("REPONSE", myResponse);
                try {
                    JSONObject jsonObject = new JSONObject(myResponse);
                    JSONArray articlesJA = jsonObject.getJSONArray("articles");
                    for (int i = 0; i < articlesJA.length(); i++) {
                        JSONObject jsonIndex = articlesJA.getJSONObject(i);
                        String title = jsonIndex.getString("title");
                        String description = jsonIndex.getString("description");
                        String imageUrls = jsonIndex.getString("urlToImage");
                        String webUrl = jsonIndex.getString("url");
                        String author = jsonIndex.getString("author");
                        if(author.equals("null")) {
                            author = "by Anonymous";
                        }  else {
                            author = "by " + author;
                        }

                        titleArrayList.add(title);
                        descriptionArrayList.add(description);
                        imageUrlArrayList.add(imageUrls);
                        authorArrayList.add(author);
                        webUrlArrayList.add(webUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Similar to Post Execute
                if (response.isSuccessful())  {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           listData = (ArrayList)getListData();
                           recyclerView.setLayoutManager(new LinearLayoutManager(NewsProActivity.this));
                           NewsAdapter adapter = new NewsAdapter(getListData(), NewsProActivity.this);
                           recyclerView.setAdapter(adapter);
                           adapter.setItemClickCallback(NewsProActivity.this);
                        }
                    });
                }
            }
        });


    }

    private List<ListItem> getListData() {
        List<ListItem> data = new ArrayList<>();
        for (int i = 0 ; i <titleArrayList.size() && i < descriptionArrayList.size() && i < imageUrlArrayList.size() && i < authorArrayList.size() && i < webUrlArrayList.size(); i++){
            ListItem item = new ListItem();
            item.setAuthorText(authorArrayList.get(i));
            item.setTitleText(titleArrayList.get(i));
            item.setDescriptionText(descriptionArrayList.get(i));
            item.setImageUrlText(imageUrlArrayList.get(i));
            item.setWebUrlText(webUrlArrayList.get(i));
            data.add(item);
        }

        return data;

    }

    @Override
    public void onItemClick(int p) {
        ListItem item = (ListItem) listData.get(p);
        String url = item.getWebUrlText();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.list_item_spinner, R.id.list_item_item, spinnerArrayList);
        getMenuInflater().inflate(R.menu.news_pro_menu, menu);
        MenuItem item = menu.findItem(R.id.npSpinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                source = (String) spinner.getItemAtPosition(position);
                fetchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
