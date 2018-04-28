package com.takehome.denshaotoko.takehome.github;

import com.takehome.denshaotoko.takehome.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link GithubPresenter}.
 */
@Module
public abstract class GithubModule {

    @ActivityScoped
    @Binds
    abstract GithubContract.Presenter githubPresenter(GithubPresenter presenter);
}
