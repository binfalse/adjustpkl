package de.binfalse.martin.adjustpkl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.binfalse.martin.adjustpkl.objects.Link;
import de.binfalse.martin.adjustpkl.objects.MassSpectrum;
import de.binfalse.martin.adjustpkl.objects.SpectrumViewer;

/**
 * @author Martin Scharm
 * visit http://binfalse.de
 */
public class adjustPKLWindow extends javax.swing.JFrame
{
	private static final long serialVersionUID = 6592489800967821841L;
	private Vector<MassSpectrum> spectra;
	private final String CHOOSER_STRING = "Choose your Spectrum";
	private final String CHOOSER_FIELD_STRING = "choose Spectrum";
	private int aktSpectrum;
	private File file;
	
	public adjustPKLWindow ()
	{
		initComponents ();
		spectra = null;
		aktSpectrum = -1;
		file = null;
	}
	
	private void loadPKL ()
	{
		JFileChooser fc = new JFileChooser(".");
		fc.setFileFilter (new javax.swing.filechooser.FileFilter ()
		{
			public boolean accept(File f)
			{
				return f.getName().toLowerCase().endsWith(".pkl") || f.isDirectory();
			}
			public String getDescription()
			{
				return "PKL files (*.pkl)";
			}
		});
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			spectra = PKLIO.readPKL (file);
			if (spectra == null || spectra.size () < 1)
			{
				JOptionPane.showMessageDialog (this, "Failed to read File!!", "Error", JOptionPane.ERROR_MESSAGE);
				jComboBoxSpectrumChooser.setModel(new javax.swing.DefaultComboBoxModel (new String[] { CHOOSER_STRING }));
				jMenuItemAdjustAll.setEnabled (false);
				jButtonAdjust.setEnabled (false);
				jMenuItemSave.setEnabled (false);
				jPanelDrawSpectrum.drawAbout ();
				this.file = null;
			}
			else
			{
				
				String [] options = new String [spectra.size () + 1];
				options[0] = CHOOSER_STRING;
				for (int i = 0; i < options.length - 1; i++)
				{
					options[i + 1] = "" + i + "-" + spectra.get (i).getOrgMoleculeMass ();
				}
				jComboBoxSpectrumChooser.setModel (new javax.swing.DefaultComboBoxModel (options));
				jMenuItemAdjustAll.setEnabled (true);
				jMenuItemSave.setEnabled (true);
				this.file = file;
			}
		}
		aktSpectrum = -1;
		jTextFieldAdjuster.setText (CHOOSER_FIELD_STRING);
	}
	private void savePKL ()
	{
		if (spectra == null || spectra.size () < 1) return;
		
		javax.swing.JPanel p = new javax.swing.JPanel ();
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(p);
		p.setLayout(layout);
		javax.swing.JLabel top = new javax.swing.JLabel ("Round to (No. of decimal places)");
		javax.swing.JLabel labelRoundMZ = new javax.swing.JLabel ("<html><I>m/z</I> values:</html>");
		javax.swing.JTextField fieldRoundMZ = new javax.swing.JTextField ("4");
		fieldRoundMZ.setHorizontalAlignment (javax.swing.JTextField.RIGHT);
		javax.swing.JLabel labelRoundIntense = new javax.swing.JLabel ("intensities:");
		javax.swing.JTextField fieldRoundIntense = new javax.swing.JTextField ("4");
		fieldRoundIntense.setHorizontalAlignment (javax.swing.JTextField.RIGHT);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addComponent(labelRoundMZ)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(fieldRoundMZ, javax.swing.GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
								)
								.addGroup(layout.createSequentialGroup()
										.addComponent(labelRoundIntense)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(fieldRoundIntense, javax.swing.GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
								)
						)
						.addContainerGap()
				)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(top)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(labelRoundMZ)
								.addComponent(fieldRoundMZ)
						)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(labelRoundIntense)
								.addComponent(fieldRoundIntense)
						)
				)
		);
		
		if (JOptionPane.showConfirmDialog (this, p, "Save options", JOptionPane.PLAIN_MESSAGE) < 0) return;
		int roundMZ = -1, roundIntense = -1;
		try
		{
			roundMZ = Integer.parseInt (fieldRoundMZ.getText ());
			roundIntense = Integer.parseInt (fieldRoundIntense.getText ());
			if (roundMZ < 0 || roundIntense < 0) throw new NumberFormatException ();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace ();
			JOptionPane.showMessageDialog (this, "Both values must be non-negative integers!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		double dRoundMZ = Math.pow (10, roundMZ), dRoundIntense = Math.pow (10, roundIntense);
		NumberFormat numForm= NumberFormat.getInstance(Locale.US);
		numForm.setGroupingUsed(false);
		numForm.setMaximumFractionDigits (20);
		
		JFileChooser fc = new JFileChooser(".");
		if (file == null) fc.setSelectedFile (new File ("adjusted_" + new java.text.SimpleDateFormat( "yyyy-MM-dd_HH-mm" ).format (new java.util.Date()) + ".pkl"));
		else
		{
			fc = new JFileChooser(file.getPath ());
			String filename = file.getName ();
			if (filename.endsWith (".pkl")) filename = filename.substring (0, filename.length () - 4) + "_adjusted_" + new java.text.SimpleDateFormat( "yyyy-MM-dd_HH-mm" ).format (new java.util.Date()) + ".pkl";
			fc.setSelectedFile (new File (filename));
		}
		fc.setFileFilter (new javax.swing.filechooser.FileFilter ()
		{
			public boolean accept(File f)
			{
				return f.getName().toLowerCase().endsWith(".pkl") || f.isDirectory();
			}
			public String getDescription()
			{
				return "PKL files (*.pkl)";
			}
		});
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			System.out.println ("saving: " + file.getAbsolutePath ());
			if (!PKLIO.writePKL (spectra, file, dRoundMZ, dRoundIntense, numForm))
				JOptionPane.showMessageDialog (this, "Failed to write File!!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void adjustAll ()
	{
		if (spectra == null) return;
		String s = (String)JOptionPane.showInputDialog(this, "Adjust by (ppm)", "Adjust all Spectra", JOptionPane.PLAIN_MESSAGE);
		if (s == null) return;
		double add = 0;
		try
		{
			 add = Double.parseDouble (s);
		}
		catch (java.lang.NumberFormatException e)
		{
			javax.swing.JOptionPane.showMessageDialog (this, "Input is not numeric!\nPlease think twice ;-)", "Ooops", javax.swing.JOptionPane.ERROR_MESSAGE);
			return;
		}
		for (int i = 0; i < spectra.size (); i++)
			spectra.elementAt (i).adjust (add);
		jPanelDrawSpectrum.repaint ();
	}
	private void adjustOne ()
	{
		if (aktSpectrum >= 0 && jTextFieldAdjuster.getText ().length () > 0)
		{
			double add = 0;
			try
			{
				 add = Double.parseDouble (jTextFieldAdjuster.getText ());
			}
			catch (java.lang.NumberFormatException e)
			{
				javax.swing.JOptionPane.showMessageDialog (this, "Input is not numeric!\nPlease think twice ;-)", "Ooops", javax.swing.JOptionPane.ERROR_MESSAGE);
				return;
			}
			spectra.elementAt (aktSpectrum).adjust (add);
			jPanelDrawSpectrum.repaint ();
		}
	}
	private void chooseSpectrum (String spectrum)
	{
		if (spectrum.equals (CHOOSER_STRING))
		{
			jPanelDrawSpectrum.drawAbout ();
			aktSpectrum = -1;
			jTextFieldAdjuster.setText (CHOOSER_FIELD_STRING);
			jButtonAdjust.setEnabled (false);
		}
		else
		{
			int num = Integer.parseInt (spectrum.split ("-")[0]);
			if (num >= 0 && num < spectra.size ())
			{
				jPanelDrawSpectrum.drawMS (spectra.elementAt (num));
				jTextFieldAdjuster.setText ("");
				jButtonAdjust.setEnabled (true);
				aktSpectrum = num;
			}
			else
			{
				aktSpectrum = -1;
				jPanelDrawSpectrum.drawAbout ();
				jTextFieldAdjuster.setText (CHOOSER_FIELD_STRING);
				jButtonAdjust.setEnabled (false);
			}
		}
	}
	
	
	public static void main (String[] args)
	{
		java.awt.EventQueue.invokeLater (new Runnable () {
			public void run()
			{
				new adjustPKLWindow ().setVisible (true);
			}
		});
	}
	private void initComponents ()
	{
		this.setLocation(100, 100);
		this.setTitle ("adjustPKL - by Martin Scharm");
		
		jLabelSpectrumChooser = new javax.swing.JLabel ();
		jComboBoxSpectrumChooser = new javax.swing.JComboBox ();
		jLabelAdjuster = new javax.swing.JLabel ();
		jLabelAdjusterUnits = new javax.swing.JLabel ();
		link = new Link ();
		jTextFieldAdjuster = new javax.swing.JTextField ();
		jButtonAdjust = new javax.swing.JButton ();
		jPanelDrawSpectrum = new SpectrumViewer ();
		jMenuBar1 = new javax.swing.JMenuBar ();
		jMenu = new javax.swing.JMenu ();
		jMenuItemLoad = new javax.swing.JMenuItem ();
		jMenuItemSave = new javax.swing.JMenuItem ();
		jSeparator1 = new javax.swing.JSeparator ();
		jMenuItemAdjustAll = new javax.swing.JMenuItem ();
		jSeparator2 = new javax.swing.JSeparator ();
		jMenuItemQuit = new javax.swing.JMenuItem ();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		jLabelSpectrumChooser.setText ("choose Spectrum:");
		
		jComboBoxSpectrumChooser.setModel(new javax.swing.DefaultComboBoxModel (new String[] { CHOOSER_STRING }));
		jComboBoxSpectrumChooser.addItemListener( new ItemListener()
		{
			public void itemStateChanged( ItemEvent e )
			{
				if (e.getStateChange () != ItemEvent.SELECTED) return;
				javax.swing.JComboBox selectedChoice = (javax.swing.JComboBox)e.getSource();
				chooseSpectrum ((String) selectedChoice.getSelectedItem());
			}
		});
		
		jLabelAdjuster.setText ("adjust by:");
		jLabelAdjusterUnits.setText ("ppm");
		
		jTextFieldAdjuster.setText (CHOOSER_FIELD_STRING);
		jTextFieldAdjuster.addActionListener (new java.awt.event.ActionListener ()
		{
			public void actionPerformed (java.awt.event.ActionEvent e)
			{
				if (jButtonAdjust.isEnabled ()) adjustOne ();
			}
		});
		
		jButtonAdjust.setText ("adjust");
		jButtonAdjust.addActionListener (new ActionListener()
		{
			public void actionPerformed (ActionEvent event)
			{
				adjustOne ();
			}
		});
		jButtonAdjust.setEnabled (false);
		
		link.setText("http://binfalse.de");
		link.setURL ("http://binfalse.de");
		link.setHorizontalAlignment (javax.swing.JLabel.RIGHT);
		
		javax.swing.GroupLayout jPanelDrawSpectrumLayout = new javax.swing.GroupLayout (jPanelDrawSpectrum);
		jPanelDrawSpectrum.setLayout (jPanelDrawSpectrumLayout);
		jPanelDrawSpectrumLayout.setHorizontalGroup (
				jPanelDrawSpectrumLayout.createParallelGroup (javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 791, Short.MAX_VALUE)
		);
		jPanelDrawSpectrumLayout.setVerticalGroup (
				jPanelDrawSpectrumLayout.createParallelGroup (javax.swing.GroupLayout.Alignment.LEADING)
				.addGap (0, 456, Short.MAX_VALUE)
		);
		jPanelDrawSpectrum.drawAbout ();
		
		jMenu.setText ("File");
		
		jMenuItemLoad.setText ("load PKL");
		jMenuItemLoad.addActionListener (new ActionListener()
		{
			public void actionPerformed (ActionEvent event)
			{
				loadPKL ();
			}
		});
		jMenu.add (jMenuItemLoad);
		
		jMenuItemSave.setText ("save PKL");
		jMenuItemSave.addActionListener (new ActionListener()
		{
			public void actionPerformed (ActionEvent event)
			{
				savePKL ();
			}
		});
		jMenuItemSave.setEnabled (false);
		jMenu.add (jMenuItemSave);
		jMenu.add (jSeparator1);
		
		jMenuItemAdjustAll.setText ("adjust all Spectra");
		jMenuItemAdjustAll.addActionListener (new ActionListener()
		{
			public void actionPerformed (ActionEvent event)
			{
				adjustAll ();
			}
		});
		jMenuItemAdjustAll.setEnabled (false);
		jMenu.add (jMenuItemAdjustAll);
		jMenu.add (jSeparator2);
		
		jMenuItemQuit.setText ("Quit");
		jMenuItemQuit.addActionListener (new ActionListener ()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		jMenu.add (jMenuItemQuit);
		
		jMenuBar1.add (jMenu);
		
		setJMenuBar (jMenuBar1);
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jPanelDrawSpectrum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addComponent(jLabelSpectrumChooser)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jComboBoxSpectrumChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabelAdjuster)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jTextFieldAdjuster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap (1, 1, 1)
										.addComponent(jLabelAdjusterUnits)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jButtonAdjust)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
										.addComponent(link)
								)
						)
						.addContainerGap()
				)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jComboBoxSpectrumChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabelSpectrumChooser)
								.addComponent(jLabelAdjuster)
								.addComponent(jTextFieldAdjuster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabelAdjusterUnits)
								.addComponent(jButtonAdjust)
								.addComponent(link)
						)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jPanelDrawSpectrum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				)
		);
		
		pack();
	}
	private javax.swing.JButton jButtonAdjust;
	private javax.swing.JComboBox jComboBoxSpectrumChooser;
	private javax.swing.JLabel jLabelSpectrumChooser;
	private javax.swing.JLabel jLabelAdjuster;
	private javax.swing.JLabel jLabelAdjusterUnits;
	private Link link;
	private javax.swing.JMenu jMenu;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItemLoad;
	private javax.swing.JMenuItem jMenuItemSave;
	private javax.swing.JMenuItem jMenuItemQuit;
	private javax.swing.JMenuItem jMenuItemAdjustAll;
	private SpectrumViewer jPanelDrawSpectrum;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JTextField jTextFieldAdjuster;
}
