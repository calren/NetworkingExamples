package com.example.caren.networkingexamples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Retrofit retrofit;
    private Subscriber networkSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareNetworkClient();
        makeNetworkRequest();
    }

    private void prepareNetworkClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit =
                new Retrofit.Builder().baseUrl("https://api.github.com/").
                        addConverterFactory(GsonConverterFactory.create()).client(client).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }

    public void makeNetworkRequest() {
        networkSubscriber = new Subscriber<ApiResponseModel>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "Successfully fetched list of gists");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Error fetching list of gists: " + e.getMessage());
            }

            @Override
            public void onNext(ApiResponseModel apiResponseModel) {
                System.out.println(apiResponseModel.login);
            }
        };


        retrofit.create(GithubApi.class).getEvents().subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(networkSubscriber);
    }

    interface GithubApi {
        @GET("users/calren/")
        Observable<ApiResponseModel> getEvents();
    }
}
