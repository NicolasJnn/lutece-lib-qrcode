package fr.paris.lutece.plugins.qrcode.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import fr.paris.lutece.plugins.qrcode.IQrCodeGenerator;
import io.nayuki.qrcodegen.QrCode;
import io.nayuki.qrcodegen.QrCode.Ecc;

/**
 * Concrete implementation of the {@link IQrCodeGenerator} interface for generating QR codes.
 * <p>
 * This class allows creating a QR code from a message, adding query parameters to the URL encoded in the QR code,
 * and placing a logo at the center of the QR code image.
 * </p>
 */
public class QrCodeGenerator implements IQrCodeGenerator {

    /**
     * The default scale factor for the QR code image (size).
     */
    private static final int DEFAULT_QRCODE_SCALE = 10;

    /**
     * The default border size for the QR code image.
     */
    private static final int DEFAULT_QRCODE_BORDER = 4;

    private BufferedImage qrCodeImage;
    private Map<String, String> parameters = new HashMap<>();
    private boolean withParameters;
    private String message;
    private CorrectionLevel correctionLevel;
    private LogoQrCode logo;

    /**
     * Private constructor used by the {@link QrCodeBuilder} to create an instance of QRcodeGenerator.
     * 
     * @param builder The {@link QrCodeBuilder} instance that initializes the QRcodeGenerator.
     */
    protected QrCodeGenerator(QrCodeBuilder builder) {
        this.message = builder.message;
        this.parameters = builder.parameters;
        this.withParameters = builder.withParameters;
        this.correctionLevel = builder.correctionLevel;
        this.logo = builder.logo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWithParameters() {
        return withParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CorrectionLevel getCorrectionLevel() {
        return correctionLevel;
    }

    /**
     * Generates the text (message + parameters) to be encoded in the QR code.
     * 
     * @return The generated text string.
     * @throws UnsupportedEncodingException If URL encoding fails.
     */
    private String generate() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        if (withParameters) {
            sb.append("?");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                sb.append("&");
            }
        }
        return sb.toString();
    }

    /**
     * Generates the QR code image using the specified scale and border size.
     * 
     * @param scale The scale of the image (size of the QR code).
     * @param border The size of the QR code's border.
     * @return The generated QR code image.
     * @throws UnsupportedEncodingException If text encoding fails.
     * @throws QrCodeGeneratorException If QR code image generation fails.
     */
    public BufferedImage toImage(int scale, int border) throws UnsupportedEncodingException, QrCodeGeneratorException {
        this.qrCodeImage = QrCode.encodeText(generate(), adaptCorrectionLevel()).toImage(scale, border);
        if (this.logo != null) {
            addLogoToQRCode( );
        }
        return this.qrCodeImage;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws QrCodeGeneratorException If QR code image generation fails.
     */
    @Override
    public BufferedImage toImage() throws UnsupportedEncodingException, QrCodeGeneratorException {
        return toImage(DEFAULT_QRCODE_SCALE, DEFAULT_QRCODE_BORDER);
    }

    /**
     * Adds a logo to the QR code image with a specific scale factor.
     * The logo will be centered within the QR code.
     * 
     * @param logoFile The logo image file to be added to the QR code.
     * @param scale The scale factor to adjust the logo size relative to the QR code.
     * @throws QrCodeGeneratorException If an error occurs while adding the logo.
     */
    private void addLogoToQRCode( ) throws QrCodeGeneratorException {
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(this.logo.getLogo());
        } catch (IOException e) {
            throw new QrCodeGeneratorException("Error reading logo file for QR code generation", e);
        }

        int qrWidth = this.qrCodeImage.getWidth();
        int qrHeight = this.qrCodeImage.getHeight();
        int logoWidth = (int) (qrWidth * this.logo.getScale());
        int logoHeight = (int) (qrHeight * this.logo.getScale());

        Image scaledLogo = logo.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
        BufferedImage scaledLogoImage = new BufferedImage(logoWidth, logoHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledLogoImage.createGraphics();
        g2d.drawImage(scaledLogo, 0, 0, null);
        g2d.dispose();

        int x = (qrWidth - logoWidth) / 2;
        int y = (qrHeight - logoHeight) / 2;

        Graphics2D g = this.qrCodeImage.createGraphics();
        g.drawImage(scaledLogoImage, x, y, null);
        g.dispose();
    }

    /**
     * Adapts the error correction level of the QR code using the {@link CorrectionLevel} enum.
     * 
     * @return The error correction level adapted for QR code generation.
     */
    private Ecc adaptCorrectionLevel() {
        return QrCode.Ecc.valueOf(this.getCorrectionLevel().name());
    }
}
