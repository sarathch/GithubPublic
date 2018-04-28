package com.takehome.denshaotoko.takehome.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.takehome.denshaotoko.takehome.data.Repo;
import com.takehome.denshaotoko.takehome.data.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GithubRepository implements GithubDataSource{

    private final GithubDataSource mGithubRemoteDataSource;

    /**
     *  Implemented caching to take load off of server
     */
    Map<String, User> mCachedUser;

    Map<String, List<Repo>> mCachedRepoList;

    @Inject
    GithubRepository(GithubDataSource githubRemoteDataSource) {
        mGithubRemoteDataSource = githubRemoteDataSource;
    }

    @Override
    public void getUser(final String userId, @NonNull final LoadUserCallback callback) {

        // lookup in cache if available
        if(mCachedUser!=null && mCachedUser.containsKey(userId)){
            Log.v("Repository::", "from cache");
            callback.onUserFetched(mCachedUser.get(userId));
            return;
        }

        mGithubRemoteDataSource.getUser(userId, new LoadUserCallback() {
            @Override
            public void onUserFetched(User user) {
                callback.onUserFetched(user);
                if(mCachedUser == null)
                    mCachedUser = new HashMap<>();
                mCachedUser.put(userId, user);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getRepos(final String userId, final LoadRepoCallback callback) {

        // lookup in cache if available
        if(mCachedRepoList!=null && mCachedRepoList.containsKey(userId)){
            callback.onRepoFetched(mCachedRepoList.get(userId));
            return;
        }

        mGithubRemoteDataSource.getRepos(userId, new LoadRepoCallback() {
            @Override
            public void onRepoFetched(List<Repo> repos) {
                callback.onRepoFetched(repos);

                if(mCachedRepoList == null)
                    mCachedRepoList = new HashMap<>();
                mCachedRepoList.put(userId, repos);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
