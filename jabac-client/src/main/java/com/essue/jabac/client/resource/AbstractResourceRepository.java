package com.essue.jabac.client.resource;

import com.essue.jabac.client.Configuration;
import com.essue.jabac.client.model.Attribute;
import com.essue.jabac.client.model.Namespace;
import com.essue.jabac.client.model.ResourceMetaData;
import com.essue.jabac.core.Resource;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractResourceRepository implements ResourceRepository {

  private static final Logger LOGGER = Logger.getLogger(AbstractResourceRepository.class.getName());

  private ResourceEvaluator resourceEvaluator;

  public AbstractResourceRepository(final ResourceEvaluator resourceEvaluator) {
    this.resourceEvaluator = resourceEvaluator;
  }

  protected abstract Object loadResourceEntity(Serializable id, final ResourceMetaData metaData);

  @Override
  public Resource findResource(
      final Serializable id, final ResourceMetaData metaData, final Configuration configuration) {

    Object entity = loadResourceEntity(id, metaData);
    if (entity == null) {
      LOGGER.fine(
          String.format(
              "Resource of type %s not found for id: %s",
              metaData.getType().getName(), id.toString()));
      return null;
    }

    return createResource(id, metaData, configuration, entity);
  }

  protected Resource createResource(
      final Serializable id,
      final ResourceMetaData metaData,
      final Configuration configuration,
      final Object entity) {
    Map<String, Object> attributes;

    Map<String, Attribute> metaDataAttributes = metaData.getAttributes();

    if (metaDataAttributes == null) {
      attributes = Collections.singletonMap("id", id);
    } else {
      attributes = new HashMap<>();
      attributes.put("id", id);
      for (final Attribute metaAttribute : metaDataAttributes.values()) {

        attributes.put(
            metaAttribute.getName(), resourceEvaluator.evaluateAttribute(entity, metaAttribute));
      }
    }

    Namespace namespace = metaData.getNamespace();
    String resourceIdentifier;
    if (namespace == null) {
      resourceIdentifier = id.toString();
    } else {
      resourceIdentifier =
          namespace.createSub(id.toString()).getFullPath(configuration.getResourceSeparator());
    }

    return new Resource(resourceIdentifier, attributes);
  }
}
