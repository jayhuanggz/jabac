package com.essue.jabac.core;

import com.essue.jabac.core.audit.AuditService;
import com.essue.jabac.core.policy.PolicyMatcher;
import com.google.common.base.Preconditions;

public class EngineBuilder {

  private PolicyMatcher policyMatcher;

  private AuditService auditService;

  public EngineBuilder authManager(PolicyMatcher policyMatcher) {
    this.policyMatcher = policyMatcher;
    return this;
  }

  public EngineBuilder auditService(AuditService auditService) {
    this.auditService = auditService;
    return this;
  }

  public Engine build() {
    Preconditions.checkArgument(policyMatcher != null, "must specify policyMatcher");
    EngineImpl engine = new EngineImpl(policyMatcher);
    engine.setAuditService(auditService);
    return engine;
  }
}
