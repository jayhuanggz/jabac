package com.essue.jabac.core.policy;

import com.essue.jabac.core.path.PathMatcher;

import java.util.Iterator;

public class PolicyUtil {

  public static boolean match(
      final Iterable patterns, final String target, final PathMatcher matcher) {

    Iterator it = patterns.iterator();
    // no principal specified in policy, return matches
    if (!it.hasNext()) {
      return true;
    }
    while (it.hasNext()) {
      // any of the conditions matches, return matches
      String condition = it.next().toString();
      if (matcher.matches(condition, target)) {
        return true;
      }
    }
    return false;
  }
}
