package com.essue.jabac.client;

import com.essue.jabac.core.Effect;
import com.essue.jabac.core.Engine;
import com.essue.jabac.core.policy.Policy;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface Client {

  Effect evaluateAccess(
      String requestId,
      Serializable resourceId,
      Class resourceType,
      String action,
      Map<String, Object> env);

  Collection<Policy> findApplicablePolicies(
      Class resourceType, String action, Map<String, Object> env);

  Engine getEngine();
}
