package fr.paris.lutce.plugins.qrcode.service;

/**
 * Exception class used for handling errors during QR code generation.
 * This exception is thrown when there is an issue with generating a QR code or adding a logo.
 * It provides constructors for specifying an error message and/or the cause of the error.
 */
public class QrCodeGeneratorException extends Exception {

    /**
     * A unique identifier for this class version. This is used for serializing
     * instances of this class if needed. 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new QrCodeGeneratorException with the specified error message.
     * 
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public QrCodeGeneratorException(String message) {
        super(message);
    }

    /**
     * Constructs a new QrCodeGeneratorException with the specified error message and cause.
     * 
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause The cause of the exception (which can be retrieved later using {@link #getCause()}).
     */
    public QrCodeGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
