package com.essue.jabac.core.policy.condition;

import com.essue.jabac.core.Principal;
import com.essue.jabac.core.Resource;
import com.essue.jabac.core.exception.JabacException;
import com.essue.jabac.core.expression.EvaluationContext;
import com.essue.jabac.core.expression.ExpressionEvaluator;
import com.essue.jabac.core.policy.Policy;
import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConditionMatcherImpl implements ConditionMatcher {

  private static final Logger LOGGER = Logger.getLogger(ConditionMatcherImpl.class.getName());

  private final Map<String, ConditionEvaluator> conditionEvaluators;

  private final ExpressionEvaluator expressionEvaluator;

  public ConditionMatcherImpl(
      final Collection<ConditionEvaluator> conditionEvaluators,
      final ExpressionEvaluator expressionEvaluator) {
    Preconditions.checkArgument(
        conditionEvaluators != null && !conditionEvaluators.isEmpty(),
        "Must specify at least one ConditionEvaluator!");
    this.conditionEvaluators = new HashMap<>(conditionEvaluators.size(), 2f);
    for (ConditionEvaluator conditionEvaluator : conditionEvaluators) {
      Preconditions.checkArgument(
          !this.conditionEvaluators.containsKey(conditionEvaluator.getOp()),
          "Duplicate ConditionEvaluator for: " + conditionEvaluator.getOp());
      this.conditionEvaluators.put(conditionEvaluator.getOp(), conditionEvaluator);
    }
    this.expressionEvaluator = expressionEvaluator;
  }

  @Override
  public boolean matchConditions(
      Policy policy, Principal principal, Resource resource, Map<String, Object> env) {

    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine(
          String.format(
              "Start to match policy %s against principal %s, resource %s......",
              policy.getIdentifier(), principal.getIdentifier(), resource.getIdentifier()));
    }

    Map<String, Map<String, Object>> conditions = policy.getConditions();
    if (conditions == null || conditions.isEmpty()) {
      // no condition specified, return true
      return true;
    }

    EvaluationContext evaluationContext = new EvaluationContext(principal, resource, env);
    for (Map.Entry<String, Map<String, Object>> condition : conditions.entrySet()) {

      ConditionEvaluator evaluator = conditionEvaluators.get(condition.getKey());
      if (evaluator == null) {
        throw new JabacException("No ConditionEvaluator found for op: " + condition.getKey());
      }

      Map<String, Object> expressions = condition.getValue();
      if (expressions.isEmpty()) {
        continue;
      }

      for (Map.Entry<String, Object> entry : expressions.entrySet()) {
        Object left = expressionEvaluator.evaluate(evaluationContext, entry.getKey(), true);

        Object valueExpression = entry.getValue();

        if (valueExpression instanceof Iterable) {
          Iterator it = ((Iterable) valueExpression).iterator();
          boolean matched = false;
          while (it.hasNext()) {
            Object expression = it.next();

            Object value =
                expression instanceof String
                    ? expressionEvaluator.evaluate(evaluationContext, expression.toString(), false)
                    : expression;

            if (evaluate(left, value, evaluator)) {
              matched = true;
              break;
            }
          }
          if (!matched) {
            if (LOGGER.isLoggable(Level.FINE)) {
              LOGGER.fine(
                  String.format("Policy condition not match for condition: %s", entry.getKey()));
            }
            return false;
          }
        } else {

          final Object right;
          if (valueExpression instanceof String) {
            right =
                expressionEvaluator.evaluate(evaluationContext, valueExpression.toString(), false);
          } else {
            right = valueExpression;
          }
          if (!evaluate(left, right, evaluator)) {
            if (LOGGER.isLoggable(Level.FINE)) {
              LOGGER.fine(
                  String.format("Policy condition not match for condition: %s", entry.getKey()));
            }
            return false;
          }
        }
      }
    }

    return true;
  }

  private boolean evaluate(Object left, Object right, ConditionEvaluator evaluator) {
    if (left == null || right == null) {
      return false;
    }

    return evaluator.evaluate(left, right);
  }
}
