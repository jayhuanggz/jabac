package com.essue.jabac.client.resource;

import com.essue.jabac.client.Configuration;
import com.essue.jabac.client.model.ResourceMetaData;
import com.essue.jabac.core.Resource;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ResourceServiceImpl implements ResourceService {

  private static final Logger LOGGER = Logger.getLogger(ResourceServiceImpl.class.getName());

  private final ConcurrentHashMap<Class, ResourceMetaData> metaDataCache =
      new ConcurrentHashMap<>();

  private Configuration configuration;

  private ResourceMetaDataFactory metaDataFactory;

  private ResourceRepository resourceRepository;

  public ResourceServiceImpl(
      final Configuration configuration,
      final ResourceMetaDataFactory metaDataFactory,
      final ResourceRepository resourceRepository) {
    this.configuration = configuration;
    this.metaDataFactory = metaDataFactory;
    this.resourceRepository = resourceRepository;
  }

  @Override
  public Resource findResource(final Serializable id, final Class type) {

    ResourceMetaData metaData = findResourceMetaData(type);
    if (metaData == null) {
      LOGGER.info("No ResourceMetaData found for type: " + type.getName());
      return null;
    }

    return resourceRepository.findResource(id, metaData, configuration);
  }

  @Override
  public ResourceMetaData findResourceMetaData(final Class type) {

    ResourceMetaData metaData = metaDataCache.get(type);
    if (metaData == null) {
      metaData = metaDataFactory.createMetaData(type, configuration);
      metaDataCache.put(type, metaData);
    }
    return metaData;
  }
}
