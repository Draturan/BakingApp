package com.example.simone.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Simone on 20/06/2018 for BakingApp project
 */
public class NetworkUtils {

    private static final String IMAGE_DOMAIN = "";

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static URL buildURL(String urlString){
        try {
            URL url = new URL(urlString);
            return url;
        }catch (MalformedURLException e ){
            e.printStackTrace();
            return null;
        }
    }

    public static Uri buildImageUrl(String path){
        return Uri.parse(IMAGE_DOMAIN).buildUpon()
                .appendEncodedPath(path)
                .build();
    }

    public static String getResponseFromHttpUrl (URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String result = null;
            if (hasInput){
                result = scanner.next();
            }
            scanner.close();
            return result;
        } finally {
            urlConnection.disconnect();
        }
    }

}
