package gq.glowman554.mikki.gui;

import javax.swing.JDialog;
import gq.glowman554.mikki.utils.Log;

import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class LoadingDialog extends JDialog
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8217383164386516L;
	private JLabel lblNewLabel;
	private JProgressBar progressBar;

	private final int max;
	private int progress = 0;
	
	/**
	 * Create the dialog.
	 */
	public LoadingDialog(int max)
	{
		this.max = max;
		
		setTitle("Loading...");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 189, 111);
		getContentPane().setLayout(null);
		
		this.lblNewLabel = new JLabel("Loading... Please wait.");
		this.lblNewLabel.setBounds(10, 11, 120, 14);
		getContentPane().add(this.lblNewLabel);
		
		this.progressBar = new JProgressBar();
		this.progressBar.setBounds(10, 36, 146, 14);
		this.progressBar.setMaximum(this.max);
		this.progressBar.setValue(this.progress);
		getContentPane().add(this.progressBar);
	}
	
	public void update(int proress)
	{
		Log.log(String.format("Updating progress to %d...", proress));
		
		this.progress = proress;
		this.progressBar.setValue(this.progress);
		this.repaint();
	}
}
