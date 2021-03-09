package com.essue.jabac.client.resource;

import com.essue.jabac.client.Configuration;
import com.essue.jabac.client.annotation.ResourceAttribute;
import com.essue.jabac.client.annotation.ResourceEntity;
import com.essue.jabac.client.annotation.ResourceId;
import com.essue.jabac.client.model.Attribute;
import com.essue.jabac.client.model.Namespace;
import com.essue.jabac.client.model.ResourceMetaData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationResourceMetaDataFactory implements ResourceMetaDataFactory {

  @Override
  public ResourceMetaData createMetaData(Class resourceType, Configuration configuration) {

    ResourceEntity entity = (ResourceEntity) resourceType.getAnnotation(ResourceEntity.class);
    if (entity == null) {
      return null;
    }

    String namespace = entity.namespace();

    Attribute idAttribute = null;

    Map<String, Attribute> attributes = new HashMap<>();

    Map<String, Field> allFields = new HashMap<>();

    Class temp = resourceType;

    // process field annotations first
    while (!temp.equals(Object.class)) {

      Field[] fields = temp.getDeclaredFields();

      for (Field field : fields) {

        allFields.putIfAbsent(field.getName(), field);

        ResourceId id = field.getAnnotation(ResourceId.class);

        if (id != null) {
          if (idAttribute == null) {
            field.setAccessible(true);
            String alias = getAlias(field, id.alias());
            idAttribute = new Attribute(field, id.name(), alias, null);
            attributes.put(field.getName(), idAttribute);
          } else {
            throw new IllegalStateException(
                "Duplicate @ResourceId in type: " + resourceType.getName());
          }
        }

        Attribute attribute = attributes.get(field.getName());
        if (attribute == null) {
          ResourceAttribute annotation = field.getAnnotation(ResourceAttribute.class);
          if (annotation != null) {
            field.setAccessible(true);
            String alias = getAlias(field, annotation.alias());
            attribute = new Attribute(field, annotation.name(), alias, annotation.path());
            attributes.put(field.getName(), attribute);
          }
        }
      }

      temp = temp.getSuperclass();
    }

    // process @ResourceId defined in class
    ResourceId resourceId = (ResourceId) resourceType.getAnnotation(ResourceId.class);
    if (resourceId != null) {
      if (idAttribute != null) {
        throw new IllegalStateException("Duplicate @ResourceId in class:" + resourceType.getName());
      }

      Field field = allFields.get(resourceId.name());

      if (field == null) {
        throw new IllegalStateException(
            String.format(
                "Class %s is annotated with @ResourceId, but the field %s does not exists!",
                resourceType.getName(), resourceId.name()));
      }

      String alias = getAlias(field, resourceId.alias());
      idAttribute = new Attribute(field, resourceId.name(), alias, null);
      attributes.put(field.getName(), idAttribute);
    }

    if (idAttribute == null) {
      throw new IllegalStateException(
          String.format(
              "Class %s is annotated with @ResourceEntity but does not have a @ResourceId attribute!",
              resourceType.getName()));
    }

    // process attributes defined in @ResourceEntity
    for (final ResourceAttribute attributeAnnotation : entity.attributes()) {

      Field field = allFields.get(attributeAnnotation.name());
      if (field == null) {
        throw new IllegalStateException(
            String.format(
                "Invalid attribute %s specified in class %s!",
                attributeAnnotation.name(), resourceType.getName()));
      }

      if (attributes.containsKey(field.getName())) {
        throw new IllegalArgumentException(
            String.format(
                "Duplicate attribute %s specified in class %s!",
                attributeAnnotation.name(), resourceType.getName()));
      }

      field.setAccessible(true);
      String alias = getAlias(field, attributeAnnotation.alias());
      Attribute attribute =
          new Attribute(field, attributeAnnotation.name(), alias, attributeAnnotation.path());
      attributes.put(field.getName(), attribute);
    }

    return new ResourceMetaData(
        resourceType,
        Namespace.parse(namespace, configuration.getResourceSeparator()),
        idAttribute,
        attributes);
  }

  private String getAlias(Field field, String alias) {

    return alias == null || alias.length() == 0 ? field.getName() : alias;
  }
}
