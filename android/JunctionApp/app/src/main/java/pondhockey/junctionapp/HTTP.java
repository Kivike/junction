package pondhockey.junctionapp;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    public String createNewUser(String account, Coordinate location, int[] interests, float range){

        HttpPost httpPost = new HttpPost("http://rope.myftp.org:8000/newuser");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("account", account));
        nameValuePairs.add(new BasicNameValuePair("location", location.toString()));
        nameValuePairs.add(new BasicNameValuePair("interests", Arrays.toString(interests)));
        nameValuePairs.add(new BasicNameValuePair("travelrange", new DecimalFormat("0.0").format(range)));

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch(java.io.UnsupportedEncodingException e){
            Log.w("Error", "Unsupported Encoding: " + e);
            System.exit(0);
        }

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

    public String changeInterests(String account, int[] interests){
        return null;
    }




}