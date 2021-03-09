package com.essue.jabac.core.path;

public interface PathMatcher {

  /**
   * check if a path matches the other
   *
   * @param left
   * @param right
   * @return
   */
  boolean matches(Path left, Path right);

  boolean matches(String left, String right);
}
