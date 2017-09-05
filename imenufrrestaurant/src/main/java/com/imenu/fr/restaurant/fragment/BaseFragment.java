package com.imenu.fr.restaurant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;


import com.imenu.fr.restaurant.ActionPerformed;
import com.imenu.fr.restaurant.R;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Bhupinder on 28/3/17.
 * <p>
 * Every Fragment must extend this fragment
 */

public class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();
    public View mContent;// For showing snackbar
    private FragmentActivity mActivity;
    private SweetAlertDialog mSweetAlertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContent = view;
    }


    /**
     * Attach fragment with or without addToBackStack
     */
    public void addFragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        Fragment fragmentOldObject = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_in, R.anim.anim_out, R.anim.anim_in_reverse_frag, R.anim.anim_out_reverse_frag);


        transaction.replace(R.id.container, fragment, tag)
                .commit();
    }

    public void showWarningAlert(final String title1, final String title2, String message1, final String message2, final String confirmtxt1, String confirmtxt2, final ActionPerformed actionPerformed) {
        if (mSweetAlertDialog == null)
            mSweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        else if (mSweetAlertDialog.isShowing()) {
            return;
        }
        mSweetAlertDialog.setTitleText(title1)
                .setContentText(message1)
                .setConfirmText(confirmtxt1)
                .setCancelText(confirmtxt2)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();

                        if (actionPerformed != null) {
                            actionPerformed.actionPerformed();
                        }


                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    public void showDoneAlert(String title, String message) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }
}












