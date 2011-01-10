package de.binfalse.martin.adjustpkl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import de.binfalse.martin.adjustpkl.objects.MassSpectrum;
import de.binfalse.martin.adjustpkl.objects.Peak;

/**
 * @author Martin Scharm
 * visit http://binfalse.de
 */
public class PKLIO
{
	public static Vector<MassSpectrum> readPKL (File f)
	{
		Vector<MassSpectrum> spectra = new Vector<MassSpectrum> ();
		MassSpectrum ms = new MassSpectrum ();
		try
		{
			BufferedReader br = new BufferedReader (new FileReader (f));
			String strLine;
			while ((strLine = br.readLine()) != null)
			{
				if (strLine.length () < 1)
				{
					if (!ms.empty ()) spectra.add (ms);
					ms = new MassSpectrum ();
					continue;
				}
				String [] tokens = strLine.split ("\\s");
				if (tokens.length == 3)
				{
					ms.setMoleculeMass (Double.parseDouble (tokens[0]));
					ms.setFiktIntensity (Double.parseDouble (tokens[1]));
					ms.setCharge (Integer.parseInt (tokens[2]));
				}
				else if (tokens.length == 2)
				{
					ms.addPeak (new Peak (Double.parseDouble (tokens[0]), Double.parseDouble (tokens[1])));
				}
				else
				{
					System.out.println ("failed to parse: " + strLine);
					return null;
				}
	    }
			br.close ();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return null;
		}
		return spectra;
	}
	
	public static boolean writePKL (Vector<MassSpectrum> spectra, File f)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter (new FileWriter (f));
			for (int i = 0; i < spectra.size (); i++)
			{
				bw.write (spectra.elementAt (i).toString ());
				bw.newLine ();
				bw.flush ();
			}
			bw.close ();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
