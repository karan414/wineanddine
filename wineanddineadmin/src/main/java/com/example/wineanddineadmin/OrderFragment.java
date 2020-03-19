package com.example.wineanddineadmin;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wineanddineadmin.dummy.DummyContent1;
import com.example.wineanddineadmin.dummy.DummyContent1.DummyItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OrderFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener,DetailFragment.OnFragmentInteractionListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("current_order");
    MyOrderRecyclerViewAdapter adpter;
    ValueEventListener ch;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OrderFragment newInstance(int columnCount) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            addItem();
            adpter = new MyOrderRecyclerViewAdapter(DummyContent1.ITEMS, mListener);
            recyclerView.setAdapter(adpter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(Home.maincont, this));
        }
        return view;
    }

    public void addItem() {
        /*if (DummyContent1.ITEM_MAP.size() > 0) {
            DummyContent1.ITEM_MAP.clear();
            DummyContent1.ITEMS.clear();
        }*/
        ch=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (DummyContent1.ITEM_MAP.size() > 0) {
                        DummyContent1.ITEM_MAP.clear();
                        DummyContent1.ITEMS.clear();
                    }
                    Map<String, Object> temp = (Map<String, Object>) dataSnapshot.getValue();
                    List<String> keys = new ArrayList<String>();
                    keys.addAll(temp.keySet());
                    for(int i=0; i<keys.size(); i++) {
                        DummyContent1.addItem(new DummyItem(keys.get(i).toUpperCase().toString()));
                        adpter.notifyDataSetChanged();
                    }
                }
                catch(Exception e){
                    Log.d("Error -> ",e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(ch);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        myRef.removeEventListener(ch);
    }

    static Map<String,Object> temp;
    static List<String> keys;
    static String table_id;
    static int pos;
    @Override
    public void onItemClick(View childView, int position) {

        pos = position;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    temp = (Map<String, Object>) dataSnapshot.getValue();
                    keys = new ArrayList<String>();
                    keys.addAll(temp.keySet());
                    table_id = keys.get(pos);
                }
                catch(Exception e) {
                    Log.d("Error -> ",e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FragmentTransaction fragmentTransaction = Home.fm1.beginTransaction();
        fragmentTransaction.replace(R.id.cont, new DetailFragment()).addToBackStack(null);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
