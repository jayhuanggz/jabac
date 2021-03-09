package com.essue.jabac.core.audit;

import com.essue.jabac.core.Effect;
import com.essue.jabac.core.AuthContext;

public class Audit {

  private AuthContext context;

  private Effect effect;

  public Audit(final AuthContext context, final Effect effect) {
    this.context = context;
    this.effect = effect;
  }

  public AuthContext getContext() {
    return context;
  }

  public Effect getEffect() {
    return effect;
  }
}
