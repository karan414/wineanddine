package com.example.karan.winedine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import pl.polak.clicknumberpicker.ClickNumberPickerListener;
import pl.polak.clicknumberpicker.PickerClickType;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class FoldingCellListAdapter extends ArrayAdapter<Item> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private ClickNumberPickerListener clickNumberPickerListener;

    public FoldingCellListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.price = (TextView) cell.findViewById(R.id.title_price);
            viewHolder.description = (TextView) cell.findViewById(R.id.title_description);
            viewHolder.title = (TextView) cell.findViewById(R.id.title);
            viewHolder.contentRequestBtn = (TextView) cell.findViewById(R.id.add_to_cart);
            viewHolder.content_price = (TextView) cell.findViewById(R.id.content_price);
            viewHolder.content_dish_desc = (TextView) cell.findViewById(R.id.content_dish_desc);
            viewHolder.content_dish_name = (TextView) cell.findViewById(R.id.content_dish_name);
            viewHolder.cell_image = (ImageView) cell.findViewById(R.id.cell_image);
            viewHolder.picker = (pl.polak.clicknumberpicker.ClickNumberPickerView) cell.findViewById(R.id.quantity);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        // bind data from selected element to view through view holder
        viewHolder.price.setText(item.getPrice());
      	viewHolder.description.setText(item.getDescription());
      	viewHolder.title.setText(item.getTitle());
        viewHolder.content_dish_name.setText(item.getTitle());
        viewHolder.content_dish_desc.setText(item.getDescription());
        viewHolder.content_price.setText(item.getPrice());
        viewHolder.picker.setPickerValue(1);

        try{
            Glide.with(viewHolder.cell_image.getContext()).load(item.getUrl())
                    .override(335, 216)
                    .fitCenter()
                    .into(viewHolder.cell_image);
        }
        catch(Exception e) {

        }

        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            viewHolder.contentRequestBtn.setId(item.id);
            viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.contentRequestBtn.setId(item.id);
            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
        }
        viewHolder.picker.setId(item.id);
        viewHolder.picker.setClickNumberPickerListener(clickNumberPickerListener);
        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    public void setClickNumberPickerListener(ClickNumberPickerListener clickNumberPickerListener) {
        this.clickNumberPickerListener = clickNumberPickerListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView price;
        TextView contentRequestBtn;
        TextView description;
        TextView title;
        TextView content_price;
        TextView content_dish_desc;
        TextView content_dish_name;
        ImageView cell_image;
        pl.polak.clicknumberpicker.ClickNumberPickerView picker;
    }
}
