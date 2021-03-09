package com.essue.jabac.client.query.criteria;

public class Eq extends AbstractCriteria {

  public Eq(final String key, final Object value) {
    super(key, value);
  }

  @Override
  public void accept(CriteriaVisitor visitor) {
    visitor.visitEq(this);
  }
}
