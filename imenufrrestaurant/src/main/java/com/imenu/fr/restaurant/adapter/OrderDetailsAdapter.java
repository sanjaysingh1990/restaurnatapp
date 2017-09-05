package com.imenu.fr.restaurant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.datatypes.Address;
import com.imenu.fr.restaurant.datatypes.Item;

import java.util.List;

/**
 * Created by android on 25/4/17.
 */

public class OrderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int ORDER_ITEM = 1;
    private final int ADDRESS = 2;
    private List<Object> itemList;
    private Activity mActivity;

    public OrderDetailsAdapter(Activity activity, List<Object> items) {
        this.itemList = items;
        this.mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case ORDER_ITEM:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_item, parent, false);
                return new ItemHolder(itemView);
            case ADDRESS:

                View addressView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_address, parent, false);
                return new AddressHolder(addressView);

        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case ORDER_ITEM:
                ItemHolder itemHolder = (ItemHolder) viewHolder;
                Item item = (Item) itemList.get(position);
                itemHolder.txtItemName.setText(item.getItemName());
                itemHolder.txtItemPrice.setText(item.getItemPrice());
                itemHolder.txtItemUnitPrice.setText(item.getItemUnitPrice());
                String quantity = "Quantity : " + item.getItemQuantity();
                itemHolder.txtItemQuantity.setText(quantity);
                break;
            case ADDRESS:
                AddressHolder addressHolder = (AddressHolder) viewHolder;
                Address address = (Address) itemList.get(position);
                addressHolder.txtBuyerName.setText(address.getBuyerName());
                addressHolder.txtBuyerAddress.setText(address.getBuyerAddress());
                addressHolder.txtOrderPrice.setText(address.getTotalPrice());
                addressHolder.txtPhoneNo.setText(address.getPhoneNo());
                addressHolder.txtDeliveryDateTime.setText(address.getDeliveryDateTime());
                break;
        }

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof Item) {
            return ORDER_ITEM;
        } else {
            return ADDRESS;
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
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

    private class AddressHolder extends RecyclerView.ViewHolder {
        private TextView txtBuyerName;
        private TextView txtBuyerAddress;
        private TextView txtOrderPrice;
        private TextView txtPhoneNo;
        private TextView txtDeliveryDateTime;


        AddressHolder(View view) {
            super(view);
            txtBuyerName = (TextView) view.findViewById(R.id.txt_buyer_name);
            txtBuyerAddress = (TextView) view.findViewById(R.id.txt_buyer_address);
            txtOrderPrice = (TextView) view.findViewById(R.id.txt_price);
            txtPhoneNo = (TextView) view.findViewById(R.id.text_phone_no);
            txtDeliveryDateTime = (TextView) view.findViewById(R.id.text_delivery_datetime);
            txtPhoneNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = "9411702581"; //mOrder.getBuyer_phone_no();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    mActivity.startActivity(intent);
                }
            });

        }

    }


}