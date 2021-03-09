package com.essue.jabac.core.path;

public class Path {

  private static final String SEPARATOR = ":";

  private String identifier;

  private Path parent;

  private String fullPath;

  public Path(final Path parent, final String identifier) {
    this.identifier = identifier;
    this.parent = parent;
    this.fullPath = parent == null ? identifier : parent.getFullPath() + SEPARATOR + identifier;
  }

  Path() {}

  public static Path parse(String path) {

    if (path == null || path.length() == 0) {
      return null;
    }

    String[] tokens = path.split(":");
    if (tokens.length == 1) {
      return new Path(null, tokens[0]);
    }
    Path current = null;
    for (final String token : tokens) {
      if (current == null) {
        current = new Path(null, token);
      } else {
        current = new Path(current, token);
      }
    }
    return current;
  }

  public String getIdentifier() {
    return identifier;
  }

  public Path getParent() {
    return parent;
  }

  public String getFullPath() {
    return fullPath;
  }

  public Path createSub(String identifier) {
    return new Path(this, identifier);
  }
}
