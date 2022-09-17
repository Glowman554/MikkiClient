package gq.glowman554.mikki.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import gq.glowman554.mikki.api.Mikki;
import gq.glowman554.mikki.api.MikkiAccountChecker;
import gq.glowman554.mikki.api.data.MikkiPage;
import gq.glowman554.mikki.utils.Log;

public class ViewFrame extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6165028023448117715L;
	private JPanel contentPane;
	private JTextPane textPane;

	private JScrollPane scrollPane;
	private JButton updateButton;
	private JButton deleteButton;

	private MikkiPage page;

	/**
	 * Create the frame.
	 */
	public ViewFrame(Mikki mikki, MikkiAccountChecker mikki_acc, MainWindow mw, String page_id)
	{

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 711, 600);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		this.scrollPane = new JScrollPane();
		this.scrollPane.setBounds(10, 11, 675, 504);
		this.contentPane.add(this.scrollPane);

		this.textPane = new JTextPane();
		this.textPane.setEditable(false);
		this.scrollPane.setViewportView(this.textPane);

		this.updateButton = new JButton("Update");
		this.updateButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{

					Log.log("Updating page " + page_id + "...");
					String mikki_token = mikki_acc.token();

					mikki.update(page_id, MikkiPage.encode(textPane.getText()), MikkiPage.encode(getTitle()), mikki_token);

					dispose();

					mw.load_pages_and_changes();
				}
				catch (IOException | IllegalArgumentException | IllegalAccessException e2)
				{
					throw new IllegalStateException(e2.getMessage());
				}
			}
		});
		this.updateButton.setEnabled(false);
		this.updateButton.setBounds(10, 526, 89, 23);
		this.contentPane.add(this.updateButton);

		this.deleteButton = new JButton("Delete");
		this.deleteButton.setEnabled(false);
		this.deleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{

					Log.log("Deleting page " + page_id + "...");
					String mikki_token = mikki_acc.token();

					mikki.delete(page_id, mikki_token);
					dispose();

					mw.load_pages_and_changes();
				}
				catch (IOException | IllegalArgumentException e2)
				{
					throw new IllegalStateException(e2.getMessage());
				}
			}
		});
		this.deleteButton.setBounds(109, 526, 89, 23);
		this.contentPane.add(this.deleteButton);

		var _this = this;
		new Thread(() -> {
			LoadingDialog ld = new LoadingDialog(2);
			ld.setVisible(true);

			Log.log(String.format("Loading page %s...", page_id));

			try
			{
				page = mikki.get(page_id);

				ld.update(1);

				try
				{
					if (mikki_acc.editor())
					{
						Log.log("Enabeling editor mode...");
						textPane.setEditable(true);
						updateButton.setEnabled(true);
						deleteButton.setEnabled(true);
					}
				}
				catch (IOException e)
				{

				}

				ld.update(2);
			}
			catch (IllegalArgumentException | IllegalAccessException | IOException e)
			{
				e.printStackTrace();
			}

			_this.setTitle(page.page_title);
			textPane.setText(page.page_text);

			ld.dispose();

			_this.toFront();
		}).start();
	}
}
