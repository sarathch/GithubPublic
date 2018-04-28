package com.takehome.denshaotoko.takehome.data.source;

import android.support.annotation.NonNull;

import com.takehome.denshaotoko.takehome.data.Repo;
import com.takehome.denshaotoko.takehome.data.User;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GithubRepository implements GithubDataSource{

    private final GithubDataSource mGithubRemoteDataSource;

    @Inject
    GithubRepository(GithubDataSource githubRemoteDataSource) {
        mGithubRemoteDataSource = githubRemoteDataSource;
    }

    @Override
    public void getUser(String userId,@NonNull final LoadUserCallback callback) {
        mGithubRemoteDataSource.getUser(userId, new LoadUserCallback() {
            @Override
            public void onUserFetched(User user) {
                callback.onUserFetched(user);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getRepos(String userId, final LoadRepoCallback callback) {
        mGithubRemoteDataSource.getRepos(userId, new LoadRepoCallback() {
            @Override
            public void onRepoFetched(List<Repo> repos) {
                callback.onRepoFetched(repos);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
