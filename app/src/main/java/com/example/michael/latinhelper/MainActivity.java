package com.example.michael.latinhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.michael.latinhelper.Exceptions.DifferentLenthOfStringArraysException;
import com.example.michael.latinhelper.Sugar.Phrase;
import com.honu.aloha.WelcomeHelper;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    static SharedPreferences prefs = null;
    static final String LOG_TAG = "MY_LOG_TAG";
    RecyclerView recyclerView;
    SearchView searchView;
    Toolbar toolbar;
    Select<Phrase> data;
    RecyclerViewMyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.searchView);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        if (WelcomeHelper.isWelcomeRequired(this)) {                                     /** AlohaView appears only on first start*/
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        if (prefs.getBoolean("firstrun", true)) {                       /** Initializing db on first start*/
            ArrayList<String> listUA = new ArrayList<>();
            Collections.addAll(listUA, getResources().getStringArray(R.array.ru));

            ArrayList<String> listLAT = new ArrayList<>();
            Collections.addAll(listLAT, getResources().getStringArray(R.array.lat));

            if (listUA.size() != listLAT.size()) {
                throw new DifferentLenthOfStringArraysException();
            }

            for (int index = 0; index < listLAT.size(); index++) {
                Phrase phrase = new Phrase(listUA.get(index), listLAT.get(index));
                phrase.save();
            }
            prefs.edit().putBoolean("firstrun", false).apply(); /** set firstrun false -> if-block won't be run anymore */

        }


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        data = Select.from(Phrase.class);/** getting data from db*/

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        adapter = new RecyclerViewMyAdapter(data.orderBy("latstring").list()); /** creating adapter with sorted data by Latin ABC*/
        recyclerView.setAdapter(adapter);


        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * depending on what button was pressed
         * recyclerView gets a new Adapter with sorted data in a new way
         */
        int id = item.getItemId();

        if (id == R.id.sortByUa) {
            recyclerView.setAdapter(new RecyclerViewMyAdapter(Select.from(Phrase.class).orderBy("uastring").list()));
            return true;
        }
        if (id == R.id.sortByLatin) {
            recyclerView.setAdapter(new RecyclerViewMyAdapter(Select.from(Phrase.class).orderBy("latstring").list()));
            return true;
        }
        if (id == R.id.sortByID) {
            recyclerView.setAdapter(new RecyclerViewMyAdapter(Select.from(Phrase.class).orderBy("id").list()));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Phrase> newData = filter(data.orderBy("latstring").list(), newText); /** get list with needed data */
        adapter.animateTo(newData);  /** say adapter to display filtered data*/
        recyclerView.scrollToPosition(0);
        return true;
    }

    private List<Phrase> filter(List<Phrase> phrases, String query) {
        /**
         * convert query to lower case;
         * create empty list;
         * run through all data. compete query with both strings(ua,latin);
         * if it matches i add it to filteredList
         *
         */
        query = query.toLowerCase();
        final List<Phrase> filteredPhrasesList = new ArrayList<>();
        for (Phrase model : phrases) {
            final String latStr = model.getLatString().toLowerCase();
            final String uaStr = model.getUaString().toLowerCase();
            if (uaStr.contains(query)) {
                filteredPhrasesList.add(model);
            } else if (latStr.contains(query)) {
                filteredPhrasesList.add(model);
            }
        }
        return filteredPhrasesList;

    }


}
