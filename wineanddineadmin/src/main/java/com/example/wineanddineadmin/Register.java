package com.example.wineanddineadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Register extends AppCompatActivity {

    TextView signin;
    EditText name, email, password, phone, address, salary, confirm_password;
    String na = "", ema = "", pass = "", ph = "", add = "", sal = "", con_pass = "";
    Button register, reset;
    Context cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        signin = (TextView) findViewById(R.id.signnin);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        salary = (EditText) findViewById(R.id.salary);
        address = (EditText) findViewById(R.id.address);
        register = (Button) findViewById(R.id.register);
        reset = (Button) findViewById(R.id.reset);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        cc = getApplicationContext();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                email.setText("");
                password.setText("");
                phone.setText("");
                salary.setText("");
                confirm_password.setText("");
                address.setText("");
                name.requestFocus();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getApplicationContext(), LandingActivity.class);
                startActivity(ii);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                na = name.getText().toString();
                ema = email.getText().toString();
                pass = password.getText().toString();
                ph = phone.getText().toString();
                add = address.getText().toString();
                sal = salary.getText().toString();
                con_pass = confirm_password.getText().toString();

                if(na.equals("")){
//                    Toast.makeText(getApplicationContext(),"Please provide name", Toast.LENGTH_LONG).show();
                    name.setError("Please provide name");
                    name.requestFocus();
                }
                else if(ema.equals("")){
//                    Toast.makeText(getApplicationContext(),"Please provide email", Toast.LENGTH_LONG).show();
                    email.setError("Please provide email");
                    email.requestFocus();
                }
                else if (!validateemail(ema)){
//                    Toast.makeText(getApplicationContext(),"Invalid email", Toast.LENGTH_LONG).show();
                    email.setError("Invalid email address");
                    email.setText("");
                    email.requestFocus();
                }
                else if (pass.length() <= 7 && pass.length() >= 20) {
//                    Toast.makeText(getApplicationContext(), "Password length too short or too long", Toast.LENGTH_LONG).show();
                    password.setError("Password length too short or too long");
                    password.setText("");
                    password.requestFocus();
                }
                else if(!con_pass.equals(pass)){
//                    Toast.makeText(getApplicationContext(),"Password doesn't match", Toast.LENGTH_LONG).show();
                    confirm_password.setError("Password doesn't match");
                    password.setText("");
                    confirm_password.setText("");
                    password.requestFocus();
                }
                else if(!isValidMobile(ph)) {
//                    Toast.makeText(getApplicationContext(),"Invalid phone number", Toast.LENGTH_LONG).show();
                    phone.setError("Not Valid Number");
                    phone.setText("");
                    phone.requestFocus();
                }
                else if(!isValidSalary(sal)) {
//                    Toast.makeText(getApplicationContext(),"Invalid salary", Toast.LENGTH_LONG).show();
                    salary.setError("Not Valid Number");
                    salary.setText("");
                    salary.requestFocus();
                }
                else {
                    getUrldata();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LandingActivity.class));
        finish();
    }

    private boolean validateemail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidMobile(String ph) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", ph)) {
            if(ph.length() < 9 || ph.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }

    private boolean isValidSalary(String sa) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", sa)) {
            if(sa.length() < 3 || sa.length() > 6) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }

    private void getUrldata() {
        class GetImage extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(Register.this);
                loading.setCancelable(false);
                loading.setMessage("Registering Admin...");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.show();
            }

            @Override
            protected void onPostExecute(String st) {
                super.onPostExecute(st);
                loading.dismiss();

                if (st.equals("unsuccess")) {
                    Toasty.error(getApplicationContext(), "Registration not done", Toast.LENGTH_SHORT, true).show();
                }
                else {
                    Toasty.success(getApplicationContext(), "Registration Successfully done !", Toast.LENGTH_SHORT, true).show();
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
                ws.setUrl("http://wineanddine.co.nf/register_admin.php");
                ws.addParam(na, "name");
                ws.addParam(ph, "phone");
                ws.addParam(ema, "email");
                ws.addParam(pass, "password");
                ws.addParam(add, "address");
                ws.addParam(sal, "salary");
                ws.addParam("admin", "role");
                ws.connect();
                String data2 = ws.getData();
                return data2;
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }
}
