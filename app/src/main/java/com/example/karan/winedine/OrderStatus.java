package com.example.karan.winedine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderStatus.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderStatus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderStatus extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String message = "";
    Button get_invoice;

    private OnFragmentInteractionListener mListener;

    public OrderStatus() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderStatus.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderStatus newInstance(String param1, String param2) {
        OrderStatus fragment = new OrderStatus();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_status, container, false);
        getFragmentManager().beginTransaction().replace(R.id.order_cont, new OrderItemFragment()).commit();

        get_invoice = (Button) v.findViewById(R.id.get_invoice);
        checkForInvoice();
//        get_invoice.setVisibility(View.VISIBLE);
        get_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                generateInvoice();
            }
        });
        return v;
    }

    public void generateInvoice() {
        try {
            getUrldata();
        } catch (Exception e) {
            Toast.makeText(MyApplication.maincontext, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error -> ", e.getMessage());
        }
    }

    int count;

    private void checkForInvoice() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("current_order");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> temp = (Map<String, Object>) dataSnapshot.getValue();
                    List<String> keys = new ArrayList<String>();
                    keys.addAll(temp.keySet());

                    count = 0;
                    Map<String, Object> temp1 = (Map<String, Object>) temp.get(keys.get(0));
                    Map<String, Object> temp2 = (Map<String, Object>) temp1.get("order");
                    List<String> keys1 = new ArrayList<String>();
                    keys1.addAll(temp2.keySet());
                    for (int i = 0; i < keys1.size(); i++) {
                        Map<String, Object> temp3 = (Map<String, Object>) temp2.get(keys1.get(i));
                        if (temp3.get("status").toString().equals("complete")) {
                            count++;
                        }
                    }
                    if (count == keys1.size()) {
                        message = "Total amount to be paid is Rs." + temp1.get("total").toString();
                        get_invoice.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.d("Error -> ", e.getMessage());
        }
    }


    private void getUrldata() {
        class GetImage extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(Home.cont);
                loading.setCancelable(false);
                loading.setMessage("Sending Invoice...");
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loading.show();
            }

            @Override
            protected void onPostExecute(String st) {
                super.onPostExecute(st);
                loading.dismiss();

                if (st.equals("true")) {
                    Toasty.success(MyApplication.maincontext, "Invoice sent", Toast.LENGTH_SHORT, true).show();
                } else if(st.equals("false")){
                    Toasty.error(MyApplication.maincontext, "Invoice not sent", Toast.LENGTH_SHORT, true).show();
                } else
                    Toasty.error(MyApplication.maincontext, "Invoice not not sent", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            protected String doInBackground(String... params) {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                Webservices ws = new Webservices();
                ws.setUrl("http://wineanddine.co.nf/send_bill.php");
                ws.addParam("9426122178", "uid");
                ws.addParam("reecha14595", "pwd");
                ws.addParam(MainActivity.phone_number, "phone");
                ws.addParam(message,"msg");
                ws.connect();
                String data2 = ws.getData();
                return data2;
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        MyApplication.counterFab.setVisibility(View.INVISIBLE);
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MyApplication.counterFab.setVisibility(View.VISIBLE);
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
