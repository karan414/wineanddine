package com.example.wineanddineadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {

    TextView signin;
    Button send;
    EditText edit_pass;
    String fst = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Forgot Password");

        signin = (TextView) findViewById(R.id.signin);
        send = (Button) findViewById(R.id.send_pass);
        edit_pass = (EditText) findViewById(R.id.forgotpassword);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LandingActivity.class);
                startActivity(i);
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fst = edit_pass.getText().toString();
                try{
                    getUrldata();
                }
                catch (Exception e) {
                    Log.d("Error -> ",e.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LandingActivity.class));
        finish();
    }


    private void getUrldata() {
        class GetImage extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(ForgotPassword.this);
                loading.setCancelable(false);
                loading.setMessage("Please Wait...");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.show();
            }

            @Override
            protected void onPostExecute(String st) {
                super.onPostExecute(st);
                loading.dismiss();

                if (st.equals("no")) {
                    Toast.makeText(getApplicationContext(), "Enter valid email address", Toast.LENGTH_LONG).show();
                    edit_pass.requestFocus();
                } else if(st.equals("fail")) {
                    Toast.makeText(getApplicationContext(), "Fail to send mail. Try after some time", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Mail successfully send", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), LandingActivity.class));
                    finish();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                Webservices ws = new Webservices();
                ws.setUrl("http://wineanddine.co.nf/forgetpassword.php");
                ws.addParam(fst, "email");
                ws.connect();
                String data2 = ws.getData();
                return data2;
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }
}
