package com.skocur.imagecipher.rest.github;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Release {

  @Expose
  @SerializedName("tag_name")
  public String tagName;

  @Expose
  @SerializedName("name")
  public String name;

  @Expose
  @SerializedName("published_at")
  public Date publishedDate;

  public Date getPublishedDate() {
    return publishedDate;
  }

  public void setPublishedDate(Date publishedDate) {
    this.publishedDate = publishedDate;
  }

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
