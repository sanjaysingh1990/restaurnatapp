package com.imenu.fr.restaurant.fragment.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imenu.fr.restaurant.ActionPerformed;
import com.imenu.fr.restaurant.HomeActivity;
import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.adapter.OrderDetailsAdapter;
import com.imenu.fr.restaurant.api.model.login.LoginRequest;
import com.imenu.fr.restaurant.api.model.login.LoginResponse;
import com.imenu.fr.restaurant.api.model.order.OrderData;
import com.imenu.fr.restaurant.api.model.order.OrderItem;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderAcceptReject;
import com.imenu.fr.restaurant.api.model.updateitemstatus.UpdateItemStatusRequest;
import com.imenu.fr.restaurant.databinding.FragmentPendingDetailsBinding;
import com.imenu.fr.restaurant.datatypes.Address;
import com.imenu.fr.restaurant.datatypes.Item;
import com.imenu.fr.restaurant.fragment.BaseFragment;
import com.imenu.fr.restaurant.fragment.login.ILoginContract;
import com.imenu.fr.restaurant.fragment.login.LoginPresenterImp;
import com.imenu.fr.restaurant.utils.Constants;
import com.imenu.fr.restaurant.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class PendingDetailsFragment extends BaseFragment implements ILoginContract.LoginView {

    private FragmentPendingDetailsBinding binding;
    private LoginPresenterImp presenterImp;
    private LoginRequest loginRequest;
    private List<Object> mItemList;
    private OrderDetailsAdapter mAdapter;
    private OrderAcceptReject orderAcceptReject;
    private OrderData orderData;

    /**
     * Create a new instance of the fragment
     */
    public static PendingDetailsFragment newInstance() {
        PendingDetailsFragment fragment = new PendingDetailsFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_pending_details, container, false));
        binding.setClickEvent(new ClickHandler());

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    void init() {
        getActivity().setTitle(getResources().getString(R.string.pending));
        presenterImp = new LoginPresenterImp(this);
        mItemList = new ArrayList<Object>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        mAdapter = new OrderDetailsAdapter(getActivity(), mItemList);
        binding.recyclerView.setAdapter(mAdapter);
        addItems();
    }


    private void addItems() {
        orderData = (OrderData) getActivity().getIntent().getSerializableExtra(Constants.DATA);
        DecimalFormat formatter = new DecimalFormat("#.##");

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

            //order accept rejecct
            orderAcceptReject = new OrderAcceptReject();
            orderAcceptReject.setOrderId(orderData.getOrderId());
            orderAcceptReject.setStoreId(1);

            if (orderData.getReadStatus() == 0) {
                int storeId = Utils.getInstance().getValue(Constants.STORE_ID, 0, getActivity());
                UpdateItemStatusRequest updateItemStatusRequest = new UpdateItemStatusRequest();
                updateItemStatusRequest.setStoreId(storeId);
                updateItemStatusRequest.setOrderId(orderData.getOrderId());
                updateItemStatusRequest.setStatus(1);
                EventBus.getDefault().post(updateItemStatusRequest);
            }
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

    @Override
    public void showProgressDialog() {


    }

    @Override
    public void hideProgressDialog() {
    }

    @Override
    public void onServerError() {
        showAlert(getResources().getString(R.string.failed_toconnnect_server));
    }

    @Override
    public void onError(String message) {

        showAlert(message);

    }

    @Override
    public void showAlert(String message) {
        Utils.getInstance().showAlert(message, getActivity());

    }

    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterImp.onDestroyView();
    }

    private void sendMessage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(orderAcceptReject);
            }
        }, 300);

    }

    public class ClickHandler {

        public void accept(View view) {

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
                    orderAcceptReject.setOrderStatus(Constants.ORDER_ACCEPTED);
                    orderAcceptReject.setType(1); //to indicate accept
                    sendMessage();
                    if (orderData.getReadStatus() == 0) {

                        EventBus.getDefault().post(0.0f);
                    }

                }
            });

        }

        public void reject(View view) {
            String title1 = "Are you sure?";
            String messag1 = "Want to reject this order?";
            String title2 = "Rejected!";
            String message2 = "Order rejected successfully";
            String confirmTxt1 = "YES";
            String confirmTxt2 = "NO";
            showWarningAlert(title1, title2, messag1, message2, confirmTxt1, confirmTxt2, new ActionPerformed() {
                @Override
                public void actionPerformed() {
                    getActivity().onBackPressed();
                    orderAcceptReject.setOrderStatus(Constants.ORDER_REJECTED);
                    orderAcceptReject.setType(2); //to indicate reject
                    sendMessage();
                    if (orderData.getReadStatus() == 0) {

                        EventBus.getDefault().post(0.0f);
                    }
                }
            });
        }
    }

}
