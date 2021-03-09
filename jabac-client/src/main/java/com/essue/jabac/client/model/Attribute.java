package com.essue.jabac.client.model;

import java.lang.reflect.Field;

public class Attribute {

  private Field field;

  private String alias;

  private String name;

  private String path;

  public Attribute(Field field, String name, String alias, String path) {
    this.field = field;
    this.alias = alias;
    this.name = name;
    this.path = path;
  }

  public Field getField() {
    return field;
  }

  public String getAlias() {
    return alias;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }
}
