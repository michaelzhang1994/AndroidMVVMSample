package aurora.mvvm.zwh.me.eyepetizer.mvp.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.zwh.mvparms.eyepetizer.R;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class MultiRecyclerView extends FrameLayout {

    private RecyclerView recyclerview;
    private FrameLayout fl_emptyView;
    private FrameLayout fl_loadingView;
    private FrameLayout fl_errorView;
    private MaterialProgressBar indicatorView;


    public MultiRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public MultiRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MultiRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        View layout = inflate(context, R.layout.layout_multi_recyclerview, this);
        fl_emptyView = (FrameLayout) layout.findViewById(R.id.fl_empty_view);
        fl_loadingView = (FrameLayout) layout.findViewById(R.id.fl_loading_view);
        fl_errorView = (FrameLayout) layout.findViewById(R.id.fl_error_view);
        recyclerview = (RecyclerView) layout.findViewById(R.id.recyclerView);
        indicatorView = (MaterialProgressBar) fl_loadingView.findViewById(R.id.loading);
    }

    public RecyclerView getRecyclerView(){
        return recyclerview;
    }

    public void setLoading(Boolean show){
        if (show){
            fl_loadingView.setVisibility(VISIBLE);
        }else {
            fl_loadingView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fl_loadingView.setVisibility(GONE);
                }
            },300);
        }
    }
}
