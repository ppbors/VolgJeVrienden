package com.example.philippebors.volgjevrienden;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ToonJeVrienden extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendDataToServer(Config.MY_NUMBER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_toonvrienden);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        connect();
    }


    private void connect() {
        String data;
        List<String> r = new ArrayList<>();
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,r);
        ListView list=(ListView)findViewById(R.id.listView1);
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(Config.CHECK_FRIENDS_URL);
            HttpResponse response = client.execute(request);
            HttpEntity entity=response.getEntity();
            data=EntityUtils.toString(entity);
            Log.e("STRING", data);
            try {

                JSONArray json = new JSONArray(data);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject obj = json.getJSONObject(i);
                    String name = obj.getString("NAME");

                    Log.e("STRING", name);
                    r.add(name);
                    list.setAdapter(adapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
    }



    private void sendDataToServer(final String phonenumber) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String QuickNUMBER = phonenumber;
                List<NameValuePair> nameValuePairs = new ArrayList<>();

                /* Me make items in the list of pairs */
                nameValuePairs.add(new BasicNameValuePair("phonenumber", QuickNUMBER));

                /* We set up a new request */
                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(Config.DATA_URL2);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {
                    Toast.makeText(ToonJeVrienden.this, "Error: 1" + e.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(ToonJeVrienden.this, "Error: 2" + e.toString(), Toast.LENGTH_LONG).show();
                }
                return QuickNUMBER;
            }
        }
        Toast.makeText(ToonJeVrienden.this, phonenumber, Toast.LENGTH_LONG).show();
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(phonenumber);
    }
}