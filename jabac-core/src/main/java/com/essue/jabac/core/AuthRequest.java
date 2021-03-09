package com.essue.jabac.core;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

public class AuthRequest {

  private final String requestId;

  private final Principal principal;

  private final Resource resource;

  private final String action;

  private final Map<String, Object> env;

  public AuthRequest(String requestId, Principal principal, Resource resource, String action) {
    this(requestId, principal, resource, action, new HashMap<>());
  }

  public AuthRequest(
      String requestId, Principal principal, Resource resource, Map<String, Object> env) {
    this(requestId, principal, resource, null, env);
  }

  public AuthRequest(String requestId, Principal principal, Resource resource) {
    this(requestId, principal, resource, null, new HashMap<>());
  }

  public AuthRequest(
      String requestId,
      Principal principal,
      Resource resource,
      String action,
      Map<String, Object> env) {
    Preconditions.checkArgument(principal != null, "must specify principal");
    Preconditions.checkArgument(resource != null, "must specify resource");
    this.requestId = requestId;
    this.principal = principal;
    this.resource = resource;
    this.action = action;
    this.env = env;
  }

  public String getRequestId() {
    return requestId;
  }

  public String getAction() {
    return action;
  }

  public Principal getPrincipal() {
    return principal;
  }

  public Resource getResource() {
    return resource;
  }

  public Map<String, Object> getEnv() {
    return env;
  }

  public static class AuthRequestBuilder {

    private String requestId;

    private Principal principal;

    private Resource resource;

    private String action;

    private Map<String, Object> env;

    public AuthRequestBuilder(String requestId) {
      this.requestId = requestId;
    }

    public AuthRequestBuilder() {}

    public AuthRequestBuilder principal(Principal principal) {
      this.principal = principal;
      return this;
    }

    public AuthRequestBuilder resource(Resource resource) {
      this.resource = resource;
      return this;
    }

    public AuthRequestBuilder action(String action) {
      this.action = action;
      return this;
    }

    public AuthRequestBuilder putEnv(String key, Object value) {

      if (env == null) {
        this.env = new HashMap<>();
      }
      this.env.put(key, value);
      return this;
    }

    public AuthRequestBuilder env(Map<String, Object> env) {
      this.env = env;
      return this;
    }

    public AuthRequest build() {
      return new AuthRequest(requestId, principal, resource, action, env);
    }
  }
}
