package com.example.soccerleague;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.soccerleague.API.Example;
import com.example.soccerleague.API.GetData;
import com.example.soccerleague.API.RetrofitClient;
import com.example.soccerleague.API.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    static ListView listView_teams;
    static Button button_drawFixture,button_getTeams;
    static List<String> list_teams;
    static ArrayList<String> list_fixture;
    static GetData getData;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    static Spinner spinner_countries;
    boolean isDarkModeOn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setExitTransition(new Explode());


        getData = RetrofitClient.getRetrofitInstance().create(GetData.class);
        list_fixture=new ArrayList<>();
        list_teams=new ArrayList<>();
        listView_teams=findViewById(R.id.listView_teams);
        button_drawFixture=findViewById(R.id.button_drawFixture);
        button_getTeams=findViewById(R.id.button_getTeams);
        button_drawFixture.setOnClickListener(this);
        button_getTeams.setOnClickListener(this);
        listView_teams.setAdapter(new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,list_teams));
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId==button_drawFixture.getId()){
            drawFixture();
            Intent intent=new Intent(MainActivity.this,MainActivity2.class);
            intent.putStringArrayListExtra("fixture",list_fixture);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //start activity with animation
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,button_drawFixture, "robot");
                startActivity(intent, options.toBundle());
            }
            else {//start activity without animation
                startActivity(intent);
            }
        }
        else if(viewId==button_getTeams.getId()){
            getTeams();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        isDarkModeOn=sharedPreferences.getBoolean("isDarkModeOn",false);
        switch (item.getItemId()) {
            case R.id.action_darkMode:
                if (isDarkModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();
                }
            default:
                editor.commit();
                return super.onOptionsItemSelected(item);
        }
    }

    public void getTeams(){
        String status = getConnectivityStatusString(this);
        if(status==null) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
        else{
            if(list_teams.isEmpty()) {
                try {
                    Call<Example> call = getData.getTeams();
                    call.enqueue(new Callback<Example>() {
                        @Override
                        public void onResponse(Call<Example> call, Response<Example> response) {
                            if (response.body() != null) {
                                list_teams.clear();
                                List<Team> list_response = response.body().getTeams();
                                //int responseSize=list_response.size();
                                int responseSize = 16;//the app and algorithm works at any amount of team but i put here a limit because there are too many teams on the list that comes from web api, you can make comment this line and uncomment the line above to see the result for all
                                if (responseSize % 2 != 0) { //The size of team list must be even number, if it is not, we delete the last team to be able to generate a fixture
                                    responseSize--;
                                }
                                for (int i = 0; i < responseSize; i++) {
                                    list_teams.add(list_response.get(i).getName());
                                }
                                listView_teams.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, list_teams));
                                listView_teams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    }
                                });
                                button_drawFixture.setEnabled(true);
                            }
                            else {
                                if (response.code()==429){
                                    Toast.makeText(MainActivity.this, "Request limit for a minute reached, please wait a few seconds", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Response body is null; Response message:\n" + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Example> call, Throwable t) {
                            t.printStackTrace();
                            System.out.println(t.getMessage());
                            Toast.makeText(MainActivity.this, "onFailure; Response: \n" + t, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(this, "Error while fetching teams from web API\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    public void drawFixture(){
        Collections.shuffle(list_teams);
        list_fixture.clear();
        FixtureGenerator<String> fixtureGenerator = new FixtureGenerator();
        List<List<Fixture<String>>> rounds = fixtureGenerator.getFixtures(list_teams, true);
        for(int i=0; i<rounds.size(); i++){
            List<Fixture<String>> round = rounds.get(i);
            String s="";
            for(Fixture<String> fixture: round){
                s+=fixture.getHomeTeam() + " (VS) " + fixture.getAwayTeam()+"\n\n";
            }
            list_fixture.add(s);
        }
    }

    public static String getConnectivityStatusString(Context context) {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";
                return status;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
                return status;
            }
        }
        else {
            return status;
        }
        return status;
    }
}