package com.example.wineanddineadmin;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.wineanddineadmin.Home.fm1;
import static com.example.wineanddineadmin.OrderFragment.temp;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView table_id, id, date, detail, content;
    Button make_payment;

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        getActivity().setTitle("Details of "+OrderFragment.table_id);
        table_id = (TextView) v.findViewById(R.id.table_id);
        id = (TextView) v.findViewById(R.id.id);
        detail = (TextView) v.findViewById(R.id.detail);
        date = (TextView) v.findViewById(R.id.date);
        content = (TextView) v.findViewById(R.id.content);
        make_payment =(Button) v.findViewById(R.id.make_payment);
        try {
            Map<String, Object> temp1 = (Map<String, Object>) OrderFragment.temp.get(OrderFragment.keys.get(0));
            Map<String, Object> temp2 = (Map<String, Object>) temp1.get("order");
            List<String> keys1 = new ArrayList<String>();
            keys1.addAll(temp2.keySet());

            date.setText(temp1.get("date").toString());
            content.setText(temp1.get("total").toString());
            id.setText(temp1.get("user").toString());
            detail.setText(keys1.size() + "");
            table_id.setText(OrderFragment.table_id);

            make_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("order_history");
                    try {
                        List<String> keys = new ArrayList<String>();
                        keys.addAll(OrderFragment.temp.keySet());
                        Map<String, Object> temp1 = (Map<String, Object>) OrderFragment.temp.get(keys.get(OrderFragment.pos));
                        Map<String, Object> temp2 = (Map<String, Object>) temp1.get("order");
                        List<String> keys1 = new ArrayList<String>();
                        keys1.addAll(temp2.keySet());
                        Map<String, Object> order = new HashMap<String, Object>();
                        for (int i = 0; i < keys1.size(); i++)
                            order.put("item" + (i + 1), keys1.get(i));

                        myRef.child(temp1.get("user").toString()).child(temp1.get("date").toString()).setValue(order);

                        DatabaseReference myRef1 = database.getReference("current_order");
                        myRef1.child(table_id.getText().toString()).removeValue();
                        FragmentTransaction fragmentTransaction = fm1.beginTransaction();
                        fragmentTransaction.replace(R.id.cont, new OrderFragment()).addToBackStack(null);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                    } catch (Exception e) {
                        Log.d("Error -> ", e.getMessage());
                    }
                }
            });
        }
        catch(Exception e) {
            Log.d("Error -> ", e.getMessage());
        }
        return v;
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
