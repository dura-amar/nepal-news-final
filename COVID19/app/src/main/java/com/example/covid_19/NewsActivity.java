package com.example.covid_19;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19.ui.news.Adapter;
import com.example.covid_19.ui.news.Utils;
import com.example.covid_19.ui.news.api.ApiClient;
import com.example.covid_19.ui.news.api.ApiInterface;
import com.example.covid_19.ui.news.models.Article;
import com.example.covid_19.ui.news.models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {


    private static final String API_KEY = "a2aad3f4a7f4482a8f592ad9718ee8be";

    private RecyclerView newsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = NewsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        newsRecyclerView.setNestedScrollingEnabled(false);

        Log.e(TAG, "Inside NewsFragment, onCreateView,  Ready to load JSON");
        loadJson("");
    }

    private void loadJson(final String keyword) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();
        String language = Utils.getLanguage();

        Call<News> call;
        if (keyword.length() > 0) {
            call = apiInterface.getNewsSearch(keyword, language, "publishedAT", API_KEY);
        } else {
            call = apiInterface.getNews(country, API_KEY);

        }
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (!articles.isEmpty()) {
                        articles.clear();
                    }
                    articles = response.body().getArticles();
                    adapter = new Adapter(articles, getApplicationContext());
                    newsRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), "No Result!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    loadJson(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadJson(newText);
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
        return true;

    }

}
