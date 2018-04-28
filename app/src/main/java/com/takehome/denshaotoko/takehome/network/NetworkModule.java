package com.takehome.denshaotoko.takehome.network;

import javax.inject.Singleton;

import butterknife.BindView;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private static final String BASE_URL = "https://api.github.com";

    public NetworkModule() {
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory  provideRxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(GsonConverterFactory gsonConverterFactory, RxJava2CallAdapterFactory rxJava2CallAdapterFactory ) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    NetworkService provideNetworkService(Retrofit retrofit) {
        return retrofit.create(NetworkService.class);
    }
}
