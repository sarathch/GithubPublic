package com.takehome.denshaotoko.takehome;

import android.support.annotation.VisibleForTesting;

import com.takehome.denshaotoko.takehome.data.source.GithubRepository;
import com.takehome.denshaotoko.takehome.di.DaggerAppComponent;
import com.takehome.denshaotoko.takehome.network.NetworkModule;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class MyApplication extends DaggerApplication {

    @Inject
    GithubRepository githubRepository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).netModule(new NetworkModule()).build();
    }

    /**
     * Our Espresso tests need to be able to get an instance of the {@link GithubRepository}
     * so that we can delete all tasks before running each test
     */
    @VisibleForTesting
    public GithubRepository getGtihubRepository() {
        return githubRepository;
    }
}
