package aurora.mvvm.zwh.me.eyepetizer.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zwh.mvparms.eyepetizer.di.module.VideoListActivityModule;
import com.zwh.mvparms.eyepetizer.mvp.ui.activity.VideoListActivity;

import dagger.Component;

@ActivityScope
@Component(modules = VideoListActivityModule.class, dependencies = AppComponent.class)
public interface VideoListActivityComponent {
    void inject(VideoListActivity activity);
}