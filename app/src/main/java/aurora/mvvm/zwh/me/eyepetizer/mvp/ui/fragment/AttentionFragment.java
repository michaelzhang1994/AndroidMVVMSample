package aurora.mvvm.zwh.me.eyepetizer.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.jess.arms.utils.StringUtils;
import com.jess.arms.utils.UiUtils;
import com.zwh.annotation.aspect.SingleClick;
import com.zwh.mvparms.eyepetizer.R;
import com.zwh.mvparms.eyepetizer.app.EventBusTags;
import com.zwh.mvparms.eyepetizer.app.constants.Constants;
import com.zwh.mvparms.eyepetizer.di.component.DaggerAttentionComponent;
import com.zwh.mvparms.eyepetizer.di.module.AttentionModule;
import com.zwh.mvparms.eyepetizer.mvp.contract.AttentionContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.AttentionInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.Category;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DataExtra;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.MyAttentionEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.User;
import com.zwh.mvparms.eyepetizer.mvp.presenter.AttentionPresenter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.AttentionAdapter;
import com.zwh.mvparms.eyepetizer.mvp.ui.adapter.AurhorListAdapter;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AttentionFragment extends BaseLazyLoadFragment<AttentionPresenter> implements AttentionContract.View, SwipeRefreshLayout.OnRefreshListener {


    RecyclerView mHeadRecyclerView;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefresh;
    View headView;
    View footView;
    private BaseQuickAdapter adapter;
    private List<AttentionInfo.ItemListBeanX> data = new ArrayList<>();
    private List<MyAttentionEntity> authors = new ArrayList<>();
    private String type = "";

    public static AttentionFragment newInstance(Category category) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.TYPE, category.getDescription());
        AttentionFragment fragment = new AttentionFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", type);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (String) getArguments().get(Constants.TYPE);
        if (savedInstanceState != null) {
            type = savedInstanceState.getString("type");
        }
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerAttentionComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .attentionModule(new AttentionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attention, container, false);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
        mHeadRecyclerView = (RecyclerView) view.findViewById(R.id.head_recyclerView);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefresh.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources()
                        .getDisplayMetrics()));
        if ("all".equals(type)) {
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.set(0, 0, 0, 20);
                }
            });
            adapter = new AttentionAdapter(R.layout.item_attention_container, data);
            adapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ("all".equals(type)) {
                        mPresenter.getAttentionVideoList(data.size(), true);
                    } else {

                    }
                }
            }, 200), mRecyclerView);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    gotoAuthorDetail(view, position);
                }
            });
            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    int id = view.getId();
//                    if (id == R.id.btn_attention) {
//                        FollowButton button = (FollowButton) view;
//                        follow(button, position);
//                    }
                }
            });
        } else {
            mSwipeRefresh.setEnabled(false);
            adapter = new AurhorListAdapter(R.layout.item_author_list, authors);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    gotoAuthorDetail(view, position);
                }
            });
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    @SingleClick
    private void gotoAuthorDetail(View view, int position) {
        if ("all".equals(type)) {
            TRouter.go(Constants.AUTHORDETAIL, new DataExtra(Constants.AUTHOR_ID, data.get(position).getData().getHeader().getId()).build());
        } else {
            TRouter.go(Constants.AUTHORDETAIL, new DataExtra(Constants.AUTHOR_ID, authors.get(position).getId()).build());
        }
    }


    @Override
    public void onRefresh() {
        if ("all".equals(type)) {
            mPresenter.getAttentionVideoList(0, false);
        } else {
            mPresenter.getMyAttentionList(BmobUser.getCurrentUser().getObjectId());
        }
    }


    @Override
    public void setData(List<AttentionInfo.ItemListBeanX> itemList, boolean isLoadMore) {
        if (isLoadMore) {
            adapter.loadMoreComplete();
            if (itemList.size() < 1) {
                adapter.setEnableLoadMore(false);
                if (footView == null) {
                    footView = getActivity().getLayoutInflater().inflate(R.layout.item_video_detail_foot, mRecyclerView, false);
                }
                adapter.addFooterView(footView);
            } else {
                data.addAll(itemList);
                adapter.addData(itemList);
            }
        } else {
            data.clear();
            adapter.removeFooterView(footView);
            adapter.setEnableLoadMore(true);
            data.addAll(itemList);
            adapter.setNewData(itemList);
        }
    }

    @Override
    public void setAuthorList(List<MyAttentionEntity> list) {
        authors.clear();
        authors.addAll(list);
        adapter.setNewData(list);
        mSwipeRefresh.setRefreshing(false);
        if (footView == null) {
            footView = getActivity().getLayoutInflater().inflate(R.layout.item_video_detail_foot, mRecyclerView, false);
            adapter.addFooterView(footView);
        }
    }

    @Override
    public void onVisible() {
        super.onVisible();
        if (!"all".equals(type) && BmobUser.getCurrentUser() != null) {
            if (mPresenter == null){
                return;
            }
            mPresenter.getMyAttentionList(BmobUser.getCurrentUser().getObjectId());
        }
    }

    @Override
    public void setData(Object data) {

    }


    @Override
    public void showLoading() {
        if (!mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    @Override
    protected void loadData() {
        if ("all".equals(type)) {
            mPresenter.getAttentionVideoList(0, false);
        } else {
            if (BmobUser.getCurrentUser() != null) {
                mPresenter.getMyAttentionList(BmobUser.getCurrentUser().getObjectId());
            } else {
                UiUtils.makeText(getActivity(), "请登录后查看");
            }

        }
    }

    @Subscriber(tag = EventBusTags.SET_USER_INFO)
    public void setUserInfo(User user) {
        if (!"all".equals(type) && user != null) {
            mPresenter.getMyAttentionList(user.getObjectId());
            mSwipeRefresh.setEnabled(true);
        }
    }

    @Subscriber(tag = EventBusTags.SETTING_ACTIVITY_LOG_OUT)
    public void logoutReset(String tag) {
        if (!"all".equals(type)){
            if (!StringUtils.isEmpty(adapter.getData())){
                adapter.getData().clear();
                adapter.notifyDataSetChanged();
            }

        }
    }
}
