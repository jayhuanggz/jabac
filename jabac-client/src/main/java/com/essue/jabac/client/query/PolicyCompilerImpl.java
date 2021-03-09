package com.essue.jabac.client.query;

import com.essue.jabac.client.query.criteria.And;
import com.essue.jabac.client.query.criteria.Criteria;
import com.essue.jabac.client.query.criteria.Or;
import com.essue.jabac.core.Principal;
import com.essue.jabac.core.expression.EvaluationContext;
import com.essue.jabac.core.expression.ExpressionEvaluator;
import com.essue.jabac.core.policy.Policy;
import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class PolicyCompilerImpl implements PolicyCompiler {

  private String resourceAttrPrefix = "resource.attributes.";

  private Map<String, ConditionCompiler> conditionCompilers;

  private ExpressionEvaluator expressionEvaluator;

  public PolicyCompilerImpl(
      final ExpressionEvaluator expressionEvaluator,
      final Map<String, ConditionCompiler> conditionCompilers) {
    this.conditionCompilers = conditionCompilers;
    this.expressionEvaluator = expressionEvaluator;
  }

  @Override
  public Criteria compile(
      final Policy policy, final Principal principal, final Map<String, Object> env) {

    Map<String, Map<String, Object>> conditions = policy.getConditions();

    EvaluationContext context = new EvaluationContext(principal, null, env);
    Collection<Criteria> ands = new LinkedList<>();

    for (final Map.Entry<String, Map<String, Object>> ops : conditions.entrySet()) {

      ConditionCompiler compiler = conditionCompilers.get(ops.getKey());
      Preconditions.checkArgument(
          compiler != null, "No ConditionCompiler found for: " + ops.getKey());

      for (final Map.Entry<String, Object> condition : ops.getValue().entrySet()) {

        String key = condition.getKey();

        if (!key.startsWith(resourceAttrPrefix)) {
          continue;
        }

        String attribute = key.substring(resourceAttrPrefix.length());

        Object valueExpression = condition.getValue();

        if (valueExpression instanceof Iterable) {
          Collection<Criteria> ors = new LinkedList<>();
          Iterator it = ((Iterable) valueExpression).iterator();
          while (it.hasNext()) {
            Object expression = it.next();

            Object value =
                expression instanceof String
                    ? expressionEvaluator.evaluate(context, expression.toString(), false)
                    : expression;

            ors.add(compiler.compile(attribute, value));
          }

          if (!ors.isEmpty()) {
            ands.add(new Or(ors));
          }

        } else {
          final Object right;
          if (valueExpression instanceof String) {
            right = expressionEvaluator.evaluate(context, valueExpression.toString(), false);
          } else {
            right = valueExpression;
          }

          ands.add(compiler.compile(attribute, right));
        }
      }
    }

    return new And(ands);
  }
}
