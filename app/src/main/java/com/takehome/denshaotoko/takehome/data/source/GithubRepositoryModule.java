package com.takehome.denshaotoko.takehome.data.source;

import com.takehome.denshaotoko.takehome.data.source.remote.GithubRemoteDataSource;
import com.takehome.denshaotoko.takehome.network.NetworkService;

import dagger.Module;
import dagger.Provides;

@Module
public class GithubRepositoryModule {

    @Provides
    GithubRepository provideMusicRepository(NetworkService networkService){
        return new GithubRepository(new GithubRemoteDataSource(networkService));
    }
}
