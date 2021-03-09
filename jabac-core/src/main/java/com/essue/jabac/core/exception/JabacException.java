package com.essue.jabac.core.exception;

public class JabacException extends RuntimeException {

  public JabacException() {}

  public JabacException(String message) {
    super(message);
  }

  public JabacException(String message, Throwable cause) {
    super(message, cause);
  }

  public JabacException(Throwable cause) {
    super(cause);
  }

  public JabacException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
