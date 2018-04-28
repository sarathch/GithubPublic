package com.takehome.denshaotoko.takehome.data.source;

import com.takehome.denshaotoko.takehome.data.Repo;
import com.takehome.denshaotoko.takehome.data.User;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Main entry point for accessing Github data.
 */

public interface GithubDataSource {

    interface LoadUserCallback {

        void onUserFetched(User user);

        void onDataNotAvailable();
    }

    interface LoadRepoCallback {
        void onRepoFetched(List<Repo> repos);

        void onDataNotAvailable();
    }

    void getUser(@NonNull String userId, @NonNull LoadUserCallback callback);

    void getRepos(@NonNull String userId, @NonNull LoadRepoCallback callback);
}
