package com.essue.jabac.core.policy;

import com.essue.jabac.core.Effect;

import java.util.Collection;
import java.util.Map;

/** An object to define actions on */
public class Policy {

  private String identifier;

  private Collection<String> resources;

  private Effect effect;

  private Collection<String> actions;

  private Map<String, Map<String, Object>> conditions;

  // a condition for matching principals that this policy
  // can be applied to. This can be a string or an array of strings
  private Object principal;

  public Policy(final String identifier) {
    this.identifier = identifier;
  }

  public Policy() {}

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(final String identifier) {
    this.identifier = identifier;
  }

  public Map<String, Map<String, Object>> getConditions() {
    return conditions;
  }

  public void setConditions(Map<String, Map<String, Object>> conditions) {
    this.conditions = conditions;
  }

  public Collection<String> getResources() {
    return resources;
  }

  public Object getPrincipal() {
    return principal;
  }

  public void setPrincipal(final Object principal) {
    this.principal = principal;
  }

  public void setResources(Collection<String> resources) {
    this.resources = resources;
  }

  public Effect getEffect() {
    return effect;
  }

  public void setEffect(Effect effect) {
    this.effect = effect;
  }

  public Collection<String> getActions() {
    return actions;
  }

  public void setActions(Collection<String> actions) {
    this.actions = actions;
  }
}
