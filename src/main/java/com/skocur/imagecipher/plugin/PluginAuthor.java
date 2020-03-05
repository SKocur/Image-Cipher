package com.skocur.imagecipher.plugin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PluginAuthor {

  private String username;
  private String email;

  public String getUsername() {
    return username;
  }

  @JsonProperty("username")
  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  @JsonProperty("email")
  public void setEmail(String email) {
    this.email = email;
  }
}
