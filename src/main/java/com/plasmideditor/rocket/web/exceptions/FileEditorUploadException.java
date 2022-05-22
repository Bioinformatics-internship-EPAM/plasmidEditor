package com.plasmideditor.rocket.web.exceptions;

public class FileEditorUploadException extends Exception {
    public FileEditorUploadException(String message) {
        super(message);
    }

    public FileEditorUploadException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
