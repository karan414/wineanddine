package com.example.wineanddineadmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

import es.dmoral.toasty.Toasty;

public class LandingActivity extends Activity {

    static String dbname = "wineanddine.db";
    EditText username, password;
    Button login, reset;
    TextView register, forgotpass;
    String user = "", pass = "";
    SQLiteDatabase mydb = null;
    String dbpath = "/data/data/com.example.wineanddineadmin/databases/";
    String mypath = dbpath + dbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MySQL my = new MySQL(getApplicationContext());
        my.checkMydb();

        checkLogin();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        reset = (Button) findViewById(R.id.reset);
        register = (TextView) findViewById(R.id.register);
        forgotpass = (TextView) findViewById(R.id.forgotpassword);

        try {
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username.setText("");
                    password.setText("");
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ii = new Intent(getApplicationContext(), Register.class);
                    startActivity(ii);
                    finish();
                }
            });

            forgotpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), ForgotPassword.class);
                    startActivity(i);
                    finish();
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user = username.getText().toString();
                    pass = password.getText().toString();
                    if (pass.length() <= 7 && pass.length() >= 20) {
//                        Toast.makeText(getApplicationContext(), "Password length too short or too long", Toast.LENGTH_LONG).show();
                        password.setText("");
                    }
                    else if(user.equals("")){
//                        Toast.makeText(getApplicationContext(),"Please provide email", Toast.LENGTH_LONG).show();
                        username.setError("Please provide email");
                        username.requestFocus();
                    }
                    else if (!validateemail(user)){
//                        Toast.makeText(getApplicationContext(),"Invalid email", Toast.LENGTH_LONG).show();
                        username.setError("Invalid email address");
                        username.setText("");
                        username.requestFocus();
                    }
                    else {
                        getUrldata();
                    }
                }
            });
        } catch (Exception e) {
            Log.d("Error message: ", e.toString());
        }
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
                //loading = ProgressDialog.show(P_Query.this, "Please wait....", null,true,true);
                loading = new ProgressDialog(LandingActivity.this);
                loading.setCancelable(false);
                loading.setMessage("Authenticating...");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.show();
            }

            @Override
            protected void onPostExecute(String st) {
                super.onPostExecute(st);
                loading.dismiss();

                StringTokenizer tok = new StringTokenizer(st, "#");
                String[] token = new String[8];
                int i = 0;

                while (tok.hasMoreTokens()) {
                    token[i] = tok.nextToken();
                    i++;
                }

                if (st.equals("no")) {
//                    Toasty.error(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT, true).show();
                    username.setText("");
                    password.setText("");
                    username.requestFocus();
                } else {
                    addDataToTable(token);
//                    Toasty.success(getApplicationContext(), "Login Successful !", Toast.LENGTH_SHORT, true).show();
                    Intent ii = new Intent(getApplicationContext(), Home.class);
                    startActivity(ii);
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
                ws.setUrl("http://wineanddine.co.nf/check_admin.php");
                ws.addParam(user, "username");
                ws.addParam(pass, "password");
                ws.connect();
                String data2 = ws.getData();
                return data2;
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }

    private void addDataToTable(String[] token) {
        try {
            mydb = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
            mydb.execSQL("delete from temp");
            mydb.execSQL("INSERT INTO temp(id,name,password,email,phone,address,salary,role) VALUES(" + token[0] + ",'" + token[1] + "','" + token[2] + "','" + token[3] + "','" + token[4] + "','" + token[5] + "','" + token[6] + "','admin')");
            mydb.close();
        } catch (Exception e) {
            Log.d("Error -> ",e.getMessage());
        }
    }

    private void checkLogin() {
        try {
            mydb = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cur = mydb.rawQuery("select * from temp", null);
            int len = cur.getCount();
            String email = "", password = "", name = "";

            while (cur.moveToNext()) {
                name = cur.getString(cur.getColumnIndex("name"));
                email = cur.getString(cur.getColumnIndex("email"));
                password = cur.getString(cur.getColumnIndex("password"));
            }

            if (!email.equals("") && !password.equals("")) {
                Intent i1 = new Intent(getApplicationContext(), Home.class);
                startActivity(i1);
                finish();
            }
            mydb.close();
        } catch (Exception e) {
            Log.d("Error -> ",e.getMessage());
        }
    }

}

