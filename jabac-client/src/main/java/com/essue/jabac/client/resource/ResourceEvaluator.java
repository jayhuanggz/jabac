package com.essue.jabac.client.resource;

import com.essue.jabac.client.model.Attribute;

public interface ResourceEvaluator {

  Object evaluateAttribute(Object resourceEntity, Attribute attribute);
}
