package com.takehome.denshaotoko.takehome.github;

import android.os.Handler;
import android.util.Log;

import com.takehome.denshaotoko.takehome.data.Repo;
import com.takehome.denshaotoko.takehome.data.User;
import com.takehome.denshaotoko.takehome.data.source.GithubDataSource;
import com.takehome.denshaotoko.takehome.data.source.GithubRepository;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Fetches the data and updates the UI as required.
 **/

public class GithubPresenter implements GithubContract.Presenter {

    private final GithubRepository mGithubRepository;

    @Nullable
    private GithubContract.View mGithubView;

    @Inject
    GithubPresenter(GithubRepository githubRepository) {
        mGithubRepository = githubRepository;
    }

    /**
     * Fetches user data from GithubDataSource
     * @param userId - github username
     */
    @Override
    public void getUserData(final String userId) {

        if (mGithubView!=null) {
            mGithubRepository.getUser(userId, new GithubDataSource.LoadUserCallback() {
                @Override
                public void onUserFetched(User user) {
                    // update user details views
                    mGithubView.showUserDetails(user.getAvatar_url(), user.getName());

                }

                @Override
                public void onDataNotAvailable() {
                    mGithubView.showDataLoadError("user");
                }
            });
        }
    }

    /**
     * Fetches public repository list from DataSource
     * @param userId - github username
     */
    @Override
    public void getRepoData(final String userId) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if(mGithubView!=null) {
                    mGithubRepository.getRepos(userId, new GithubDataSource.LoadRepoCallback() {
                        @Override
                        public void onRepoFetched(List<Repo> repos) {
                            // update recycler view
                            mGithubView.updateRepoList(repos);
                        }

                        @Override
                        public void onDataNotAvailable() {
                            mGithubView.showDataLoadError("repo");
                        }
                    });
                }
            }
        }, 500);
    }

    @Override
    public void takeView(GithubContract.View view) {
        mGithubView = view;
    }

    @Override
    public void dropView() {
        mGithubView = null;
    }
}
