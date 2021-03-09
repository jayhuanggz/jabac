package com.essue.jabac.core.expression;

import com.essue.jabac.core.Principal;
import com.essue.jabac.core.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class MvelExpressionEvaluatorTest {

  private MvelExpressionEvaluator evaluator = new MvelExpressionEvaluator();

  private Principal principal;

  private Resource resource;

  private EvaluationContext context;

  @Before
  public void init() {
    principal = new Principal("1");
    principal.getAttributes().put("username", "jay");
    principal.getAttributes().put("departmentId", new Long(100));

    resource = new Resource("IMAGE:2");
    resource.getAttributes().put("departmentId", new Long(100));

    context = new EvaluationContext(principal, resource, new HashMap<>());
  }

  @Test
  public void evaluate() {

    Assert.assertEquals(
        principal.getIdentifier(), evaluator.evaluate(context, "@{principal.identifier}", false));
    Assert.assertEquals(
        resource.getIdentifier(), evaluator.evaluate(context, "@{resource.identifier}", false));
    Assert.assertEquals(
        resource.getAttributes().get("departmentId"),
        evaluator.evaluate(context, "@{resource.attributes.departmentId}", false));
  }

  @Test
  public void evaluate_nonExpression() {

    Assert.assertEquals(
        resource.getAttributes().get("departmentId").toString(),
        evaluator.evaluate(
            context, resource.getAttributes().get("departmentId").toString(), false));
  }
}
