package com.essue.jabac.core.path;

import org.junit.Assert;
import org.junit.Test;

public class DefaultPathMatcherTest {

  private final PathMatcher pathMatcher = new DefaultPathMatcher();

  @Test
  public void matches_wildCardMatchesAll() {
    Path left = Path.parse("*");
    Path right = Path.parse("order:1");

    Assert.assertTrue(pathMatcher.matches(left, right));
  }

  @Test
  public void matches_samePathMathes() {
    Path left = Path.parse("order:1");
    Path right = Path.parse("order:1");

    Assert.assertTrue(pathMatcher.matches(left, right));
  }

  @Test
  public void matches_compositePathWithWildCard() {
    Path left = Path.parse("nr:service:order:*");
    Path right = Path.parse("nr:service:order:123");

    Assert.assertTrue(pathMatcher.matches(left, right));
  }

  @Test
  public void matches_pathNotEqual() {
    Path left = Path.parse("nr:service:order:1");
    Path right = Path.parse("nr:service:order:123");

    Assert.assertFalse(pathMatcher.matches(left, right));
  }

  @Test
  public void matches_singlePathNotMatch() {
    Path left = Path.parse("1");
    Path right = Path.parse("12");

    Assert.assertFalse(pathMatcher.matches(left, right));
  }

  @Test
  public void matches_wildCardNotMatch() {
    Path left = Path.parse("nr:order:*");
    Path right = Path.parse("nr:customer:1");

    Assert.assertFalse(pathMatcher.matches(left, right));
  }
}
