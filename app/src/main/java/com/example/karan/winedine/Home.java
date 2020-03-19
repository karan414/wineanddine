package com.example.karan.winedine;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.karan.winedine.dummy.DummyContent;
import com.example.karan.winedine.dummy.DummyContent1;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;

import static com.example.karan.winedine.MyApplication.counterFab;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , MenuFragment.OnFragmentInteractionListener, MenuPage.OnFragmentInteractionListener, CartFragment.OnListFragmentInteractionListener,
                    empty_cart.OnFragmentInteractionListener, CartFinal.OnFragmentInteractionListener, OrderItemFragment.OnListFragmentInteractionListener,
                    OrderHistoryFragment.OnFragmentInteractionListener, OrderStatus.OnFragmentInteractionListener {

    TextView home_name, home_email;
    static FragmentManager fm;
    static Context cont;
    static AppCompatActivity myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.maincont, new MenuFragment()).addToBackStack(null);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);

        cont = getApplicationContext();
        myContext = this;
        
        counterFab = (CounterFab) findViewById(R.id.counter_fab);
        counterFab.setCount(0);
        counterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(MyApplication.cart_item.isEmpty())
                    fm.beginTransaction().replace(R.id.maincont, new empty_cart()).commit();
                else {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.maincont, new CartFinal()).addToBackStack(null);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View child = navigationView.getHeaderView(0);
        home_name = (TextView) child.findViewById(R.id.home_name);
        home_email = (TextView) child.findViewById(R.id.home_email);
        Intent ii = getIntent();
        String temp = ii.getStringExtra("name");
        String temp1 = ii.getStringExtra("email");
        home_name.setText(temp);
        home_email.setText(temp1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() == 1) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Check Out")
                        .setMessage("Want to Check Out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                closeApp();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            else {
                super.onBackPressed();
            }
        }
    }

    private void closeApp() {
        super.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.maincont, new MenuFragment()).addToBackStack(null);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
        }
        else if (id == R.id.nav_waiter) {
            Toasty.info(getApplicationContext(), "Waiter will respond soon", Toast.LENGTH_SHORT, true).show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("call_a_waiter");
            Random rand = new Random();
            int n = rand.nextInt(10000) + 1;
            Map<String,String> waiter_details = new HashMap<>();
            waiter_details.put(MainActivity.table_number,n+"");
            myRef.setValue(waiter_details);
        }
        else if (id == R.id.nav_history) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.maincont, new OrderHistoryFragment()).addToBackStack(null);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
        }
        else if (id == R.id.nav_logout) {
            logout();
        }
        /*else if (id == R.id.nav_about_us) {

        }
        else if (id == R.id.nav_contact_us) {

        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    void logout() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Check Out")
                .setMessage("Want to Check Out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), QR_Scanner.class));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onListFragmentInteraction(DummyContent1.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
