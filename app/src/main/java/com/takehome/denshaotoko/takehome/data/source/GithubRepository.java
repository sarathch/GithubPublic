package com.takehome.denshaotoko.takehome.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.takehome.denshaotoko.takehome.data.Repo;
import com.takehome.denshaotoko.takehome.data.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository implementation to fetch data from the data sources as required
 * <p>
 *     Implemented a cache to reduce redundant request. Note that logic to refresh caches not implemented.
 * </p>
 */

@Singleton
public class GithubRepository implements GithubDataSource{

    private final GithubDataSource mGithubRemoteDataSource;

    /**
     *  Implemented caching to take load off of server
     */
    Map<String, User> mCachedUser;

    Map<String, List<Repo>> mCachedRepoList;

    /**
     * By marking the constructor with {@code @Inject}, Dagger will try to inject the dependencies
     * required to create an instance of the TasksRepository. Because {@link GithubRepository} is an
     * interface, we must provide to Dagger a way to build those arguments, this is done in
     * {@link GithubRepositoryModule}.
     * <P>
     * When two arguments or more have the same type, we must provide to Dagger a way to
     * differentiate them. This is done using a qualifier.
     * <p>
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    GithubRepository(GithubDataSource githubRemoteDataSource) {
        mGithubRemoteDataSource = githubRemoteDataSource;
    }

    /**
     * Gets user data from cache, remote data source, whichever available
     * @param userId    -   Github username
     * @param callback  -   LoadUserCallBack to listen on response
     */
    @Override
    public void getUser(final String userId, @NonNull final LoadUserCallback callback) {

        // lookup in cache if available
        if(mCachedUser!=null && mCachedUser.containsKey(userId)){
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

    /**
     * Gets public repository data from cache, remote data source, whichever available
     * @param userId    -   Github username
     * @param callback  -   LoadRepoCallBack to listen on response
     */
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
