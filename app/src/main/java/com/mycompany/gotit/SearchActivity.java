package com.mycompany.gotit;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import static com.mycompany.gotit.MainActivity.QUERY_KEY;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.activity_search);

        String query = getIntent().getStringExtra(QUERY_KEY);

        FragmentManager fm = getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.search_toolBar);
        toolbar.setTitle(R.string.search_activity_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fm.beginTransaction().add(R.id.activity_search_fragment_container, BookListFragment.newInstance(-1, query)).commit();
    }
}
