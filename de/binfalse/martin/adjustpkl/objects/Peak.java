
package de.binfalse.martin.adjustpkl.objects;

/**
 * @author Martin Scharm
 * visit http://binfalse.de
 */
public class Peak
{
	private double mass;
	private double intensity;
	public Peak ()
	{
		mass = 0;
		intensity = 0;
	}
	public Peak (double mass, double intensity)
	{
		this.mass = mass;
		this.intensity = intensity;
	}
	public double getMass ()
	{
		return mass;
	}
	public void setMass (double mass)
	{
		this.mass = mass;
	}
	public double getIntensity ()
	{
		return intensity;
	}
	public void setIntensity (double intensity)
	{
		this.intensity = intensity;
	}
	public void adjustMass (double add)
	{
		this.mass += add;
	}
	
	public String toString ()
	{
		return mass + " " + intensity;
	}
}
