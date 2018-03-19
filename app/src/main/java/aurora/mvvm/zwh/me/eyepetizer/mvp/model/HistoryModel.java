package aurora.mvvm.zwh.me.eyepetizer.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.utils.StringUtils;
import com.zwh.mvparms.eyepetizer.app.utils.GreenDaoHelper;
import com.zwh.mvparms.eyepetizer.mvp.contract.HistoryContract;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.DaoMaster;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDaoEntity;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoDaoEntityDao;
import com.zwh.mvparms.eyepetizer.mvp.model.entity.VideoListInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;


@ActivityScope
public class HistoryModel extends BaseModel implements HistoryContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public HistoryModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<VideoDaoEntity>> getListFromDb(int start) {
        return Observable.create((ObservableOnSubscribe<List<VideoDaoEntity>>) e -> {
            VideoDaoEntity entity = new VideoDaoEntity();
            List<VideoDaoEntity> list = GreenDaoHelper.getInstance().create(entity.getDbName())
                    .getMaster().newSession()
                    .getVideoDaoEntityDao().queryBuilder()
                    .limit(10)
                    .offset(start)
                    .orderDesc(VideoDaoEntityDao.Properties.Date)
                    .list();
            List<VideoDaoEntity> infolist = new ArrayList<VideoDaoEntity>();
            if (!StringUtils.isEmpty(list)) {
                for (VideoDaoEntity entity1 : list) {
                    entity1.setVideo(mGson.fromJson(entity1.getBody(), VideoListInfo.Video.VideoData.class));
                    infolist.add(entity1);
                }
            }
            e.onNext(infolist);
        });
    }

    @Override
    public Observable<Boolean> deleteFromDb(VideoDaoEntity daoEntity) {
        return Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            DaoMaster master = GreenDaoHelper.getInstance().create(daoEntity.getDbName()).getMaster();
            if (master.newSession().getVideoDaoEntityDao().loadByRowId(daoEntity.getId()) == null) {
            } else {
                master.newSession()
                        .getVideoDaoEntityDao()
                        .delete(daoEntity);
                e.onNext(true);
            }
        });
    }

    @Override
    public Observable<List<VideoDaoEntity>> getListFromNet(int start, String userid) {
        return Observable.create((ObservableOnSubscribe<List<VideoDaoEntity>>) emitter -> {

            BmobQuery<VideoDaoEntity> query = new BmobQuery<VideoDaoEntity>();
            query.addWhereEqualTo("userId", userid);
            query.setLimit(10);
            query.order("-updatedAt");
            query.setSkip(start);
            query.findObjects(new FindListener<VideoDaoEntity>() {
                @Override
                public void done(List<VideoDaoEntity> list, BmobException e) {
                    List<VideoDaoEntity> infolist = new ArrayList<VideoDaoEntity>();
                    if (!StringUtils.isEmpty(list)) {
                        for (VideoDaoEntity entity1 : list) {
                            entity1.setVideo(mGson.fromJson(entity1.getBody(), VideoListInfo.Video.VideoData.class));
                            infolist.add(entity1);
                        }
                    }
                    emitter.onNext(infolist);
                }
            });
        });
    }

    @Override
    public Observable<Boolean> deleteFromNet(VideoDaoEntity entity) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            entity.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        emitter.onNext(true);
                    }
                }
            });
        });
    }
}