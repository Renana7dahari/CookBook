package com.example.newcookbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.newcookbook.DownloadCallBack;

public class Utility {
    private static String TAG = "Utility Log";

    public static boolean isOnline(Context ctx)//Checking Internet is available or not
    {
        ConnectivityManager connMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    public static String downloadImgFromUrl(final String myurl, final DownloadCallBack callBack) throws IOException {
        InputStream is = null;
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(myurl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000); // time in milliseconds
                        conn.setConnectTimeout(15000); // time in milliseconds
                        conn.setRequestMethod("GET"); // request method GET OR POST
                        conn.setDoInput(true);
                        // Starts the query
                        conn.connect(); // calling the web address
                        int response = conn.getResponseCode();
                        Log.d(TAG, "The response is: " + response);
                        InputStream is = conn.getInputStream();

                        // Convert the InputStream into a string
                        Bitmap bmp = BitmapFactory.decodeStream(is);
                        if (callBack!= null)
                            callBack.done(bmp);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    // Reads an InputStream and converts it to a String.
    public static String readInputStream(InputStream stream) throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer)))
            writer.write(buffer, 0, n);
        return writer.toString();
    }
}