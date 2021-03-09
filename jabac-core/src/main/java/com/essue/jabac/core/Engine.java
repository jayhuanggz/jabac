package com.essue.jabac.core;

import com.essue.jabac.core.policy.Policy;

import java.util.Collection;

public interface Engine {

  /**
   * Authorize the given request
   *
   * @param policies
   * @param request
   * @return {@see Effect.ALLOW} if the request is permitted, {@see Effect.DENY} otherwise
   */
  Effect evaluateAccess(Collection<Policy> policies, AuthRequest request);

  Collection<Policy> findApplicablePolicies(
      Collection<Policy> policies, Principal principal, String action);
}
