package fr.paris.lutce.plugins.qrcode.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import fr.paris.lutce.plugins.qrcode.IQrCodeGenerator;
import io.nayuki.qrcodegen.QrCode;
import io.nayuki.qrcodegen.QrCode.Ecc;


/**
 * Concrete implementation of the QR code generator.
 * This class allows creating a QR code from a message, adding parameters to it, 
 * and placing a logo at the center of the QR code.
 */
public class QRcodeGenerator implements IQrCodeGenerator{
	
	public static QrCodeBuilder builder(String message) {
		return new QrCodeBuilder(message);
	}

    private static final int DEFAULT_QRCODE_SCALE = 10;
    private static final int DEFAULT_QRCODE_BORDER = 4;
    private static final double DEFAULT_QRCODE_LOGO_SCALE = 0.2;
    
    private BufferedImage qrCodeImage;
    private Map<String, String> parameters = new HashMap<>();
    private boolean withParameters;
    private String message;
    private CorrectionLevel correctionLevel;

    /**
     * Private constructor used by the Builder to create an instance of QRcodeGenerator.
     * 
     * @param builder The Builder object that initializes the QRcodeGenerator.
     */
    protected QRcodeGenerator(QrCodeBuilder builder) {
        this.message = builder.message;
        this.parameters = builder.parameters;
        this.withParameters = builder.withParameters;
        this.correctionLevel = builder.correctionLevel;
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
     * @param scale The scale of the image.
     * @param border The size of the QR code border.
     * @return The generated QR code image.
     * @throws UnsupportedEncodingException If text encoding fails.
     */
    public BufferedImage toImage(int scale, int border) throws UnsupportedEncodingException {
    	this.qrCodeImage = QrCode.encodeText( generate( ), adaptCorrectionLevel( ) ).toImage(scale, border);
        return this.qrCodeImage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public BufferedImage toImage( ) throws UnsupportedEncodingException {
        return toImage(DEFAULT_QRCODE_SCALE, DEFAULT_QRCODE_BORDER);
    }

	
    /**
     * Adds a logo to the QR code image with a specific scale factor.
     *
     * @param logoFile The logo image file to be added.
     * @param scale The scale factor to adjust the logo size relative to the QR code.
     * @throws QrCodeGeneratorException If an error occurs while adding the logo.
     */
    public void addLogoToQRCode(InputStream logoFile, double scale) throws QrCodeGeneratorException {
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(logoFile);
        } catch (IOException e) {
        	throw new QrCodeGeneratorException("Error reading logo file for QR code generation", e);
        }

        int qrWidth = this.qrCodeImage.getWidth();
        int qrHeight = this.qrCodeImage.getHeight();
        int logoWidth = (int) (qrWidth * scale);
        int logoHeight = (int) (qrHeight * scale);

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
     * {@inheritDoc}
     */
    @Override
	public void addLogoToQRCode(InputStream logoFile) throws QrCodeGeneratorException {
		addLogoToQRCode(logoFile, DEFAULT_QRCODE_LOGO_SCALE);
	}

    /**
     * Adapts the error correction level of the QR code using the CorrectionLevel enum.
     * 
     * @return The error correction level adapted for QR code generation.
     */
    private Ecc adaptCorrectionLevel() {
		return QrCode.Ecc.valueOf(this.getCorrectionLevel().name());
	}
    
}
