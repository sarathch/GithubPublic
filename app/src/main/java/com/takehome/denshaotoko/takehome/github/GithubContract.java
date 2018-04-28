package com.takehome.denshaotoko.takehome.github;

import com.takehome.denshaotoko.takehome.BasePresenter;
import com.takehome.denshaotoko.takehome.BaseView;
import com.takehome.denshaotoko.takehome.data.Repo;

import java.util.List;

public interface GithubContract {

    interface View extends BaseView<Presenter>{

        void showUserDetails(String url, String name);

        void updateRepoList(List<Repo> repoList);

        void showDataLoadError(String result);

    }

    interface Presenter extends BasePresenter<View>{

        void getUserData(String userId);

        void getRepoData(String userId);
    }
}
