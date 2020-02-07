package com.mycompany.gotit;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BookFragment extends Fragment {

    private static final String TAG = "BookFragment";

    private ViewPager mViewPager;

    //新书速递 文学 文化 科技
    private int mColumnNum = 4;
    ArrayList<String> mTabListTitle = new ArrayList<>();

    public void addColumns(ArrayList<String> columns){
        if(columns.size() == 0) return;
        for(String string : columns){
            if(!mTabListTitle.contains(string)){
                mColumnNum++;
                mTabListTitle.add(string);
                mViewPager.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle onSaveInstanceState){
        Log.d(TAG, "create book fragment view.");
        View v = inflater.inflate(R.layout.fragment_book, parent, false);

        mViewPager = v.findViewById(R.id.book_viewPager);
        final TabLayout mTabLayout = v.findViewById(R.id.book_tabLayout);

        initTabLayout(mTabLayout);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                return BookListFragment.newInstance(i, mTabListTitle.get(i));
            }

            @Override
            public int getCount() {
                return mColumnNum;
            }

            @Override
            public CharSequence getPageTitle(int position){
                return mTabListTitle.get(position);
            }
        });
        mViewPager.setOffscreenPageLimit(6);

        mTabLayout.setupWithViewPager(mViewPager);

        return v;
    }

    private void initTabLayout(TabLayout mTabLayout){
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.book_express));
        mTabListTitle.add(getString(R.string.book_express));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.book_literature));
        mTabListTitle.add(getString(R.string.book_literature));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.book_culture));
        mTabListTitle.add(getString(R.string.book_culture));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.book_technique));
        mTabListTitle.add(getString(R.string.book_technique));
    }
}
