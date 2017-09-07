package com.imenu.fr.restaurant.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.imenu.fr.restaurant.R;
import com.imenu.fr.restaurant.app.Imenufr;
import com.imenu.fr.restaurant.fragment.cryptography.SharedPreferencesEncryption;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author Bhupinder.
 */
public class Utils {

    public static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 124;

    private static Utils utilities;
    private Toast msg;

    private Dialog mProgressDialog;

    public static Utils getInstance() {
        if (utilities == null) {
            utilities = new Utils();
        }
        return utilities;

    }


    public int getWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }


    /**
     * hide the soft keyboard
     *
     * @param ctx
     */
    public void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Show toast.
     *
     * @param context the context
     * @param toast   String value which needs to shown in the toast.
     * @description if you want to print a toast just call this method and pass
     * what you want to be shown.
     */
    public synchronized Toast showToast(Context context, String toast) {
        if (context != null && msg == null || msg.getView().getWindowVisibility() != View.VISIBLE) {
            msg = Toast.makeText(context, toast, Toast.LENGTH_LONG);
            msg.setGravity(Gravity.BOTTOM, 0, 150);
            msg.show();
        }
        return msg;
    }


    /**
     * Show toast.
     *
     * @param message String value which needs to shown in the Snackbar.
     * @description if you want to print a Snackbar just call this method and pass
     * what you want to be shown.
     */
    public synchronized Snackbar showSnackbar(View content, String message) {
        Snackbar snackbar = null;
        if (content != null) {
            snackbar = Snackbar.make(content, message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        return snackbar;
    }


    private Fragment getCurrentFragment(FragmentManager fragmentManager) {
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

//    public void startActivityWithAnimation(Activity activityContext, Class<?> activity, Bundle bundle) {
//        Intent intent = new Intent(activityContext, activity);
//        if (bundle != null) {
//            intent.putExtra(Constants.PARCEL_KEY, bundle);
//        }
//        // Experimental feature, remove if cause issues(7-5-2017)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        activityContext.startActivity(intent);
//        activityContext.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
//        activityContext.finish();
//
//    }

//    public void startActivityWithAnimation(Activity activityContext, Class<?> activity, Bundle bundle, boolean finish) {
//        Intent intent = new Intent(activityContext, activity);
//        if (bundle != null) {
//            intent.putExtra(Constants.PARCEL_KEY, bundle);
//        }
//        // Experimental feature, remove if cause issues(7-5-2017)
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        activityContext.startActivity(intent);
//        activityContext.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
//        if (finish) {
//            activityContext.finish();
//        }
//
//    }


    public String getPathFromURI(Context context, Uri uri) {
        if (uri == null) {
            throw new NullPointerException("Uri must not be null");
        }
        String path;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    public String getTimeLeft(long futurestartmilliseconds) {
        String timeleft = String.format("%02dd %02dh %02dm %02ds", TimeUnit.MILLISECONDS.toDays(futurestartmilliseconds), TimeUnit.MILLISECONDS.toHours(futurestartmilliseconds), TimeUnit.MILLISECONDS.toMinutes(futurestartmilliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(futurestartmilliseconds)), TimeUnit.MILLISECONDS.toSeconds(futurestartmilliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(futurestartmilliseconds)));

        return timeleft.replace("00d", "").replace("00h", "").replace("00m", "").replace("00s", "");

    }


    /*
    *  Getting the real path of image after select the image through gallery
    * */
    public synchronized String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /*
    *  Getting the real path of image through camera
    * */
    public synchronized String getOriginalImagePath(FragmentActivity fragmentActivity) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = fragmentActivity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int column_index_data = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        return cursor.getString(column_index_data);
    }


    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    public String formatDate(String dateStr, String currentFormatStr, String requiredFormatStr) {
        if (dateStr == null) return null;
        String date = "Format Exception";
        SimpleDateFormat currentFormat = new SimpleDateFormat(currentFormatStr, Locale.getDefault());
        SimpleDateFormat requiredFormat = new SimpleDateFormat(requiredFormatStr, Locale.getDefault());
        try {
            Date getDate = currentFormat.parse(dateStr);
            date = requiredFormat.format(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * Only letters are allowed to passed EditTexts, or you can customize accordingly
     *
     * @param editTexts Edittext which needs a filter
     */
    public void setInputFilter(EditText... editTexts) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 0; i < s.toString().length(); i++) {
                    if (!Character.isLetter(s.charAt(i))) {
                        s.replace(i, i + 1, "");
                    }
                }
            }
        };
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(textWatcher);
        }
    }


    /**
     * ****************** get current date in utc format ******************
     *
     * @return
     */
    public String getFormatedDateTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utctime = simpleDateFormat.format(calendar.getTime());

        return utctime;
    }

    public String[] getFormatedDateTime(String startTime, int addminutes) {
        String[] data = new String[]{"", "", "", ""};

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = simpleDateFormat.parse(startTime);
            DateFormat date = new SimpleDateFormat("EEE");
            DateFormat date2 = new SimpleDateFormat("dd/MM/yy");
            DateFormat time = new SimpleDateFormat("hh:mm a");
            data[0] = String.valueOf(date.format(myDate));
            data[1] = String.valueOf(date2.format(myDate));
            data[2] = String.valueOf(time.format(myDate)).replace(".", "");

            //add minutes to given time
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            Date d = df.parse(time.format(myDate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, addminutes);
            data[3] = df.format(cal.getTime()).replace(".", "");

        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
        return data;
    }

    public String getFormatedDateTime2(String startTime, String format) {
        String result = "";

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = simpleDateFormat.parse(startTime);
            DateFormat date = new SimpleDateFormat(format);
            result = date.format(myDate);


        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
        return result.replace(".", "");
    }

    /**
     * Save value to shared preference.
     *
     * @param key     On which key you want to save the value.
     * @param value   The value which needs to be saved.
     * @param context the context
     * @description To save the value to a preference file on the specified key.
     */
    public synchronized void saveValue(String key, String value, Context context) {
        if (context == null) return;

        SharedPreferences prefs = new SharedPreferencesEncryption(context);
        SharedPreferences.Editor saveValue = prefs.edit();
        saveValue.putString(key, value);
        saveValue.apply();
    }

    /**
     * Save value to shared preference.
     *
     * @param key     On which key you want to save the value.
     * @param value   The value which needs to be saved.
     * @param context the context
     * @description To save the value to a preference file on the specified key.
     */
    public synchronized void saveValue(String key, long value, Context context) {
        if (context == null) return;
        SharedPreferences prefs = new SharedPreferencesEncryption(context);
        SharedPreferences.Editor saveValue = prefs.edit();
        saveValue.putLong(key, value);
        saveValue.apply();
    }

    /**
     * Gets value from shared preference.
     *
     * @param key          The key from you want to get the value.
     * @param defaultValue Default value, if nothing is found on that key.
     * @param context      the context
     * @return the value from shared preference
     * @description To get the value from a preference file on the specified
     * key.
     */
    public synchronized String getValue(String key, String defaultValue, Context context) {
        if (context == null) return null;
        SharedPreferences prefs = new SharedPreferencesEncryption(context);
        return prefs.getString(key, defaultValue);
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

    /**
     * Save value to shared preference.
     *
     * @param key     On which key you want to save the value.
     * @param value   The value which needs to be saved.
     * @param context the context
     * @description To save the value to a saved preference file on the
     * specified key.
     */
    public synchronized void saveValue(String key, boolean value, Context context) {
        if (context == null) return;
        SharedPreferences prefs = new SharedPreferencesEncryption(context);
        SharedPreferences.Editor saveValue = prefs.edit();
        saveValue.putBoolean(key, value);
        saveValue.apply();
    }

    /**
     * Gets value from shared preference.
     *
     * @param key          The key from you want to get the value.
     * @param defaultValue Default value, if nothing is found on that key.
     * @param context      the context
     * @return the value from shared preference
     * @description To get the value from a preference file on the specified
     * key.
     */
    public synchronized int getValue(String key, int defaultValue, Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = new SharedPreferencesEncryption(context);
        return prefs.getInt(key, defaultValue);
    }

    /**
     * Gets value from shared preference.
     *
     * @param key          The key from you want to get the value.
     * @param defaultValue Default value, if nothing is found on that key.
     * @param context      the context
     * @return the value from shared preference
     * @description To get the value from a preference file on the specified
     * key.
     */
    public synchronized long getValue(String key, long defaultValue, Context context) {
        if (context == null) return 0l;
        SharedPreferences prefs = new SharedPreferencesEncryption(context);
        return prefs.getLong(key, defaultValue);
    }


    /**
     * Clear values of shared preference.
     *
     * @param context the context
     * @description If user logout then clear all the saved values from the
     * shared preference file
     */
    public synchronized void clearValueOfKey(Context context, String key) {
        if (context == null) return;
        SharedPreferences prefs = new SharedPreferencesEncryption(context);
        SharedPreferences.Editor saveValue = prefs.edit();
        saveValue.remove(key).apply();
    }

    /**
     * Gets value from shared preference.
     *
     * @param key          The key from you want to get the value.
     * @param defaultValue Default value, if nothing is found on that key.
     * @param context      the context
     * @return the value from shared preference
     * @description To get the value from a saved preference file on the
     * specified key.
     */
    public synchronized boolean getValue(String key, boolean defaultValue, Context context) {
        if (context == null) return false;
        SharedPreferences prefs = new SharedPreferencesEncryption(context);
        return prefs.getBoolean(key, defaultValue);
    }

    /**
     * Is email valid.
     *
     * @param email Pass a email in string format and this method check if it is
     *              a valid address or not.
     * @return True if email is valid otherwise false.
     */
    public boolean isEmailValid(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean hasReadLocationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                return false;
            }
        }
        return true;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermissionLocation(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return false;
            }
        }
        return true;

    }

    public boolean isPermissionDeniedPermanently(Fragment fragment, String permission) {
        return fragment.shouldShowRequestPermissionRationale(permission);
    }


    /**
     * Is online.
     *
     * @return True, if device is having a Internet connection available.
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void showAlert(String message, Context context) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Alert")
                .setContentText(message)
                .show();
    }


    public boolean isCurrentDay(long selectedMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return selectedMillis == calendar.getTimeInMillis();
    }


    /**
     * Gets date time.
     *
     * @param format Type of the format of date or time
     * @return Get current date and time of the device, according to the format
     * @description Return string contains date if this format yyyy/MM/dd is
     * passed or time if this HH:mm:ss is passed and return both
     * with combined format yyyy/MM/dd HH:mm:ss
     */
    public String getDateTimeString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        String Time = null;
        Date date = new Date();
        Time = dateFormat.format(date);
        return Time;
    }

    /**
     * Gets date time.
     *
     * @param format Type of the format of date or time
     * @return Get current date and time of the device, according to the format
     * @description Return string contains date if this format yyyy/MM/dd is
     * passed or time if this HH:mm:ss is passed and return both
     * with combined format yyyy/MM/dd HH:mm:ss
     */
    public String getTimeFromDate(String format, String desiredFormat, String dateStr) {
        Date date;
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat desiredFormatter = new SimpleDateFormat(desiredFormat, Locale.getDefault());
        String Time = null;
        try {
            date = dateFormat.parse(dateStr);
            Time = desiredFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Time;
    }

    /**
     * /**
     * Gets date time.
     *
     * @param format Type of the format of date or time
     * @return Get current date and time of the device, according to the format
     * @description Return string contains date if this format yyyy/MM/dd is
     * passed or time if this HH:mm:ss is passed and return both
     * with combined format yyyy/MM/dd HH:mm:ss
     */
    public String getTimeFromDateSent(String format, String desiredFormat, String dateStr) {
        Date date;
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        SimpleDateFormat desiredFormatter = new SimpleDateFormat(desiredFormat, Locale.getDefault());
        desiredFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String Time = null;
        try {
            date = dateFormat.parse(dateStr);
            Time = desiredFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Time;
    }

    /**
     * Gets date time.
     *
     * @param format Type of the format of date or time
     * @return Get current date and time of the device, according to the format
     * @description Return string contains date if this format yyyy/MM/dd is
     * passed or time if this HH:mm:ss is passed and return both
     * with combined format yyyy/MM/dd HH:mm:ss
     */
    public String getTimeFromDateReceive(String format, String desiredFormat, String dateStr) {
        Date date;
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat desiredFormatter = new SimpleDateFormat(desiredFormat, Locale.getDefault());
        desiredFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String Time = null;
        try {
            date = dateFormat.parse(dateStr);
            Time = desiredFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Time;
    }

    public String[] getDateTime(String startTime) {
        String[] data = new String[]{"", "", ""};
        /*
         ********************** DATE AND TIME FORMATTING EXCPETION ******************
         */
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = simpleDateFormat.parse(startTime);
            DateFormat date = new SimpleDateFormat("EEE dd");
            DateFormat date2 = new SimpleDateFormat("EEE dd hh:mm a");
            DateFormat time = new SimpleDateFormat("hh:mm a");
            data[0] = String.valueOf(date.format(myDate));
            data[1] = String.valueOf(time.format(myDate));
            data[2] = String.valueOf(date2.format(myDate));

        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }

        return data;
    }

    public Date getDate(String startTime) {
        Date myDate = new Date();
        // date formatting exception
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            myDate = simpleDateFormat.parse(startTime);

        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
        return myDate;

    }

    public Date getDateNew(String startTime) {
        Date myDate = new Date();
        // date formatting exception
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            myDate = simpleDateFormat.parse(startTime);

        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
        return myDate;

    }

    public String getTime(String startTime) {
        Date myDate = new Date();
        String hour = null;
        // date formatting exception
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat format = new SimpleDateFormat("hh", Locale.getDefault());
            myDate = simpleDateFormat.parse(startTime);
            hour = format.format(myDate);
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
        }
        return hour;

    }

    /**
     * show Loader view
     */
    public synchronized void showProgress(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(Imenufr.getContext(), android.R.style.Theme_Translucent);
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