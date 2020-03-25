package com.example.covid_19.ui.country;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19.R;

import java.util.List;

public class CovidCountryAdapter extends RecyclerView.Adapter<CovidCountryAdapter.CovidViewHolder> {


    private List<CovidCountry> covidCountries;
    private Context context;

    public CovidCountryAdapter(Context context, List covidCountries) {
        this.context = context;
        this.covidCountries = covidCountries;
    }

    @NonNull
    @Override
    public CovidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_country_card, parent, false);
        return new CovidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidCountryAdapter.CovidViewHolder holder, int position) {

        CovidCountry covidCountry = covidCountries.get(position);
        holder.tvCountryName.setText(covidCountry.getmCovidCountry());
        holder.tvTotalCases.setText(covidCountry.getmCases());
        holder.tvTodayCases.setText(covidCountry.getmTodayCases());
        holder.tvTotalDeaths.setText(covidCountry.getmDeaths());
        holder.tvTodayDeaths.setText(covidCountry.getmTodayDeaths());
        holder.tvTotalCritical.setText(covidCountry.getmCritical());
        holder.tvTotalRecovered.setText(covidCountry.getmRecovered());

    }

    @Override
    public int getItemCount() {
        return covidCountries.size();
    }

    public static class CovidViewHolder extends RecyclerView.ViewHolder {

        TextView tvCountryName, tvTotalCases, tvTodayCases, tvTotalDeaths, tvTodayDeaths, tvTotalCritical, tvTotalRecovered;

        public CovidViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountryName = itemView.findViewById(R.id.countryName);
            tvTotalCases = itemView.findViewById(R.id.totalCases);
            tvTodayCases = itemView.findViewById(R.id.todayCases);
            tvTotalDeaths = itemView.findViewById(R.id.totalDeaths);
            tvTodayDeaths = itemView.findViewById(R.id.todayDeaths);
            tvTotalCritical = itemView.findViewById(R.id.totalCritical);
            tvTotalRecovered = itemView.findViewById(R.id.totalRecovered);

        }
    }
}
