package com.essue.jabac.core.policy.condition;

import com.essue.jabac.core.Principal;
import com.essue.jabac.core.Resource;
import com.essue.jabac.core.policy.Policy;

import java.util.Map;

public interface ConditionMatcher {

  /**
   * Match conditions of the given policy against the principal and resource Only return success if
   * ALL conditions are met or no condition specified
   *
   * @param policy
   * @return
   */
  boolean matchConditions(
      Policy policy, Principal principal, Resource resource, Map<String, Object> env);
}
