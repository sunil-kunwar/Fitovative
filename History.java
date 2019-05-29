package com.example.fitnessapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class History extends AppCompatActivity {

    ArrayList<ModelSteps> arrayList;
    ListView listView;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        pd=new ProgressDialog(History.this);
        listView=findViewById(R.id.list);
        arrayList=new ArrayList<>();
        getHistoryData();
    }

    private void getHistoryData() {
        AsyncHttpClient client=new AsyncHttpClient();
        pd.show();
        RequestParams rp=new RequestParams();
        rp.put("userid",getSharedPreferences("pref",MODE_PRIVATE).getString("id",""));
        client.post(Constants.url+"getHistoryData.php",rp,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray ja=response.getJSONArray("data");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);

                        ModelSteps modelSteps=new ModelSteps();
                        modelSteps.setSteps(jo.getString("steps"));
                        modelSteps.setTime(jo.getString("times"));
                        arrayList.add(modelSteps);
                    }
                    listView.setAdapter(new CustomAdapter());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                pd.dismiss();
            }
        });
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.historyrow,viewGroup,false);
            TextView no=view.findViewById(R.id.no);
            TextView times=view.findViewById(R.id.times);

            TextView steps=view.findViewById(R.id.steps);
            steps.setText(arrayList.get(i).getSteps());
            times.setText(arrayList.get(i).getTime());
            no.setText((i+1)+"");
            return view;
        }
    }
}
