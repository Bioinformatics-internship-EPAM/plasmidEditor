package com.plasmidEditor.sputnik.exceptions;

public class FileUploadException extends RuntimeException {
  public static final long serialVersionUID = 1007L;
  public FileUploadException(String message, Throwable cause) {
    super(message, cause);
  }
}
