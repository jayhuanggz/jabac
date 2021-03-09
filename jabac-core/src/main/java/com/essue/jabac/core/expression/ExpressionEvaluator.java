package com.essue.jabac.core.expression;

public interface ExpressionEvaluator {

  Object evaluate(EvaluationContext context, String expression, boolean forKey);
}
