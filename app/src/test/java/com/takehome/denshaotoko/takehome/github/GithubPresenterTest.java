package com.takehome.denshaotoko.takehome.github;

import com.google.common.collect.Lists;
import com.takehome.denshaotoko.takehome.data.Repo;
import com.takehome.denshaotoko.takehome.data.User;
import com.takehome.denshaotoko.takehome.data.source.GithubDataSource;
import com.takehome.denshaotoko.takehome.data.source.GithubRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLooper;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link GithubPresenter}
 */
@RunWith(RobolectricTestRunner.class)
public class GithubPresenterTest {

    private static User user;

    private static List<Repo> repos;

    private static final String USERID = "octocat";

    @Mock
    GithubRepository mGithubRepository;

    @Mock
    GithubContract.View mGithubView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<GithubDataSource.LoadUserCallback> mLoadUserCallbackCaptor;

    @Captor
    private ArgumentCaptor<GithubDataSource.LoadRepoCallback> mLoadRepoCallbackCaptor;

    private GithubPresenter mGithubPresenter;

    @Before
    public void setUpGithubPresenter(){

        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get reference to the current class under test
        mGithubPresenter = new GithubPresenter(mGithubRepository);
        mGithubPresenter.takeView(mGithubView);

        // Load the sample data for user and repos
        user = new User("name1","avatar1");
        repos = Lists.newArrayList(new Repo("name1","desc1","update1","stars1","forks1"),
                new Repo("name2","desc2","update2","stars2","forks2"),
                new Repo("name3","desc3","update3","stars3","forks3"));

    }

    @Test
    public void getUserDataFromRepository_LoadIntoView(){

        // call the method to fetch
        mGithubPresenter.getUserData(USERID);

        // Callback is captured and invoked with stubbed tasks
        verify(mGithubRepository).getUser(eq(USERID), mLoadUserCallbackCaptor.capture());
        mLoadUserCallbackCaptor.getValue().onUserFetched(user);

        // Capture arguments when loading to views
        ArgumentCaptor<String> showUserArgument1Captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> showUserArgument2Captor = ArgumentCaptor.forClass(String.class);
        verify(mGithubView).showUserDetails(showUserArgument1Captor.capture(), showUserArgument2Captor.capture());
        assertTrue(showUserArgument1Captor.getValue().equals("avatar1"));
        assertTrue(showUserArgument2Captor.getValue().equals("name1"));
    }

    @Test
    public void getRepoDataFromRepository_LoadIntoView(){

        // call the method to fetch
        mGithubPresenter.getRepoData(USERID);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        // Callback is captured and invoked with stubbed tasks
        verify(mGithubRepository).getRepos(eq(USERID), mLoadRepoCallbackCaptor.capture());
        mLoadRepoCallbackCaptor.getValue().onRepoFetched(repos);

        // Capture arguments when loading to views
        ArgumentCaptor<List> showRepoArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mGithubView).updateRepoList(showRepoArgumentCaptor.capture());
        assertTrue(showRepoArgumentCaptor.getValue().size() == 3);
    }

    @Test
    public void getUserFailed_ShowsError() {

        // call the method to fetch
        mGithubPresenter.getUserData(USERID);

        // Callback is captured and set to unavailable
        verify(mGithubRepository).getUser(eq(USERID), mLoadUserCallbackCaptor.capture());
        mLoadUserCallbackCaptor.getValue().onDataNotAvailable();

        // Then an error message is shown
        ArgumentCaptor<String> showArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mGithubView).showDataLoadError(showArgumentCaptor.capture());
        assertTrue(showArgumentCaptor.getValue().equals("user"));
    }

    @Test
    public void getRepoListFailed_ShowsError() {

        // call the method to fetch
        mGithubPresenter.getRepoData(USERID);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        // Callback is captured and invoked with stubbed tasks
        verify(mGithubRepository).getRepos(eq(USERID), mLoadRepoCallbackCaptor.capture());
        mLoadRepoCallbackCaptor.getValue().onDataNotAvailable();

        // Then an error message is shown
        ArgumentCaptor<String> showArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mGithubView).showDataLoadError(showArgumentCaptor.capture());
        assertTrue(showArgumentCaptor.getValue().equals("repo"));
    }
}
