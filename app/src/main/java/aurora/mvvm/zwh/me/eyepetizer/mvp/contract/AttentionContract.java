package aurora.mvvm.zwh.me.eyepetizer.mvp.contract;


import android.database.Observable;

import java.util.List;

import aurora.mvvm.zwh.me.eyepetizer.mvp.model.entity.AttentionInfo;
import aurora.mvvm.zwh.me.eyepetizer.mvp.model.entity.MyAttentionEntity;


public interface AttentionContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void setData(List<AttentionInfo.ItemListBeanX> itemList, boolean isLoadMore);
        void setAuthorList(List<MyAttentionEntity> list);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<AttentionInfo> getAttentionVideoList(int start);
        Observable<List<MyAttentionEntity>> getMyAttentionList(String userid);
    }
}
