package com.essue.jabac.client.query;

import com.essue.jabac.client.query.criteria.Criteria;
import com.essue.jabac.core.Principal;
import com.essue.jabac.core.policy.Policy;

import java.util.Map;

public interface PolicyCompiler {

  Criteria compile(Policy policy, Principal principal, Map<String,Object> env);
}
