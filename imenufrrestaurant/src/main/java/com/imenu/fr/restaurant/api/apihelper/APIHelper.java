package com.imenu.fr.restaurant.api.apihelper;


import com.imenu.fr.restaurant.api.model.Status;
import com.imenu.fr.restaurant.api.service.Service;
import com.imenu.fr.restaurant.utils.Constants;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Lakhwinder Singh{lsingh@openkey.io} on 18/4/17.
 * <p>
 * {@link APIHelper} handles all the API calls to our server
 */

public class APIHelper {
    private static APIHelper apiHelper;
    private Retrofit mRetrofit;
    private OkHttpClient okHttpClient;

    public static APIHelper getInstance() {
        if (apiHelper == null) {
            apiHelper = new APIHelper();
        }
        return apiHelper;
    }

    /**
     * HttpLoggingInterceptor is used for displaying the body of the sender and receiver responce
     * in  form of JSONOBJECT
     * and setting connecttimeout,readtimeout etc.
     */
    private OkHttpClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    /**
     * Get retrofit Object for accessing web services.
     *
     * @return Service instance
     */
    public Service createService() {
        if (mRetrofit == null) {
            okHttpClient = getClient();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit.create(Service.class);
    }

    public String handleApiError(ResponseBody responseBody) {
        Converter<ResponseBody, Status> errorConverter = mRetrofit
                .responseBodyConverter(Status.class, new Annotation[0]);
        try {
            Status error = errorConverter.convert(responseBody);
            return error.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown Error";
        }
    }

    public void cancelRequest() {
        if (okHttpClient != null)
            okHttpClient.dispatcher().cancelAll();

    }
}
