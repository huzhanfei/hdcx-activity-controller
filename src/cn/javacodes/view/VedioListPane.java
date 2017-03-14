package cn.javacodes.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import cn.javacodes.filter.VedioFileFilter;
import cn.javacodes.launcher.ActivitiesController;
import cn.javacodes.util.CustomJList;
import cn.javacodes.util.JListUtilities;
import cn.javacodes.util.VedioPlayer;
import uk.co.caprica.vlcj.player.MediaPlayer;

@SuppressWarnings("serial")
public class VedioListPane extends JPanel {
	
	
	private CustomJList<String> vedioList;
	
	private JScrollPane videoListScrollPane;
	
	private JPanel vedioListBtnPane;
	
	private JButton addVedioBtn;
	
	private JButton playVedioBtn;
	
	private JButton moveUpVedioBtn;
	
	private JButton moveDownVedioBtn;
	
	private JListUtilities<String, File> jListUtil;
	
	private int currentVedioIndex;
	
	
	private MediaPlayer vedioPlayer = VedioPlayer.getMediaPlayerComponent().getMediaPlayer();
		
	public JListUtilities<String, File> getjListUtil() {
		return jListUtil;
	}
	
	
	public VedioListPane() {
		
	
		
		// 设置布局
		this.setBorder(new TitledBorder(null, "\u89C6\u9891", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.setLayout(new BorderLayout(0, 0));
		
		
		// 创建JList列表对象
		vedioList = new CustomJList<>();
		videoListScrollPane = new JScrollPane(vedioList);
		videoListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		videoListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(videoListScrollPane, BorderLayout.CENTER);
		
		jListUtil = new JListUtilities<>(vedioList);
		vedioList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				// 添加鼠标右键呼出菜单的事件监听
				CustomJList.addMouseRightBtnClickedMenu(jListUtil, e);
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					if (!vedioList.isSelectionEmpty()) {
						if (ActivitiesController.getMainFrame().getState() == 0) {
							ActivitiesController.getMainFrame().getExpansionWindow().setVisible(false);
						} else if (ActivitiesController.getMainFrame().getState() == 1){
							ActivitiesController.getMainFrame().getExpansionWindow().setVisible(true);
						}
						currentVedioIndex = vedioList.getSelectedIndex();
						vedioPlayer.playMedia(jListUtil.get(vedioList.getSelectedIndex()).getAbsolutePath());
					}
					
				}
				
			}
			
		});
		
		
		vedioList.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (!vedioList.isSelectionEmpty() && vedioList.hasFocus()) {	
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						if (vedioList.getSelectedIndex() > 0) {
							vedioList.setSelectedIndex(vedioList.getSelectedIndex() - 1);
						}
						break;
					case KeyEvent.VK_DOWN:
						if (vedioList.getSelectedIndex() < jListUtil.getSize() - 1) {
							vedioList.setSelectedIndex(vedioList.getSelectedIndex() + 1);
						}
						break;
					default:
						break;
					}	
				}
			}
			
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (!vedioList.isSelectionEmpty() && vedioList.hasFocus()) {	
					switch (e.getKeyCode()) {
					case KeyEvent.VK_DELETE:
						int index = vedioList.getSelectedIndex();
						jListUtil.remove(index);
						if (jListUtil.getSize() > 0) {
							vedioList.setSelectedIndex(index);
						}
						break;
					case KeyEvent.VK_ENTER:
						if (ActivitiesController.getMainFrame().getState() == 0) {
							ActivitiesController.getMainFrame().getExpansionWindow().setVisible(false);
						} else if (ActivitiesController.getMainFrame().getState() == 1){
							ActivitiesController.getMainFrame().getExpansionWindow().setVisible(true);
						}
						currentVedioIndex = vedioList.getSelectedIndex();
						vedioPlayer.playMedia(jListUtil.get(vedioList.getSelectedIndex()).getAbsolutePath());
						break;
					default:
						break;
					}	
				}
			}
		});

		
		// 创建按钮面板
		vedioListBtnPane = new JPanel();
		this.add(vedioListBtnPane, BorderLayout.SOUTH);
		vedioListBtnPane.setLayout(new GridLayout(0, 2, 0, 0));

		
		// 添加视频按钮
		addVedioBtn = new JButton("\u6DFB\u52A0");
		addVedioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(true);
				VedioFileFilter vedioFileFilter = new VedioFileFilter();
				jfc.addChoosableFileFilter(vedioFileFilter);
				jfc.setFileFilter(vedioFileFilter);
				
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
		vedioListBtnPane.add(addVedioBtn);

		
		// 播放视频按钮
		playVedioBtn = new JButton("\u64AD\u653E");
		playVedioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!vedioList.isSelectionEmpty()) {
					currentVedioIndex = vedioList.getSelectedIndex();
					vedioPlayer.playMedia(jListUtil.get(vedioList.getSelectedIndex()).getAbsolutePath());
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		vedioListBtnPane.add(playVedioBtn);

		
		// 视频列表项目上移按钮
		moveUpVedioBtn = new JButton("\u4E0A\u79FB");
		moveUpVedioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jListUtil.moveUp(vedioList.getSelectedIndex());
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		vedioListBtnPane.add(moveUpVedioBtn);

		
		// 视频列表项目下移按钮
		moveDownVedioBtn = new JButton("\u4E0B\u79FB");
		moveDownVedioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jListUtil.moveDown(vedioList.getSelectedIndex());
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		vedioListBtnPane.add(moveDownVedioBtn);
	}


	public int getCurrentVedioIndex() {
		return currentVedioIndex;
	}


	public void setCurrentVedioIndex(int currentVedioIndex) {
		this.currentVedioIndex = currentVedioIndex;
	}


	public CustomJList<String> getVedioList() {
		return vedioList;
	}


	
	
	

}
