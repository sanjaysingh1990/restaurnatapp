package com.imenu.fr.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.imenu.fr.restaurant.HomeActivity;
import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.model.order.OrderResponse;
import com.imenu.fr.restaurant.app.Imenufr;
import com.imenu.fr.restaurant.fragment.details.AcceptedDetailsFragment;
import com.imenu.fr.restaurant.fragment.details.PendingDetailsFragment;
import com.imenu.fr.restaurant.fragment.orders.IOrderContract;
import com.imenu.fr.restaurant.fragment.orders.OrderPresenterImp;
import com.imenu.fr.restaurant.utils.Constants;
import com.imenu.fr.restaurant.utils.Utils;

import org.greenrobot.eventbus.EventBus;

public class DetailsActivity extends BaseActivity implements IOrderContract.OrderView {
    private OrderPresenterImp presenterImp;
    private boolean fromNotification;
    private int mOrderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        presenterImp = new OrderPresenterImp(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showDetails();
    }

    void showDetails() {
        switch (getIntent().getExtras().getInt(Constants.DETAIL_VIEW)) {
            case Constants.PENDING_ORDER_DETAILS:

                addFragment(PendingDetailsFragment.newInstance());
                break;

            case Constants.ACCEPTED_ORDER_DETAILS:
                addFragment(AcceptedDetailsFragment.newInstance());
                break;
            default:
                int orderId = getIntent().getIntExtra(Constants.ORDER_ID, 0);
                presenterImp.getOrderDetails(orderId);
                fromNotification = true;

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon action bar is clicked; go to parent activity
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void showProgressDialog() {
        showProgress();
    }

    @Override
    public void hideProgressDialog() {
        hideProgress();

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
        Utils.getInstance().showAlert(message, this);
    }

    @Override
    public void onSuccess(OrderResponse orderResponse) {
        mOrderStatus = getIntent().getIntExtra(Constants.ORDER_STATUS, 0);

        if (orderResponse.getData() != null) {
            Intent intent = new Intent();
            intent.putExtra(Constants.DATA, orderResponse.getData().get(0));
            setIntent(intent);
            if (mOrderStatus == Constants.PENDING) {
                intent.putExtra(Constants.DETAIL_HEADING, getResources().getString(R.string.pending));
                addFragment(PendingDetailsFragment.newInstance());
            } else if (mOrderStatus == Constants.COMPLETED) {
                intent.putExtra(Constants.DETAIL_HEADING, getResources().getString(R.string.pending));
                addFragment(AcceptedDetailsFragment.newInstance());

            }
        }
    }

    @Override
    public void onSuccess(String message) {
        //nothing to do
    }

    @Override
    public void onSuccess(Status status) {
        //nothing to do
    }

    @Override
    public void onItemStatusUpdateSuccess(Status status) {

    }

    @Override
    public void onBackPressed() {

        if (fromNotification) {
            Imenufr imenufr = (Imenufr) getApplication();

            if (!imenufr.getStatus()) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (mOrderStatus == Constants.PENDING) {
                EventBus.getDefault().post(1d); //refresh the pending tab

            } else if (mOrderStatus == Constants.COMPLETED) {
                EventBus.getDefault().post(2d); //refresh the completed tab

            }


        }
        finish();


    }
}
