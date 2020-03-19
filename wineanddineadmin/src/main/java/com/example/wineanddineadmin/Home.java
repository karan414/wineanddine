package com.example.wineanddineadmin;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wineanddineadmin.dummy.DummyContent1;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onurciner.toastox.ToastOXDialog;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OrderFragment.OnListFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("menu");
    static Context maincont;
    static android.app.FragmentManager fm1;
    RelativeLayout f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        maincont = getApplicationContext();
        f = (RelativeLayout) findViewById(R.id.cont);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fm1 = getFragmentManager();

        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                f.getBackground().setAlpha(00);
                f.setAlpha(0f);
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton action_add = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_add);
        com.getbase.floatingactionbutton.FloatingActionButton action_update = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_update);
        com.getbase.floatingactionbutton.FloatingActionButton action_delete = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_delete);

        action_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        action_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

        action_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        checkWaiterCall();
        checkKitchcenWaiterCall();

        FragmentTransaction fragmentTransaction = fm1.beginTransaction();
        fragmentTransaction.replace(R.id.cont, new OrderFragment()).addToBackStack(null);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }

    public void addItem() {
        final Dialog dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.add_item_layout);
        dialog.setTitle("Add Item");
        final EditText category = (EditText)dialog.findViewById(R.id.add_item_cat);
        final EditText sub_category = (EditText)dialog.findViewById(R.id.add_item_subcat);
        final EditText name = (EditText)dialog.findViewById(R.id.add_item_name);
        final EditText desc = (EditText)dialog.findViewById(R.id.add_item_desc);
        final EditText price = (EditText)dialog.findViewById(R.id.add_item_price);
        Button add = (Button) dialog.findViewById(R.id.add_item_addbtn);
        Button cancel = (Button) dialog.findViewById(R.id.add_item_cancel);

        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> add_item = new HashMap<String, Object>();
                add_item.put("name",name.getText().toString());
                add_item.put("price",price.getText().toString());
                myRef.child(category.getText().toString()).child(sub_category.getText().toString()).child(name.getText().toString()).setValue(add_item);
                dialog.dismiss();
            }
        });
    }

    public void deleteItem() {
        final Dialog dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.delete_item_layout);
        final EditText category = (EditText)dialog.findViewById(R.id.delete_cat);
        final EditText sub_category = (EditText)dialog.findViewById(R.id.delete_subcat);
        final EditText name = (EditText)dialog.findViewById(R.id.delete_name);
        Button delete = (Button) dialog.findViewById(R.id.delete_button);
        Button cancel = (Button) dialog.findViewById(R.id.delete_cancel);
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(category.getText().toString()).child(sub_category.getText().toString()).child(name.getText().toString()).removeValue();
                dialog.dismiss();
            }
        });
    }

    public void updateItem() {
        final Dialog dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.update_item_layout);
        final EditText category = (EditText)dialog.findViewById(R.id.update_cat);
        final EditText sub_category = (EditText)dialog.findViewById(R.id.update_subcat);
        final EditText name = (EditText)dialog.findViewById(R.id.update_name);
        Button next = (Button) dialog.findViewById(R.id.update_next);
        Button cancel = (Button) dialog.findViewById(R.id.update_can);
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(category.getText().toString()).child(sub_category.getText().toString()).child(name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Dialog d = new Dialog(Home.this);
                        d.setContentView(R.layout.update_item);
                        final EditText price = (EditText)d.findViewById(R.id.update_price);
                        final EditText desc = (EditText)d.findViewById(R.id.update_desc);
                        final EditText name = (EditText)d.findViewById(R.id.update_name);
                        Button update = (Button) d.findViewById(R.id.update_btn);
                        Button can = (Button) d.findViewById(R.id.update_cancel);
                        Map<String, Object> temp = (Map<String, Object>) dataSnapshot.getValue();
                        price.setText(temp.get("price").toString());
                        name.setText(temp.get("name").toString());
                        dialog.dismiss();
                        d.show();

                        myRef.child(category.getText().toString()).child(sub_category.getText().toString()).child(name.getText().toString()).removeEventListener(this);
                        can.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myRef.child(category.getText().toString()).child(sub_category.getText().toString()).child(name.getText().toString()).removeValue();
                                Map<String,Object> add_item = new HashMap<String, Object>();
                                add_item.put("name",name.getText().toString());
                                add_item.put("price",price.getText().toString());
                                myRef.child(category.getText().toString()).child(sub_category.getText().toString()).child(name.getText().toString()).setValue(add_item);
                                d.dismiss();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    void checkWaiterCall() {
        DatabaseReference myRef1 = database.getReference();
        try {
            myRef1.child("call_a_waiter").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String temp = dataSnapshot.getKey();
                    dialogOpen(temp);
//                    addNotification(temp.toString());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    String temp = dataSnapshot.getKey();
                    dialogOpen(temp);
//                    addNotification(temp.toString());
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
            Log.d("Error -> ",e.getMessage());
        }
    }

    private void checkKitchcenWaiterCall() {
        DatabaseReference myRef1 = database.getReference();
        myRef1.child("call_a_waiter_kitchen").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String temp = dataSnapshot.getKey();
                dialogKitchenOpen(temp);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String temp = dataSnapshot.getKey();
                dialogKitchenOpen(temp);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void dialogOpen(final String table){
        try {
            new ToastOXDialog.Build(this)
                    .setTitle(table.toUpperCase())
                    .setContent("Waiter needed at " + table)
                    .setPositiveText("Okay! Got it")
                    .setPositiveBackgroundColorResource(R.color.bluegrey50)
                    .setPositiveTextColorResource(R.color.grey900)
                    .onPositive(new ToastOXDialog.ButtonCallback() {
                        @Override
                        public void onClick(@NonNull ToastOXDialog toastOXDialog) {
                            database.getReference("call_a_waiter").child(table).removeValue();
                        }
                    }).show();
        }
        catch(Exception e){
            Log.d("Error -> ",e.getMessage());
        }
    }

    private void dialogKitchenOpen(final String tab){
        try {
            new ToastOXDialog.Build(this)
                    .setTitle(tab.toUpperCase())
                    .setContent("Waiter needed at " + tab)
                    .setPositiveText("Okay! Got it")
                    .setPositiveBackgroundColorResource(R.color.bluegrey50)
                    .setPositiveTextColorResource(R.color.grey900)
                    .onPositive(new ToastOXDialog.ButtonCallback() {
                        @Override
                        public void onClick(@NonNull ToastOXDialog toastOXDialog) {
                            database.getReference("call_a_waiter_kitchen").child("kitchen").removeValue();
                        }
                    }).show();
        }
        catch(Exception e){
            Log.d("Error -> ",e.getMessage());
        }
    }

    private void addNotification(String table) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.phone)
                        .setContentTitle(table.toUpperCase())
                        .setContentText("Need of Waiter at " + table);

        Intent notificationIntent = new Intent(this, Home.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.InboxStyle());
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getFragmentManager().getBackStackEntryCount() == 1) {
                moveTaskToBack(false);
            }
            else {
                super.onBackPressed();
            }
        }
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
      /*  int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
//            fm1.beginTransaction().replace(R.id.cont,new OrderFragment()).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void logout() {
//        Toast.makeText(getApplicationContext(),"Log out", Toast.LENGTH_SHORT).show();
        SQLiteDatabase mydb = null;
        String dbname = "wineanddine.db";
        String dbpath = "/data/data/com.example.wineanddineadmin/databases/";
        String mypath = dbpath + dbname;
        try {
            mydb = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
            mydb.execSQL("delete from temp");
            Intent i1 = new Intent(getApplicationContext(), LandingActivity.class);
            startActivity(i1);
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent1.DummyItem item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}