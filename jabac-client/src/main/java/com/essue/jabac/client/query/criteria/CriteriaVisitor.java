package com.essue.jabac.client.query.criteria;

public interface CriteriaVisitor {

  void visitEq(Eq criteria);

  void visitAnd(And criteria);

  void visitOr(Or criteria);
}
