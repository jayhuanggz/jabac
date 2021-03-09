package com.essue.jabac.core.policy.condition;

import com.essue.jabac.core.Effect;
import com.essue.jabac.core.Principal;
import com.essue.jabac.core.Resource;
import com.essue.jabac.core.expression.MvelExpressionEvaluator;
import com.essue.jabac.core.policy.Policy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConditionMatcherImplTest {

  private ConditionMatcherImpl matcher;

  private Policy policy;

  private Principal principal;

  private Resource resource;

  private Map<String, Object> env;

  @Before
  public void init() {

    matcher =
        new ConditionMatcherImpl(
            ConditionEvaluator.DEFAULT_EVALUATORS, new MvelExpressionEvaluator());

    // a policy to ALLOW "WRITE" action on resources of all orders
    // with a condition on
    // resource.attributes.merchantId = principal.attributes merchantId
    policy = new Policy();
    policy.setActions(Arrays.asList("WRITE"));
    policy.setEffect(Effect.ALLOW);
    policy.setResources(Arrays.asList("order:.*"));
    policy.setConditions(
        Collections.singletonMap(
            "equals",
            Collections.singletonMap(
                "resource.attributes.departmentId", "@{principal.attributes.departmentId}")));

    principal = new Principal("1");
    principal.getAttributes().put("departmentId", "1000");

    resource = new Resource("order:");
    resource.getAttributes().put("departmentId", "1000");

    env = new HashMap<>();
  }

  @Test
  public void matchCondition_oneConditionWithOneKeyAndOneValue() {
    Assert.assertTrue(matcher.matchConditions(policy, principal, resource, env));
  }

  @Test
  public void matchCondition_oneConditionWithOneKeyAndOneValueWhenResourceNotMatch() {
    resource.getAttributes().put("departmentId", "1001");
    Assert.assertFalse(matcher.matchConditions(policy, principal, resource, env));
  }

  @Test
  public void matchCondition_oneConditionWithOneKeyAndOneValueWhenPrincipalNotMatch() {
    principal.getAttributes().put("departmentId", "1001");
    Assert.assertFalse(matcher.matchConditions(policy, principal, resource, env));
  }

  @Test
  public void matchCondition_oneConditionWithOneKeyAndMultipleValues() {

    policy.setConditions(
        Collections.singletonMap(
            "equals",
            Collections.singletonMap(
                "resource.attributes.departmentId", Arrays.asList("1000", "1001", "1002"))));

    boolean result = matcher.matchConditions(policy, principal, resource, env);
    Assert.assertTrue(result);

    resource.getAttributes().put("departmentId", "1001");

    result = matcher.matchConditions(policy, principal, resource, env);

    resource.getAttributes().put("departmentId", "1002");
    result = matcher.matchConditions(policy, principal, resource, env);
    Assert.assertTrue(result);

    resource.getAttributes().put("departmentId", "1003");
    result = matcher.matchConditions(policy, principal, resource, env);
    Assert.assertFalse(result);
  }

  @Test
  public void matchCondition_oneConditionWithOneKeyAndOneValueForInOp() {

    principal.getAttributes().put("departmentIds", Arrays.asList("1000", "1001", "1002"));

    policy.setConditions(
        Collections.singletonMap(
            "in",
            Collections.singletonMap(
                "resource.attributes.departmentId", "@{principal.attributes.departmentIds}")));
    boolean result = matcher.matchConditions(policy, principal, resource, env);
    Assert.assertTrue(result);

    resource.getAttributes().put("departmentId", "1003");
    result = matcher.matchConditions(policy, principal, resource, env);

    Assert.assertFalse(result);
  }

  @Test
  public void matchCondition_multipleConditions() {

    Map<String, Object> equalsConditions = new HashMap<>();

    // set up a composite condition:
    // 1. resource's departmentId in 1000,1001,1002
    // 2. resource's merchantId = principal's merchantId
    // 3. resource's tenantId = principal's tenantId
    // 4. resource's deleted = false
    // 5. resource's departmentId in one of principal's departmentIds
    // 6. resource's ownerId in one of principal's authorized user ids
    equalsConditions.put("resource.attributes.departmentId", Arrays.asList("1000", "1001", "1002"));

    equalsConditions.put("resource.attributes.merchantId", "@{principal.attributes.merchantId}");

    equalsConditions.put("resource.attributes.tenantId", "@{principal.attributes.tenantId}");

    principal.getAttributes().put("departmentIds", Arrays.asList("1000", "1001", "1002"));

    policy.setConditions(
        Collections.singletonMap(
            "in",
            Collections.singletonMap(
                "resource.attributes.departmentId", "@{principal.attributes.departmentIds}")));

    resource.getAttributes().put("merchantId", "testMerchantId");
    principal.getAttributes().put("merchantId", "testMerchantId");

    resource.getAttributes().put("tenantId", "testTenantId");
    principal.getAttributes().put("tenantId", "testTenantId");

    resource.getAttributes().put("ownerId", "100");
    principal.getAttributes().put("authorizedUserIds", Arrays.asList("100", "200", "300"));

    principal.getAttributes().put("departmentIds", Arrays.asList("1000", "2000", "3000"));

    resource.getAttributes().put("deleted", false);

    Map<String, Map<String, Object>> conditions = new HashMap<>();

    conditions.put("equals", equalsConditions);

    conditions.put("bool", Collections.singletonMap("resource.attributes.deleted", false));

    conditions.put(
        "in",
        Collections.singletonMap(
            "resource.attributes.ownerId", "@{principal.attributes.authorizedUserIds}"));

    policy.setConditions(conditions);

    Assert.assertTrue(matcher.matchConditions(policy, principal, resource, env));

    resource.getAttributes().put("deleted", true);
    boolean result = matcher.matchConditions(policy, principal, resource, env);
    Assert.assertFalse(result);
  }
}
