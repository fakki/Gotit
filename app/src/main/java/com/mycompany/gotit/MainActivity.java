package com.mycompany.gotit;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int currFragmentId = 0;

    private BookFragment bookFragment;
    private MovieFragment movieFragment;
    private MusicFragment musicFragment;
    private MyFragment myFragment;

    public final static int CHANGE_COLUMN_REQUEST_CODE = 0;

    public static final String CURRENT_ID_KEY = "current_id_key";

    public static final String COLUMNS_KEY = "columns_key";

    public static final String QUERY_KEY = "query_key";

    private ArrayList<Fragment> fragments = new ArrayList<>(4);

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.main_activity_toolbar));

        final FragmentManager fm = getSupportFragmentManager();
        init(fm);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bot_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d(TAG, "into case");
                switch (menuItem.getItemId()){
                    case R.id.book_item:
                        Log.d(TAG, "book");
                        fm.beginTransaction().hide(fragments.get(currFragmentId)).show(bookFragment).commit();
                        currFragmentId = 0;
                        return true;

                    case R.id.movie_item:
                        Log.d(TAG, "movie");
                        fm.beginTransaction().hide(fragments.get(currFragmentId)).show(movieFragment).commit();
                        currFragmentId = 1;
                        return true;
                    case R.id.music_item:
                        Log.d(TAG, "music"+currFragmentId);
                        fm.beginTransaction().hide(fragments.get(currFragmentId)).show(musicFragment).commit();
                        currFragmentId = 2;
                        return true;
                    case R.id.my_item:
                        Log.d(TAG, "my");
                        fm.beginTransaction().hide(fragments.get(currFragmentId)).show(myFragment).commit();
                        currFragmentId = 3;
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_column:
                Intent intent = new Intent(this, ChangeColumnActivity.class);
                intent.putExtra(CURRENT_ID_KEY, currFragmentId);
                Log.d(TAG, "start activity.");
                startActivityForResult(intent, CHANGE_COLUMN_REQUEST_CODE);
                Log.d(TAG, "started activity.");
                return true;
            default:
                return false;
        }
    }

    private void updateColumns(ArrayList<String> columns){
        switch (currFragmentId){
            case 0:
                bookFragment.addColumns(columns);
                break;
            default:
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CHANGE_COLUMN_REQUEST_CODE) {
                Bundle bundle = data.getExtras();
                if(bundle != null){
                    Log.d(TAG, "update columns");
                    updateColumns(bundle.getStringArrayList(COLUMNS_KEY));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra(QUERY_KEY, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void init(FragmentManager fm){
        bookFragment = new BookFragment();
        movieFragment = new MovieFragment();
        musicFragment = new MusicFragment();
        myFragment = new MyFragment();
        fragments.add(bookFragment);
        fragments.add(movieFragment);
        fragments.add(musicFragment);
        fragments.add(myFragment);
        fm.beginTransaction().add(R.id.fragment_container, bookFragment).hide(bookFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, movieFragment).hide(movieFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, musicFragment).hide(musicFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, myFragment).hide(myFragment).commit();
        fm.beginTransaction().show(fragments.get(currFragmentId)).commit();
    }
}
