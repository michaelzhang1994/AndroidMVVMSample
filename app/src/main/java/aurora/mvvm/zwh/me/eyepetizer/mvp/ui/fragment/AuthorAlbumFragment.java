package aurora.mvvm.zwh.me.eyepetizer.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apt.TRouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseLazyLoadFragment;
import com.jess.arms.di.component.AppComponent;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerAuthorDetailAlbumComponent;
import com.zwh.mvparms.eyepetizer.di.module.AuthorDetailModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.AuthorDetailContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorAlbumInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorDynamicInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorIndexInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AuthorTabsInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.ShareInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.presenter.AuthorDetailPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.AuthorAlbumAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\11\23 0023.
 */

public class AuthorAlbumFragment extends BaseLazyLoadFragment<AuthorDetailPresenter> implements AuthorDetailContract.View, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefresh;
    RecyclerView mRecyclerView;
    private AuthorAlbumAdapter adapter;
    private List<AuthorAlbumInfo.Album> data = new ArrayList<>();
    private View footView;
    private int id;


    public static AuthorAlbumFragment newInstance(int id) {
        Bundle arguments = new Bundle();
        arguments.putInt(Constants.AUTHOR_ID, id);
        AuthorAlbumFragment fragment = new AuthorAlbumFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.AUTHOR_ID, id);
        getArguments().putBundle(Constants.AUTHOR_ID, outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            id = savedInstanceState.getInt(Constants.AUTHOR_ID);
        }else {
            id = (int) getArguments().get(Constants.AUTHOR_ID);
        }
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerAuthorDetailAlbumComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .authorDetailModule(new AuthorDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.common_recyclerview, container, false);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources()
                        .getDisplayMetrics()));
        mSwipeRefresh.setOnRefreshListener(this);
        adapter = new AuthorAlbumAdapter(R.layout.item_attention_container,data);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                gotoPlaylist(view,position);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getAuthorAlbumList(id,adapter.getData().size(),true);
            }
        },mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    @SingleClick
    private void gotoPlaylist(View view, int position) {
        TRouter.go(Constants.VIDEOLIST,new DataExtra(Constants.COMMON_ID,
                        adapter.getData().get(position).getData().getHeader().getId()).add(Constants.COMMON_TYPE,adapter.getData().get(position).getData().getHeader().getTitle()).build()
                );
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void setData(Object data) {

    }

    @Override
    protected void loadData() {
        mPresenter.getAuthorAlbumList(id,0,false);
    }

    @Override
    public void showLoading() {
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public void setVideosData(List<VideoListInfo.Video> videos, boolean isLoadMore) {

    }

    @Override
    public void setTabs(AuthorTabsInfo info) {

    }

    @Override
    public void setShareInfo(ShareInfo info) {

    }

    @Override
    public void setIndexInfo(AuthorIndexInfo info) {

    }

    @Override
    public void setAuthorAlbumInfo(List<AuthorAlbumInfo.Album> itemList, boolean isLoadMore) {
        if (itemList.size()<10){
            if (footView == null){
                footView = getActivity().getLayoutInflater().inflate(R.layout.item_video_detail_foot, mRecyclerView, false);
            }
            if (adapter.getFooterLayoutCount()<=0){
                adapter.addFooterView(footView);
            }
            adapter.setEnableLoadMore(false);
        }
        if (isLoadMore){
            adapter.addData(itemList);
            adapter.loadMoreComplete();
        }else {
            adapter.setEnableLoadMore(true);
            if (adapter.getFooterLayoutCount()>0){
                adapter.removeFooterView(footView);
            }
            adapter.setNewData(itemList);
        }
    }

    @Override
    public void setAuthorDynamicInfo(List<AuthorDynamicInfo.Dynamic> itemList, boolean isLoadMore) {

    }

    @Override
    public void onRefresh() {
        mPresenter.getAuthorAlbumList(id,0,false);
    }
}
