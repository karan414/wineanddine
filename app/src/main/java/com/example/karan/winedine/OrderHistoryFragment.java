package com.example.karan.winedine;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView history_listView;
    private ArrayAdapter<String> listAdapter;
    private LinearLayout order_history_linear_layout;

    private OnFragmentInteractionListener mListener;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("order_history").child(MainActivity.phone_number);
    Map <String, Object> user_data;
    List<String> keyset = new ArrayList<String>();
    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryFragment newInstance(String param1, String param2) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
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
        View v = inflater.inflate(R.layout.fragment_order_history, container, false);
        history_listView = (ListView) v.findViewById(R.id.order_history_listview);
        getActivity().setTitle("Order History");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    user_data = (Map<String,Object>) dataSnapshot.getValue();
                    keyset.addAll(user_data.keySet());
                    listAdapter = new ArrayAdapter<String>(Home.myContext,R.layout.order_history_view, R.id.order_history_date, keyset);
                    history_listView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                }
                catch(Exception e){
                    Log.d("Error -> ",e.getMessage());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        itemClick();
        return v;
    }

    public  void itemClick() {
        history_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(Home.myContext);
                dialog.setContentView(R.layout.order_history_dialog);
                dialog.setTitle(keyset.get(position));
                order_history_linear_layout = (LinearLayout) dialog.findViewById(R.id.order_history_linear_layout);

                Map<String,Object> temp = (Map<String,Object>) user_data.get(keyset.get(position));
                List<String> item_list = new ArrayList<String>();
                item_list.addAll(temp.keySet());

                for(int i=0; i<item_list.size(); i++) {
                    TextView tv = new TextView(Home.myContext);
                    tv.setId(i);
                    float density =getResources().getDisplayMetrics().density;
                    tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    tv.setText(temp.get(item_list.get(i)).toString());
                    tv.setPadding((int)(10*density),(int)(10*density), (int)(10*density), (int)(10*density));
                    order_history_linear_layout.addView(tv);
                }
                dialog.show();
            }
        });
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
        MyApplication.counterFab.setVisibility(View.VISIBLE);
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
