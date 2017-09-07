package com.imenu.fr.restaurant.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.imenu.fr.restaurant.ActionCallBack;
import com.imenu.fr.restaurant.ActionPerformed;
import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.activity.DetailsActivity;
import com.imenu.fr.restaurant.adapter.Adapter;
import com.imenu.fr.restaurant.api.model.order.OrderData;
import com.imenu.fr.restaurant.api.model.orderoperation.OrderReadyToDeliver;
import com.imenu.fr.restaurant.databinding.FragmentDemoListBinding;
import com.imenu.fr.restaurant.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AcceptedFragment extends BaseFragment {
    public ArrayList<OrderData> itemsData;
    public boolean isDataLoaded;
    private LinearLayoutManager mLayoutManager;
    private Adapter adapter;
    private int mPosition;
    private OrderReadyToDeliver orderReadyToDeliver;
    private FragmentDemoListBinding binding;
    private int moffset = 0; // for load more page count
    public boolean mNoMoreLoad = true;

    SwipeRefreshLayout.OnRefreshListener swipeRefreshListner = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            reset();
            EventBus.getDefault().post(new Integer(1));
        }
    };

    /**
     * Create a new instance of the fragment
     */
    public static AcceptedFragment newInstance() {
        AcceptedFragment fragment = new AcceptedFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_demo_list, container, false));
        init();
        return binding.getRoot();

    }


    /**
     * Init the fragment
     */
    private void init() {

        mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);

        itemsData = new ArrayList<>();


        adapter = new Adapter(itemsData, new ActionCallBack() {
            @Override
            public void acceptOrder(final int position) {
                //nothing to do

            }

            @Override
            public void cancelOrder(final int position) {
                //nothing to do
            }

            @Override
            public void readyToDeliver(final int position) {
                String title1 = "Alert";
                String messag1 = "Are you sure you want to dispatch this order ?";
                String title2 = "Delivered!";
                String message2 = "Order now in delivery mode";
                String confirmTxt1 = "YES";
                String confirmTxt2 = "NO";
                showWarningAlert(title1, title2, messag1, message2, confirmTxt1, confirmTxt2, new ActionPerformed() {
                    @Override
                    public void actionPerformed() {
                        mPosition = position;
                        OrderData orderData = itemsData.get(position);
                        orderReadyToDeliver = new OrderReadyToDeliver();
                        orderReadyToDeliver.setOrderId(orderData.getOrderId());
                        orderReadyToDeliver.setStoreId(1);
                        orderReadyToDeliver.setOrderStatus(Constants.ORDER_READY_TO_DELIVER);

                        EventBus.getDefault().post(orderReadyToDeliver);
                        //removeItem(position);
                    }
                });

            }

            @Override
            public void showDetails(int position) {
                mPosition = position;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(Constants.DETAIL_VIEW, Constants.ACCEPTED_ORDER_DETAILS);
                intent.putExtra(Constants.FROM, Constants.ACCEPTED);
                intent.putExtra(Constants.DETAIL_HEADING, getResources().getString(R.string.accepted));

                intent.putExtra(Constants.DATA, itemsData.get(mPosition));
                getActivity().startActivity(intent);
            }
        });
        binding.recyclerView.setAdapter(adapter);
        //pull to refresh listener
        binding.swipeRefreshLayout.setOnRefreshListener(swipeRefreshListner);


        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //recycler view reached top restart the timer


                if (dy > 0) //check for scroll down
                {


                    if (!mNoMoreLoad) {

                        if ((mLayoutManager.getChildCount() + mLayoutManager.findFirstVisibleItemPosition()) >= mLayoutManager.getItemCount()) {

                            mNoMoreLoad = true;
                            loadMore();
                        }
                    }


                }
            }

        };
        binding.recyclerView.addOnScrollListener(onScrollListener);//add load more funtionality


    }

    /**
     * ************ show load more indicator **********
     */
    private void loadMore() {
        isDataLoaded = false;
        moffset = itemsData.size();
        OrderData loader = new OrderData();
        //to show load more
        loader.setDataType(Constants.LOADER);
        itemsData.add(loader);
        int loaderIndex = itemsData.size() - 1;
        if (loaderIndex >= 0)

            adapter.notifyItemInserted(loaderIndex);
        //call api
        EventBus.getDefault().post(String.valueOf(loaderIndex));


    }
    /**
     * ********************* call pull to refresh
     * ********************** programatically *********************
     */
    public void startPullToRefresh() {
        isDataLoaded=false;
        binding.swipeRefreshLayout.setRefreshing(true);
        swipeRefreshListner.onRefresh();
    }

    public void loadData(List<OrderData> orderDataList) {
        isDataLoaded = true;
        hidePullToRefresh();
        int index = itemsData.size() - 1;
        /**
         ************ to check loader is running or not *****************
         */
        if (index >= 0 && itemsData.get(index).getDataType() == Constants.LOADER) {
            itemsData.remove(index);

        } else {
            itemsData.clear();
        }
        for (OrderData orderData : orderDataList) {
            orderData.setDataType(Constants.ACCEPTED);
            itemsData.add(orderData);

        }
        adapter.notifyDataSetChanged();
    }

    //rest all filters
    public void reset() {
        mNoMoreLoad = true;
        isDataLoaded = false;
        moffset = 0;

    }


    public void removeItem(int position) {
        itemsData.remove(position);
        adapter.notifyItemRemoved(position);
        if (itemsData.size() == 0) {
            EventBus.getDefault().post(new Object());
        }
    }

    public void removeLoadmore() {
        /**
         ************ to check loader is running or not *****************
         */
        int index = itemsData.size() - 1;

        if (index >= 0 && itemsData.get(index).getDataType() == Constants.LOADER) {
            itemsData.remove(index);
            adapter.notifyItemRemoved(index);

        }
    }

    public void hidePullToRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);

    }

    public void showDone() {
        removeItem(mPosition);
        showDoneAlert("Dispatched!", "Order dispatched successfully");


    }

    public void updateStatus() {
        OrderData orderData = itemsData.get(mPosition);
        orderData.setReadStatus(1);
        adapter.notifyItemChanged(mPosition);
    }

    /**
     * Refresh
     */
    public void refresh() {
        if (binding.recyclerView != null) {
            binding.recyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (binding.fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            binding.fragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    public void willBeHidden() {
        if (binding.fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            binding.fragmentContainer.startAnimation(fadeOut);
        }
    }


}
