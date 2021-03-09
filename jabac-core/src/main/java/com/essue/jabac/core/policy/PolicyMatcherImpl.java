package com.essue.jabac.core.policy;

import com.essue.jabac.core.AuthContext;
import com.essue.jabac.core.Principal;
import com.essue.jabac.core.Resource;
import com.essue.jabac.core.path.PathMatcher;
import com.essue.jabac.core.policy.condition.ConditionMatcher;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PolicyMatcherImpl implements PolicyMatcher {

  private static final Logger LOGGER = Logger.getLogger(PolicyMatcherImpl.class.getName());

  private final PathMatcher pathMatcher;

  private final ConditionMatcher conditionMatcher;

  public PolicyMatcherImpl(final PathMatcher pathMatcher, final ConditionMatcher conditionMatcher) {
    this.pathMatcher = pathMatcher;
    this.conditionMatcher = conditionMatcher;
  }

  @Override
  public boolean matchPolicy(final Policy policy, final AuthContext context) {

    if (!matchPrincipal(policy, context.getRequest().getPrincipal())) {
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.fine(
            String.format(
                "Principal %s not match for policy %s",
                context.getRequest().getPrincipal().getIdentifier(), policy.getIdentifier()));
      }
      return false;
    }

    if (!matchAction(policy, context.getRequest().getAction())) {
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.fine(
            String.format(
                "Action %s not match for policy %s",
                context.getRequest().getAction(), policy.getIdentifier()));
      }
      return false;
    }

    if (!matchResource(policy, context.getRequest().getResource())) {
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.fine(
            String.format(
                "Resource %s not match for policy %s",
                context.getRequest().getResource().getIdentifier(), policy.getIdentifier()));
      }
      return false;
    }

    if (!matchConditions(policy, context)) {
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.fine(String.format("Conditions not match for policy %s", policy.getIdentifier()));
      }
      return false;
    }

    return true;
  }

  @Override
  public boolean matchPrincipal(final Policy policy, final Principal principal) {

    Object condition = policy.getPrincipal();
    if (condition == null) {
      return true;
    }

    if (condition instanceof Iterable) {
      return PolicyUtil.match((Iterable) condition, principal.getIdentifier(), pathMatcher);
    } else {
      return pathMatcher.matches(condition.toString(), principal.getIdentifier());
    }
  }

  @Override
  public boolean matchResource(final Policy policy, final Resource resource) {
    Collection<String> conditions = policy.getResources();
    if (conditions == null || conditions.isEmpty()) {
      return true;
    }

    return PolicyUtil.match(conditions, resource.getIdentifier(), pathMatcher);
  }

  @Override
  public boolean matchAction(final Policy policy, final String action) {
    Collection<String> conditions = policy.getActions();
    if (conditions == null || conditions.isEmpty()) {
      return true;
    }

    return PolicyUtil.match(conditions, action, pathMatcher);
  }

  @Override
  public boolean matchConditions(final Policy policy, final AuthContext context) {
    return conditionMatcher.matchConditions(
        policy,
        context.getRequest().getPrincipal(),
        context.getRequest().getResource(),
        context.getEnv());
  }
}
