package aurora.mvvm.zwh.me.eyepetizer.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.IndextVideoListInfo;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.List;

import io.reactivex.Observable;


public interface VideoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        RxPermissions getRxPermissions();
        void setData(List<VideoListInfo.Video> list, Boolean isPullRefresh);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<VideoListInfo> getVideoList(String type, String lastIdQueried, int startCount, boolean update);
        Observable<IndextVideoListInfo> getIndexVideoList(long date, int num);
        Observable<IndextVideoListInfo> getMoreIndexVideoList(int page);
    }
}
