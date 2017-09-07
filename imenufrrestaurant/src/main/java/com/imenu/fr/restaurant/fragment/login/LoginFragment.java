package com.imenu.fr.restaurant.fragment.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.imenu.fr.restaurant.HomeActivity;
import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.model.login.LoginRequest;
import com.imenu.fr.restaurant.api.model.login.LoginResponse;
import com.imenu.fr.restaurant.databinding.LoginFragmentBinding;
import com.imenu.fr.restaurant.fragment.BaseFragment;
import com.imenu.fr.restaurant.utils.Constants;
import com.imenu.fr.restaurant.utils.DrawableClickListener;
import com.imenu.fr.restaurant.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LoginFragment extends BaseFragment implements ILoginContract.LoginView {

    private LoginFragmentBinding binding;
    private LoginPresenterImp presenterImp;
    private LoginRequest loginRequest;
    private boolean isPasswordShown;

    /**
     * Create a new instance of the fragment
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.login_fragment, container, false));
        binding.setClickEvent(new ClickHandler());
        init();
        return binding.getRoot();

    }

    void init() {
        presenterImp = new LoginPresenterImp(this);
        binding.btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        setListener();

    }

    private boolean validateFields() {
        String email = binding.edittextEmail.getText().toString();
        String password = binding.edittextPassword.getText().toString();
        if (email.length() == 0) {
            binding.edittextEmail.requestFocus();
            binding.edittextEmail.setError(getString(R.string.error_empty_email));
            return false;
        }
        if (!emailValidator(email)) {
            binding.edittextEmail.requestFocus();
            binding.edittextEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }
        if (password.length() == 0) {
            binding.edittextPassword.requestFocus();
            binding.edittextPassword.setError(getString(R.string.error_empty_password));
            return false;
        }
        loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);


        return true;
    }

    /**
     * ********************** to validate email field *****************************
     *
     * @param email
     * @return
     */
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void showProgressDialog() {
        binding.btnSignIn.setProgress(10);

    }

    @Override
    public void hideProgressDialog() {
        binding.btnSignIn.setProgress(0);
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
        Utils.getInstance().showAlert(message, getActivity());
        enable();

    }

    @Override
    public void onLoginSuccess(final LoginResponse loginResponse) {
        binding.btnSignIn.setProgress(100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();


                //save data to shared preference

                Utils.getInstance().saveValue(Constants.USER_ID, loginResponse.getData().getUserId(), getActivity());
                Utils.getInstance().saveValue(Constants.STORE_ID, loginResponse.getData().getStoreId(), getActivity());
                Utils.getInstance().saveValue(Constants.LOGGED_IN, true, getActivity());


            }
        }, 200);
    }

    @Override
    public void onOtpSendSuccess(Status status) {
        //nothing to do
    }

    @Override
    public void onOtpVerifiedSuccess(Status status) {
        //nothing to do
    }

    @Override
    public void onPasswordResetSuccess(Status status) {
        //nothing to do
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterImp.onDestroyView();
    }

    private void disable() {
        binding.btnSignIn.setEnabled(false);
        binding.edittextEmail.setEnabled(false);
        binding.edittextPassword.setEnabled(false);

    }

    private void enable() {
        binding.btnSignIn.setEnabled(true);
        binding.edittextEmail.setEnabled(true);
        binding.edittextPassword.setEnabled(true);

    }

    public class ClickHandler {

        public void signIn(View view) {
            if (validateFields()) {
                if (Utils.getInstance().isNetworkAvailable(getActivity())) {
                    disable();
                    presenterImp.validateAndLoginUser(loginRequest);

                } else {
                    showAlert(getResources().getString(R.string.no_internet_message));

                }

            }
        }

        public void forgotPassword(View view) {

            binding.edittextEmail.setError(null);
            binding.edittextPassword.setError(null);
            addFragment(EnterEmailFragment.newInstance());
        }
    }

    public void setListener() {
        binding.edittextPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.edittextPassword) {
            @Override
            public boolean onDrawableClick() {
                // your action here
                if (!isPasswordShown) {
                    isPasswordShown = true;
                    binding.edittextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.edittextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0);
                } else {
                    isPasswordShown = false;
                    binding.edittextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.edittextPassword
                            .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_close, 0);
                }
                return false;
            }
        });

        binding.edittextEmail.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(binding.edittextPassword) {
            @Override
            public boolean onDrawableClick() {
                // your action here
                binding.edittextEmail.setText(null);
                return false;
            }
        });


        binding.edittextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    new ClickHandler().signIn(null);
                    return true;
                }
                return false;
            }


        });

    }
}
