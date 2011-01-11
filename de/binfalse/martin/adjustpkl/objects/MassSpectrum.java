
package de.binfalse.martin.adjustpkl.objects;

import java.text.NumberFormat;
import java.util.Vector;

/**
 * @author Martin Scharm
 * visit http://binfalse.de
 */
public class MassSpectrum
{
	private Vector<Peak> peaks;
	private double minMass, maxMass;
	private double minIntensity, maxIntensity;
	private double moleculeMass;
	private double orgMoleculeMass;
	private double fiktIntensity;
	private int charge;
	private boolean empty;
	
	public MassSpectrum ()
	{
		peaks = new Vector<Peak> ();
		orgMoleculeMass = moleculeMass = fiktIntensity = charge = 0;
		minMass = minIntensity = Double.POSITIVE_INFINITY;
		maxMass = maxIntensity = Double.NEGATIVE_INFINITY;
		empty = true;
	}
	
	public MassSpectrum (Vector<Peak> peaks)
	{
		this.peaks = peaks;
		orgMoleculeMass = moleculeMass = fiktIntensity = charge = 0;
		minMass = minIntensity = Double.POSITIVE_INFINITY;
		maxMass = maxIntensity = Double.NEGATIVE_INFINITY;
		if (peaks.size () > 0)
		{
			for (int i = 0; i < peaks.size (); i++)
			{
				if (peaks.elementAt (i).getMass () < minMass) minMass = peaks.elementAt (i).getMass ();
				if (peaks.elementAt (i).getMass () > maxMass) maxMass = peaks.elementAt (i).getMass ();
				if (peaks.elementAt (i).getIntensity () < minIntensity) minIntensity = peaks.elementAt (i).getIntensity ();
				if (peaks.elementAt (i).getIntensity () > maxIntensity) maxIntensity = peaks.elementAt (i).getIntensity ();
			}
		}
		empty = true;
	}
	
	public double getMinMass ()
	{
		return minMass;
	}

	public void setMinMass (double minMass)
	{
		this.minMass = minMass;
	}

	public double getMaxMass ()
	{
		return maxMass;
	}

	public void setMaxMass (double maxMass)
	{
		this.maxMass = maxMass;
	}

	public double getMinIntensity ()
	{
		return minIntensity;
	}

	public void setMinIntensity (double minIntensity)
	{
		this.minIntensity = minIntensity;
	}

	public double getMaxIntensity ()
	{
		return maxIntensity;
	}

	public void setMaxIntensity (double maxIntensity)
	{
		this.maxIntensity = maxIntensity;
	}

	public Vector<Peak> getPeaks ()
	{
		return peaks;
	}

	public double getMoleculeMass ()
	{
		return moleculeMass;
	}

	public double getOrgMoleculeMass ()
	{
		return orgMoleculeMass;
	}

	public void setOrgMoleculeMass (double moleculeIntensity)
	{
		this.moleculeMass = moleculeIntensity;
		this.orgMoleculeMass = moleculeIntensity;
		empty = false;
	}

	public void setMoleculeMass (double moleculeIntensity)
	{
		this.moleculeMass = moleculeIntensity;
		empty = false;
	}

	public double getFiktIntensity ()
	{
		return fiktIntensity;
	}

	public void setFiktIntensity (double fiktIntensity)
	{
		this.fiktIntensity = fiktIntensity;
		empty = false;
	}

	public int getCharge ()
	{
		return charge;
	}

	public void setCharge (int charge)
	{
		this.charge = charge;
		empty = false;
	}

	public void addPeak (Peak p)
	{
		peaks.add (p);
		if (p.getMass () < minMass) minMass = p.getMass ();
		if (p.getMass () > maxMass) maxMass = p.getMass ();
		if (p.getIntensity () < minIntensity) minIntensity = p.getIntensity ();
		if (p.getIntensity () > maxIntensity) maxIntensity = p.getIntensity ();
		empty = false;
	}
	
	public void adjust (double add)
	{
		minMass = Double.POSITIVE_INFINITY;
		maxMass = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < peaks.size (); i++)
		{
			peaks.elementAt (i).adjustMass (add);
			if (peaks.elementAt (i).getMass () < minMass) minMass = peaks.elementAt (i).getMass ();
			if (peaks.elementAt (i).getMass () > maxMass) maxMass = peaks.elementAt (i).getMass ();
		}
		double adjust = moleculeMass * add / 1000000;
		moleculeMass += adjust;

		if (moleculeMass < minMass) minMass = moleculeMass;
		if (moleculeMass > maxMass) maxMass = moleculeMass;
		
	}
	
	public boolean empty ()
	{
		return empty;
	}
	
	public String getOutput (double dRoundMZ, double dRoundIntense, NumberFormat numForm)
	{
		String s = numForm.format (Math.round (moleculeMass * dRoundMZ) / dRoundMZ) + " " + numForm.format (Math.round (fiktIntensity * dRoundIntense) / dRoundIntense) + " " + charge + System.getProperty("line.separator");
		for (int i = 0; i < peaks.size (); i++)
			s += peaks.elementAt (i).getOutput (dRoundMZ, dRoundIntense, numForm) + System.getProperty("line.separator");
		return s;
	}
	
	public String toString ()
	{
		String s = moleculeMass + " " + fiktIntensity + " " + charge + System.getProperty("line.separator");
		for (int i = 0; i < peaks.size (); i++)
			s += peaks.elementAt (i) + System.getProperty("line.separator");
		return s;
	}
}
