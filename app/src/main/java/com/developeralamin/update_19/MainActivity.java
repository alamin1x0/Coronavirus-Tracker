package com.developeralamin.update_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CountryCodePicker countryCodePicker;
    TextView mtodaytotal,mtotal,mactive,mtodayactive,mdeath,mtodaydeath,mrecovered,mtodayrecoverd;

    private List<ModelClass> modelClassList;
    private List<ModelClass> modelClassList2;
    String country;
    TextView mfilter;
    String[] types={"cases","deaths","recovered","active"};
    Spinner spinner;

    PieChart mpiechart;
    private RecyclerView recyclerView;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        countryCodePicker=findViewById(R.id.ccp);
        mtodayactive=findViewById(R.id.todayactive);
        mtodaytotal=findViewById(R.id.todaytotal);
        mtodayrecoverd=findViewById(R.id.todayrecovered);
        mtodaydeath=findViewById(R.id.todaydeaths);
        mtotal=findViewById(R.id.totalcase);
        mrecovered=findViewById(R.id.recovered);
        mdeath=findViewById(R.id.deaths);
        mactive=findViewById(R.id.active);
        mpiechart=findViewById(R.id.piechart);
        modelClassList=new ArrayList<>();
        modelClassList2=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview);
        mfilter=findViewById(R.id.filter);
        spinner=findViewById(R.id.spinner);


        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,types);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setSelection(0, true);
        View v = spinner.getSelectedView();
        ((TextView)v).setTextColor(Color.parseColor("#2b2e4a"));

        ApiUtilities.getAPIInterface().getcountrydata().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {
                modelClassList2.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });
        // fetchData();

        adapter=new Adapter(getApplicationContext(),modelClassList2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);








        countryCodePicker.setAutoDetectedCountry(true);
        country=countryCodePicker.getSelectedCountryName();
        //    Toast.makeText(getApplicationContext(),country,Toast.LENGTH_SHORT).show();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(getApplicationContext(),countryCodePicker.getSelectedCountryName(),Toast.LENGTH_SHORT).show();
                country=countryCodePicker.getSelectedCountryName();
                fetchData();
            }
        });

        fetchData();








    }

    private void fetchData() {

        ApiUtilities.getAPIInterface().getcountrydata().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {
                modelClassList.addAll(response.body());
                for(int i=0;i<modelClassList.size();i++)
                {
                    if(modelClassList.get(i).getCountry().equals(country))
                    {

                        //    Toast.makeText(getApplicationContext(),"country is"+country,Toast.LENGTH_SHORT).show();

                        int death=Integer.parseInt(modelClassList.get(i).getDeaths());
                        int todaydeath=Integer.parseInt(modelClassList.get(i).getTodayDeaths());
                        int recovered=Integer.parseInt(modelClassList.get(i).getRecovered());
                        int todayrecovered=Integer.parseInt(modelClassList.get(i).getTodayRecovered());
                        int active=Integer.parseInt(modelClassList.get(i).getActive());
                        int total=Integer.parseInt(modelClassList.get(i).getCases());
                        int todaytotal=Integer.parseInt(modelClassList.get(i).getTodayCases());
                        //     Toast.makeText(getApplicationContext(),"re"+todayrecovered,Toast.LENGTH_SHORT).show();

                        mactive.setText(NumberFormat.getInstance().format(active));
                        mtodaydeath.setText("+"+NumberFormat.getInstance().format(todaydeath));
                        mtodayrecoverd.setText("+"+NumberFormat.getInstance().format(todayrecovered));
                        mtodaytotal.setText("+"+NumberFormat.getInstance().format(todaytotal));
                        mdeath.setText(NumberFormat.getInstance().format(death));
                        mrecovered.setText(NumberFormat.getInstance().format(recovered));
                        mtotal.setText(NumberFormat.getInstance().format(total));
                        mtodayactive.setText(null);


                        updategraph(total,active,recovered,death);




                    }
                }
            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void updategraph(int total, int active, int recovered, int death) {
        mpiechart.clearChart();

        mpiechart.addPieSlice(new PieModel("Confirm",total, Color.parseColor("#FFB701")));
        // Toast.makeText(getApplicationContext(),"Total is"+total,Toast.LENGTH_SHORT).show();
        mpiechart.addPieSlice(new PieModel("Active",active, Color.parseColor("#FF4CAF50")));
        mpiechart.addPieSlice(new PieModel("Recovered",recovered, Color.parseColor("#38ACDD")));
        mpiechart.addPieSlice(new PieModel("Death",death, Color.parseColor("#F55c47")));

        mpiechart.startAnimation();
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item=types[position];
        // Toast.makeText(getApplicationContext(),item,Toast.LENGTH_SHORT).show();
        mfilter.setText(item);
        adapter.filter(item);
        ((TextView) view).setTextColor(Color.parseColor("#2b2e4a"));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}