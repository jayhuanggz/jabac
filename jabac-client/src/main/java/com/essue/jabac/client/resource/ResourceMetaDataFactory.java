package com.essue.jabac.client.resource;

import com.essue.jabac.client.Configuration;
import com.essue.jabac.client.model.ResourceMetaData;

public interface ResourceMetaDataFactory {

  ResourceMetaData createMetaData(Class resourceType, Configuration configuration);
}
