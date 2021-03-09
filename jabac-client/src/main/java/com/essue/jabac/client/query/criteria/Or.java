package com.essue.jabac.client.query.criteria;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Or implements Criteria {

  private List<Criteria> criterion = new LinkedList<>();

  public Or(final Criteria... criterion) {
    this.criterion.addAll(Arrays.asList(criterion));
  }

  public Or(final Collection<Criteria> criterion) {
    this.criterion.addAll(criterion);
  }

  @Override
  public String getKey() {
    return null;
  }

  @Override
  public Object getValue() {
    return null;
  }

  @Override
  public Criteria and(final Criteria another) {
    return new And(this, another);
  }

  @Override
  public Criteria or(final Criteria... ors) {
    this.criterion.addAll(Arrays.asList(ors));
    return this;
  }

  @Override
  public void accept(CriteriaVisitor visitor) {
    visitor.visitOr(this);
  }
}
