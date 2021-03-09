package com.essue.jabac.client.principal;

import com.essue.jabac.core.Principal;

import java.util.Map;

/** Responsible for loading principal based on the current env/request */
public interface PrincipalProvider {

  Principal provide(Map<String, Object> env);
}
