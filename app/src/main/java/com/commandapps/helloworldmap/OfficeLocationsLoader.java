package com.commandapps.helloworldmap;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.commandapps.helloworldmap.model.OfficeLocation;
import com.commandapps.helloworldmap.model.OfficeLocations;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael on 10/8/2014.
 */
public class OfficeLocationsLoader extends AsyncTaskLoader<List<OfficeLocation>> {

    public OfficeLocationsLoader(Context context) {
        super(context);
    }

    @Override
    public List<OfficeLocation> loadInBackground() {

        // even if fail return empty list and print exception stack trace
        List<OfficeLocation> result = new ArrayList<OfficeLocation>();

        // Do not use HttpClient, see http://android-developers.blogspot.com/2011/09/androids-http-clients.html
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://www.helloworld.com/helloworld_locations.json").openConnection();
            // by default it's a GET request
            try {
                InputStream input = new BufferedInputStream(httpURLConnection.getInputStream());
                String jsonResponse = streamToString(input);
                Log.d("REST_LOG", "response: " + jsonResponse);
                Gson gson = new Gson();
                OfficeLocations officeLocations = gson.fromJson(jsonResponse, OfficeLocations.class);
                result.addAll(Arrays.asList(officeLocations.getLocations()));

            } finally {
                httpURLConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;


    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    private static String streamToString(InputStream stream) throws IOException {
        Writer writer = new StringWriter();
        InputStreamReader input = new InputStreamReader(new BufferedInputStream(stream), "UTF-8");

        try {
            final char[] buffer = new char[1024];
            int read;

            while ((read = input.read(buffer)) != -1)
                writer.write(buffer, 0, read);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            input.close();
        }

        return writer.toString();
    }

}
