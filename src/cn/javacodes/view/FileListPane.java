package cn.javacodes.view;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import cn.javacodes.launcher.ActivitiesController;
import cn.javacodes.util.CustomJList;
import cn.javacodes.util.JListUtilities;
import java.awt.Color;

@SuppressWarnings("serial")
public class FileListPane extends JPanel {
	
	
	private CustomJList<String> fileList;

	private JScrollPane fileListScrollPane;

	private JPanel fileListBtnPane;

	private JButton addfileBtn;

	private JButton openFileBtn;

	private JButton moveUpFileBtn;

	private JButton moveDownFileBtn;

	private JListUtilities<String, File> jListUtil;


	public JListUtilities<String, File> getjListUtil() {
		return jListUtil;
	}

	public FileListPane() {

		// 设置布局
		this.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u6587\u4EF6\u5F52\u6863", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		this.setLayout(new BorderLayout(0, 0));
		
		
		// 创建JList列表对象
		fileList = new CustomJList<>();
		fileListScrollPane = new JScrollPane(fileList);
		fileListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		fileListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(fileListScrollPane, BorderLayout.CENTER);
		
		jListUtil = new JListUtilities<>(fileList);
		fileList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				// 添加鼠标右键呼出菜单的事件监听
				CustomJList.addMouseRightBtnClickedMenu(jListUtil, e);
				// 双击事件
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					if (!fileList.isSelectionEmpty()) {
						try {
							if (jListUtil.get(fileList.getSelectedIndex()).exists()) {
								Desktop.getDesktop().open(jListUtil.get(fileList.getSelectedIndex()));
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
				}		
			}	
		});
		
		fileList.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (!fileList.isSelectionEmpty() && fileList.hasFocus()) {	
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						if (fileList.getSelectedIndex() > 0) {
							fileList.setSelectedIndex(fileList.getSelectedIndex() - 1);
						}
						break;
					case KeyEvent.VK_DOWN:
						if (fileList.getSelectedIndex() < jListUtil.getSize() - 1) {
							fileList.setSelectedIndex(fileList.getSelectedIndex() + 1);
						}
						break;
					default:
						break;
					}	
				}
			}
			
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (!fileList.isSelectionEmpty() && fileList.hasFocus()) {	
					switch (e.getKeyCode()) {
					case KeyEvent.VK_DELETE:
						int index = fileList.getSelectedIndex();
						jListUtil.remove(index);
						if (jListUtil.getSize() > 0) {
							fileList.setSelectedIndex(index);
						}
						break;
					case KeyEvent.VK_ENTER:
						if (jListUtil.get(fileList.getSelectedIndex()).exists()) {
							try {
								Desktop.getDesktop().open(jListUtil.get(fileList.getSelectedIndex()));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						break;
					default:
						break;
					}	
				}
			}
		});

		
		// 创建按钮面板
		fileListBtnPane = new JPanel();
		this.add(fileListBtnPane, BorderLayout.SOUTH);
		fileListBtnPane.setLayout(new GridLayout(0, 2, 0, 0));

		
		// 添加文件按钮
		addfileBtn = new JButton("\u6DFB\u52A0");
		addfileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(true);
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					SwingUtilities.updateComponentTreeUI(jfc);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}	
				jfc.showOpenDialog(null);
				File[] files = jfc.getSelectedFiles();
				for (File file : files) {
					jListUtil.add(file.getName(), file);
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		fileListBtnPane.add(addfileBtn);

		
		// 打开文件按钮
		openFileBtn = new JButton("\u6253\u5F00");
		openFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!fileList.isSelectionEmpty()) {
					try {
						if (jListUtil.get(fileList.getSelectedIndex()).exists()) {
							Desktop.getDesktop().open(jListUtil.get(fileList.getSelectedIndex()));
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		fileListBtnPane.add(openFileBtn);

		
		// 列表项目上移按钮
		moveUpFileBtn = new JButton("\u4E0A\u79FB");
		moveUpFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jListUtil.moveUp(fileList.getSelectedIndex());
				
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
			
			
		});
		fileListBtnPane.add(moveUpFileBtn);

		
		// 列表项目下移按钮
		moveDownFileBtn = new JButton("\u4E0B\u79FB");
		moveDownFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jListUtil.moveDown(fileList.getSelectedIndex());
				
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		fileListBtnPane.add(moveDownFileBtn);
	}

	public CustomJList<String> getFileList() {
		return fileList;
	}


}

