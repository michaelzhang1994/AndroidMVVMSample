package aurora.mvvm.zwh.me.eyepetizer.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.zwh.mvparms.eyepetizer.mvp.contract.VideoListActivityContract;
import com.zwh.mvparms.eyepetizer.mvp.model.VideoListActivityModel;

import dagger.Module;
import dagger.Provides;


@Module
public class VideoListActivityModule {
    private VideoListActivityContract.View view;

    /**
     * 构建VideoListActivityModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VideoListActivityModule(VideoListActivityContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VideoListActivityContract.View provideVideoListActivityView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VideoListActivityContract.Model provideVideoListActivityModel(VideoListActivityModel model) {
        return model;
    }
}