package com.essue.jabac.client;

import com.essue.jabac.core.Principal;

import java.util.Map;

public class Context {

  private Principal principal;

  private Map<String, Object> env;

  public Context(final Principal principal, final Map<String, Object> env) {
    this.principal = principal;
    this.env = env;
  }

  public Principal getPrincipal() {
    return principal;
  }

  public Map<String, Object> getEnv() {
    return env;
  }
}
