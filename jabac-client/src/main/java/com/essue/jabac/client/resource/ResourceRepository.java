package com.essue.jabac.client.resource;

import com.essue.jabac.client.Configuration;
import com.essue.jabac.client.model.ResourceMetaData;
import com.essue.jabac.core.Resource;

import java.io.Serializable;

public interface ResourceRepository {

  Resource findResource(Serializable id, ResourceMetaData metaData, Configuration configuration);
}
