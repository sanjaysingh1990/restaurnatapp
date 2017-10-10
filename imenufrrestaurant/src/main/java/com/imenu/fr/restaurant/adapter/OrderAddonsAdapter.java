package com.imenu.fr.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.api.model.order.Addon;
import com.imenu.fr.restaurant.utils.Constants;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by android on 25/4/17.
 */

public class OrderAddonsAdapter extends RecyclerView.Adapter<OrderAddonsAdapter.ItemHolder> {


    private List<Addon> itemList;

    public OrderAddonsAdapter(List<Addon> items) {
        this.itemList = items;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_addons, parent, false);
        return new ItemHolder(itemView);


    }

    @Override
    public void onBindViewHolder(ItemHolder viewHolder, int position) {

        Addon addon = itemList.get(position);
        DecimalFormat formatter = new DecimalFormat("0.00");
        Float itemPrice = 0.0f;
        int quant = 0;
        try {
            itemPrice = Float.parseFloat(addon.getPrice());
            quant = Integer.parseInt(addon.getQuantity());
        } catch (Exception ex) {

        }
        Float totalAddonsPrice = quant * itemPrice*1.0f;
        viewHolder.txtItemName.setText(addon.getAddonName());
        viewHolder.txtItemPrice.setText(Constants.EURO + formatter.format(totalAddonsPrice));
        viewHolder.txtItemUnitPrice.setText(Constants.EURO + formatter.format(itemPrice));
        String quantity = "Quantity : " + addon.getQuantity();
        viewHolder.txtItemQuantity.setText(quantity);


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView txtItemName;
        private TextView txtItemQuantity;
        private TextView txtItemPrice;
        private TextView txtItemUnitPrice;

        ItemHolder(View view) {
            super(view);
            txtItemName = (TextView) view.findViewById(R.id.txt_item_name);
            txtItemQuantity = (TextView) view.findViewById(R.id.txt_quantity);
            txtItemPrice = (TextView) view.findViewById(R.id.txt_price);
            txtItemUnitPrice = (TextView) view.findViewById(R.id.txt_unit_price);
        }


    }


}