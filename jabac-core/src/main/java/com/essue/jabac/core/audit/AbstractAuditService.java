package com.essue.jabac.core.audit;

import com.essue.jabac.core.Effect;
import com.essue.jabac.core.AuthContext;

public abstract class AbstractAuditService implements AuditService {

  protected abstract boolean shouldAudit(final AuthContext context, final Effect effect);

  @Override
  public Audit audit(final AuthContext context, final Effect effect) {

    if (!shouldAudit(context, effect)) {
      return null;
    }

    Audit audit = new Audit(context, effect);
    return saveAudit(audit);
  }

  protected abstract Audit saveAudit(Audit audit);
}
