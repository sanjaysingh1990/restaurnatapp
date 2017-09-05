package com.imenu.fr.restaurant.fragment.orders;


import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.model.devicetoken.UpdteTokenRequest;
import com.imenu.fr.restaurant.api.model.order.OrderRequest;
import com.imenu.fr.restaurant.api.model.order.OrderResponse;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderOperationRequest;
import com.imenu.fr.restaurant.api.model.updateitemstatus.UpdateItemStatusRequest;
import com.imenu.fr.restaurant.fragment.IBasePresenter;
import com.imenu.fr.restaurant.fragment.IBaseView;

/**
 * Created by android on 16/5/17.
 */

public interface IOrderContract {
    interface OrderView extends IBaseView {
        void onSuccess(OrderResponse orderResponse);

        void onSuccess(String message);

        void onSuccess(Status status);

        void onItemStatusUpdateSuccess(Status status);

    }

    interface OrderPresenter extends IBasePresenter {
        void requestData(OrderRequest orderRequest);

        void orderOperation(OrderOperationRequest orderOperationRequest);

        void updateItemStatus(UpdateItemStatusRequest updateItemStatusRequest);
        void onSuccess(OrderResponse orderResponse);

        void onSuccess(String message);
        void onItemStatusUpdateSuccess(Status status);

        void onSuccess(Status status);

        void updateDeviceToken(UpdteTokenRequest updteTokenRequest);
        void getOrderDetails(int orderId);


    }
}
