package com.skocur.imagecipher.plugin.di;

import com.skocur.imagecipher.plugin.PluginLoader;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {LoaderModule.class})
public interface LoaderComponent {

  void inject(PluginLoader pluginLoader);
}
