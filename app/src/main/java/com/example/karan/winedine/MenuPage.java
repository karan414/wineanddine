package com.example.karan.winedine;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import pl.polak.clicknumberpicker.ClickNumberPickerListener;
import pl.polak.clicknumberpicker.PickerClickType;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseDatabase database;
    FoldingCellListAdapter adapter;
    LinearLayout top_menu;
    static ArrayList<Item> items = new ArrayList<>();
    static Map<String,Item> item_map=new HashMap<>();
    final Item i = new Item();
    ListView theListView;

    private OnFragmentInteractionListener mListener;

    public MenuPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuPage.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuPage newInstance(String param1, String param2) {
        MenuPage fragment = new MenuPage();
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
        View v = inflater.inflate(R.layout.fragment_menu_page, container, false);
        top_menu = (LinearLayout) v.findViewById(R.id.top_menu);
        theListView = (ListView) v.findViewById(R.id.mainListView);

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        adapter = new FoldingCellListAdapter(Home.cont, items);

        // add default btn handler for each request btn on each item if custom handler not found
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tt=(TextView)v;
                String quantity = "" + i.getQuantity();
                String title = "" + items.get(tt.getId()).getTitle();
                String price = "" + items.get(tt.getId()).getPrice();
                String url = "" + items.get(tt.getId()).getUrl();
                if(!(MyApplication.cart_item.containsKey(title)))
                    MyApplication.counterFab.increase();
                Toasty.success(MyApplication.maincontext, title+" added to cart", Toast.LENGTH_SHORT, true).show();
                MyApplication.cart_item.put(items.get(tt.getId()).getTitle()+"", new CartTemp(title, price, quantity,url));
            }
        });

        adapter.setClickNumberPickerListener(new ClickNumberPickerListener() {
            @Override
            public void onValueChange(float previousValue, float currentValue, PickerClickType pickerClickType) {
                i.setQuantity((int)currentValue);
            }
        });

        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });
        setItemsList();
        return v;
    }

    Map<String,Object> allsubmenu;
    public void setItemsList() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("menu");
        myRef.child(MyApplication.foodtype).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Object> temp = (Map<String, Object>) dataSnapshot.getValue();
                final List<String> allkeys = new ArrayList<String>();
                allkeys.addAll(temp.keySet());

                for (int i = 0; i < allkeys.size(); i++) {
                    Button tv1 = new Button(MyApplication.maincontext);
                    tv1.setId(i);
                    float density =getResources().getDisplayMetrics().density;
                    tv1.setLayoutParams(new ViewGroup.LayoutParams(300, ViewGroup.LayoutParams.MATCH_PARENT));
                    tv1.setText(allkeys.get(i));
                    tv1.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
                    tv1.setBackgroundResource(R.color.bgContentTop);
                    tv1.setTextColor(Color.WHITE);
                    tv1.setPadding((int)(0*density),(int)(0*density), (int)(10*density), (int)(10*density));

                    TextView tv = new TextView(Home.cont);
                    tv.setLayoutParams(new ViewGroup.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT));
                    tv.setBackgroundResource(R.color.bgContent);

                    if(i==0)
                    {
                        int getid=0;
                        try {
                            items.clear();
                        }
                        catch(Exception e) {
                            Log.d("Error Message: ",e.getMessage());
                        }
                        allsubmenu = (Map<String,Object>)temp.get(allkeys.get(getid));
                        List<String> allkeys1 = new ArrayList<String>();
                        allkeys1.addAll(allsubmenu.keySet());
                        items.clear();
                        for (int j = 0; j < allkeys1.size(); j++) {
                            Map<String, Object> allsubmenu1 = (Map<String, Object>) allsubmenu.get(allkeys1.get(j));
                            Item ii = new Item("\u20B9"+allsubmenu1.get("price").toString(),allsubmenu1.get("description").toString(),allsubmenu1.get("name")+"",allsubmenu1.get("url").toString());
                            ii.setId(j);
                            items.add(ii);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    tv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int getid = v.getId();
                            allsubmenu = (Map<String,Object>)temp.get(allkeys.get(getid));
                            List<String> allkeys1 = new ArrayList<String>();
                            allkeys1.addAll(allsubmenu.keySet());
                            items.clear();
                            for (int j = 0; j < allkeys1.size(); j++) {
                                try {
                                    Map<String, Object> allsubmenu1 = (Map<String, Object>) allsubmenu.get(allkeys1.get(j));
                                    Item ii = new Item("\u20B9" + allsubmenu1.get("price").toString(), allsubmenu1.get("description").toString(), allsubmenu1.get("name") + "",allsubmenu1.get("url").toString());
                                    ii.setId(j);
                                    items.add(ii);
                                    adapter.notifyDataSetChanged();
                                }catch(Exception e) {
                                    Log.d("Error -> ",e.getMessage());
                                }
                            }
                        }
                    });
                    top_menu.addView(tv1);
                    top_menu.addView(tv);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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