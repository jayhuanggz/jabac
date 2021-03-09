package com.essue.jabac.core.policy.condition;

import java.util.Arrays;
import java.util.Collection;

public interface ConditionEvaluator {

  Equals EQUALS = new Equals();
  In IN = new In();
  Bool BOOL = new Bool();

  Collection<ConditionEvaluator> DEFAULT_EVALUATORS = Arrays.asList(EQUALS, BOOL, IN);

  boolean evaluate(Object left, Object right);

  String getOp();

  class Equals implements ConditionEvaluator {

    @Override
    public boolean evaluate(Object left, Object right) {
      return left.equals(right);
    }

    @Override
    public String getOp() {
      return "equals";
    }
  }

  class Bool implements ConditionEvaluator {

    @Override
    public boolean evaluate(Object left, Object right) {
      return Boolean.valueOf(left.toString()).equals(Boolean.valueOf(right.toString()));
    }

    @Override
    public String getOp() {
      return "bool";
    }
  }

  class In implements ConditionEvaluator {

    @Override
    public boolean evaluate(Object left, Object right) {

      if (right instanceof Collection) {
        return ((Collection) right).contains(left);
      }
      return false;
    }

    @Override
    public String getOp() {
      return "in";
    }
  }
}
