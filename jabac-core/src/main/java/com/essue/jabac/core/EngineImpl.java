package com.essue.jabac.core;

import com.essue.jabac.core.audit.AuditService;
import com.essue.jabac.core.policy.Policy;
import com.essue.jabac.core.policy.PolicyMatcher;
import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.LinkedList;

public class EngineImpl implements Engine {

  private final PolicyMatcher policyMatcher;

  private AuditService auditService;

  public EngineImpl(final PolicyMatcher policyMatcher) {
    this.policyMatcher = policyMatcher;
  }

  public void setAuditService(final AuditService auditService) {
    this.auditService = auditService;
  }

  @Override
  public Effect evaluateAccess(final Collection<Policy> policies, final AuthRequest request) {

    Preconditions.checkArgument(policies != null && !policies.isEmpty(), "must specify policies");
    Preconditions.checkArgument(request != null, "must specify request");

    AuthContext context = new AuthContext(policies, request);

    // default effect DENY
    Effect result = Effect.DENY;

    // for each policy, match against the request,
    // if matches DENY, then return DENY directly
    // if an ALLOW is matched, return ALLOW
    for (Policy policy : policies) {

      if (policyMatcher.matchPolicy(policy, context)) {
        if (policy.getEffect() == Effect.DENY) {
          return Effect.DENY;
        } else {
          result = Effect.ALLOW;
        }
      }
    }

    if (auditService != null) {
      auditService.audit(context, result);
    }
    return result;
  }

  @Override
  public Collection<Policy> findApplicablePolicies(
      final Collection<Policy> policies, final Principal principal, final String action) {

    Preconditions.checkArgument(policies != null && !policies.isEmpty(), "must specify policies");
    Preconditions.checkArgument(principal != null, "must specify principal");
    Preconditions.checkArgument(action != null, "must specify action");

    Collection<Policy> result = new LinkedList<>();

    for (Policy policy : policies) {

      if (policyMatcher.matchAction(policy, action)
          && policyMatcher.matchPrincipal(policy, principal)) {
        result.add(policy);
      }
    }

    return result;
  }
}
