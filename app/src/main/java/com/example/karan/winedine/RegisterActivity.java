package com.example.karan.winedine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends Activity {

    EditText phone, name, email;
    Button register, reset;
    String na = "", phn = "", ema = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Register Yourself");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        phone = (EditText) findViewById(R.id.phone);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);

        register = (Button) findViewById(R.id.register);
        reset = (Button) findViewById(R.id.reset);

        Intent ii = getIntent();
        phn = ii.getStringExtra("phno");
        phone.setText(phn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phn = phone.getText().toString();
                na = name.getText().toString();
                ema = email.getText().toString();

                if (na.equals("")) {
                    Drawable dr = getResources().getDrawable(R.drawable.error);
                    dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                    name.setError("Please Provide name", dr);
                    name.requestFocus();
                }
                else if (!ema.equals("")) {
                    if (!validateemail(ema)) {
                        Drawable dr = getResources().getDrawable(R.drawable.error);
                        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                        email.setError("Invalid email address", dr);
                        email.setText("");
                        email.requestFocus();
                    }
                }
                else {
                    try {
                        getUrldata();
                    }
                    catch(Exception e){
                        Log.d("Error -> ",e.getMessage());
                    }
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                email.setText("");
                name.requestFocus();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    private boolean validateemail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void getUrldata() {
        class GetImage extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(RegisterActivity.this);
                loading.setCancelable(false);
                loading.setMessage("Registering User....");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.show();
            }

            @Override
            protected void onPostExecute(String st) {
                super.onPostExecute(st);
                loading.dismiss();

                if (!st.equals("unsuccess")) {
                    Toasty.success(getApplicationContext(), "Success!", Toast.LENGTH_SHORT, true).show();
                    Intent ii = new Intent(getApplicationContext(), Home.class);
                    ii.putExtra("name", na);
                    if (ema == null)
                        ii.putExtra("email", " ");
                    else
                        ii.putExtra("email", ema);
                    startActivity(ii);
                    finish();
                } else {
                    Toasty.error(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT, true).show();
                    name.setText("");
                    email.setText("");
                    name.requestFocus();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                Webservices ws = new Webservices();
                ws.setUrl("http://wineanddine.co.nf/register_user.php");
                ws.addParam(na, "name");
                ws.addParam(phn, "phone");
                ws.addParam(ema, "email");
                ws.connect();
                String data2 = ws.getData();
                return data2;
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }
}