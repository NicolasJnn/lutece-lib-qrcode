package fr.paris.lutce.plugins.qrcode;

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

import io.nayuki.qrcodegen.QrCode;
import io.nayuki.qrcodegen.QrCode.Ecc;

public class QRcodeGenerator {
	
	private static final String FILE_EXTENSION = "png";
	
	private Map<String, String> parameters = new HashMap<String, String>( );
	private boolean withParameters;
	private String message;
	private QrCode qrCode = null;
	private QrCode.Ecc correctionLevel = QrCode.Ecc.MEDIUM;
	
	
	
	
	public QRcodeGenerator(String message) {
		super();
		this.message = message;
		this.withParameters = false;
	}
	public QRcodeGenerator(Map<String, String> parameters, String url, Ecc correctionLevel) {
		this.parameters = parameters;
		this.message = url;
		this.correctionLevel = correctionLevel;
		this.withParameters = true;
	}
	
	public QRcodeGenerator(String message, Ecc correctionLevel) {
		this.message = message;
		this.correctionLevel = correctionLevel;
		this.withParameters = false;
	}
	
	public QRcodeGenerator(String url, boolean withParameters, Ecc correctionLevel) {
		this.withParameters = withParameters;
		this.message = url;
		this.correctionLevel = correctionLevel;
	}

	public String getMessage( ) {
		return message;
	}
	public void setMessage( String url ) {
		this.message = url;
	}
	public boolean isWithParameters( ) {
		return withParameters;
	}
	public void setWithParameters( boolean withParameters ) {
		this.withParameters = withParameters;
	}
	public QrCode.Ecc getCorrectionLevel( ) {
		return correctionLevel;
	}
	public void setCorrectionLevel( QrCode.Ecc correctionLevel ) {
		this.correctionLevel = correctionLevel;
	}
	public void addParameter( String key, String value ) {
		this.withParameters = true;
		parameters.put( key, value );
	}
	
	public void addParameters( Map<String, String> params ) {
		this.withParameters = true;
		parameters.putAll( params );
	}
	
	public void removeParameter( String key ) {
		parameters.remove( key );
	}
	
	public void clearParameters() {
		this.withParameters = false;
		parameters.clear();
	}
	
	private String generate( ) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
		sb.append( message );
		if ( withParameters ) 
		{
	        sb.append("?");
	        for ( Map.Entry<String, String> entry : parameters.entrySet( ) ) {
	            sb.append( URLEncoder.encode( entry.getKey( ), "UTF-8" ) );
	            sb.append( "=" );
	            sb.append( URLEncoder.encode( entry.getValue( ), "UTF-8" ) );
	            sb.append( "&" );
	        }
		}
		return sb.toString( );
	}
	
	public QrCode build( ) throws UnsupportedEncodingException {
		this.qrCode = QrCode.encodeText( generate( ), correctionLevel );
		return qrCode;
	}
	
	public BufferedImage createBufferedImage() throws UnsupportedEncodingException {
		if (qrCode == null) {
			build();
		}
		return qrCode.toImage(10, 4);
	}
	
	public void generateImageFile(File file, int scale, int border) throws IOException {
		if ( qrCode == null ) {
			build( );
		}
		BufferedImage img = qrCode.toImage( scale, border );
		ImageIO.write( img, FILE_EXTENSION, file );
	}

	public BufferedImage toImage(int scale, int border) throws UnsupportedEncodingException {
		if (qrCode == null) {
			build();
		}
		return qrCode.toImage(scale, border);
	}
	
	/**
     * Adds a logo image to the center of a given QR code image.
     * The logo is resized to a specific scale relative to the QR code size.
     *
     * @param qrCode The BufferedImage of the QR code.
     * @param logoFile The logo image file to overlay onto the QR code.
     * @param scale The scale factor for the logo size (e.g., 0.2 for 20% of the QR code size).
     * @throws IOException If an error occurs while reading the logo image file.
     */
    public static void addLogoToQRCode(BufferedImage qrCode, InputStream logoFile, double scale) {
        BufferedImage logo = null;
		try {
			logo = ImageIO.read(logoFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // Scale the logo image
        int qrWidth = qrCode.getWidth();
        int qrHeight = qrCode.getHeight();
        int logoWidth = (int) (qrWidth * scale);
        int logoHeight = (int) (qrHeight * scale);
        
        // Resize logo to fit in QR code
        Image scaledLogo = logo.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
        BufferedImage scaledLogoImage = new BufferedImage(logoWidth, logoHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledLogoImage.createGraphics();
        g2d.drawImage(scaledLogo, 0, 0, null);
        g2d.dispose();

        // Calculate the position to place the logo (centered)
        int x = (qrWidth - logoWidth) / 2;
        int y = (qrHeight - logoHeight) / 2;

        // Overlay the logo onto the QR code image
        Graphics2D g = qrCode.createGraphics();
        g.drawImage(scaledLogoImage, x, y, null);
        g.dispose();
    }
}
