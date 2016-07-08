/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.iquesoft.project.seed.presentation.di.components;

import android.content.Context;

import net.iquesoft.project.seed.domain.executor.PostExecutionThread;
import net.iquesoft.project.seed.domain.executor.ThreadExecutor;
import net.iquesoft.project.seed.presentation.di.modules.ApplicationModule;
import net.iquesoft.project.seed.presentation.di.modules.LoginModule;
import net.iquesoft.project.seed.presentation.view.activity.BaseActivity;
import net.iquesoft.project.seed.presentation.view.fragment.GalleryFragment;
import net.iquesoft.project.seed.presentation.view.fragment.LoginFragment;
import net.iquesoft.project.seed.presentation.view.fragment.SignUpFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    //Exposed to sub-graphs.
    Context context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    LoginComponent plus(LoginModule loginModule);

    void inject(LoginFragment loginFragment);

    void inject(GalleryFragment galleryFragment);

    void inject(SignUpFragment signUpFragment);
}
