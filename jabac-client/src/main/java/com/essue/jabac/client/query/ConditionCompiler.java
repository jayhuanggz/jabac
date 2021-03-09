package com.essue.jabac.client.query;

import com.essue.jabac.client.query.criteria.Criteria;

public interface ConditionCompiler {

  Criteria compile(String key, Object value);

  String getOp();
}
