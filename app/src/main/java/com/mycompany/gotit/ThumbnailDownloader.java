package com.mycompany.gotit;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThumbnailDownloader<Token> extends HandlerThread {
    private final static String TAG = "ThumbnailDownloader";
    private final static int MESSAGE_DOWNLOAD = 0;
    Handler mHandler;
    Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
    Handler mResponseHandler;
    Listener<Token> mListener;
    private int cacheSize = 10*1024*1024;
    private LruCache<String, Bitmap> mCache = new LruCache<>(cacheSize);

    public interface Listener<Token>{
        void onThumbnailDownload(Token token, Bitmap bitmap);
    }

    public void setListener(Listener<Token> listener){
        mListener = listener;
    }


    @SuppressLint("HandlerLeak")
    @Override
    public void onLooperPrepared(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == MESSAGE_DOWNLOAD){
                    @SuppressWarnings("unchecked")
                    Token token = (Token) msg.obj;
                    Log.i(TAG, "got a request for url: "+ requestMap.get(token));
                    handleRequest(token);
                }
            }
        };

    }

    public ThumbnailDownloader(Handler responseHandler){
        super(TAG);
        mResponseHandler = responseHandler;
    }

    private void showBitmap(final Token token, final String url){
        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                requestMap.remove(token);
                mListener.onThumbnailDownload(token, mCache.get(url));
            }
        });
    }

    public void queueThumbnail(Token token, String url){
        Log.i(TAG, "Got an URL: "+ url);
        //mCache.get(token);
        if(mCache.get(url) == null){
            requestMap.put(token, url);
            mHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
        } else{
            showBitmap(token, url);
        }
    }

    private void handleRequest(Token token){
        try{
            final String url = requestMap.get(token);
            if(url == null) return;
            byte[] bitmapBytes = getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created.");
            mCache.put(url, bitmap);

            showBitmap(token, url);

        } catch (IOException ioe){
            Log.e(TAG, "Error downloading image.", ioe);
        }

    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) return null;

            byte[] bytes = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = in.read(bytes)) > 0) {
                out.write(bytes, 0, bytesRead);
            }
        } finally {
            out.close();
        }
        return out.toByteArray();
    }

    public void clearQueue(){
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }
}
