package com.essue.jabac.client.query.criteria;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class And implements Criteria {

  private List<Criteria> criterion = new LinkedList<>();

  public And(final Criteria... criterion) {
    this.criterion.addAll(Arrays.asList(criterion));
  }

  public And(final Collection<Criteria> criterion) {
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
    criterion.add(another);
    return this;
  }

  @Override
  public Criteria or(final Criteria... ors) {
    return null;
  }

  @Override
  public void accept(CriteriaVisitor visitor) {
    visitor.visitAnd(this);
  }
}
