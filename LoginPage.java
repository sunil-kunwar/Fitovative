package com.example.fitnessapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginPAge extends AppCompatActivity {

    EditText email,password;
    SharedPreferences sp;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        pd=new ProgressDialog(LoginPAge.this);
        
        sp= getSharedPreferences("pref",MODE_PRIVATE);
        if(sp.contains("id")){
            startActivity(new Intent(getApplicationContext(),DrawerActivity.class));
            finish();

        }

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginCall();
            }
        });

    }

    private void LoginCall() {
        pd.show();
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams rp=new RequestParams();
        rp.put("email",email.getText().toString());
        rp.put("password",password.getText().toString());
        client.post(Constants.url+"Login.php",rp,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response.getString("response").equals("Success")){

                      sp.edit()
                                .putString("id",response.getJSONArray("data").getJSONObject(0).getString("id"))
                              .putString("name",response.getJSONArray("data").getJSONObject(0).getString("name"))

                              .putString("email",response.getJSONArray("data").getJSONObject(0).getString("email"))

                              .apply();
                       startActivity(new Intent(getApplicationContext(),DrawerActivity.class));
                    }
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

    public void OpenSignUp(View view) {
        startActivity(new Intent(getApplicationContext(),SignUpPage.class));
    }
}
