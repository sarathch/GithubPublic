package com.takehome.denshaotoko.takehome.github;

import com.takehome.denshaotoko.takehome.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class GithubModule {

    @ActivityScoped
    @Binds
    abstract GithubContract.Presenter githubPresenter(GithubPresenter presenter);
}
