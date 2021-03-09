package com.essue.jabac.core.expression;

import com.essue.jabac.core.Principal;
import com.essue.jabac.core.Resource;

import java.util.Map;

public class EvaluationContext {

  private final Principal principal;

  private final Resource resource;

  private final Map<String, Object> env;

  public EvaluationContext(final Principal principal, final Resource resource, final Map<String, Object> env) {
    this.principal = principal;
    this.resource = resource;
    this.env = env;
  }

  public Map<String, Object> getEnv() {
    return env;
  }

  public Principal getPrincipal() {
    return principal;
  }

  public Resource getResource() {
    return resource;
  }
}
