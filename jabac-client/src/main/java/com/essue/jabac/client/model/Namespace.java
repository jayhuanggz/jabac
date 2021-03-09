package com.essue.jabac.client.model;

import java.io.Serializable;

public final class Namespace implements Serializable {

  private String identifier;

  private Namespace parent;

  public Namespace(final Namespace parent, final String identifier) {
    this.identifier = identifier;
    this.parent = parent;
  }

  Namespace() {}

  public static Namespace parse(String namespace, String separator) {

    if (namespace == null || namespace.length() == 0) {
      return null;
    }

    String[] tokens = namespace.split(separator);
    if (tokens.length == 1) {
      return new Namespace(null, tokens[0]);
    }
    Namespace current = null;
    for (final String token : tokens) {
      if (current == null) {
        current = new Namespace(null, token);
      } else {
        current = new Namespace(current, token);
      }
    }
    return current;
  }

  public String getIdentifier() {
    return identifier;
  }

  public Namespace getParent() {
    return parent;
  }

  public String getFullPath(String separator) {
    return parent == null ? identifier : parent.getFullPath(separator) + separator + identifier;
  }

  public Namespace createSub(String identifier) {
    return new Namespace(this, identifier);
  }
}
