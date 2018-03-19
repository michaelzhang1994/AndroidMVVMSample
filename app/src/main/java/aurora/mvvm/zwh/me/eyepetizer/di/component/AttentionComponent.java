package aurora.mvvm.zwh.me.eyepetizer.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.zwh.mvparms.eyepetizer.di.module.AttentionModule;
import com.zwh.mvparms.eyepetizer.mvp.ui.fragment.AttentionFragment;

import dagger.Component;

@ActivityScope
@Component(modules = AttentionModule.class, dependencies = AppComponent.class)
public interface AttentionComponent {
    void inject(AttentionFragment fragment);
}