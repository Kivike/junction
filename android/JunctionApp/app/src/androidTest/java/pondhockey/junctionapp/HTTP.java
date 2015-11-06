package pondhockey.junctionapp;

public class HTTP{

    public static HTTP singleton;

    public HttpClient httpClient;

    public HTTP(){
        if(HTTP != null) return singleton;
        singleton = this;

        this.httpClient = new DefaultHttpClient();
    }

    public String createNewUser(string account, Coordinate location, int[] interests, float range){

        HttpPost httpPost = new HttpPost('www.rope.myftp.org/newuser');

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("account", account));
        nameValuePairs.add(new BasicNameValuePair("location", location.toString()));
        nameValuePairs.add(new BasicNameValuePair("interests", Arrays.toString(interests));
        nameValuePairs.add(new BasicNameValuePair("range", range));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpResponse response = this.httpClient.execute(httpPost);
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();

            return responseString;

        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            return response.getEntity().writeTo(out).toString();
        }
    }




}