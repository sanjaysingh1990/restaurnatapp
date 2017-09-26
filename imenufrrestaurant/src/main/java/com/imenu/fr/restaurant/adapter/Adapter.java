package com.imenu.fr.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imenu.fr.restaurant.ActionCallBack;
import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.api.model.order.OrderData;
import com.imenu.fr.restaurant.api.model.order.Tookan;
import com.imenu.fr.restaurant.utils.Constants;

import java.util.List;

/**
 *
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<OrderData> mDataset;
    private ActionCallBack mCallBack;
    private Context mContext;

    public Adapter(List<OrderData> dataset, ActionCallBack actionCallBack,Context context) {
        mDataset = dataset;
        mContext=context;
        mCallBack = actionCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case Constants.PENDING:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending, parent, false);
                return new ViewHolderPending(view1);
            case Constants.ACCEPTED:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accepted, parent, false);
                return new ViewHolderAccepted(view2);
            case Constants.REJECTED:
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_else, parent, false);
                return new ViewHolderOther(view3);

            case Constants.DISPATCHED:
                View view4 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_else, parent, false);
                return new ViewHolderOther(view4);
            case Constants.COMPLETED:
                View view5 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_else, parent, false);
                return new ViewHolderOther(view5);
            case Constants.LOADER:
                View view6 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loadmore, parent, false);
                return new ViewHolderLoader(view6);

        }
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderData orderData = mDataset.get(position);
        String deliveryDateTime;
        ViewHolderOther viewHolderOther;
        switch (holder.getItemViewType()) {

            case Constants.PENDING:
                ViewHolderPending viewHolderPending = (ViewHolderPending) holder;
                viewHolderPending.mTextOrderNo.setText(String.valueOf(orderData.getOrderId()));
                viewHolderPending.mTextPrice.setText(String.valueOf(orderData.getTotalAmount()));
                deliveryDateTime = orderData.getDeliveryDate() + "  " + orderData.getDeliveryTime();
                viewHolderPending.mTextOrderDateTime.setText(deliveryDateTime);
                /**
                 * ******************* to check order read or not *************
                 */
                if(orderData.getReadStatus()==0)
                {
                    viewHolderPending.mImgIndicator.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewHolderPending.mImgIndicator.setVisibility(View.INVISIBLE);
                }
                break;
            case Constants.ACCEPTED:
                ViewHolderAccepted viewHolderAccepted = (ViewHolderAccepted) holder;
                viewHolderAccepted.mTextOrderNo.setText(String.valueOf(orderData.getOrderId()));
                viewHolderAccepted.mTextPrice.setText(String.valueOf(orderData.getTotalAmount()));
                deliveryDateTime = orderData.getDeliveryDate() + "  " + orderData.getDeliveryTime();
                viewHolderAccepted.mTextOrderDateTime.setText(deliveryDateTime);

                break;
            case Constants.REJECTED:
                viewHolderOther = (ViewHolderOther) holder;
                viewHolderOther.mTextOrderNo.setText(String.valueOf(orderData.getOrderId()));
                viewHolderOther.mTextPrice.setText(String.valueOf(orderData.getTotalAmount()));
                deliveryDateTime = orderData.getDeliveryDate() + "  " + orderData.getDeliveryTime();
                viewHolderOther.mTextOrderDateTime.setText(deliveryDateTime);
                viewHolderOther.mTextStatusIndicator.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_order_rejected, 0, 0);
                viewHolderOther.mTextStatusIndicator.setText("ORDER REJECTED");

                break;
            case Constants.DISPATCHED:
                viewHolderOther = (ViewHolderOther) holder;
                viewHolderOther.mTextOrderNo.setText(String.valueOf(orderData.getOrderId()));
                viewHolderOther.mTextPrice.setText(String.valueOf(orderData.getTotalAmount()));
                deliveryDateTime = orderData.getDeliveryDate() + "  " + orderData.getDeliveryTime();
                viewHolderOther.mTextOrderDateTime.setText(deliveryDateTime);
                viewHolderOther.mTextOrderStatus.setVisibility(View.VISIBLE);
                viewHolderOther.mTextStatusIndicator.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_order_dispatched, 0, 0);
                viewHolderOther.mTextStatusIndicator.setText("ORDER DISPATCHED");

                break;
            case Constants.COMPLETED:
                viewHolderOther = (ViewHolderOther) holder;
                viewHolderOther.mTextOrderNo.setText(String.valueOf(orderData.getOrderId()));
                viewHolderOther.mTextPrice.setText(String.valueOf(orderData.getTotalAmount()));
                deliveryDateTime = orderData.getDeliveryDate() + "  " + orderData.getDeliveryTime();
                viewHolderOther.mTextOrderDateTime.setText(deliveryDateTime);
                viewHolderOther.mTextStatusIndicator.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_icon_order_completed, 0, 0);
                viewHolderOther.mTextStatusIndicator.setText("ORDER COMPLETED");

                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataset.get(position).getDataType() == Constants.PENDING) {
            return Constants.PENDING;
        } else if (mDataset.get(position).getDataType() == Constants.ACCEPTED) {
            return Constants.ACCEPTED;
        } else if (mDataset.get(position).getDataType() == Constants.REJECTED) {
            return Constants.REJECTED;
        } else if (mDataset.get(position).getDataType() == Constants.DISPATCHED) {
            return Constants.DISPATCHED;
        } else if (mDataset.get(position).getDataType() == Constants.COMPLETED) {
            return Constants.COMPLETED;
        }
        else if (mDataset.get(position).getDataType() == Constants.LOADER) {
            return Constants.LOADER;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class ViewHolderPending extends RecyclerView.ViewHolder {
        private TextView mTextOrderNo;
        private TextView mTextPrice;
        private TextView mTextOrderDateTime;
        private ImageButton mBtnAccept;
        private ImageButton mBtnCancel;
        private LinearLayout mContainer;
        private ImageView mImgIndicator;


        ViewHolderPending(View v) {
            super(v);
            mTextOrderNo = (TextView) v.findViewById(R.id.txt_order_number);
            mTextPrice = (TextView) v.findViewById(R.id.txt_price);
            mTextOrderDateTime = (TextView) v.findViewById(R.id.txt_orderdate);
            mImgIndicator = (ImageView) v.findViewById(R.id.image_indicator);

            mBtnAccept = (ImageButton) v.findViewById(R.id.btn_accept);
            mBtnCancel = (ImageButton) v.findViewById(R.id.btn_cancel);
            mContainer = (LinearLayout) v.findViewById(R.id.view_container);
            mBtnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallBack != null) {
                        mCallBack.acceptOrder(getAdapterPosition());
                    }
                }
            });

            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallBack != null) {
                        mCallBack.cancelOrder(getAdapterPosition());
                    }
                }
            });

            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallBack != null) {
                        mCallBack.showDetails(getAdapterPosition());
                    }
                }
            });

        }
    }

    private class ViewHolderAccepted extends RecyclerView.ViewHolder {
        private TextView mTextOrderNo;
        private TextView mTextPrice;
        private TextView mTextOrderDateTime;
        private Button mBtnReadyToDeliver;
        private LinearLayout mContainer;


        ViewHolderAccepted(View v) {
            super(v);

            mTextOrderNo = (TextView) v.findViewById(R.id.txt_order_number);
            mTextPrice = (TextView) v.findViewById(R.id.txt_price);
            mTextOrderDateTime = (TextView) v.findViewById(R.id.txt_orderdate);
            mBtnReadyToDeliver = (Button) v.findViewById(R.id.btn_ready_to_deliver);
            mContainer = (LinearLayout) v.findViewById(R.id.view_container);

            //ready to deliver
            mBtnReadyToDeliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallBack != null) {
                        mCallBack.readyToDeliver(getAdapterPosition());
                    }
                }
            });

            //to oepn full detail view
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallBack != null) {
                        mCallBack.showDetails(getAdapterPosition());
                    }
                }
            });


        }
    }

    public class ViewHolderOther extends RecyclerView.ViewHolder {
        private TextView mTextOrderNo;
        private TextView mTextPrice;
        private TextView mTextOrderStatus;
        private TextView mTextOrderDateTime;
        private TextView mTextStatusIndicator;
        private LinearLayout mContainer;

        public ViewHolderOther(View v) {
            super(v);
            mTextOrderNo = (TextView) v.findViewById(R.id.txt_order_number);
            mTextOrderStatus = (TextView) v.findViewById(R.id.text_order_status);
            mTextPrice = (TextView) v.findViewById(R.id.txt_price);
            mTextOrderDateTime = (TextView) v.findViewById(R.id.txt_orderdate);
            mTextStatusIndicator = (TextView) v.findViewById(R.id.text_status_indicator);
            mContainer = (LinearLayout) v.findViewById(R.id.view_container);
            //to oepn full detail view
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallBack != null) {
                        mCallBack.showDetails(getAdapterPosition());
                    }
                }
            });
        mTextOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tookan tokaan = mDataset.get(getAdapterPosition()).getTookan();
                if(tokaan!=null)
                {
                    try
                    {
                        double latitude1=Double.parseDouble(tokaan.getTookanLatitude());
                        double longitude1=Double.parseDouble(tokaan.getTookanLongitude());

                        Uri uri = Uri.parse("geo:0,0?q="+latitude1+","+longitude1+"(Delivery Boy)");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        mContext.startActivity(intent);
                    }
                     catch (Exception ex)
                     {

                     }
                }
            }
        });
        }
    }
    class ViewHolderLoader extends RecyclerView.ViewHolder {


        ViewHolderLoader(View itemView) {
            super(itemView);


        }
    }

}
