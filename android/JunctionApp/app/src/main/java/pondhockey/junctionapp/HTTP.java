package pondhockey.junctionapp;

import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HTTP{

    public static HTTP instance;

    public HttpClient httpClient;

    private HTTP(){
        this.httpClient = new DefaultHttpClient();
    }

    public static HTTP getInstance(){
        if(instance == null)
            instance = new HTTP();
        return instance;
    }

    public String createNewUser(String account, String location, int[] interests, float range){

        HttpPost httpPost = new HttpPost("http://rope.myftp.org:8000/newuser");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("account", account));
        nameValuePairs.add(new BasicNameValuePair("location", location));
        nameValuePairs.add(new BasicNameValuePair("interests", Arrays.toString(interests)));
        nameValuePairs.add(new BasicNameValuePair("travelrange", new DecimalFormat("0.0").format(range)));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch(java.io.UnsupportedEncodingException e){
            Log.w("Error", "Unsupported Encoding: " + e);
            System.exit(0);
        }

        return sendHttpCall(httpPost);
    }

    public String changeLocation(String account, String location){

        HttpPost httpPost = new HttpPost("http://rope.myftp.org:8000/changelocation");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("account", account));
        nameValuePairs.add(new BasicNameValuePair("location", location));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch(java.io.UnsupportedEncodingException e){
            Log.w("Error", "Unsupported Encoding: " + e);
            System.exit(0);
        }

        return sendHttpCall(httpPost);
    }

    public String changeInterests(String account, int[] interests){
        HttpPost httpPost = new HttpPost("http://rope.myftp.org:8000/changeinterests");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("account", account));
        nameValuePairs.add(new BasicNameValuePair("interests", Arrays.toString(interests)));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch(java.io.UnsupportedEncodingException e){
            Log.w("Error", "Unsupported Encoding: " + e);
            System.exit(0);
        }

        return sendHttpCall(httpPost);
    }

    public String changeTravelRange(String account, float range){

        HttpPost httpPost = new HttpPost("http://rope.myftp.org:8000/changetravelrange");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("account", account));
        nameValuePairs.add(new BasicNameValuePair("travelrange", new DecimalFormat("0.0").format(range)));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch(java.io.UnsupportedEncodingException e){
            Log.w("Error", "Unsupported Encoding: " + e);
            System.exit(0);
        }

        return sendHttpCall(httpPost);
    }

    public String createEvent(Event event){
        return createEvent(event.getTitle(), event.getDescription(), event.getStartDate(), event.getEndDate(), event.getLocationString(), event.getSportType());
    }

    public String createEvent(String title, String description, SimpleDateFormat startingTime, SimpleDateFormat endingTime, String location, int type){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpPost httpPost = new HttpPost("http://rope.myftp.org:8000/createEvent");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("title", title));
        nameValuePairs.add(new BasicNameValuePair("description", description));
        nameValuePairs.add(new BasicNameValuePair("startingTime", startingTime.toPattern()));
        nameValuePairs.add(new BasicNameValuePair("endingTime", endingTime.toPattern()));
        nameValuePairs.add(new BasicNameValuePair("location", location));
        nameValuePairs.add(new BasicNameValuePair("type", "" + type));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch(java.io.UnsupportedEncodingException e){
            Log.w("Error", "Unsupported Encoding: " + e);
            System.exit(0);
        }

        return sendHttpCall(httpPost);
    }

    public String getEvents(String account){

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://rope.myftp.org:8000/createEvent?" + account);
        try{
            HttpResponse response = httpclient.execute(httpGet);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            in.close();

            String responseString = in.toString();
            return responseString;

        }catch(Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        return null;
    }

    private String sendHttpCall(HttpPost httpPost){
        try {
            HttpResponse response = this.httpClient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){

                response.getEntity().writeTo(out);
                String responseString = out.toString();

                out.close();

                return responseString;

            } else{
                //Closes the connection
                response.getEntity().getContent().close();
                return statusLine.getReasonPhrase();
            }
        }catch(java.io.IOException e){
            Log.w("Error", "Cannot write data: " + e);
            System.exit(0);
        }
        return "Error";
    }
}