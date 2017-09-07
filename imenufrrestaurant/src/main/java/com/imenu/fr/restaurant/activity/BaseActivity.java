package com.imenu.fr.restaurant.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.imenu.fr.restaurant.R;

/**
 * Created by roma on 16.10.15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Dialog mProgressDialog;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
       // super.onSaveInstanceState(outState);
    }

    public void replaceFragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_in, R.anim.anim_out, R.anim.anim_in_reverse_frag, R.anim.anim_out_reverse_frag);
        transaction.replace(R.id.container, fragment, tag)
                .commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_in, R.anim.anim_out, R.anim.anim_in_reverse_frag, R.anim.anim_out_reverse_frag);
        transaction.add(R.id.container, fragment, tag)
                .commitAllowingStateLoss();
    }

    /**
     * show Loader view
     */
    public synchronized void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(this, android.R.style.Theme_Translucent);
            mProgressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setContentView(R.layout.loader);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    /**
     * hide Loader view
     */
    public synchronized void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
