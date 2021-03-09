package com.essue.jabac.core.policy;

import com.essue.jabac.core.expression.MvelExpressionEvaluator;
import com.essue.jabac.core.path.DefaultPathMatcher;
import com.essue.jabac.core.path.PathMatcher;
import com.essue.jabac.core.policy.condition.ConditionEvaluator;
import com.essue.jabac.core.policy.condition.ConditionMatcher;
import com.essue.jabac.core.policy.condition.ConditionMatcherImpl;
import com.google.common.base.Preconditions;

public class PolicyMatcherBuilder {

  private PathMatcher pathMatcher = new DefaultPathMatcher();

  private ConditionMatcher conditionMatcher;

  public PolicyMatcherBuilder pathMatcher(PathMatcher pathMatcher) {
    this.pathMatcher = pathMatcher;
    return this;
  }

  public PolicyMatcherBuilder conditionMatcher(ConditionMatcher conditionMatcher) {
    this.conditionMatcher = conditionMatcher;
    return this;
  }

  public PolicyMatcher build() {
    Preconditions.checkArgument(pathMatcher != null, "pathMatcher cannot be null!");

    if (conditionMatcher == null) {
      conditionMatcher =
          new ConditionMatcherImpl(
              ConditionEvaluator.DEFAULT_EVALUATORS, new MvelExpressionEvaluator());
    }

    return new PolicyMatcherImpl(pathMatcher, conditionMatcher);
  }
}
