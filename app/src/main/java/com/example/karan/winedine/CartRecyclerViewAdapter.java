package com.example.karan.winedine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.karan.winedine.CartFragment.OnListFragmentInteractionListener;
import com.example.karan.winedine.dummy.DummyContent1;
import com.example.karan.winedine.dummy.DummyContent1.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CartRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mDetailView.setText(mValues.get(position).details);
        try{
            Glide.with(holder.imageView.getContext()).load(mValues.get(position).url)
                    .override(300, 600)
                    .fitCenter()
                    .into(holder.imageView);
        }
        catch(Exception e)
        {
            Log.d("Error -> ",e.getMessage());
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder((Activity)v.getContext())
                    .setTitle("Remove the Item")
                    .setMessage("Are you sure to remove the item?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        MyApplication.cart_item.remove(holder.mIdView.getText()+"");
                        DummyItem d= DummyContent1.ITEM_MAP.get(holder.mIdView.getText()+"");
                        DummyContent1.ITEMS.remove(d);
                        DummyContent1.ITEM_MAP.remove(holder.mIdView.getText()+"");
                        CartFragment.adpter.notifyDataSetChanged();
                        CartFinal.addprice();
                        MyApplication.counterFab.decrease();
                        if(MyApplication.cart_item.isEmpty()) {
                            Home.fm.beginTransaction().replace(R.id.maincont, new empty_cart()).commit();
                        }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            }
        });

        holder.mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(holder.mDetailView.getText()+"");
                if(temp > 1) {
                    temp -= 1;
                    holder.mDetailView.setText(temp+"");
                    String quantity = "" + holder.mDetailView.getText();
                    String title = "" +  holder.mIdView.getText();
                    String price = "" + holder.mContentView.getText();
                    String ur = mValues.get(position).url + "";
                    DummyItem d= DummyContent1.ITEM_MAP.get(holder.mIdView.getText()+"");
                    DummyContent1.ITEMS.remove(d);
                    MyApplication.cart_item.put(title, new CartTemp(title,price,quantity,ur));
                    DummyContent1.addItem(new DummyItem(title,price,quantity,ur));
                    CartFragment.adpter.notifyDataSetChanged();
                    CartFinal.addprice();
                }
                else {
                    new AlertDialog.Builder((Activity)v.getContext())
                            .setTitle("Remove the Item")
                            .setMessage("Are you sure to remove the item?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    MyApplication.cart_item.remove(holder.mIdView.getText()+"");
                                    DummyItem d= DummyContent1.ITEM_MAP.get(holder.mIdView.getText()+"");
                                    DummyContent1.ITEMS.remove(d);
                                    DummyContent1.ITEM_MAP.remove(holder.mIdView.getText()+"");
                                    CartFragment.adpter.notifyDataSetChanged();
                                    MyApplication.counterFab.decrease();
                                    CartFinal.addprice();
                                    if(MyApplication.cart_item.isEmpty()) {
                                        Home.fm.beginTransaction().replace(R.id.maincont, new empty_cart()).commit();
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        holder.mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(holder.mDetailView.getText()+"");
                temp += 1;
                holder.mDetailView.setText(temp+"");
                String quantity = "" + holder.mDetailView.getText();
                String title = "" +  holder.mIdView.getText();
                String price = "" + holder.mContentView.getText();
                String ur = mValues.get(position).url + "";
                DummyItem d= DummyContent1.ITEM_MAP.get(holder.mIdView.getText()+"");
                DummyContent1.ITEMS.remove(d);
                MyApplication.cart_item.put(title, new CartTemp(title,price,quantity,ur));
                DummyContent1.addItem(new DummyItem(title,price,quantity,ur));
                CartFinal.addprice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mDetailView;
        public DummyItem mItem;
        public TextView mCancelButton;
        public TextView mPlusButton;
        public TextView mMinusButton;
        public final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mDetailView = (TextView) view.findViewById(R.id.detail);
            mCancelButton = (TextView) view.findViewById(R.id.cancel_button);
            mMinusButton = (TextView) view.findViewById(R.id.minus_button);
            mPlusButton = (TextView) view.findViewById(R.id.plus_button);
            imageView = (ImageView) view.findViewById(R.id.cart_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'" + "\t" + " '" + mContentView.getText() + "'" + "\t" + " '" + mDetailView.getText();
        }
    }
}
