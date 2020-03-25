package com.example.covid_19.ui.country;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryFragment extends Fragment {

    private static final String TAG = CountryFragment.class.getSimpleName();
    private RecyclerView rvCovidCountry;
    private ProgressBar progressBar;
    private TextView totalTerritory;
    private List<CovidCountry> covidCountries;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_country, container, false);


        //call view
        totalTerritory = root.findViewById(R.id.total_territory);
        rvCovidCountry = root.findViewById(R.id.rvCountry);
        progressBar = root.findViewById(R.id.progress_circular_home);
        rvCovidCountry.setLayoutManager(new LinearLayoutManager(getActivity()));

        //call volley method
        getDataFromServer();
        return root;
    }

    private void showRecyclerView() {
        CovidCountryAdapter adapter = new CovidCountryAdapter(getContext(), covidCountries);
        rvCovidCountry.setAdapter(adapter);
    }

    private void getDataFromServer() {

        String url = "https://corona.lmao.ninja/countries";

        covidCountries = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    Log.e(TAG, "Got :: onResponse: " + response);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        totalTerritory.setText(jsonArray.length() + " Territories");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.e(TAG, "Getting ARRAY  ready");
                            JSONObject data = jsonArray.getJSONObject(i);
                            CovidCountry cC = new CovidCountry();
                            cC.setmCovidCountry(data.getString("country"));
                            cC.setmCases(data.getString("cases"));
                            cC.setmTodayCases(data.getString("todayCases"));
                            cC.setmDeaths(data.getString("deaths"));
                            cC.setmTodayDeaths(data.getString("todayDeaths"));
                            cC.setmCritical(data.getString("critical"));
                            cC.setmRecovered(data.getString("recovered"));
                            covidCountries.add(cC);
                        }
                    } catch (JSONException e) {
                        e.getStackTrace();
                    }
                    showRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onResponse : " + error);
            }
        }
        );
        final Request<?> request = stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
