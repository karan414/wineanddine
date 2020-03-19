package com.example.karan.winedine;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import java.util.ArrayList;
import java.util.List;

public class TestStackAdapter extends StackAdapter<Integer> {

    public TestStackAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            h.onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        view = getLayoutInflater().inflate(R.layout.list_card_item, parent, false);
        return new ColorItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.list_card_item;
    }

    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;
        String[] menu_names = new String[]{"Chinese", "Punjabi", "South Indian", "Fast Food", "Desserts",  "Beverages"};
        Integer[] menu_images = new Integer[]{R.drawable.food_1,R.drawable.food2,R.drawable.food3,R.drawable.food4,R.drawable.food5,R.drawable.food6};

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  MyApplication.foodtype=menu_names[v.getId()];
                    FragmentTransaction fragmentTransaction = Home.fm.beginTransaction();
                    fragmentTransaction.replace(R.id.maincont, new MenuPage()).addToBackStack(null);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }
            });
        }

        @Override
        public void onItemExpand(boolean b) {
            //mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
//            mLayout.getResources().getLayout(R.layout.layout_empty_view);
//            mLayout.setBackgroundResource(menu_images[position]);
            mLayout.setId(position);

            mTextTitle.setText(menu_names[position].toString());
//            mTextTitle.setVisibility(View.INVISIBLE);
        }

    }
}
