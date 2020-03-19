package com.example.karan.winedine;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Simple POJO model for example
 */
public class Item {

    private String price;
    private String description;
    private String title;
    public int id;
    private int quantity;
    private String url;

    private View.OnClickListener requestBtnClickListener;

    public Item() {
    }

    public Item(String price, String description, String title, String url) {
        this.price = price;
        this.description = description;
        this.title = title;
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (price != null ? !price.equals(item.price) : item.price != null) return false;
        if (description != null ? !description.equals(item.description) : item.description != null)
            return false;
//        if (title != null ? !title.equals(item.title) : item.title != null)
//            return false;
        return !(title != null ? !title.equals(item.title) : item.title != null);
    }

    @Override
    public int hashCode() {
        int result = price != null ? price.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return price+" "+title;
    }

    /**
     * @return List of elements prepared for tests
     */
  /*  public static ArrayList<Item> getTestingList() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("$14", "description about the dish ABC", "ABC"));
        items.add(new Item("$23", "description about the dish DEF", "DEF"));
        items.add(new Item("$63", "description about the dish PQR", "PQR"));
        items.add(new Item("$19", "description about the dish MNO", "MNO"));
        items.add(new Item("$50", "description about the dish XYZ", "XYZ"));
        return items;

    }
*/
}
