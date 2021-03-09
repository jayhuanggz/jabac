package com.essue.jabac.client.resource;

import com.essue.jabac.client.model.Attribute;
import com.google.common.cache.Cache;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.lang.reflect.Field;

public class MvelResourceEvaluator implements ResourceEvaluator {

  private Cache<Field, CompiledTemplate> cache;

  public MvelResourceEvaluator(final Cache<Field, CompiledTemplate> cache) {
    this.cache = cache;
  }

  public MvelResourceEvaluator() {}

  public void setCache(final Cache<Field, CompiledTemplate> cache) {
    this.cache = cache;
  }

  @Override
  public Object evaluateAttribute(final Object resourceEntity, final Attribute attribute) {

    String path = attribute.getPath();
    if (path == null || path.length() == 0) {
      try {
        return attribute.getField().get(resourceEntity);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    CompiledTemplate template = null;
    if (cache != null) {
      template = cache.getIfPresent(attribute.getField());
      if (template == null) {
        template = TemplateCompiler.compileTemplate("@{" + path + "}");
        cache.put(attribute.getField(), template);
      }
    }

    if (template == null) {
      template = TemplateCompiler.compileTemplate("@{" + path + "}");
    }

    return TemplateRuntime.execute(template, resourceEntity);
  }
}
