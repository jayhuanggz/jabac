package com.essue.jabac.core.policy;

import com.essue.jabac.core.Principal;
import com.essue.jabac.core.Resource;
import com.essue.jabac.core.AuthContext;

public interface PolicyMatcher {

  boolean matchPolicy(Policy policy, AuthContext context);

  boolean matchPrincipal(Policy policy, Principal principal);

  boolean matchResource(Policy policy, Resource resource);

  boolean matchAction(Policy policy, String action);

  boolean matchConditions(Policy policy, AuthContext context);
}
