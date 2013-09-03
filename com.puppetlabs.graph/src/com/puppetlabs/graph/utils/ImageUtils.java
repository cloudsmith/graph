/**
 * Copyright (c) 2006-2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Cloudsmith
 * 
 */
package com.puppetlabs.graph.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Provides scaling, stacking, adding borders for images.
 * 
 */
public class ImageUtils {
	public static enum ImageType {
		PNG, GIF, JPEG, UNSUPPORTED
	}

	private static enum Scaling {
		WIDTH, HEIGHT, NONE
	};

	public static final int MAX_IMAGE_SIZE = 1000;;

	/**
	 * Returns a buffered image of the wanted type as a byte array
	 * 
	 * @param image
	 * @param imageType
	 * @return
	 */
	public static byte[] asByteArray(BufferedImage image, ImageType imageType) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, imageTypeToFormatName(imageType), out);
			return out.toByteArray();
		}
		catch(IOException e) { // do nothing except return null at the end
		}
		return null;
	}

	/**
	 * Scales an image so it fits within the stated size. The image's longest side
	 * will be scaled to the stated size. Ratio is preserved.
	 * 
	 * @param imageData
	 * @param size
	 * @param imageType
	 * @return returns a new image unless the image has the wanted dimensions.
	 */
	public static byte[] centerInWhiteRect(byte[] imageData, int width, int height, ImageType imageType) {
		int bufferType = imageType == ImageType.PNG
				? BufferedImage.TYPE_INT_ARGB
				: BufferedImage.TYPE_INT_RGB;
		BufferedImage bImg = new BufferedImage(width, height, bufferType);
		Graphics2D g2d = null;
		try {
			g2d = bImg.createGraphics();
			g2d.setBackground(Color.WHITE);
			g2d.clearRect(0, 0, width, height);

		}
		finally {
			g2d.dispose();
		}
		byte[] bg = asByteArray(bImg, imageType);
		byte[] fg = scaleToFitRect(imageData, width, height, imageType);

		return composeCenter(bg, fg, imageType);

	}

	/**
	 * Composes three images by placing the midground center at the center of the background,
	 * and then placing foreground center at the center of the background.
	 * 
	 * @param background
	 * @param midground
	 *            (can be null)
	 * @param foreground
	 *            (can be null)
	 * @param imageType
	 * @return new image data, unless both midground and background are null
	 */
	public static byte[] composeCenter(byte[] background, byte[] midground, byte[] foreground, ImageType imageType) {
		if(background == null || background.length < 1)
			throw new IllegalArgumentException("background can not be null or empty");
		ImageIcon bg = new ImageIcon(background);

		ImageIcon mg = null;
		if(midground != null && midground.length > 0)
			mg = new ImageIcon(midground);

		ImageIcon fg = null;
		if(foreground != null && foreground.length > 0)
			fg = new ImageIcon(foreground);

		if(fg == null && mg == null)
			return background;

		// preserve alpha channel in ARGB
		int bufferType = imageType == ImageType.PNG
				? BufferedImage.TYPE_INT_ARGB
				: BufferedImage.TYPE_INT_RGB;
		int h = bg.getIconHeight();
		int w = bg.getIconWidth();
		BufferedImage bImg = new BufferedImage(w, h, bufferType);

		Graphics2D g2d = null;
		try {
			g2d = bImg.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.drawImage(bg.getImage(), 0, 0, w, h, null);

			int w2;
			int h2;
			int x;
			int y;

			if(mg != null) {
				w2 = fg.getIconWidth();
				h2 = fg.getIconHeight();
				x = (w - w2) / 2;
				y = (h - h2) / 2;
				g2d.drawImage(fg.getImage(), x, y, w2, h2, null);
			}
			if(fg != null) {
				w2 = fg.getIconWidth();
				h2 = fg.getIconHeight();
				x = (w - w2) / 2;
				y = (h - h2) / 2;
				g2d.drawImage(fg.getImage(), x, y, w2, h2, null);
			}
		}
		finally {
			if(g2d != null)
				g2d.dispose();
		}

		return asByteArray(bImg, imageType);
	}

	/**
	 * Composes two images by placing the foreground center at the center of the background.
	 * 
	 * @param background
	 * @param foreground
	 *            (can be null)
	 * @param imageType
	 * @return new image data unless foreground is null
	 */
	public static byte[] composeCenter(byte[] background, byte[] foreground, ImageType imageType) {
		return composeCenter(background, null, foreground, imageType);
	}

	/**
	 * Converts the input imageData to PNG.
	 * (This is a useful method as GIF images does not work well when written after interpolation).
	 * 
	 * @param imageData
	 * @return
	 */
	public static byte[] convertToPNG(byte[] imageData) {
		ImageIcon icon = new ImageIcon(imageData);
		BufferedImage bImg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = null;
		try {
			g2d = bImg.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.drawImage(icon.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(), null);
		}
		finally {
			g2d.dispose();
		}
		return asByteArray(bImg, ImageType.PNG);

	}

	public static byte[] drawBorder(byte[] imageData, int borderWidth, int rgbColor, boolean left, boolean right,
			boolean top, boolean bottom, ImageType imageType) {
		ImageIcon icon = new ImageIcon(imageData);

		// preserve alpha channel in ARGB
		int bufferType = imageType == ImageType.PNG
				? BufferedImage.TYPE_INT_ARGB
				: BufferedImage.TYPE_INT_RGB;
		BufferedImage bImg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), bufferType);

		Graphics2D g2d = null;
		try {
			g2d = bImg.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.drawImage(icon.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(), null);
			g2d.setColor(new Color(rgbColor));
			if(top)
				g2d.drawLine(0, 0, icon.getIconWidth(), 0);
			if(bottom)
				g2d.drawLine(0, icon.getIconHeight(), icon.getIconWidth(), icon.getIconHeight());
			if(left)
				g2d.drawLine(0, 0, 0, icon.getIconHeight());
			if(right)
				g2d.drawLine(icon.getIconWidth() - 1, 0, icon.getIconWidth() - 1, icon.getIconHeight());
		}
		finally {
			g2d.dispose();
		}
		return asByteArray(bImg, imageType);
	}

	/**
	 * Returns the image format name for an image of specified type.
	 * 
	 * @param imageType
	 * @return
	 */
	public static String imageTypeToFormatName(ImageType imageType) {
		// could probably be done more elegantly with values in the enum
		switch(imageType) {
			case PNG:
				return "png";
			case JPEG:
				return "jpeg";
			case GIF:
				return "gif";
			default:
				return "unsupported";
		}
	}

	public static boolean isSupportedImageType(String mimeType) {
		return mimeToImageType(mimeType) == ImageType.UNSUPPORTED
				? false
				: true;
	}

	/**
	 * Loads an image from a resource (file).
	 * 
	 * @param resourcePath
	 *            path to resource - e.g. "/static/img/imagename.gif"
	 * @return loaded image in an image buffer - or null if load could not be made
	 */
	public static byte[] loadImage(String resourcePath) {
		byte[] imageBuffer = null;
		InputStream in = ImageUtils.class.getResourceAsStream(resourcePath);
		if(in != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			try {
				int read = in.read(buffer);
				while(read != -1) {
					out.write(buffer, 0, read);
					read = in.read(buffer);
				}

				imageBuffer = out.toByteArray();
			}
			catch(IOException e) {
				System.err.print("ImageUtils: I/O error while reading resource: " + resourcePath);
			}
		}
		return imageBuffer;

	}

	public static void main(String args[]) {
		String[] list = ImageIO.getWriterFormatNames();
		for(String s : list)
			System.out.print(s + "\n");

	}

	public static ImageType mimeToImageType(String mimeType) {
		if(mimeType.equals("image/png") || mimeType.equals("image/x-png"))
			return ImageType.PNG;
		if(mimeType.equals("image/jpeg") || mimeType.equals("image/jpg") || mimeType.equals("image/pjpeg") ||
				mimeType.equals("image/pjpg"))
			return ImageType.JPEG;
		if(mimeType.equals("image/gif"))
			return ImageType.GIF;
		return ImageType.UNSUPPORTED;
	}

	/**
	 * Scales the image to fit within a square, to fit width, or height. Only one
	 * of the three parameters fit, width, or height can be set to a positive value, the
	 * other values should be set to -1. All thre scaling methods preserve the ratio of w/h.
	 * 
	 * @param imageData
	 * @param fit
	 * @param width
	 * @param height
	 * @param imageType
	 * @return a new image unless the image already has the wanted dimensions
	 */
	private static byte[] scale(byte[] imageData, boolean fit, int width, int height, ImageType imageType) {
		boolean rescale = false;
		double ratio = 1.0;
		ImageIcon icon = null;
		int size = 0;
		Scaling scale = Scaling.NONE;

		// Check if the image needs to be rescaled
		// one of "width", "height" or "fit" can be used
		// ratio of w/h is preserved in all three cases.

		if(fit) {
			int sizeW = Math.min(MAX_IMAGE_SIZE, width);
			int sizeH = Math.min(MAX_IMAGE_SIZE, height);
			icon = new ImageIcon(imageData);
			double wRatio = (double) sizeW / icon.getIconWidth();
			double hRatio = (double) sizeH / icon.getIconHeight();

			// scale along the axis with the smallest scaling ratio
			// (i.e. that way, aspect is preserved as well as fitting within area)
			if(hRatio < wRatio) {
				size = sizeH;
				scale = Scaling.HEIGHT;
			}
			else {
				size = sizeW;
				scale = Scaling.WIDTH;
			}
		}
		else if(width > 0) {
			size = Math.min(MAX_IMAGE_SIZE, width);
			icon = new ImageIcon(imageData);
			scale = Scaling.WIDTH;
		}
		else if(height > 0) {
			size = Math.min(MAX_IMAGE_SIZE, height);
			icon = new ImageIcon(imageData);
			scale = Scaling.HEIGHT;
		}

		if(scale == Scaling.WIDTH) {
			if(size > 0 && size != icon.getIconWidth()) {
				width = size;
				ratio = (double) width / icon.getIconWidth();
				height = (int) (icon.getIconHeight() * ratio);
				rescale = true;
			}
		}
		else if(scale == Scaling.HEIGHT) {
			if(size > 0 && size != icon.getIconHeight()) {
				height = size;
				ratio = (double) height / icon.getIconHeight();
				width = (int) (icon.getIconWidth() * ratio);
				rescale = true;
			}
		}

		// Rescale the image if required
		if(rescale) {
			// preserve alpha channel in ARGB
			int bufferType = imageType == ImageType.PNG
					? BufferedImage.TYPE_INT_ARGB
					: BufferedImage.TYPE_INT_RGB;
			BufferedImage bImg = new BufferedImage(width, height, bufferType);

			Graphics2D g2d = null;
			try {
				g2d = bImg.createGraphics();
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				g2d.drawImage(icon.getImage(), 0, 0, width, height, null);
			}
			finally {
				g2d.dispose();
			}
			return asByteArray(bImg, imageType);
		}
		// no scaling was required
		return imageData;
	}

	/**
	 * Scales an image so it fits exactly the stated height. Ratio is preserved.
	 * 
	 * @param imageData
	 * @param size
	 * @param imageType
	 * @return returns a new image unless the image has the wanted dimensions.
	 */
	public static byte[] scaleToFitHeight(byte[] imageData, int size, ImageType imageType) {
		if(imageData == null || imageData.length == 0)
			return imageData;
		return scale(imageData, false, -1, size, imageType);
	}

	/**
	 * Scales an image so it fits within the stated size. Ratio is preserved
	 * 
	 * @param imageData
	 * @param size
	 * @param imageType
	 * @return returns a new image unless the image has the wanted dimensions.
	 */
	public static byte[] scaleToFitRect(byte[] imageData, int width, int height, ImageType imageType) {
		if(imageData == null || imageData.length == 0)
			return imageData;
		return scale(imageData, true, width, height, imageType);
	}

	/**
	 * Scales an image so it fits within the stated size. The image's longest side
	 * will be scaled to the stated size. Ratio is preserved.
	 * 
	 * @param imageData
	 * @param size
	 * @param imageType
	 * @return returns a new image unless the image has the wanted dimensions.
	 */
	public static byte[] scaleToFitSquare(byte[] imageData, int size, ImageType imageType) {
		if(imageData == null || imageData.length == 0)
			return imageData;
		return scale(imageData, true, size, size, imageType);
	}

	/**
	 * Scales an image so it fits exactly the stated width. Ratio is preserved.
	 * 
	 * @param imageData
	 * @param size
	 * @param imageType
	 * @return returns a new image unless the image has the wanted dimensions.
	 */
	public static byte[] scaleToFitWidth(byte[] imageData, int size, ImageType imageType) {
		if(imageData == null || imageData.length == 0)
			return imageData;
		return scale(imageData, false, size, -1, imageType);
	}

	/**
	 * Returns new image data for two images stacked from top to bottom.
	 * 
	 * @param top
	 *            - image that will be placed on top
	 * @param mid
	 *            - image that will be placed below top (can be null)
	 * @param bottom
	 *            - image that will placed below mid (can be null)
	 * @param imageType
	 *            - the type of the resulting image
	 * @return new image data
	 */
	public static byte[] stack(byte[] top, byte[] mid, byte[] bottom, ImageType imageType) {
		if(top == null || top.length < 1)
			throw new IllegalArgumentException("top can not be null or empty");
		ImageIcon topIcon = new ImageIcon(top);
		int h1 = topIcon.getIconHeight();
		int w1 = topIcon.getIconWidth();

		ImageIcon midIcon = null;
		int h2 = 0;
		int w2 = 0;
		if(mid != null && mid.length > 0) {
			midIcon = new ImageIcon(mid);
			h2 = midIcon.getIconHeight();
			w2 = midIcon.getIconWidth();
		}

		ImageIcon bottomIcon = null;
		int h3 = 0;
		int w3 = 0;
		if(bottom != null && bottom.length > 0) {
			bottomIcon = new ImageIcon(bottom);
			h3 = bottomIcon.getIconHeight();
			w3 = bottomIcon.getIconWidth();
		}

		if(bottomIcon == null && midIcon == null)
			return top;

		// preserve alpha channel in ARGB
		int bufferType = imageType == ImageType.PNG
				? BufferedImage.TYPE_INT_ARGB
				: BufferedImage.TYPE_INT_RGB;
		BufferedImage bImg = new BufferedImage(w1, h1 + h2 + h3, bufferType);

		Graphics2D g2d = null;
		try {
			g2d = bImg.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setBackground(Color.WHITE);
			g2d.clearRect(0, 0, w1, h1 + h2 + h3);

			int y = 0;
			g2d.drawImage(topIcon.getImage(), 0, y, w1, h1, null);

			y += h1;
			if(h2 > 0) {
				g2d.drawImage(midIcon.getImage(), 0, y, w2, h2, null);
				y += h2;
			}
			if(bottomIcon != null) {
				g2d.drawImage(bottomIcon.getImage(), 0, y, w3, h3, null);
			}
		}
		finally {
			if(g2d != null)
				g2d.dispose();
		}

		return asByteArray(bImg, imageType);

	}

	/**
	 * Returns new image data for two images stacked from top to bottom.
	 * 
	 * @param top
	 *            - image that will be placed on top
	 * @param bottom
	 *            - image that will placed below top (can be null)
	 * @param imageType
	 *            - the type of the resulting image
	 * @return new image data
	 */
	public static byte[] stack(byte[] top, byte[] bottom, ImageType imageType) {
		return stack(top, null, bottom, imageType);
	}

}
