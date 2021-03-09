package com.essue.jabac.core;

import com.essue.jabac.core.policy.Policy;
import com.essue.jabac.core.policy.PolicyMatcherBuilder;
import com.essue.jabac.core.policy.condition.ConditionEvaluator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class EngineTest {

  private Engine engine;

  private Collection<Policy> policies;

  private Principal principal;

  private Resource resource;

  private String tenantId = "testtenantId";

  @Before
  public void init() {

    this.policies = createPolicies();

    engine = new EngineBuilder().authManager(new PolicyMatcherBuilder().build()).build();

    principal = new Principal("1");
    principal.getAttributes().put("tenantId", tenantId);

    resource = new Resource("order:*");
    resource.getAttributes().put("tenantId", tenantId);
    resource.getAttributes().put("ownerId", principal.getIdentifier());
    resource.getAttributes().put("deletable", true);
    resource.getAttributes().put("private", true);
  }

  // policy1: READ on "order:*" is ALLOW if
  // resource.attributes.tenantId = principal.attributes.tenantId

  // policy2: WRITE on "order:*" is ALLOW if
  // resource.attributes.ownerId = principal.identifier

  // policy3: DELETE on "order:*" is ALLOW if
  // resource.attributes.ownerId = principal.attributes.ownerId
  // and resource.attribute.deletable = true

  private Collection<Policy> createPolicies() {

    Policy policy1 = new Policy("p1");
    policy1.setResources(Arrays.asList("order:*"));
    policy1.setEffect(Effect.ALLOW);
    policy1.setActions(Arrays.asList("READ"));
    policy1.setConditions(
        Collections.singletonMap(
            ConditionEvaluator.EQUALS.getOp(),
            Collections.singletonMap(
                "resource.attributes.tenantId", "@{principal.attributes.tenantId}")));

    Policy policy2 = new Policy("p2");
    policy2.setResources(Arrays.asList("order:*"));
    policy2.setEffect(Effect.ALLOW);
    policy2.setActions(Arrays.asList("WRITE"));
    policy2.setConditions(
        Collections.singletonMap(
            ConditionEvaluator.EQUALS.getOp(),
            Collections.singletonMap("resource.attributes.ownerId", "@{principal.identifier}")));

    Policy policy3 = new Policy("p3");
    policy3.setResources(Arrays.asList("order:*"));
    policy3.setEffect(Effect.ALLOW);
    policy3.setActions(Arrays.asList("DELETE"));
    Map<String, Map<String, Object>> conditions3 = new HashMap<>();

    conditions3.put(
        ConditionEvaluator.EQUALS.getOp(),
        Collections.singletonMap("resource.attributes.ownerId", "@{principal.identifier}"));

    conditions3.put(
        ConditionEvaluator.BOOL.getOp(),
        Collections.singletonMap("resource.attributes.deletable", true));
    policy3.setConditions(conditions3);

    return Arrays.asList(policy1, policy2, policy3);
  }

  @Test
  public void evaluateAccess_allowWrite() {

    Assert.assertEquals(
        Effect.ALLOW,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("WRITE")
                .build()));
  }

  @Test
  public void evaluateAccess_allowRead() {

    Assert.assertEquals(
        Effect.ALLOW,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("READ")
                .build()));
  }

  @Test
  public void evaluateAccess_allowDelete() {

    Assert.assertEquals(
        Effect.ALLOW,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("DELETE")
                .build()));
  }

  @Test
  public void evaluateAccess_denyDeleteIfOwnerIdNotMatch() {

    principal.setIdentifier("2");

    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("DELETE")
                .build()));
  }

  @Test
  public void evaluateAccess_denyDeleteIfAttributeNotMatch() {
    resource.getAttributes().put("deletable", false);

    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("DELETE")
                .build()));
  }

  @Test
  public void evaluateAccess_denyWriteIfOwnerIdNotMatch() {
    principal.setIdentifier("2");

    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("WRITE")
                .build()));
  }

  @Test
  public void evaluateAccess_denyReadIfResourceNotMatch() {
    resource.setIdentifier("image:");
    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("READ")
                .build()));
  }

  @Test
  public void evaluateAccess_denyReadIfTenantIdNotMatch() {
    resource.getAttributes().put("tenantId", "anotherId");
    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("READ")
                .build()));
  }

  @Test
  public void evaluateAccess_denyAllIfNoPolicyMatches() {
    resource.getAttributes().put("tenantId", "anotherId");
    resource.getAttributes().put("ownerId", "anotherId");

    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("READ")
                .build()));

    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("WRITE")
                .build()));

    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("DELETE")
                .build()));
  }

  @Test
  public void evaluateAccess_denyIfActionNotMatch() {

    for (final Policy policy : policies) {
      policy.setActions(Arrays.asList("order:WRITE"));
    }

    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("order:READ")
                .build()));
  }

  @Test
  public void evaluateAccess_actionWithWildCardMatch() {

    for (final Policy policy : policies) {
      policy.setActions(Arrays.asList("order:*"));
    }

    Assert.assertEquals(
        Effect.ALLOW,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("order:READ")
                .build()));
  }

  @Test
  public void evaluateAccess_actionWithWildCardNotMatch() {

    for (final Policy policy : policies) {
      policy.setActions(Arrays.asList("order:*"));
    }

    Assert.assertEquals(
        Effect.DENY,
        engine.evaluateAccess(
            policies,
            new AuthRequest.AuthRequestBuilder(UUID.randomUUID().toString())
                .principal(principal)
                .resource(resource)
                .action("customer:READ")
                .build()));
  }
}
