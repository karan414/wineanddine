package com.example.karan.winedine;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karan.winedine.dummy.DummyContent;
import com.example.karan.winedine.dummy.DummyContent1;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFinal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFinal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFinal extends Fragment implements CartFragment.OnListFragmentInteractionListener, OrderItemFragment.OnListFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button place_order;
    static TextView cart_price;

    private OnFragmentInteractionListener mListener;

    public CartFinal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFinal.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFinal newInstance(String param1, String param2) {
        CartFinal fragment = new CartFinal();
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
        View v = inflater.inflate(R.layout.fragment_cart_final, container, false);
        getFragmentManager().beginTransaction().replace(R.id.cart_cont,new CartFragment()).commit();
        getActivity().setTitle("Cart");

        place_order = (Button)v.findViewById(R.id.place_order);
        cart_price = (TextView)v.findViewById(R.id.cart_price);
        addprice();

        if(MyApplication.cart_item.isEmpty())
            getFragmentManager().beginTransaction().replace(R.id.maincont,new empty_cart()).commit();

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
        return v;
    }

    static String st="";
    static void addprice() {
        double temp=0;
        DummyContent1.DummyItem d;
        for(int i = 0; i< DummyContent1.ITEMS.size(); i++) {
            d = DummyContent1.ITEMS.get(i);
            st = d.getContent();
            temp += (Double.parseDouble(st.substring(1))*Double.parseDouble(d.getDetails()));
        }
        cart_price.setText(temp+"");
    }

    void placeOrder() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Map<String, Object> upload_order = new HashMap<String, Object>();
        upload_order.put("user",MainActivity.phone_number);
        upload_order.put("date",date);
        upload_order.put("total",cart_price.getText().toString());
        upload_order.put("order", DummyContent1.ITEM_MAP);
        myRef.child("current_order").child(MainActivity.table_number).setValue(upload_order);
        Toasty.success(MyApplication.maincontext, "Order successfully placed", Toast.LENGTH_SHORT, true).show();
        MyApplication.counterFab.setCount(0);
        MyApplication.cart_item.clear();

        getFragmentManager().beginTransaction().replace(R.id.maincont,new OrderItemFragment()).commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyApplication.counterFab.setVisibility(View.INVISIBLE);
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

    @Override
    public void onListFragmentInteraction(DummyContent1.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

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
