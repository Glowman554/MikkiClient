package gq.glowman554.mikki.gui;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gq.glowman554.mikki.api.Mikki;
import gq.glowman554.mikki.api.MikkiAccountChecker;
import gq.glowman554.mikki.api.data.MikkiPage;
import gq.glowman554.mikki.utils.ExceptionUtils;
import gq.glowman554.mikki.utils.Log;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

public class MainWindow extends Thread
{

	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane;
	private JTable pagesTable;
	private JScrollPane scrollPane_1;
	private JTable changelogTable;

	private JLabel preloadLabel;
	private JProgressBar preloadProgress;
	private JPanel panel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JTextField usernameText;
	private JTextField passwordText;
	private JButton loginButton;
	private JButton registerButton;
	private JButton logoutButton;

	private JCheckBox editorBox;
	private JButton newButton;
	private JTextField newPageName;
	private JPanel panel_1;
	private JPanel preloadPanel;

	private Mikki mikki = new Mikki();
	private MikkiAccountChecker mikki_acc = new MikkiAccountChecker(mikki);

	/**
	 * Launch the application.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		ExceptionUtils.intercept();

		// Reflex.setDebug(true);

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow()
	{
		initialize();
		start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 968, 742);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(null);

		this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		this.tabbedPane.setBounds(10, 11, 932, 629);
		this.frame.getContentPane().add(this.tabbedPane);

		this.scrollPane = new JScrollPane();
		this.tabbedPane.addTab("Pages", null, this.scrollPane, null);

		this.pagesTable = new JTable();
		var _this = this;
		this.pagesTable.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String page_id = (String) pagesTable.getValueAt(pagesTable.getSelectedRow(), 1);
				Log.log(String.format("Mouse clicked! (%s)", page_id));

				new ViewFrame(mikki, mikki_acc, _this, page_id).setVisible(true);
			}
		});
		this.pagesTable.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] {"Page name", "Page id"})
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 5246877781813310466L;
			Class<?>[] columnTypes = new Class[] {String.class, String.class};

			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] {false, false};

			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		});

		this.scrollPane.setViewportView(this.pagesTable);

		this.scrollPane_1 = new JScrollPane();
		this.tabbedPane.addTab("Changelog", null, this.scrollPane_1, null);

		this.changelogTable = new JTable();
		this.changelogTable.setModel(new DefaultTableModel(new Object[][] {}, new String[] {"When", "What"})
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -8365163519470922240L;
			Class<?>[] columnTypes = new Class[] {String.class, String.class};

			public Class<?> getColumnClass(int columnIndex)
			{
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] {false, false};

			public boolean isCellEditable(int row, int column)
			{
				return columnEditables[column];
			}
		});
		this.scrollPane_1.setViewportView(this.changelogTable);

		this.panel = new JPanel();
		this.tabbedPane.addTab("Account", null, this.panel, null);
		this.panel.setLayout(null);

		this.lblNewLabel_1 = new JLabel("Username:");
		this.lblNewLabel_1.setBounds(10, 11, 55, 14);
		this.panel.add(this.lblNewLabel_1);

		this.lblNewLabel_2 = new JLabel("Password:");
		this.lblNewLabel_2.setBounds(10, 36, 55, 14);
		this.panel.add(this.lblNewLabel_2);

		this.usernameText = new JTextField();
		this.usernameText.setBounds(75, 8, 222, 20);
		this.panel.add(this.usernameText);
		this.usernameText.setColumns(10);

		this.passwordText = new JTextField();
		this.passwordText.setBounds(75, 33, 222, 20);
		this.panel.add(this.passwordText);
		this.passwordText.setColumns(10);

		this.loginButton = new JButton("Login");
		this.loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Log.log(String.format("Logging in %s@%s", usernameText.getText(), passwordText.getText()));

				try
				{
					mikki_acc.login(usernameText.getText(), passwordText.getText());
					login_success();
				}
				catch (IllegalArgumentException | IllegalAccessException | IOException e1)
				{
					e1.printStackTrace();
				}

			}
		});
		this.loginButton.setBounds(10, 72, 89, 23);
		this.panel.add(this.loginButton);

		this.registerButton = new JButton("Register");
		this.registerButton.setBounds(109, 72, 89, 23);
		this.registerButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Log.log(String.format("Creating %s@%s", usernameText.getText(), passwordText.getText()));

				try
				{			
					mikki_acc.create(usernameText.getText(), passwordText.getText());
					login_success();
				}
				catch (IllegalArgumentException | IllegalAccessException | IOException e1)
				{
					e1.printStackTrace();
				}

			}
		});
		this.panel.add(this.registerButton);

		this.logoutButton = new JButton("Logout");
		this.logoutButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mikki_acc.delete();
				login_reset();
			}
		});
		this.logoutButton.setEnabled(false);
		this.logoutButton.setBounds(208, 72, 89, 23);
		this.panel.add(this.logoutButton);

		this.editorBox = new JCheckBox("Can edit");
		this.editorBox.setEnabled(false);
		this.editorBox.setBounds(305, 7, 97, 23);
		this.panel.add(this.editorBox);

		this.preloadPanel = new JPanel();
		this.preloadPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Preload", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		this.preloadPanel.setBounds(10, 651, 269, 36);
		this.frame.getContentPane().add(this.preloadPanel);
		this.preloadPanel.setLayout(null);

		this.preloadLabel = new JLabel("Preload progress:");
		this.preloadLabel.setBounds(6, 16, 101, 14);
		this.preloadPanel.add(this.preloadLabel);

		this.preloadProgress = new JProgressBar();
		this.preloadProgress.setBounds(117, 16, 146, 14);
		this.preloadPanel.add(this.preloadProgress);

		this.panel_1 = new JPanel();
		this.panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "New page", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		this.panel_1.setBounds(745, 651, 197, 45);
		this.frame.getContentPane().add(this.panel_1);
		this.panel_1.setLayout(null);

		this.newButton = new JButton("New page");
		this.newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				try
				{

					Log.log("Creating page " + newPageName.getText() + "...");
	
					MikkiPage page =  mikki.create(MikkiPage.encode("# New page"), MikkiPage.encode(newPageName.getText()), mikki_acc.token());					
					
					load_pages_and_changes();
					
					new ViewFrame(mikki, mikki_acc, _this, page.page_id).setVisible(true);
				}
				catch (IOException | IllegalArgumentException | IllegalAccessException e2)
				{
					throw new IllegalStateException(e2.getMessage());
				}
			}
		});
		this.newButton.setBounds(6, 16, 89, 23);
		this.panel_1.add(this.newButton);

		this.newPageName = new JTextField();
		this.newPageName.setBounds(105, 17, 86, 20);
		this.panel_1.add(this.newPageName);
		this.newPageName.setColumns(10);
	}

	@Override
	public void run()
	{
		load_pages_and_changes();
		
		if (mikki_acc.check())
		{
			login_success();
			
			try
			{
				editorBox.setSelected(mikki_acc.editor());
			}
			catch (IllegalArgumentException | IllegalAccessException | IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if (want_preload())
		{
			mikki.setPr((p) -> preloadProgress.setValue(p));
			mikki.start();
		}
		else
		{
			frame.getContentPane().remove(preloadPanel);
			this.frame.repaint();
		}
	}

	public void load_pages_and_changes()
	{
		LoadingDialog ld = new LoadingDialog(2);
		ld.setVisible(true);

		Log.log("Starting to load pages/changelog...");

		try
		{
			while (((DefaultTableModel) this.pagesTable.getModel()).getRowCount() != 0)
			{
				((DefaultTableModel) this.pagesTable.getModel()).removeRow(0);
			}

			var pages = mikki.list();
			preloadProgress.setMaximum(pages.$.len());

			for (int i = 0; i < pages.$.len(); i++)
			{
				var page = pages.$.get(i);

				((DefaultTableModel) this.pagesTable.getModel()).addRow(new Object[] {page.page_title, page.page_id});

				Log.log(String.format("Found page %s (%s)", page.page_id, page.page_title));
			}
			ld.update(1);

			var changes = mikki.changelog();
			
			while (((DefaultTableModel) this.changelogTable.getModel()).getRowCount() != 0)
			{
				((DefaultTableModel) this.changelogTable.getModel()).removeRow(0);
			}


			for (int i = 0; i < changes.$.len(); i++)
			{
				var change = changes.$.get(i);

				((DefaultTableModel) this.changelogTable.getModel()).addRow(new Object[] {new Date(change.when).toString(), change.what});

				Log.log(String.format("Found change %d %s", change.when, change.what));
			}

			ld.update(2);

			this.frame.repaint();
		}
		catch (IllegalArgumentException | IllegalAccessException | IOException e)
		{
			e.printStackTrace();
		}

		ld.dispose();
	}

	private void login_success()
	{
		Log.log("Changing login state to success...");
		loginButton.setEnabled(false);
		registerButton.setEnabled(false);
		usernameText.setEnabled(false);
		usernameText.setText("");
		passwordText.setEnabled(false);
		passwordText.setText("");
		logoutButton.setEnabled(true);

		frame.repaint();
	}

	private void login_reset()
	{
		Log.log("Resetting login ui...");
		loginButton.setEnabled(true);
		registerButton.setEnabled(true);
		usernameText.setEnabled(true);
		usernameText.setText("");
		passwordText.setEnabled(true);
		passwordText.setText("");
		logoutButton.setEnabled(false);

		frame.repaint();
	}

	public boolean want_preload()
	{
		String[] options = new String[] {"yes", "no"};
		int x = JOptionPane.showOptionDialog(null, "Do you want to preload all pages?", "Preload", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

		return x == 0;
	}
}
