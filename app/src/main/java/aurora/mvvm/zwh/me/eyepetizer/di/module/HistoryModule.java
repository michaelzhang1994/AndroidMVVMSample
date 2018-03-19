package aurora.mvvm.zwh.me.eyepetizer.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.zwh.mvparms.eyepetizer.mvp.contract.HistoryContract;
import com.zwh.mvparms.eyepetizer.mvp.model.HistoryModel;

import dagger.Module;
import dagger.Provides;


@Module
public class HistoryModule {
    private HistoryContract.View view;

    /**
     * 构建HistoryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HistoryModule(HistoryContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HistoryContract.View provideHistoryView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HistoryContract.Model provideHistoryModel(HistoryModel model) {
        return model;
    }
}