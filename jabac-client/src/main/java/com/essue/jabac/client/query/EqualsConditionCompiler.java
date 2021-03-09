package com.essue.jabac.client.query;

import com.essue.jabac.client.query.criteria.Criteria;
import com.essue.jabac.client.query.criteria.Eq;
import com.essue.jabac.core.policy.condition.ConditionEvaluator;

public class EqualsConditionCompiler implements ConditionCompiler {

  @Override
  public Criteria compile(final String key, final Object value) {
    return new Eq(key, value);
  }

  @Override
  public String getOp() {
    return ConditionEvaluator.EQUALS.getOp();
  }
}
