package com.wibe.backend.library;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class Http {

        private static Http http;
        private final String USER_AGENT = "Mozilla/5.0";

        private Http(){

        }

        public static Http getInstance(){
                if (http == null){
                        http = new Http();
                }
                return http;
        }

        //HTTP GET Request
        public String sendGet(String url) throws Exception {
                URL obj = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");

                //add request header
                con.setRequestProperty("User-Agent", USER_AGENT);

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString();

    }

    // HTTP POST request
    public String sendPost(String url, String urlParameters) throws Exception {

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

                    //print result
            return response.toString();
    }
    
    public static String post(String url, JSONObject headers, JSONObject body){
    	HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		Iterator<?> keys = headers.keys();
		while (keys.hasNext()){
			String key = (String)keys.next();
			try {
				post.setHeader(key, headers.getString(key));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		post.setEntity(new StringEntity(body.toString(), "UTF-8"));
		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		try {
		    BufferedReader reader = 
		           new BufferedReader(new InputStreamReader(response.getEntity()
		        		   .getContent()), 65728);
		    String line = null;

		    while ((line = reader.readLine()) != null) {
		        sb.append(line);
		    }
		}
		catch (IOException e) { e.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }
		
		return sb.toString();

    }
    
    public static String delete(String url , JSONObject headers){
    	HttpClient client = HttpClientBuilder.create().build();
		HttpDelete delete = new HttpDelete(url);
		
		Iterator<?> keys = headers.keys();
		while (keys.hasNext()){
			String key = (String)keys.next();
			try {
				delete.setHeader(key, headers.getString(key));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		HttpResponse response = null;
		try {
			response = client.execute(delete);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		try {
		    BufferedReader reader = 
		           new BufferedReader(new InputStreamReader(response.getEntity()
		        		   .getContent()), 65728);
		    String line = null;

		    while ((line = reader.readLine()) != null) {
		        sb.append(line);
		    }
		}
		catch (IOException e) { e.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }
		
		return sb.toString();
    }


   }
