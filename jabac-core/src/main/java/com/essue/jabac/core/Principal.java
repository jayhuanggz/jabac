package com.essue.jabac.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an object that is requesting actions for a resource, it can be a logged in user, a
 * system, etc
 */
public class Principal {

  private String identifier;

  private Map<String, Object> attributes;

  public Principal(String identifier) {
    this.identifier = identifier;
    this.attributes = new HashMap<>();
  }

  public Principal() {}

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }
}
