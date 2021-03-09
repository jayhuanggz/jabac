package com.essue.jabac.core.audit;

import com.essue.jabac.core.Effect;
import com.essue.jabac.core.AuthContext;

public interface AuditService {
  Audit audit(AuthContext context, Effect effect);
}
