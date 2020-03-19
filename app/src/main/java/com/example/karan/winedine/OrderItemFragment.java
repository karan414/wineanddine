package com.example.karan.winedine;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karan.winedine.dummy.DummyContent;
import com.example.karan.winedine.dummy.DummyContent.DummyItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.karan.winedine.CartFragment.adpter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OrderItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("current_order");
    MyOrderItemRecyclerViewAdapter adaapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OrderItemFragment newInstance(int columnCount) {
        OrderItemFragment fragment = new OrderItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_order_item_list, container, false);
        getActivity().setTitle("Order Status");
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
            adaapter = new MyOrderItemRecyclerViewAdapter(DummyContent.ITEMS, mListener);
            recyclerView.setAdapter(adaapter);
        }
        return view;
    }

    public void addItem() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (DummyContent.ITEM_MAP.size() > 0) {
                        DummyContent.ITEM_MAP.clear();
                        DummyContent.ITEMS.clear();
                    }
                    Map<String, Object> temp = (Map<String, Object>) dataSnapshot.getValue();
                    List<String> keys = new ArrayList<String>();
                    keys.addAll(temp.keySet());

                    Map<String, Object> temp1 = (Map<String, Object>) temp.get(keys.get(0));
                    Map<String, Object> temp2 = (Map<String, Object>) temp1.get("order");
                    List<String> keys1 = new ArrayList<String>();
                    keys1.addAll(temp2.keySet());
                    for (int i = 0; i < keys1.size(); i++) {
                        Map<String, Object> temp3 = (Map<String, Object>) temp2.get(keys1.get(i));
                        DummyContent.addItem(new DummyContent.DummyItem(temp3.get("id").toString(), temp3.get("details").toString(), temp3.get("status").toString()));
                        adaapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e){
                    Log.d("Error -> ",e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyApplication.counterFab.setVisibility(View.INVISIBLE);
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
        MyApplication.counterFab.setVisibility(View.VISIBLE);
        mListener = null;
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
