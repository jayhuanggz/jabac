package com.essue.jabac.client.resource;

import com.essue.jabac.client.model.ResourceMetaData;
import com.essue.jabac.core.Resource;

import java.io.Serializable;

public interface ResourceService {

  Resource findResource(Serializable id, Class type);

  ResourceMetaData findResourceMetaData(Class type);
}
