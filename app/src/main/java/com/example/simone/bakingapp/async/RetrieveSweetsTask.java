package com.example.simone.bakingapp.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.simone.bakingapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Simone on 20/06/2018 for BakingApp project
 */
public class RetrieveSweetsTask extends AsyncTask<URL, Void, String>{
    private AsyncTaskCompleteListener<String> listener;

    private static final String SWEETS_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public RetrieveSweetsTask(AsyncTaskCompleteListener<String> listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreTaskExecute();
    }

    @Override
    protected String doInBackground(URL... urls) {
        String result = null;
        URL url = NetworkUtils.buildURL(SWEETS_URL);
        if (url != null){
            try{
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    result = jsonArray.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onTaskComplete(s);
    }
}
