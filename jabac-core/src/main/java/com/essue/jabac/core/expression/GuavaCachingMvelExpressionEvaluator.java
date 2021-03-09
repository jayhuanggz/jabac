package com.essue.jabac.core.expression;

import com.google.common.cache.Cache;

import java.util.function.Function;

public class GuavaCachingMvelExpressionEvaluator extends MvelExpressionEvaluator {

  private final Cache<String, Function<EvaluationContext, Object>> cache;

  public GuavaCachingMvelExpressionEvaluator(
      Cache<String, Function<EvaluationContext, Object>> cache) {
    this.cache = cache;
  }

  @Override
  protected Function<EvaluationContext, Object> saveCompiledFunctionToCacheIfAbsent(
      String expression, Function<EvaluationContext, Object> function) {
    cache.put(expression, function);
    return null;
  }

  @Override
  protected Function<EvaluationContext, Object> getCompiledFunctionFromCache(String expression) {
    return cache.getIfPresent(expression);
  }
}
