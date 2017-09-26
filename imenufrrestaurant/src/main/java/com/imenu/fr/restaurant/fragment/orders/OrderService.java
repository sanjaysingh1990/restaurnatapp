package com.imenu.fr.restaurant.fragment.orders;


import android.util.Log;

import com.imenu.fr.restaurant.api.apihelper.APIHelper;
import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.model.devicetoken.UpdteTokenRequest;
import com.imenu.fr.restaurant.api.model.order.OrderRequest;
import com.imenu.fr.restaurant.api.model.order.OrderResponse;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderOperationRequest;
import com.imenu.fr.restaurant.api.model.updateitemstatus.UpdateItemStatusRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by android on 1/3/17.
 */

public class OrderService {
    private IOrderContract.OrderPresenter mOrderPresenter;

    public OrderService(IOrderContract.OrderPresenter orderPresenter) {
        this.mOrderPresenter = orderPresenter;
    }
    public void getOrders(OrderRequest orderRequest) {


        Call<OrderResponse> loginResponseCall = APIHelper.getInstance().createService().getOrder(orderRequest.getStore_id(), orderRequest.getStatus(), orderRequest.getLimit(), orderRequest.getOffset());
        loginResponseCall.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {

                if (response.isSuccessful()) {
                    mOrderPresenter.onSuccess(response.body());

                } else {

                    mOrderPresenter.onApiError(APIHelper.getInstance().handleApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                if (!call.isCanceled()) // to check weather request cancelled or not
                {
                    mOrderPresenter.onServerError();
                }

            }
        });

    }


    public void orderOperation(OrderOperationRequest orderOperationRequest) {


        Call<Status> loginResponseCall = APIHelper.getInstance().createService().orderOperation(orderOperationRequest);
        loginResponseCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    mOrderPresenter.onSuccess(response.body().getMessage());

                } else {
                    mOrderPresenter.onApiError(APIHelper.getInstance().handleApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                mOrderPresenter.onServerError();

            }
        });

    }


    public void updateDeviceToken(UpdteTokenRequest updteTokenRequest) {


        Call<Status> loginResponseCall = APIHelper.getInstance().createService().updateToken(updteTokenRequest);
        loginResponseCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    mOrderPresenter.onSuccess(response.body());

                } else {
                    mOrderPresenter.onApiError(APIHelper.getInstance().handleApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                mOrderPresenter.onServerError();

            }
        });

    }

    public void updateItemStatus(UpdateItemStatusRequest updateItemStatusRequest) {


        Call<Status> loginResponseCall = APIHelper.getInstance().createService().updateItemStatus(updateItemStatusRequest);
        loginResponseCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    mOrderPresenter.onItemStatusUpdateSuccess(response.body());

                } else {
                    mOrderPresenter.onApiError(APIHelper.getInstance().handleApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                mOrderPresenter.onServerError();

            }
        });

    }

    public void getOrderDetails(int orderId) {


        Call<OrderResponse> loginResponseCall = APIHelper.getInstance().createService().getOrderDetails(orderId);
        loginResponseCall.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    mOrderPresenter.onSuccess(response.body());

                } else {
                    mOrderPresenter.onApiError(APIHelper.getInstance().handleApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {

                mOrderPresenter.onServerError();


            }
        });

    }


}
