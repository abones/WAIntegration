package com.whatsapp.integration;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;

import com.whatsapp.integration.dagger.components.ActivityComponent;
import com.whatsapp.integration.dagger.components.ApplicationComponent;
import com.whatsapp.integration.dagger.components.DaggerActivityComponent;
import com.whatsapp.integration.dagger.components.DaggerApplicationComponent;
import com.whatsapp.integration.dagger.components.DaggerReceiverComponent;
import com.whatsapp.integration.dagger.components.DaggerServiceComponent;
import com.whatsapp.integration.dagger.components.ReceiverComponent;
import com.whatsapp.integration.dagger.components.ServiceComponent;
import com.whatsapp.integration.dagger.modules.ActivityModule;
import com.whatsapp.integration.dagger.modules.ApplicationModule;
import com.whatsapp.integration.viewmodels.IWhatsappIntegrationApplicationViewModel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 *
 */
public class WhatsappIntegrationApplication
        extends Application {
    @Inject
    protected IWhatsappIntegrationApplicationViewModel viewModel;
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
            //noinspection LawOfDemeter
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

    public ReceiverComponent getReceiverComponent(Context context) {
        //noinspection LawOfDemeter
        return DaggerReceiverComponent.builder()
                                      .applicationComponent(applicationComponent)
                                      //.receiverModule(new ReceiverModule(context))
                                      .build();
    }

    public ServiceComponent getServiceComponent(Service service) {
        //noinspection LawOfDemeter
        return DaggerServiceComponent.builder()
                                     .applicationComponent(applicationComponent)
                                     //.serviceModule(new ServiceModule(service))
                                     .build();
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
