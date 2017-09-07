package com.imenu.fr.restaurant.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.imenu.fr.restaurant.HomeActivity;
import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.databinding.SplashFragmentBinding;
import com.imenu.fr.restaurant.fragment.login.LoginFragment;
import com.imenu.fr.restaurant.utils.Constants;
import com.imenu.fr.restaurant.utils.Utils;

/**
 *
 */
public class SplashFragment extends BaseFragment implements Animation.AnimationListener {
    private static final int SPLASH_DELAY = 1000;
    SplashFragmentBinding binding;
    private Animation anim1;
    private Animation anim2;
    private Handler handler = new Handler();

    Runnable delay = new Runnable() {
        @Override
        public void run() {
            startAnimation();
        }
    };

    /**
     * Create a new instance of the fragment
     */
    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.splash_fragment, container, false));
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

    }

    private void init() {
        anim1 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_fade_in);
        anim2 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_up);
        anim1.setAnimationListener(this);
        anim2.setAnimationListener(this);
        handler.postDelayed(delay, 200);

    }

    private void startAnimation() {

        binding.cardView.startAnimation(anim1);
        binding.cardView.setVisibility(View.VISIBLE);

    }

    void startTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Utils.getInstance().getValue(Constants.LOGGED_IN, false, getActivity())) {
                    replaceFragment(new LoginFragment());
                } else {
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();

                }
            }
        }, SPLASH_DELAY);
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == anim1) {
            binding.textHeading1.setVisibility(View.VISIBLE);
            binding.textHeading1.startAnimation(anim2);
        } else {
            startTimer();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
