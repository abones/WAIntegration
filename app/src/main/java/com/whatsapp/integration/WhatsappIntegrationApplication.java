package com.whatsapp.integration;

import android.app.Activity;
import android.app.Application;

import com.rubius.androidshared.viewmodels.IApplicationViewModel;
import com.whatsapp.integration.dagger.components.ActivityComponent;
import com.whatsapp.integration.dagger.components.ApplicationComponent;
import com.whatsapp.integration.dagger.components.DaggerActivityComponent;
import com.whatsapp.integration.dagger.components.DaggerApplicationComponent;
import com.whatsapp.integration.dagger.modules.ActivityModule;
import com.whatsapp.integration.dagger.modules.ApplicationModule;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 *
 */
public class WhatsappIntegrationApplication extends Application {
    @Inject
    protected IApplicationViewModel viewModel;
    private ApplicationComponent applicationComponent;

    private final Map<Class<? extends Activity>, ActivityComponent> activityClassComponents = new HashMap<>();

    private ApplicationComponent createApplicationComponent() {
        //noinspection LawOfDemeter
        return DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    }

    public ActivityComponent getActivityComponent(Activity activity) {
        Class<? extends Activity> activityClass = activity.getClass();

        ActivityComponent result = activityClassComponents.get(activityClass);

        if (result == null) {
            result = DaggerActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(activity))
                .build();
            activityClassComponents.put(activityClass, result);
        }

        return result;
    }

    public void releaseActivityComponent(Class<? extends Activity> activityClass) {
        activityClassComponents.remove(activityClass);
    }

    // region Overrides of Application

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = createApplicationComponent();
        applicationComponent.inject(this);
        viewModel.onCreate(null, null);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        viewModel.onTerminate();
    }

    // endregion Overrides of Application
}