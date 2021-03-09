package com.essue.jabac.client.model;

import java.util.Map;

public class ResourceMetaData {

  private Class type;

  private Namespace namespace;

  private Attribute idAttribute;

  private Map<String, Attribute> attributes;

  public ResourceMetaData(
      final Class type,
      final Namespace namespace,
      final Attribute idAttribute,
      final Map<String, Attribute> attributes) {
    this.type = type;
    this.namespace = namespace;
    this.idAttribute = idAttribute;
    this.attributes = attributes;
  }

  public Class getType() {
    return type;
  }

  public Namespace getNamespace() {
    return namespace;
  }

  public Attribute getIdAttribute() {
    return idAttribute;
  }

  public Map<String, Attribute> getAttributes() {
    return attributes;
  }
}
