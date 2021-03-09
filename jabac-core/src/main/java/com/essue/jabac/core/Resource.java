package com.essue.jabac.core;

import java.util.HashMap;
import java.util.Map;

/** A protected resource */
public class Resource {

  private String identifier;

  private Map<String, Object> attributes;

  public Resource(String identifier) {
    this.identifier = identifier;
    this.attributes = new HashMap<>();
  }

  public Resource(String identifier, Map<String, Object> attributes) {
    this.identifier = identifier;
    this.attributes = attributes;
  }

  public Resource() {}

  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}
