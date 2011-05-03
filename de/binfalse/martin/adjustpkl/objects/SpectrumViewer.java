package de.binfalse.martin.adjustpkl.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

/**
 * @author Martin Scharm
 * visit http://binfalse.de
 */
public class SpectrumViewer extends javax.swing.JPanel 
{
	private static final long serialVersionUID = -3517860099651733754L;
	private MassSpectrum ms;
	
	public SpectrumViewer ()
	{
		super ();
		ms = null;
	}
	
	public void drawAbout ()
	{
		this.setBackground(Color.WHITE);
		ms = null;
		this.repaint ();
	}
	public void drawMS (MassSpectrum ms)
	{
		this.ms = ms;
		this.repaint ();
	}
	
	
	private void drawAbout (Graphics2D g2)
	{
		g2.setColor (Color.WHITE);
		g2.fillRect (getX (), getY (), WIDTH, HEIGHT);
		/*g.setColor (Color.BLUE);
		g.drawLine (10, 10, 100, 100);*/
		g2.setColor (Color.BLUE);
		g2.setFont (new Font ("Serif", Font.BOLD, 55));
		g2.drawString ("adjustPKL", getX () + 50, getY () + 50);
		g2.setColor (Color.GRAY);
		g2.setFont (new Font ("SansSerif", Font.PLAIN, 16));
		g2.drawString ("adjusting PKL Files", getX () + 150, getY () + 90);
		g2.drawString ("author: Martin Scharm", getX () + 150, getY () + 120);
		g2.drawString ("visit http://binfalse.de", getX () + 150, getY () + 150);
	}
	private void drawMS (Graphics2D g2)
	{
		String text;
		Rectangle2D rect;
		g2.setColor (Color.WHITE);
		g2.fillRect (getX (), getY (), getWidth (), getHeight ());

		double originX = 50;
		double originY = getHeight () - 50;
		double maxX = getWidth () - 20;
		double maxY = 20;

		double startX = originX + 10;
		double startY = originY;
		double endX = maxX - 10;
		double endY = maxY + 10;
		
		g2.setColor (Color.BLACK);
		g2.drawLine ((int) originX, (int) originY, (int) originX, (int) maxY);
		g2.drawLine ((int) originX, (int) originY, (int) maxX, (int) originY);
		
		double maxIntense = Math.round (.5 + ms.getMaxIntensity ());
		double maxMass = Math.round (.5 + (ms.getMaxMass () / 10)) * 10;
		double minMass = Math.floor ((ms.getMinMass () / 10)) * 10;
		
		Font f = new Font("SansSerif", Font.PLAIN, 10);
		g2.setFont (f);
		FontMetrics fm = g2.getFontMetrics(f);
		NumberFormat nf = new DecimalFormat("0.0E0");
		
		text = "" + nf.format (maxIntense);
		rect = fm.getStringBounds (text, g2);
		g2.drawLine ((int) originX, (int) endY, (int) originX - 5, (int) endY);
		g2.drawString (text, (int) (originX - 10 - rect.getWidth ()), (int) (endY + rect.getHeight () / 2));
		
		text = "" + nf.format (maxIntense * 3 / 4);
		rect = fm.getStringBounds (text, g2);
		g2.drawLine ((int) originX, (int) (endY + (startY - endY) / 4), (int) originX - 5, (int) (endY + (startY - endY) / 4));
		g2.drawString (text, (int) (originX - 10 - rect.getWidth ()), (int) (endY + (startY - endY) / 4 + rect.getHeight () / 2));
		
		text = "" + nf.format (maxIntense / 2);
		rect = fm.getStringBounds (text, g2);
		g2.drawLine ((int) originX, (int) (endY + (startY - endY) / 2), (int) originX - 5, (int) (endY + (startY - endY) / 2));
		g2.drawString (text, (int) (originX - 10 - rect.getWidth ()), (int) (endY + (startY - endY) / 2 + rect.getHeight () / 2));
		
		text = "" + nf.format (maxIntense / 4);
		rect = fm.getStringBounds (text, g2);
		g2.drawLine ((int) originX, (int) (endY + (startY - endY) * 3 / 4), (int) originX - 5, (int) (endY + (startY - endY) * 3 / 4));
		g2.drawString (text, (int) (originX - 10 - rect.getWidth ()), (int) (endY + (startY - endY) * 3 / 4 + rect.getHeight () / 2));
		
		
		

		text = "" + maxMass;
		rect = fm.getStringBounds (text, g2);
		g2.drawLine ((int) endX, (int) originY, (int) endX, (int) originY + 5);
		g2.drawString (text, (int) (endX - rect.getWidth () / 2), (int) (originY + 10 + rect.getHeight ()));

		text = "" + minMass;
		rect = fm.getStringBounds (text, g2);
		g2.drawLine ((int) startX, (int) originY, (int) startX, (int) originY + 5);
		g2.drawString (text, (int) (startX - rect.getWidth () / 2), (int) (originY + 10 + rect.getHeight ()));

		text = "" + (minMass + maxMass) * 3 / 4;
		rect = fm.getStringBounds (text, g2);
		g2.drawLine ((int) ((startX + endX) * 3 / 4), (int) originY, (int) ((startX + endX) * 3 / 4), (int) originY + 5);
		g2.drawString (text, (int) (((startX + endX) * 3 / 4) - rect.getWidth () / 2), (int) (originY + 10 + rect.getHeight ()));

		text = "" + (minMass + maxMass) / 2;
		rect = fm.getStringBounds (text, g2);
		g2.drawLine ((int) ((startX + endX) / 2), (int) originY, (int) ((startX + endX) / 2), (int) originY + 5);
		g2.drawString (text, (int) (((startX + endX) / 2) - rect.getWidth () / 2), (int) (originY + 10 + rect.getHeight ()));

		text = "" + (minMass + maxMass) / 4;
		rect = fm.getStringBounds (text, g2);
		g2.drawLine ((int) ((startX + endX) / 4), (int) originY, (int) ((startX + endX) / 4), (int) originY + 5);
		g2.drawString (text, (int) (((startX + endX) / 4) - rect.getWidth () / 2), (int) (originY + 10 + rect.getHeight ()));

		Vector<Peak> peaks = ms.getPeaks ();
		for (int i = 0; i < peaks.size (); i++)
		{
			Peak p = peaks.elementAt (i);
			double x = startX + (((p.getMass () - minMass)/(maxMass - minMass)) * (endX - startX));
			g2.drawLine ((int) x, (int) originY, (int) x, (int) (startY + (endY - startY) * p.getIntensity () / maxIntense));
		}
		
	}
	
	public void paint(Graphics g)
	{
		super.paint (g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint (RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint (RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		if (ms == null) drawAbout (g2);
		else drawMS (g2);
  }

}
