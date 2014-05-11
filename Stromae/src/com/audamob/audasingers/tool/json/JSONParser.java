package com.audamob.audasingers.tool.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

         InputStream is = null;
         JSONObject jObj = null;
         String json = "";

        // constructor jsonParser
        public JSONParser() {

        }

        // function get json from url
        // by making HTTP POST or GET mehtod
        public JSONObject makeHttpRequest(String url, String method,
                        List<NameValuePair> params) {

                HttpEntity httpEntity;
                // Making HTTP request
                try {

                        // check for request method
                        if (method == "POST") {
                                // request method is POST
                                // defaultHttpClient
                                Log.e("JSON PARSER"," JSON : 2");
                                DefaultHttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost(url);
                                
                                httpPost.setEntity(new UrlEncodedFormEntity(params));
                                
                                try {
                                        HttpResponse httpResponse = httpClient.execute(httpPost);
                                } catch (Exception e) {
                                        // TODO: handle exception
                                        Log.e("JSON PARSER"," error catch "+e);
                                }
                                

                        } else if (method == "PUT") {
                                Log.e("JSON PARSER"," JSON : lé lé");
                                // request method is PUT
                                // defaultHttpClient
                                DefaultHttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPut = new HttpPost(url);
                                Log.e("httpPut", httpPut.toString());
                                httpPut.setEntity(new UrlEncodedFormEntity(params));

                                HttpResponse httpResponse = httpClient.execute(httpPut);
                                int status = httpResponse.getStatusLine().getStatusCode();

                                if (status == HttpStatus.SC_OK) {
                                        httpEntity = httpResponse.getEntity();
                                        json = httpEntity != null ? EntityUtils.toString(
                                                        httpEntity, "iso-8859-1") : null;
                                } else {
                                        Log.e("Server responded with error status ", "" + status);
                                }

                        } else if (method == "GET") {
                                // request method is GET
                                DefaultHttpClient httpClient = new DefaultHttpClient();
                                String paramString = URLEncodedUtils.format(params, "utf-8");
                                if(!paramString.isEmpty())
                                	url += "?" + paramString;
                                
                                Log.i("JSON PARSER","url : "+url);
                                HttpGet httpGet = new HttpGet(url);

                                HttpResponse httpResponse = httpClient.execute(httpGet);
                                StatusLine statusLine = httpResponse.getStatusLine();
                                int statusCode = statusLine.getStatusCode();

                                if (statusCode == 200 && httpResponse != null) {
                                        httpEntity = httpResponse.getEntity();
                                        if(is == null) {
                                        is = httpEntity.getContent();
                                        Log.i("JSON PARSER","is : " +url+"\n"+is);
                                        }  		
                                } else {
                                        Log.e("JSON PARSER", "httpResponse is null : " +url+"\n");
                                }
                        }

                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.e("JSON PARSER"," JSON : 11: " +url+"\n");
                } catch (ClientProtocolException e) {
                        e.printStackTrace();
                        Log.e("JSON PARSER"," JSON : 12: " +url+"\n");
                } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("JSON PARSER"," JSON : 13: " +url+"\n"+e.getMessage());
                }

                try {
                        if (is != null) {
                                BufferedReader reader = new BufferedReader(
                                                new InputStreamReader(is, "iso-8859-1"), 8);
                                StringBuilder sb = new StringBuilder();
                                String line = null;
                                while ((line = reader.readLine()) != null) {
                                        sb.append(line + "\n");
                                }
                               // is.close();
                                json = sb.toString();
                                Log.i("JSON PARSER", "json : " +url+"\n"+json);
                                
                                if(json != null) {
                                	try {
	                                    // try parse the string to a JSON object
	                                	jObj = new JSONObject(json);
                                	} catch (JSONException e) {
                                                Log.e("JSON PARSER", " Error parsing data : " +url+"\n"+ e.toString());
                                    }
                                }
                                	
                                
                        } else {
                                Log.e("JSON Parser", "is( httpEntity.getContent() ) - is null : " +url+"\n");
                        }
                        
                       
                } catch (Exception e) {
                        Log.e("Buffer Error", "Error converting result : " +url+"\n" + e.toString());
                } 

               

                // return JSON String
                return jObj;

        }
}