package com.essue.jabac.core.policy.condition;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ConditionEvaluatorTest {

  @Test
  public void equals_string() {
    Assert.assertTrue(new ConditionEvaluator.Equals().evaluate("jay", "jay"));
  }

  @Test
  public void equals_stringNotMatch() {
    Assert.assertFalse(new ConditionEvaluator.Equals().evaluate("jay", "jay2"));
  }

  @Test
  public void equals_stringEmpty() {
    Assert.assertTrue(new ConditionEvaluator.Equals().evaluate("", ""));
  }

  @Test
  public void bool() {
    Assert.assertTrue(new ConditionEvaluator.Bool().evaluate(true, true));
  }

  @Test
  public void bool_notMatch() {
    Assert.assertFalse(new ConditionEvaluator.Bool().evaluate(true, false));
  }

  @Test
  public void bool_typeNotMatch() {
    Assert.assertFalse(new ConditionEvaluator.Bool().evaluate(true, "false"));
  }

  @Test
  public void in() {
    Assert.assertTrue(new ConditionEvaluator.In().evaluate(1, Arrays.asList(1, 2, 3)));
  }

  @Test
  public void in_notMatch() {
    Assert.assertFalse(new ConditionEvaluator.In().evaluate(4, Arrays.asList(1, 2, 3)));
  }

  @Test
  public void in_string() {
    Assert.assertTrue(new ConditionEvaluator.In().evaluate("1", Arrays.asList("1", "2")));
  }
}
