package com.essue.jabac.core.path;

public class DefaultPathMatcher implements PathMatcher {

  private static final char WILD_CARD = '*';

  @Override
  public boolean matches(final Path left, final Path right) {

    if (left == null && right == null) {
      return true;
    }

    if (left == null || right == null) {
      return false;
    }

    String leftPath = left.getFullPath();
    String rightPath = right.getFullPath();

    return matches(leftPath, rightPath);
  }

  @Override
  public boolean matches(final String left, final String right) {
    if (left.equals(right)) {
      return true;
    }

    char[] chars = left.toCharArray();

    for (int i = 0; i < chars.length; i++) {
      char leftChar = chars[i];

      if (right.length() > i) {
        char rightChar = right.charAt(i);
        if (leftChar == rightChar) {
          continue;
        }

        if (leftChar == WILD_CARD) {
          return true;
        }
        break;

      } else {
        return false;
      }
    }

    return false;
  }
}
