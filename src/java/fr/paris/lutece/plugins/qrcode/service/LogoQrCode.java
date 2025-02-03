package fr.paris.lutece.plugins.qrcode.service;

import java.io.InputStream;

/**
 * This class is responsible for handling the logo that will be embedded into the QR code.
 * It allows for setting a custom scale for the logo's size relative to the QR code and provides
 * access to the logo image as an {@link InputStream}.
 */
public class LogoQrCode {

    /**
     * Default scale factor for the logo, representing 20% of the QR code size.
     */
    private static final double DEFAULT_SCALE = 0.2;

    /**
     * The scale factor to adjust the logo's size relative to the QR code.
     * It determines how large or small the logo will be placed in the center of the QR code.
     */
    private double scale;

    /**
     * The input stream representing the logo image to be embedded in the QR code.
     * The logo image will be scaled according to the defined scale factor.
     */
    private InputStream logo;

    /**
     * Constructs a {@link LogoQrCode} with a custom scale factor and the logo input stream.
     * 
     * @param scale The scale factor for the logo size (e.g., 0.2 for 20% of QR code size).
     * @param logo The input stream representing the logo image to be embedded.
     */
    public LogoQrCode(double scale, InputStream logo) {
        this.scale = scale;
        this.logo = logo;
    }

    /**
     * Constructs a {@link LogoQrCode} with the default scale factor (0.2) and the logo input stream.
     * 
     * @param logo The input stream representing the logo image to be embedded.
     */
    public LogoQrCode(InputStream logo) {
        this.scale = DEFAULT_SCALE;
        this.logo = logo;
    }

    /**
     * Returns the scale factor for the logo relative to the QR code size.
     * 
     * @return The scale factor for the logo.
     */
    public double getScale() {
        return scale;
    }

    /**
     * Returns the input stream representing the logo image to be embedded in the QR code.
     * 
     * @return The input stream for the logo image.
     */
    public InputStream getLogo() {
        return logo;
    }
}
