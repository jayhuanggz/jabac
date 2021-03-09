package com.essue.jabac.client.query.criteria;

public interface Criteria {

  String getKey();

  Object getValue();

  Criteria and(Criteria another);

  Criteria or(Criteria... ors);

  void accept(CriteriaVisitor visitor);
}
