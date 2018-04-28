package com.takehome.denshaotoko.takehome.github;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.takehome.denshaotoko.takehome.R;
import com.takehome.denshaotoko.takehome.data.Repo;
import com.takehome.denshaotoko.takehome.data.User;
import com.takehome.denshaotoko.takehome.network.NetworkService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;

public class GithubActivity extends DaggerAppCompatActivity implements GithubContract.View{

    @Inject
    GithubPresenter mGithubPresenter;

    @BindView(R.id.et_search)
    EditText mSearchText;

    @BindView(R.id.iv_user_icon)
    ImageView mUserIcon;

    @BindView(R.id.tv_user_name)
    TextView mUserView;

    @BindView(R.id.list_repo_details)
    RecyclerView mRecyclerView;

    /**
     * Listener for clicks on repo list view item
     */
    RepoItemListener repoItemListener = new RepoItemListener() {

        @Override
        public void onRepoClick(Repo clickedRepo) {

            showBottomSheetDialog(clickedRepo);

        }
    };

    private RepoAdapter mRepoAdapter;

    private Animation animTranslate;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // binding butter knife
        ButterKnife.bind(this);

        // set up the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRepoAdapter = new RepoAdapter(new ArrayList<Repo>(0), repoItemListener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRepoAdapter);

        // load translate animation
        animTranslate = AnimationUtils.loadAnimation(this,
                R.anim.anim_translate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGithubPresenter.takeView(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mGithubPresenter.dropView();
    }

    @OnClick(R.id.bt_search)
    public void onSearch(){
        userId = mSearchText.getText().toString().toLowerCase();
        mGithubPresenter.getUserData(userId);

        // hide soft keyboard
        InputMethodManager inputManager = (InputMethodManager) getSystemService(GithubActivity.INPUT_METHOD_SERVICE);
        if(inputManager!=null && getCurrentFocus() !=null)
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        // hide old views
        hideViews();
    }

    private void showBottomSheetDialog(Repo clickedRepo){

        BottomSheetDialog dialog = new BottomSheetDialog(GithubActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_bottom, null);

        TextView lastUpdateView, repoStarsView, repoForksView;
        lastUpdateView = dialogView.findViewById(R.id.tv_last_update);
        repoStarsView = dialogView.findViewById(R.id.tv_repo_stars);
        repoForksView = dialogView.findViewById(R.id.tv_repo_forks);

        String dateStr = clickedRepo.getUpdated_at();

        // parsing date from UTF
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
            final Date dateObj = sdf.parse(dateStr);
            dateStr = new SimpleDateFormat("MMM dd, yyyy KK:mm:ss a", Locale.CANADA).format(dateObj);
        } catch (ParseException ignored) {
        }

        lastUpdateView.setText(dateStr);
        repoStarsView.setText(clickedRepo.getStargazers_count());
        repoForksView.setText(clickedRepo.getForks());

        dialog.setContentView(dialogView);
        dialog.show();
    }

    @Override
    public void showUserDetails(String url, String name) {

        final ViewGroup transitionContainer = findViewById(R.id.layout_user_details);

        Picasso.with(this).load(url).into(mUserIcon);
        mUserView.setText(name);

        // start translate animation
        transitionContainer.startAnimation(animTranslate);

        mGithubPresenter.getRepoData(userId);
    }

    @Override
    public void updateRepoList(List<Repo> repoList) {

        mRepoAdapter.refreshData(repoList);

        mRecyclerView.setVisibility(View.VISIBLE);
        //start translate animation for rv
        mRecyclerView.startAnimation(animTranslate);
    }

    public void hideViews() {

        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showDataLoadError(String result) {
        switch (result) {
            case "user":
                Snackbar.make(findViewById(android.R.id.content), "User "+ userId+ " not found! Please enter valid username!", Snackbar.LENGTH_LONG).show();
                break;
            case "repo":
                Snackbar.make(findViewById(android.R.id.content), "Unable to fetch public repositories for " + userId, Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(findViewById(android.R.id.content), "Cannot load data! Check Internet?", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    public interface RepoItemListener {
        void onRepoClick(Repo clickedRepo);
    }

    public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.MyViewHolder>{

        private List<Repo> repoList;
        private RepoItemListener repoItemListener;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            @BindView(R.id.cv_repo_item)
            CardView repoCardView;

            @BindView(R.id.tv_repo_title)
            TextView repoTitle;

            @BindView(R.id.tv_repo_description)
            TextView repoDesc;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public RepoAdapter(List<Repo> repoList, RepoItemListener repoItemListener) {
            this.repoList = repoList;
            this.repoItemListener = repoItemListener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_repo_view, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.repoTitle.setText(repoList.get(position).getName());
            holder.repoDesc.setText(repoList.get(position).getDescription());

            holder.repoCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // on item click
                    repoItemListener.onRepoClick(repoList.get(position));
                }
            });
        }

        public void refreshData(List<Repo> repos) {

            if (repoList != null){
                if (repoList.size() > 0)
                    repoList.clear();
                repoList.addAll(repos);
            }
            notifyDataSetChanged();

            // scroll to start of the list
            mRecyclerView.getLayoutManager().scrollToPosition(0);
        }

        @Override
        public int getItemCount() {
            return repoList.size();
        }

    }
}
