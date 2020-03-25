package com.skocur.imagecipher.rest.di;

import com.skocur.imagecipher.tools.UpdateChecker;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ServiceModule.class})
public interface ServiceComponent {

  void injectUpdater(UpdateChecker updateChecker);
}
