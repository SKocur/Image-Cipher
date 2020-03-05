package com.skocur.imagecipher.plugin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imagecipher.icsdk.PluginInstance;

import java.util.List;

public class PluginConfiguration {

  private String pluginVersion;
  private String pluginName;
  private List<PluginAuthor> pluginAuthors;
  private String pluginDescription;
  private String mainClassPath;

  private PluginInstance pluginInstance;

  public String getPluginVersion() {
    return pluginVersion;
  }

  @JsonProperty("pluginVersion")
  public void setPluginVersion(String pluginVersion) {
    this.pluginVersion = pluginVersion;
  }

  public String getPluginName() {
    return pluginName;
  }

  @JsonProperty("pluginName")
  public void setPluginName(String pluginName) {
    this.pluginName = pluginName;
  }

  public List<PluginAuthor> getPluginAuthors() {
    return pluginAuthors;
  }

  @JsonProperty("pluginAuthors")
  public void setPluginAuthors(List<PluginAuthor> pluginAuthors) {
    this.pluginAuthors = pluginAuthors;
  }

  public String getMainClassPath() {
    return mainClassPath;
  }

  @JsonProperty("mainClassPath")
  public void setMainClassPath(String mainClassPath) {
    this.mainClassPath = mainClassPath;
  }

  public String getPluginDescription() {
    return pluginDescription;
  }

  @JsonProperty("pluginDescription")
  public void setPluginDescription(String pluginDescription) {
    this.pluginDescription = pluginDescription;
  }

  public PluginInstance getPluginInstance() {
    return pluginInstance;
  }

  public void setPluginInstance(PluginInstance pluginInstance) {
    this.pluginInstance = pluginInstance;
  }
}
