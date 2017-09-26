package com.imenu.fr.restaurant.fragment.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imenu.fr.restaurant.ActionPerformed;
import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.adapter.OrderDetailsAdapter;
import com.imenu.fr.restaurant.api.model.order.OrderData;
import com.imenu.fr.restaurant.api.model.order.OrderItem;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderReadyToDeliver;
import com.imenu.fr.restaurant.databinding.FragmentAcceptedDetailsBinding;
import com.imenu.fr.restaurant.datatypes.Address;
import com.imenu.fr.restaurant.datatypes.Item;
import com.imenu.fr.restaurant.fragment.BaseFragment;
import com.imenu.fr.restaurant.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class AcceptedDetailsFragment extends BaseFragment {

    private FragmentAcceptedDetailsBinding binding;
    private List<Object> mItemList;
    private OrderDetailsAdapter mAdapter;
    private OrderReadyToDeliver orderReadyToDeliver;

    /**
     * Create a new instance of the fragment
     */
    public static AcceptedDetailsFragment newInstance() {
        AcceptedDetailsFragment fragment = new AcceptedDetailsFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_accepted_details, container, false));
        binding.setClickEvent(new ClickHandler());

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    void init() {
        String title = getActivity().getIntent().getStringExtra(Constants.DETAIL_HEADING);
        getActivity().setTitle(title);
        mItemList = new ArrayList<Object>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        mAdapter = new OrderDetailsAdapter(getActivity(), mItemList);
        binding.recyclerView.setAdapter(mAdapter);
        addItems();
    }


    private void addItems() {
        OrderData orderData = (OrderData) getActivity().getIntent().getSerializableExtra(Constants.DATA);
        DecimalFormat formatter = new DecimalFormat("0.00");

        if (orderData != null) {
            for (OrderItem dataitem : orderData.getItems()) {
                Item item = new Item();
                item.setItemName(dataitem.getItemName());
                item.setItemPrice(Constants.EURO+formatter.format(dataitem.getItemPrice()));
                item.setItemUnitPrice(Constants.EURO+formatter.format(dataitem.getUnitPrice()));
                item.setItemQuantity(String.valueOf(dataitem.getQuantity()));
                mItemList.add(item);
            }
            //add address
            Address address = new Address();
            address.setBuyerName(orderData.getBuyerName());
            address.setBuyerAddress(orderData.getBuyerAddress());
            address.setTotalPrice(orderData.getTotalAmount());
            String deliveryDateTime = orderData.getDeliveryDate() + "  " + orderData.getDeliveryTime();
            address.setDeliveryDateTime(deliveryDateTime);
            address.setPhoneNo(orderData.getBuyerPhoneNo());
            mItemList.add(address);
            mAdapter.notifyDataSetChanged();
            orderReadyToDeliver = new OrderReadyToDeliver();
            //order ready to deliver
            orderReadyToDeliver.setOrderId(orderData.getOrderId());
            orderReadyToDeliver.setStoreId(1);
            orderReadyToDeliver.setOrderStatus(Constants.ORDER_READY_TO_DELIVER);
        }
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Constants.FROM)) {
           // binding.btnAccept.setVisibility(View.VISIBLE);
        } else {
           // binding.btnAccept.setVisibility(View.GONE);

        }
    }


    /**
     * ********************** to validate email field *****************************
     *
     * @param email
     * @return
     */
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public class ClickHandler {

        public void readytodeliver(View view) {

            String title1 = "Are you sure?";
            String messag1 = "Want to accept this order?";
            String title2 = "Accepted!";
            String message2 = "Order accepted successfully";
            String confirmTxt1 = "YES";
            String confirmTxt2 = "NO";
            showWarningAlert(title1, title2, messag1, message2, confirmTxt1, confirmTxt2, new ActionPerformed() {
                @Override
                public void actionPerformed() {
                    getActivity().onBackPressed();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            EventBus.getDefault().post(orderReadyToDeliver);
                        }
                    }, 300);

                }
            });

        }


    }


}
