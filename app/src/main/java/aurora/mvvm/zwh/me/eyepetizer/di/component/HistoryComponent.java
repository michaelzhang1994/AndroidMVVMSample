package aurora.mvvm.zwh.me.eyepetizer.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zwh.mvparms.eyepetizer.di.module.HistoryModule;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.HistoryActivity;

import dagger.Component;

@ActivityScope
@Component(modules = HistoryModule.class, dependencies = AppComponent.class)
public interface HistoryComponent {
    void inject(HistoryActivity activity);
}