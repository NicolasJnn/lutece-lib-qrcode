package fr.paris.lutce.plugins.qrcode;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;

import fr.paris.lutce.plugins.qrcode.service.CorrectionLevel;

/**
 * Interface representing a QR code generator.
 * This interface defines the methods needed for creating a QR code and adding a logo to it.
 */
public interface IQrCodeGenerator {

    /**
     * Returns the message encoded in the QR code.
     *
     * @return The message to encode in the QR code.
     */
    String getMessage();
    
    /**
     * Indicates whether the QR code should include additional parameters.
     *
     * @return true if the QR code should include parameters, false otherwise.
     */
    boolean isWithParameters();
    
    /**
     * Returns the error correction level of the QR code.
     *
     * @return The error correction level (LOW, MEDIUM, HIGH, etc.).
     */
    CorrectionLevel getCorrectionLevel();
    
    /**
     * Generates the QR code image.
     *
     * @return The generated QR code image with default values.
     * @throws UnsupportedEncodingException If text encoding fails.
     */
    BufferedImage toImage( ) throws Exception;

}

