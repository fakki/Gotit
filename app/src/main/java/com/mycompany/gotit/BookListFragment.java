package com.mycompany.gotit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class BookListFragment extends Fragment {

    private ArrayList<Book> mBooks;

    private static final String TAG = "BookListFragment";

    private static final String ID_KEY = "id_key";

    private static final String TAG_KEY = "tag_key";

    private ListView mListView;

    private SwipeRefreshLayout mSwipe;

    private int ID;

    private String mQueryTag;

    private Spider spider = new Spider();

    //use start to locate where to crawl
    private int start = 0;

    private ThumbnailDownloader<ImageView> mThumbnailThread;

    private class FetchBookTask extends AsyncTask<Void, Void, ArrayList<Book>>{
        @Override
        protected ArrayList<Book> doInBackground(Void... params) {
            Activity activity = getActivity();
            if(activity == null) return new ArrayList<>();
            Log.d(TAG, "into doInBg");

            switch (ID){
                case 0:
                    //book express
                    Log.d(TAG, "into case 0.");
                    return spider.getBookExpress();
                case -1:
                    Log.d(TAG, "into case -1.");
                    return spider.getSearchResult(mQueryTag, start);

                default:
                    Log.d(TAG, "into case default");
                    return spider.getBookWithTag(mQueryTag, start);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books){
            if(ID != 0) {
                //not book express, can be refreshed
                start += books.size();
                mBooks.addAll(0, books);
            }
            else mBooks = books;
            setupAdapter();
            mSwipe.setRefreshing(false);
        }

    }

    public static BookListFragment newInstance(int id, String tag){
        Bundle args = new Bundle();
        BookListFragment fragment = new BookListFragment();
        args.putInt(ID_KEY, id);
        args.putString(TAG_KEY, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setRetainInstance(true);

        mBooks = new ArrayList<>();

        ID = getArguments().getInt(ID_KEY);
        mQueryTag = getArguments().getString(TAG_KEY);

        mThumbnailThread = new ThumbnailDownloader<>(new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownload(ImageView imageView, Bitmap bitmap) {
                if(isVisible()){
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle onSaveInstanceState){
        View v = inflater.inflate(R.layout.fragment_book_list, null);

        mListView = v.findViewById(R.id.book_list_ListView);

        FloatingActionButton floatingButton = v.findViewById(R.id.book_list_FloatingButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.smoothScrollToPosition(0);
            }
        });


        new FetchBookTask().execute();

        mSwipe = v.findViewById(R.id.book_list_SwipeRefreshLayout);
        mSwipe.setColorSchemeResources(R.color.refresh_ball);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "refreshed.");
                new FetchBookTask().execute();
            }
        });

        return v;
    }

    private void setupAdapter(){
        if(getActivity() == null) return;
        if(mBooks != null) mListView.setAdapter(new BookAdapter(mBooks));
        else mListView.setAdapter(null);
    }


    private class BookAdapter extends ArrayAdapter<Book>{
        public BookAdapter(ArrayList<Book> books){
            super(getActivity(), 0, books);
        }

        @Override
        public View getView(int position, View convertedView, ViewGroup parent) throws NullPointerException{
            //if(convertedView == null){
            convertedView = getActivity().getLayoutInflater().inflate(R.layout.list_item_book, null);

            Book book = getItem(position);

            //wait to be modified, now test it
            ImageView imageView = convertedView.findViewById(R.id.book_pic_ImageView);
            imageView.setImageResource(R.mipmap.loading);
            mThumbnailThread.queueThumbnail(imageView, book.getImageUrl());


            TextView titleText = convertedView.findViewById(R.id.book_title_TextView);
            titleText.setText(book.getTitle());
            Log.d(TAG, "get View: " + book.getTitle() + " url: " + imageView);

            TextView markText = convertedView.findViewById(R.id.mark_TextView);
            markText.setText(new Double(book.getMark()).toString()+"分");

            TextView publishInfoText = convertedView.findViewById(R.id.book_publish_info_TextView);
            publishInfoText.setText(book.getPublishInfo());

            TextView introductionText = convertedView.findViewById(R.id.book_introduction_TextView);
            introductionText.setText(book.getIntroduction());

            return convertedView;
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    public void onDestroy(){
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i(TAG, "Background thread destroyed.");
    }
}
