package com.imenu.fr.restaurant;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.imenu.fr.AHBottomNavigation;
import com.imenu.fr.AHBottomNavigationAdapter;
import com.imenu.fr.AHBottomNavigationViewPager;
import com.imenu.fr.restaurant.activity.BaseActivity;
import com.imenu.fr.restaurant.api.apihelper.APIHelper;
import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.model.devicetoken.UpdteTokenRequest;
import com.imenu.fr.restaurant.api.model.order.OrderRequest;
import com.imenu.fr.restaurant.api.model.order.OrderResponse;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderAcceptReject;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderOperationRequest;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderReadyToDeliver;
import com.imenu.fr.restaurant.api.model.updateitemstatus.UpdateItemStatusRequest;
import com.imenu.fr.restaurant.app.Imenufr;
import com.imenu.fr.restaurant.databinding.ActivityHomeBinding;
import com.imenu.fr.restaurant.fragment.AcceptedFragment;
import com.imenu.fr.restaurant.fragment.PendingFragment;
import com.imenu.fr.restaurant.fragment.orders.IOrderContract;
import com.imenu.fr.restaurant.fragment.orders.OrderPresenterImp;
import com.imenu.fr.restaurant.utils.Constants;
import com.imenu.fr.restaurant.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends BaseActivity implements IOrderContract.OrderView {

    private Fragment currentFragment;
    private DemoViewPagerAdapter adapter;
    private AHBottomNavigationAdapter navigationAdapter;
    private int[] tabColors;
    private Handler handler = new Handler();
    private OrderPresenterImp presenterImp;
    private OrderRequest orderRequest = new OrderRequest();
    private int mTotalNotification = 0;
    boolean mDoubleBackToExitPressedOnce = false;
    private boolean isPullToRefresh=false;
    // UI
    private AHBottomNavigationViewPager viewPager;
    private AHBottomNavigation bottomNavigation;
    private OrderOperationRequest mOrderOperationRequest;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean enabledTranslucentNavigation = getSharedPreferences("shared", Context.MODE_PRIVATE)
                .getBoolean("translucentNavigation", false);
        setTheme(enabledTranslucentNavigation ? R.style.AppTheme_TranslucentNavigation : R.style.AppTheme);
        //  setContentView(R.layout.activity_home);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        presenterImp = new OrderPresenterImp(this);
        EventBus.getDefault().register(this);
        Imenufr imenufr = (Imenufr) getApplication();
        imenufr.setStatus(true);


        initUI();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:


                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                Utils.getInstance().clearValueOfKey(this, Constants.LOGGED_IN);
                Utils.getInstance().saveValue(Constants.LOGGED_OUT, true, this);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        presenterImp.onDestroyView();
        EventBus.getDefault().unregister(this);
        Imenufr imenufr = (Imenufr) getApplication();
        imenufr.setStatus(false);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void readyToDeliver(OrderReadyToDeliver orderReadyToDeliver) {
        mOrderOperationRequest.setOrderStatus(orderReadyToDeliver.getOrderStatus());
        mOrderOperationRequest.setStoreId(orderReadyToDeliver.getStoreId());
        mOrderOperationRequest.setOrderId(orderReadyToDeliver.getOrderId());
        presenterImp.orderOperation(mOrderOperationRequest);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void readloadPendingData(Double type) {
        if (type == 1) {
            PendingFragment pendingFragment = (PendingFragment) adapter.getItem(0);
            pendingFragment.startPullToRefresh();
        } else if (type == 2) {
            extraFragment = (ExtraFragment) adapter.getItem(4);
            extraFragment.startPullToRefresh();

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshList(Integer position) {
        //   APIHelper.getInstance().cancelRequest(); //if in any tab request is pending cancel it
        isPullToRefresh=true;
        sendRequest(position, 0);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadmore(String offset) {
        int currentpos = viewPager.getCurrentItem();
        sendRequest(currentpos, Integer.valueOf(offset));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateStatus(UpdateItemStatusRequest updateItemStatusRequest) {
        presenterImp.updateItemStatus(updateItemStatusRequest);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBatchCount(Float val) {
        mTotalNotification--;
        updateBatch(mTotalNotification);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void noMoreData(Object obj) {
        showEmptyScreen(getResources().getString(R.string.no_data_found));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptRejectOrder(OrderAcceptReject orderAcceptReject) {
        mOrderOperationRequest.setOrderStatus(orderAcceptReject.getOrderStatus());
        mOrderOperationRequest.setStoreId(orderAcceptReject.getStoreId());
        mOrderOperationRequest.setOrderId(orderAcceptReject.getOrderId());
        presenterImp.orderOperation(mOrderOperationRequest);
//        Gson gson = new Gson();
//        Log.e("request", gson.toJson(mOrderOperationRequest));

    }


    /**
     * Init UI
     */
    private void initUI() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        viewPager = (AHBottomNavigationViewPager) findViewById(R.id.view_pager);
        tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
        navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_5);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);


        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setColored(true);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (currentFragment == null) {
                    currentFragment = adapter.getCurrentFragment();
                }

                if (wasSelected) {
                    if (currentFragment instanceof ExtraFragment) {

                        ((ExtraFragment) currentFragment).refresh();
                    } else if (currentFragment instanceof PendingFragment) {
                        ((PendingFragment) currentFragment).refresh();
                    } else if (currentFragment instanceof AcceptedFragment) {
                        ((AcceptedFragment) currentFragment).refresh();
                    }
                    return true;
                }

                if (currentFragment != null) {
                    if (currentFragment instanceof ExtraFragment) {

                        ((ExtraFragment) currentFragment).willBeHidden();
                    } else if (currentFragment instanceof PendingFragment) {
                        ((PendingFragment) currentFragment).willBeHidden();
                    } else if (currentFragment instanceof AcceptedFragment) {
                        ((AcceptedFragment) currentFragment).willBeHidden();
                    }

                }

                viewPager.setCurrentItem(position, false);

                if (currentFragment == null) {
                    return true;
                }

                currentFragment = adapter.getCurrentFragment();
                if (currentFragment instanceof ExtraFragment) {

                    ((ExtraFragment) currentFragment).willBeDisplayed();
                } else if (currentFragment instanceof PendingFragment) {
                    ((PendingFragment) currentFragment).willBeDisplayed();
                } else if (currentFragment instanceof AcceptedFragment) {
                    ((AcceptedFragment) currentFragment).willBeDisplayed();
                }


                showProgressDialog();
                sendRequest(position, 0);
                return true;
            }
        });


        viewPager.setOffscreenPageLimit(4);
        adapter = new DemoViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        currentFragment = adapter.getCurrentFragment();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressDialog();
                sendRequest(0, 0);
            }
        }, 300);

        mOrderOperationRequest = new OrderOperationRequest();
        updateFcmDeviceToken();
    }

    /**
     * ******************* manage all request to server here *********************
     *
     * @param position
     * @param offset
     */
    private void sendRequest(int position, int offset) {
        // APIHelper.getInstance().cancelRequest(); //if in any tab request is pending cancel it
        hideEmptyScreen();
        /**
         ****************** check network connection
         */
        if (!Utils.getInstance().isNetworkAvailable(this)) {
            showAlert(getResources().getString(R.string.no_internet_message));
            showEmptyScreen(getResources().getString(R.string.no_internet_message));
            hideProgressDialog();
            return;
        }
        ExtraFragment extraFragment;
        int storeId = Integer.parseInt(Utils.getInstance().getValue(Constants.STORE_ID, "0", this));
        switch (position) {

            case 0:
                PendingFragment pendingFragment = (PendingFragment) adapter.getItem(0);

                if (!pendingFragment.isDataLoaded) {
                    orderRequest.setStore_id(storeId);
                    orderRequest.setStatus(Constants.PENDING);
                    orderRequest.setLimit(Constants.LIMIT);
                    orderRequest.setOffset(offset);
                    presenterImp.requestData(orderRequest);
                } else {
                    hideProgressDialog();
                    showEmptyScreen(getResources().getString(R.string.no_data_found));
                }

                break;
            case 1:
                AcceptedFragment acceptedFragment = (AcceptedFragment) adapter.getItem(1);
                if (!acceptedFragment.isDataLoaded) {
                    orderRequest.setStore_id(storeId);
                    orderRequest.setStatus(Constants.ACCEPTED);
                    orderRequest.setLimit(Constants.LIMIT);
                    orderRequest.setOffset(offset);
                    presenterImp.requestData(orderRequest);
                } else {
                    hideProgressDialog();
                    showEmptyScreen(getResources().getString(R.string.no_data_found));

                }
                break;
            case 2:
                extraFragment = (ExtraFragment) adapter.getItem(2);
                if (!extraFragment.isDataLoaded) {
                    orderRequest.setStore_id(storeId);
                    orderRequest.setStatus(Constants.REJECTED);
                    orderRequest.setLimit(Constants.LIMIT);
                    orderRequest.setOffset(offset);
                    presenterImp.requestData(orderRequest);
                } else {
                    hideProgressDialog();
                    showEmptyScreen(getResources().getString(R.string.no_data_found));
                }
                break;

            case 3:
                extraFragment = (ExtraFragment) adapter.getItem(3);
                if (!extraFragment.isDataLoaded) {
                    orderRequest.setStore_id(storeId);
                    orderRequest.setStatus(Constants.DISPATCHED);
                    orderRequest.setLimit(Constants.LIMIT);
                    orderRequest.setOffset(offset);
                    presenterImp.requestData(orderRequest);
                } else {
                    hideProgressDialog();
                    showEmptyScreen(getResources().getString(R.string.no_data_found));
                }
                break;
            case 4:
                extraFragment = (ExtraFragment) adapter.getItem(4);
                if (!extraFragment.isDataLoaded) {
                    orderRequest.setStore_id(storeId);
                    orderRequest.setStatus(Constants.COMPLETED);
                    orderRequest.setLimit(Constants.LIMIT);
                    orderRequest.setOffset(offset);
                    presenterImp.requestData(orderRequest);
                } else {
                    hideProgressDialog();
                    showEmptyScreen(getResources().getString(R.string.no_data_found));

                }
                break;

        }
       Gson gson = new Gson();
        Log.e("request", gson.toJson(orderRequest));
    }

    @Override
    public void showProgressDialog() {
        showProgress();

    }

    @Override
    public void hideProgressDialog() {
        hideProgress();
        hidePullToRefresh(viewPager.getCurrentItem());
    }

    @Override
    public void onServerError() {
        showAlert(getResources().getString(R.string.failed_toconnnect_server));
        showEmptyScreen(getResources().getString(R.string.no_data_found));


    }

    @Override
    public void onError(String message) {
        showAlert(message);
        showEmptyScreen(getResources().getString(R.string.no_data_found));

    }

    @Override
    public void showAlert(String message) {

        Utils.getInstance().showAlert(message, this);
    }

    @Override
    public void onSuccess(OrderResponse orderResponse) {
        /**
         *********************** show data for current tab in viewpager ******************
         */

        int currentpage = viewPager.getCurrentItem();
        if (currentpage == 0 && orderResponse.getCount() > 0) {
            mTotalNotification = orderResponse.getCount();
            updateBatch(orderResponse.getCount());
        }
        ExtraFragment extraFragment = null;
        hideEmptyScreen(); //firt hide empty screen
        switch (orderResponse.getOrderStatus()) {
            case Constants.PENDING:
                PendingFragment pendingFragment = (PendingFragment) adapter.getItem(0);
                if (orderResponse.getData() != null) {
                    isPullToRefresh=false;
                    pendingFragment.loadData(orderResponse.getData());
                    /***
                     ***************** to check data is available for load more or not ***************
                     */
                    if (orderResponse.getData().size() < Constants.LIMIT) {
                        pendingFragment.mNoMoreLoad = true;
                    } else {
                        pendingFragment.mNoMoreLoad = false;

                    }
                } else {
                    showEmptyScreen(getResources().getString(R.string.no_data_found));
                    pendingFragment.removeLoadmore();
                    pendingFragment.isDataLoaded = true;
                    if(isPullToRefresh)
                    {
                        isPullToRefresh=false;
                        pendingFragment.clearList();
                    }
                }

                break;
            case Constants.ACCEPTED:
                AcceptedFragment acceptedFragment = (AcceptedFragment) adapter.getItem(1);
                if (orderResponse.getData() != null) {
                    isPullToRefresh=false;
                    acceptedFragment.loadData(orderResponse.getData());
                    /***
                     ***************** to check data is available for load more or not ***************
                     */

                    if (orderResponse.getData().size() < Constants.LIMIT) {
                        acceptedFragment.mNoMoreLoad = true;
                    } else {
                        acceptedFragment.mNoMoreLoad = false;

                    }
                } else {
                    showEmptyScreen(getResources().getString(R.string.no_data_found));
                    acceptedFragment.removeLoadmore();
                    acceptedFragment.isDataLoaded = true;
                    if(isPullToRefresh)
                    {
                        isPullToRefresh=false;
                        acceptedFragment.clearList();
                    }
                }

                break;
            case Constants.REJECTED:
                extraFragment = (ExtraFragment) adapter.getItem(2);
                if (orderResponse.getData() != null) {
                    isPullToRefresh=false;
                    extraFragment.loadData(orderResponse.getData());
                    /***
                     ***************** to check data is available for load more or not ***************
                     */

                    if (orderResponse.getData().size() < Constants.LIMIT) {
                        extraFragment.mNoMoreLoad = true;
                    } else {
                        extraFragment.mNoMoreLoad = false;

                    }
                } else {
                    showEmptyScreen(getResources().getString(R.string.no_data_found));
                    extraFragment.removeLoadmore();
                    extraFragment.isDataLoaded = true;
                    if(isPullToRefresh)
                    {
                        isPullToRefresh=false;
                        extraFragment.clearList();
                    }
                }
                break;
            case Constants.DISPATCHED:
                extraFragment = (ExtraFragment) adapter.getItem(3);
                if (orderResponse.getData() != null) {
                    isPullToRefresh=false;
                    extraFragment.loadData(orderResponse.getData());
                    /***
                     ***************** to check data is available for load more or not ***************
                     */

                    if (orderResponse.getData().size() < Constants.LIMIT) {
                        extraFragment.mNoMoreLoad = true;
                    } else {
                        extraFragment.mNoMoreLoad = false;

                    }
                } else {
                    showEmptyScreen(getResources().getString(R.string.no_data_found));
                    extraFragment.removeLoadmore();
                    extraFragment.isDataLoaded = true;
                    if(isPullToRefresh)
                    {
                        isPullToRefresh=false;
                        extraFragment.clearList();
                    }
                }
                break;
            case Constants.COMPLETED:
                extraFragment = (ExtraFragment) adapter.getItem(4);
                if (orderResponse.getData() != null) {
                    isPullToRefresh=false;
                    extraFragment.loadData(orderResponse.getData());
                    /***
                     ***************** to check data is available for load more or not ***************
                     */

                    if (orderResponse.getData().size() < Constants.LIMIT) {
                        extraFragment.mNoMoreLoad = true;
                    } else {
                        extraFragment.mNoMoreLoad = false;

                    }
                } else {
                    showEmptyScreen(getResources().getString(R.string.no_data_found));
                    extraFragment.removeLoadmore();
                    extraFragment.isDataLoaded = true;
                    if(isPullToRefresh)
                    {
                        isPullToRefresh=false;
                        extraFragment.clearList();
                    }

                }
                break;

        }
    }

    private void updateBatch(int batchCount) {
        if (batchCount > 0)
            bottomNavigation.setNotification(String.valueOf(batchCount), 0);
        else
            bottomNavigation.setNotification("", 0);

    }

    /**
     * ************************* hide pull to refresh if runnint *********************
     *
     * @param currentpage
     */
    private void hidePullToRefresh(int currentpage) {
        ExtraFragment extraFragment;
        switch (currentpage) {
            case 0:
                PendingFragment pendingFragment = (PendingFragment) adapter.getItem(0);

                pendingFragment.hidePullToRefresh();


                break;
            case 1:
                AcceptedFragment acceptedFragment = (AcceptedFragment) adapter.getItem(1);
                acceptedFragment.hidePullToRefresh();

                break;
            case 2:
                extraFragment = (ExtraFragment) adapter.getItem(2);
                extraFragment.hidePullToRefresh();
                break;
            case 3:
                extraFragment = (ExtraFragment) adapter.getItem(3);
                extraFragment.hidePullToRefresh();

                break;
            case 4:
                extraFragment = (ExtraFragment) adapter.getItem(4);
                extraFragment.hidePullToRefresh();

                break;

        }
    }

    /**
     * ******************* order accepted,rejected and dispatched succesffully *************
     *
     * @param message
     */
    @Override
    public void onSuccess(String message) {
        /**
         *************** order dispatched for delivery ***************
         */

        if (mOrderOperationRequest.getOrderStatus() == 3) {
            AcceptedFragment acceptedFragment = (AcceptedFragment) adapter.getItem(1);
            acceptedFragment.showDone();
            ExtraFragment extraFragment = (ExtraFragment) adapter.getItem(3);
            extraFragment.isDataLoaded = false;

        }
        // order accepted done
        else if (mOrderOperationRequest.getOrderStatus() == 1) {
            PendingFragment pendingFragment = (PendingFragment) adapter.getItem(0);
            pendingFragment.showDone(1);
            AcceptedFragment acceptedFragment = (AcceptedFragment) adapter.getItem(1);
            acceptedFragment.isDataLoaded = false;

        }
        //order rejected done
        else if (mOrderOperationRequest.getOrderStatus() == 2) {
            PendingFragment pendingFragment = (PendingFragment) adapter.getItem(0);
            pendingFragment.showDone(2);
            ExtraFragment extraFragment = (ExtraFragment) adapter.getItem(2);
            extraFragment.isDataLoaded = false;

        }

    }


    @Override
    public void onSuccess(Status status) {
        Utils.getInstance().saveValue(Constants.IS_DEVICE_REGISTERED, true, this);
    }

    @Override
    public void onItemStatusUpdateSuccess(Status status) {

        if (status.getStatus() == 1) {
            PendingFragment pendingFragment = (PendingFragment) adapter.getItem(0);
            pendingFragment.updateStatus();
            mTotalNotification--;
            updateBatch(mTotalNotification);

        }
    }

    /**
     * ******************* update device token for push notification*****************
     */

    private void updateFcmDeviceToken() {
        boolean status = Utils.getInstance().getValue(Constants.IS_DEVICE_REGISTERED, false, this);
        if (!status) {
            String devicetoken = FirebaseInstanceId.getInstance().getToken();
             Log.e("device token", devicetoken + "");
            if (devicetoken != null) {
                UpdteTokenRequest updteTokenRequest = new UpdteTokenRequest();
                updteTokenRequest.setDeviceToken(devicetoken);
                int user_Id=Integer.valueOf(Utils.getInstance().getValue(Constants.USER_ID, "0", this));
                updteTokenRequest.setUserId(user_Id);
                presenterImp.updateDeviceToken(updteTokenRequest);

            }
        }
    }

    ExtraFragment extraFragment = null;

    private void showEmptyScreen(String message) {
        int currentpage = viewPager.getCurrentItem();
        hidePullToRefresh(currentpage);
        switch (currentpage) {
            case 0:
                final PendingFragment pendingFragment = (PendingFragment) adapter.getItem(0);

                if (pendingFragment.itemsData.size() == 0) {
                    show(message);

                }
                binding.emptyScreenLayout.btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pendingFragment.startPullToRefresh();
                    }
                });

                break;
            case 1:
                final AcceptedFragment acceptedFragment = (AcceptedFragment) adapter.getItem(1);

                if (acceptedFragment.itemsData.size() == 0) {
                    show(message);


                }
                binding.emptyScreenLayout.btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        acceptedFragment.startPullToRefresh();
                    }
                });

                break;
            case 2:
                extraFragment = (ExtraFragment) adapter.getItem(2);

                if (extraFragment.itemsData.size() == 0) {
                    show(message);
                }
                binding.emptyScreenLayout.btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        extraFragment.startPullToRefresh();
                    }
                });
                break;
            case 3:
                extraFragment = (ExtraFragment) adapter.getItem(3);

                if (extraFragment.itemsData.size() == 0) {
                    show(message);
                }
                binding.emptyScreenLayout.btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        extraFragment.startPullToRefresh();
                    }
                });
                break;
            case 4:
                extraFragment = (ExtraFragment) adapter.getItem(4);

                if (extraFragment.itemsData.size() == 0) {
                    show(message);
                }
                binding.emptyScreenLayout.btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        extraFragment.startPullToRefresh();
                    }
                });
                break;


        }


    }

    private void show(String message) {
        binding.emptyScreenLayout.emptyScreenContainer.setVisibility(View.VISIBLE);
        binding.emptyScreenLayout.textEmptyScreen.setText(message);

    }

    private void hideEmptyScreen() {
        binding.emptyScreenLayout.emptyScreenContainer.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.mDoubleBackToExitPressedOnce = true;
        Utils.getInstance().showToast(this, getResources().getString(R.string.double_back_press));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }


}
