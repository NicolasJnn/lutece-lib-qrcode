package fr.paris.lutce.plugins.qrcode.service;

/**
 * Enum representing the error correction levels for a QR code.
 * The error correction level determines the amount of redundancy (error protection) in the QR code.
 * A higher error correction level allows the QR code to be read correctly even if part of the code is damaged.
 */
public enum CorrectionLevel {
    /**
     * The QR Code can tolerate about 7% erroneous codewords.
     */
    LOW(1),

    /**
     * The QR Code can tolerate about 15% erroneous codewords.
     */
    MEDIUM(0),

    /**
     * The QR Code can tolerate about 25% erroneous codewords.
     */
    QUARTILE(3),

    /**
     * The QR Code can tolerate about 30% erroneous codewords.
     */
    HIGH(2);

    /**
     * The integer value representing the error correction level.
     * This value is used internally to configure the error correction behavior in the QR code.
     */
    private final int value;

    /**
     * Constructs a {@link CorrectionLevel} with the specified integer value.
     * 
     * @param value The integer value representing the error correction level.
     */
    CorrectionLevel(int value) {
        this.value = value;
    }

    /**
     * Returns the integer value associated with this error correction level.
     * 
     * @return The integer value of the error correction level.
     */
    public int getValue() {
        return value;
    }
}
