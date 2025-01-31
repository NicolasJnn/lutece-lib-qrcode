package fr.paris.lutce.plugins.qrcode.service;

import java.util.HashMap;
import java.util.Map;

import fr.paris.lutce.plugins.qrcode.IQrCodeGenerator;

/**
 * Builder class for constructing instances of {@link QRcodeGenerator}.
 * This class provides a fluent API for setting various properties of the QR code generator, such as the message,
 * additional parameters, and the error correction level.
 */
public class QrCodeBuilder {

    /**
     * The message that will be encoded in the QR code.
     */
    protected String message;

    /**
     * A map containing parameters to be appended to the message in the QR code.
     * These parameters are included in the URL query string.
     */
    protected Map<String, String> parameters = new HashMap<>();

    /**
     * Flag indicating whether the QR code should include additional parameters.
     * Defaults to false.
     */
    protected boolean withParameters = false;

    /**
     * The error correction level to be used for the QR code.
     * Defaults to {@link CorrectionLevel#MEDIUM}.
     */
    protected CorrectionLevel correctionLevel = CorrectionLevel.MEDIUM;

    /**
     * Constructs a new {@link QrCodeBuilder} with the specified message.
     * 
     * @param message The message that will be encoded in the QR code.
     */
    public QrCodeBuilder(String message) {
        this.message = message;
    }

    /**
     * Adds a single parameter to be included in the QR code's message.
     * 
     * @param key The key for the parameter.
     * @param value The value for the parameter.
     * @return The current {@link QrCodeBuilder} instance for method chaining.
     */
    public QrCodeBuilder addParameter(String key, String value) {
        this.withParameters = true;
        parameters.put(key, value);
        return this;
    }

    /**
     * Adds multiple parameters to be included in the QR code's message.
     * 
     * @param parameters A map of parameters to be added.
     * @return The current {@link QrCodeBuilder} instance for method chaining.
     */
    public QrCodeBuilder withParameters(Map<String, String> parameters) {
        this.withParameters = true;
        this.parameters.putAll(parameters);
        return this;
    }

    /**
     * Sets the error correction level to be used when generating the QR code.
     * 
     * @param correctionLevel The desired error correction level.
     * @return The current {@link QrCodeBuilder} instance for method chaining.
     */
    public QrCodeBuilder withCorrectionLevel(CorrectionLevel correctionLevel) {
        this.correctionLevel = correctionLevel;
        return this;
    }

    /**
     * Builds and returns an instance of {@link QRcodeGenerator} based on the current builder configuration.
     * 
     * @return A new {@link QRcodeGenerator} instance configured according to the builder's properties.
     */
    public IQrCodeGenerator build() {
        return new QRcodeGenerator(this);
    }
}
