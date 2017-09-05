package com.imenu.fr.restaurant.fragment.orders;


import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.model.devicetoken.UpdteTokenRequest;
import com.imenu.fr.restaurant.api.model.order.OrderRequest;
import com.imenu.fr.restaurant.api.model.order.OrderResponse;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderOperationRequest;
import com.imenu.fr.restaurant.api.model.updateitemstatus.UpdateItemStatusRequest;

/**
 * Created by android on 16/5/17.
 */

public class OrderPresenterImp implements IOrderContract.OrderPresenter {
    private IOrderContract.OrderView mOrderView;
    private OrderService model;

    public OrderPresenterImp(IOrderContract.OrderView orderView) {
        this.mOrderView = orderView;
        model = new OrderService(this);
    }


    @Override
    public void onServerError() {
        if (mOrderView != null) {
            mOrderView.hideProgressDialog();
            mOrderView.onServerError();
        }
    }

    @Override
    public void onApiError(String message) {
        if (mOrderView != null) {
            mOrderView.hideProgressDialog();
            mOrderView.onError(message);
        }
    }


    @Override
    public void onDestroyView() {
        mOrderView = null;
        model = null;

    }

    @Override
    public void requestData(OrderRequest orderRequest) {
        if (mOrderView != null) {
            // mOrderView.showProgressDialog();
            model.getOrders(orderRequest);
        }
    }

    @Override
    public void orderOperation(OrderOperationRequest orderOperationRequest) {
        if (mOrderView != null) {
            mOrderView.showProgressDialog();
            model.orderOperation(orderOperationRequest);
        }
    }

    @Override
    public void updateItemStatus(UpdateItemStatusRequest updateItemStatusRequest) {
        if (mOrderView != null) {

            model.updateItemStatus(updateItemStatusRequest);
        }
    }

    @Override
    public void onSuccess(OrderResponse orderResponse) {
        if (mOrderView != null) {
            mOrderView.hideProgressDialog();
            mOrderView.onSuccess(orderResponse);
        }
    }

    @Override
    public void onSuccess(String message) {
        if (mOrderView != null) {
            mOrderView.hideProgressDialog();
            mOrderView.onSuccess(message);
        }
    }

    @Override
    public void onItemStatusUpdateSuccess(Status status) {
        if (mOrderView != null) {
            mOrderView.onItemStatusUpdateSuccess(status);
        }
    }


    @Override
    public void onSuccess(Status status) {
        if (mOrderView != null) {
            mOrderView.hideProgressDialog();
            mOrderView.onSuccess(status);
        }
    }

    @Override
    public void updateDeviceToken(UpdteTokenRequest updteTokenRequest) {
        if (mOrderView != null) {

            model.updateDeviceToken(updteTokenRequest);
        }
    }

    @Override
    public void getOrderDetails(int orderId) {
        if (mOrderView != null) {
            mOrderView.showProgressDialog();
            model.getOrderDetails(orderId);
        }
    }
}
