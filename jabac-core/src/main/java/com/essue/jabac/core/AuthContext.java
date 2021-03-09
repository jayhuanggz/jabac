package com.essue.jabac.core;

import com.essue.jabac.core.policy.Policy;

import java.util.Collection;
import java.util.Map;

public class AuthContext {

  private final Collection<Policy> policies;

  private final AuthRequest request;

  private final Map<String, Object> env;

  public AuthContext(Collection<Policy> policies, AuthRequest request) {
    this.policies = policies;
    this.request = request;
    this.env = request.getEnv();
  }

  public AuthRequest getRequest() {
    return request;
  }

  public Collection<Policy> getPolicies() {
    return policies;
  }

  public Map<String, Object> getEnv() {
    return env;
  }
}
