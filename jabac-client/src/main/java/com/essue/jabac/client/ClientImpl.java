package com.essue.jabac.client;

import com.essue.jabac.client.policy.PolicyLoader;
import com.essue.jabac.client.principal.PrincipalProvider;
import com.essue.jabac.client.resource.ResourceService;
import com.essue.jabac.core.*;
import com.essue.jabac.core.policy.Policy;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class ClientImpl implements Client {

  private static final Logger LOGGER = Logger.getLogger(ClientImpl.class.getName());

  private PrincipalProvider principalProvider;

  private PolicyLoader policyLoader;

  private ResourceService resourceService;

  private Engine engine;

  public ClientImpl(
      final Engine engine,
      final PrincipalProvider principalProvider,
      final PolicyLoader policyLoader,
      final ResourceService resourceService) {
    this.engine = engine;
    this.principalProvider = principalProvider;
    this.policyLoader = policyLoader;
    this.resourceService = resourceService;
  }

  @Override
  public Effect evaluateAccess(
      final String requestId,
      final Serializable resourceId,
      final Class resourceType,
      final String action,
      final Map<String, Object> env) {

    Preconditions.checkArgument(requestId != null, "must specify requestId!");
    Preconditions.checkArgument(resourceId != null, "must specify resourceId!");
    Preconditions.checkArgument(resourceType != null, "must specify resourceType!");
    Preconditions.checkArgument(action != null, "must specify action!");

    Principal principal = principalProvider.provide(env);
    Preconditions.checkArgument(principal != null, "No principal found in the current request!");

    Resource resource = resourceService.findResource(resourceId, resourceType);
    if (resource == null) {
      LOGGER.info(
          String.format(
              "No resource of type %s found with id %s", resourceType.getName(), resourceId));
      return Effect.DENY;
    }

    Context context = new Context(principal, env);
    Collection<Policy> policies = policyLoader.loadPolicies(context);
    if (policies == null || policies.isEmpty()) {
      LOGGER.info("No policies found for the request");
      return Effect.DENY;
    }

    AuthRequest request =
        new AuthRequest.AuthRequestBuilder(requestId)
            .action(action)
            .principal(principal)
            .resource(resource)
            .env(env)
            .build();

    return engine.evaluateAccess(policies, request);
  }

  @Override
  public Collection<Policy> findApplicablePolicies(
      final Class resourceType, final String action, final Map<String, Object> env) {

    Preconditions.checkArgument(resourceType != null, "must specify resourceType!");

    Principal principal = principalProvider.provide(env);
    Preconditions.checkArgument(principal != null, "No principal found in the current request!");

    Context context = new Context(principal, env);

    Collection<Policy> policies = policyLoader.loadPolicies(context);

    if (policies == null || policies.isEmpty()) {
      LOGGER.info("No policies found for the request");
      return Collections.emptyList();
    }

    return engine.findApplicablePolicies(policies, principal, action);
  }

  @Override
  public Engine getEngine() {
    return engine;
  }
}
