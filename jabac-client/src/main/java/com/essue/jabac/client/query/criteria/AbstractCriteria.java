package com.essue.jabac.client.query.criteria;

public abstract class AbstractCriteria implements Criteria {

  protected String key;

  protected Object value;

  public AbstractCriteria(final String key, final Object value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Criteria and(final Criteria another) {
    return new And(this, another);
  }

  @Override
  public Criteria or(final Criteria... ors) {
    return new Or(this).or(ors);
  }
}
