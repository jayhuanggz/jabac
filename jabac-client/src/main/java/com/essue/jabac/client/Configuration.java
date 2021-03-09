package com.essue.jabac.client;

import java.util.Properties;

public class Configuration {

  public static final String RESOURCE_SEPARATOR = "resourceSeparator";

  public static final String RESOURCE_ROOT_NAMESPACE = "resourceRootNamespace";

  public static final String PRINCIPAL_SEPARATOR = "principalSeparator";

  public static final String PRINCIPAL_ROOT_NAMESPACE = "principalRootNamespace";

  private String resourceRootNamespace = "";

  private String resourceSeparator = ":";

  private String principalSeparator = ":";

  private String principalRootNamespace = "";

  public Configuration() {}

  public Configuration(Properties properties) {

    resourceRootNamespace =
        properties.getOrDefault(RESOURCE_ROOT_NAMESPACE, resourceRootNamespace).toString();

    resourceSeparator = properties.getOrDefault(RESOURCE_SEPARATOR, resourceSeparator).toString();

    principalSeparator =
        properties.getOrDefault(PRINCIPAL_SEPARATOR, principalSeparator).toString();

    principalRootNamespace =
        properties.getOrDefault(PRINCIPAL_ROOT_NAMESPACE, principalRootNamespace).toString();
  }

  public String getResourceRootNamespace() {
    return resourceRootNamespace;
  }

  public void setResourceRootNamespace(final String resourceRootNamespace) {
    this.resourceRootNamespace = resourceRootNamespace;
  }

  public String getPrincipalRootNamespace() {
    return principalRootNamespace;
  }

  public void setPrincipalRootNamespace(final String principalRootNamespace) {
    this.principalRootNamespace = principalRootNamespace;
  }

  public String getResourceSeparator() {
    return resourceSeparator;
  }

  public void setResourceSeparator(final String resourceSeparator) {
    this.resourceSeparator = resourceSeparator;
  }

  public String getPrincipalSeparator() {
    return principalSeparator;
  }

  public void setPrincipalSeparator(final String principalSeparator) {
    this.principalSeparator = principalSeparator;
  }
}
