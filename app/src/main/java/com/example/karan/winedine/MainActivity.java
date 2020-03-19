package com.example.karan.winedine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class MainActivity extends Activity {

    EditText phone;
    Button check;
    String phn = "";
    static String table_number;
    static String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        table_number = i.getStringExtra("qrresult");

        phone = (EditText) findViewById(R.id.phone_number);
        check = (Button) findViewById(R.id.check);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phn = phone.getText().toString();
                phone_number = phone.getText().toString();

                if (isValidMobile(phn)) {
                    try {
                        getUrldata();
                    } catch (Exception e) {
                        Log.d("Error -> ",e.getMessage());
                    }
                } else {
                    setPhoneError();
                }
            }
        });
    }

    private boolean isValidMobile(String ph) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", ph)) {
            if(ph.length() < 9 || ph.length() > 13) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }

    private void setPhoneError(){
        Drawable dr = getResources().getDrawable(R.drawable.error);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
        if (phn.length() < 9)
            phone.setError("Phone number length too short", dr);
        else if (phn.length() > 13)
            phone.setError("Phone number length too long", dr);
        phone.setText("");
        phone.requestFocus();
    }

    private void getUrldata() {
        class GetImage extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(MainActivity.this);
                loading.setCancelable(false);
                loading.setMessage("Authenticating....");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.show();
            }

            @Override
            protected void onPostExecute(String st) {
                super.onPostExecute(st);
                loading.dismiss();

                StringTokenizer tok = new StringTokenizer(st, "#");
                String[] token = new String[3];
                int i = 0;

                while (tok.hasMoreTokens()) {
                    token[i] = tok.nextToken();
                    i++;
                }

                if (st.equals("no")) {
                    Toasty.info(getApplicationContext(), "Please Register Yourself", Toast.LENGTH_SHORT, true).show();
                    Intent ii = new Intent(getApplicationContext(), RegisterActivity.class);
                    ii.putExtra("phno", phn);
                    startActivity(ii);
                    finish();
                }

                else if(st.equals("NotConnected")) {
                    Toasty.warning(getApplicationContext(), "Internet not working properly", Toast.LENGTH_SHORT, true).show();
                }

                else {
                    try {
                        Intent ii = new Intent(getApplicationContext(), Home.class);
                        ii.putExtra("name", token[0]);
                        if (token[2] == null)
                            ii.putExtra("email", " ");
                        else
                            ii.putExtra("email", token[2]);
                        startActivity(ii);
                        finish();
                        Toasty.success(getApplicationContext(), "Success!", Toast.LENGTH_SHORT, true).show();
                    } catch (Exception e) {
                        Log.d("Error -> ",e.getMessage());
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                Webservices ws = new Webservices();
                ws.setUrl("http://wineanddine.co.nf/check.php");
                ws.addParam(phn, "phone");
                ws.connect();
                String data2 = ws.getData();
                return data2;
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }
}