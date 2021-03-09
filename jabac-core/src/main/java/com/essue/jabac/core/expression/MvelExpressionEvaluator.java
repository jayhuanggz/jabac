package com.essue.jabac.core.expression;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.util.function.Function;

public class MvelExpressionEvaluator implements ExpressionEvaluator {

  @Override
  public Object evaluate(EvaluationContext context, String expression, boolean forKey) {

    final String fullExpression;
    if (forKey) {
      // expression for a key, need to enclose expression with orb tag
      fullExpression = "@{" + expression + "}";
    } else {
      fullExpression = expression;
    }

    String cacheKey = getCacheKey(fullExpression, forKey);
    Function<EvaluationContext, Object> function = getCompiledFunctionFromCache(cacheKey);
    if (function == null) {

      if (forKey || fullExpression.startsWith("@{")) {
        final CompiledTemplate compiled = TemplateCompiler.compileTemplate(fullExpression);
        function = evaluationContext -> TemplateRuntime.execute(compiled, evaluationContext);
      } else {
        function = (evaluationContext) -> expression;
      }

      Function<EvaluationContext, Object> prev =
          saveCompiledFunctionToCacheIfAbsent(cacheKey, function);
      if (prev != null) {
        function = prev;
      }
    }

    try {
      return function.apply(context);
    } catch (Exception e) {
      return null;
    }
  }

  protected String getCacheKey(String expression, boolean forKey) {
    return forKey ? expression + ":forKey" : expression;
  }

  protected Function<EvaluationContext, Object> getCompiledFunctionFromCache(String expression) {
    return null;
  }

  protected Function<EvaluationContext, Object> saveCompiledFunctionToCacheIfAbsent(
      String expression, Function<EvaluationContext, Object> function) {
    return null;
  }
}
