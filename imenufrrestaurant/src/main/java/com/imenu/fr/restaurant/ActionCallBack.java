package com.imenu.fr.restaurant;

/**
 * Created by android on 20/6/17.
 */

public interface ActionCallBack {
    void acceptOrder(int position);
    void cancelOrder(int position);
    void readyToDeliver(int position);
    void showDetails(int position);



}
