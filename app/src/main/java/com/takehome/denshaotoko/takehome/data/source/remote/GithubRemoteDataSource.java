package com.takehome.denshaotoko.takehome.data.source.remote;

import android.util.Log;

import com.takehome.denshaotoko.takehome.data.Repo;
import com.takehome.denshaotoko.takehome.data.User;
import com.takehome.denshaotoko.takehome.data.source.GithubDataSource;
import com.takehome.denshaotoko.takehome.network.NetworkService;

import org.reactivestreams.Subscription;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GithubRemoteDataSource implements GithubDataSource{

    private final NetworkService mNetworkService;

    public GithubRemoteDataSource(NetworkService networkService){
        mNetworkService = networkService;
    }

    @Override
    public void getUser(String userId, final LoadUserCallback callback) {
        mNetworkService.getUser(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.computation())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v("Response received", "subscribed");
                    }

                    @Override
                    public void onNext(User user) {
                        callback.onUserFetched(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("Response received", "error:::"+e.toString());
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getRepos(String userId, final LoadRepoCallback callback) {
        mNetworkService.getUsersRepositories(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.computation())
                .subscribe(new Observer<List<Repo>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v("Response received", "subscribed");
                    }

                    @Override
                    public void onNext(List<Repo> list) {
                        Log.v("Response received:",""+list.size());
                        callback.onRepoFetched(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("Response received:","error");
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
