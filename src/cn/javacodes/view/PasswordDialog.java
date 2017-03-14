package cn.javacodes.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.javacodes.launcher.ActivitiesController;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class PasswordDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordField;

	/**
	 * Create the dialog.
	 */
	public PasswordDialog() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setTitle("\u9ED1\u5927\u521B\u534F\u6D3B\u52A8\u7535\u5B50\u573A\u63A7\u7CFB\u7EDF");
		// …Ë÷√ƒ¨»œŒª÷√º∞≥ﬂ¥Á
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 316;
		int height = 128;
		setBounds(((int) screenSize.getWidth() - width) >> 1, (((int) screenSize.getHeight() - height) >> 1) - 32,
				width, height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JLabel passLabel = new JLabel("\u8BF7\u8F93\u5165\u5BC6\u7801\uFF1A");
		contentPanel.add(passLabel);

		passwordField = new JPasswordField();
		passwordField.setColumns(25);
		contentPanel.add(passwordField);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new BorderLayout(0, 0));

		JPanel btnPanel = new JPanel();
		buttonPane.add(btnPanel, BorderLayout.EAST);

		JButton okButton = new JButton("\u786E\u5B9A");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String passInput = new String(passwordField.getPassword());
				if (passInput.equals("hdcxkjsjb")) {
					MainFrame mainFrame = ActivitiesController.getMainFrame();
					mainFrame.setVisible(true);
					mainFrame.getContentPane().requestFocus();
					PasswordDialog.this.setVisible(false);
					PasswordDialog.this.dispose();
				} else {
					JOptionPane.showMessageDialog(PasswordDialog.this, "√‹¬Î¥ÌŒÛ£¨«Î÷ÿ–¬ ‰»Î!", "√‹¬Î¥ÌŒÛ",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		
		btnPanel.add(okButton);
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("\u5173\u95ED");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnPanel.add(cancelButton);
		cancelButton.setActionCommand("Cancel");

		JPanel versonPanel = new JPanel();
		FlowLayout fl_versonPanel = (FlowLayout) versonPanel.getLayout();
		fl_versonPanel.setVgap(10);
		fl_versonPanel.setHgap(10);
		fl_versonPanel.setAlignment(FlowLayout.LEFT);
		buttonPane.add(versonPanel);

		JLabel verLabel = new JLabel("Verson 1.0");
		versonPanel.add(verLabel);

	}
}
