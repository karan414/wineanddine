package com.example.winedine.wineanddinekitchen;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.winedine.wineanddinekitchen.dummy.DummyContent;
import com.example.winedine.wineanddinekitchen.dummy.DummyContent1;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements OrderFragment.OnListFragmentInteractionListener, ItemFragment.OnListFragmentInteractionListener {

    static Context maincont;
    Button call_waiter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maincont = getApplicationContext();


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        call_waiter = (Button) findViewById(R.id.call_a_waiter);
        call_waiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("call_a_waiter_kitchen");
                Toasty.info(getApplicationContext(), "Waiter will respond soon", Toast.LENGTH_SHORT, true).show();
                Random rand = new Random();
                int n = rand.nextInt(10000) + 1;
                Map<String,String> waiter_details = new HashMap<>();
                waiter_details.put("kitchen",n+"");
                myRef.setValue(waiter_details);
            }
        });


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.maincont, new OrderFragment()).addToBackStack(null);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }

    @Override
    public void onListFragmentInteraction(DummyContent1.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            super.onBackPressed();
        }
    }
}
