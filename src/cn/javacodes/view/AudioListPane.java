package cn.javacodes.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import cn.javacodes.filter.AudioFileFilter;
import cn.javacodes.launcher.ActivitiesController;
import cn.javacodes.util.AudioPlayer;
import cn.javacodes.util.CustomJList;
import cn.javacodes.util.JListUtilities;
import uk.co.caprica.vlcj.player.MediaPlayer;

@SuppressWarnings("serial")
public class AudioListPane extends JPanel {

	private CustomJList<String> audioList;

	private JScrollPane aideoListScrollPane;

	private JPanel audioListBtnPane;

	private JButton addAudioBtn;

	private JButton playAudioBtn;

	private JButton moveUpAudioBtn;

	private JButton moveDownAudioBtn;

	private JButton fadeInPlayBtn;

	private JButton fadeOutPlayBtn;

	private JListUtilities<String, File> jListUtil;

	private int currentAudioIndex;

	private boolean fadeIn = false;

	private MediaPlayer audioPlayer = AudioPlayer.getMediaPlayerComponent().getMediaPlayer();

	public JListUtilities<String, File> getjListUtil() {
		return jListUtil;
	}

	public AudioListPane() {

		// 设置布局
		this.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u97F3\u9891",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		this.setLayout(new BorderLayout(0, 0));

		// 创建JList列表对象
		audioList = new CustomJList<>();
		aideoListScrollPane = new JScrollPane(audioList);
		aideoListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		aideoListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(aideoListScrollPane, BorderLayout.CENTER);

		jListUtil = new JListUtilities<>(audioList);
		audioList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 添加鼠标右键呼出菜单的事件监听
				CustomJList.addMouseRightBtnClickedMenu(jListUtil, e);
				
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					if (!audioList.isSelectionEmpty()) {
						currentAudioIndex = audioList.getSelectedIndex();
						audioPlayer.playMedia(jListUtil.get(audioList.getSelectedIndex()).getAbsolutePath());
					}
				}

			}

		});
		
		audioList.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (!audioList.isSelectionEmpty() && audioList.hasFocus()) {	
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						if (audioList.getSelectedIndex() > 0) {
							audioList.setSelectedIndex(audioList.getSelectedIndex() - 1);
						}
						break;
					case KeyEvent.VK_DOWN:
						if (audioList.getSelectedIndex() < jListUtil.getSize() - 1) {
							audioList.setSelectedIndex(audioList.getSelectedIndex() + 1);
						}
						break;
					default:
						break;
					}	
				}
			}
			
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (!audioList.isSelectionEmpty() && audioList.hasFocus()) {	
					switch (e.getKeyCode()) {
					case KeyEvent.VK_DELETE:
						int index = audioList.getSelectedIndex();
						jListUtil.remove(index);
						if (jListUtil.getSize() > 0) {
							audioList.setSelectedIndex(index);
						}
						break;
					case KeyEvent.VK_ENTER:
						currentAudioIndex = audioList.getSelectedIndex();
						audioPlayer.playMedia(jListUtil.get(audioList.getSelectedIndex()).getAbsolutePath());
						break;
					default:
						break;
					}	
				}
			}
		});
		

		// 创建按钮面板
		audioListBtnPane = new JPanel();
		this.add(audioListBtnPane, BorderLayout.SOUTH);
		audioListBtnPane.setLayout(new GridLayout(0, 2, 0, 0));

		// 添加按钮
		addAudioBtn = new JButton("\u6DFB\u52A0");
		addAudioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(true);
				 AudioFileFilter audioFileFilter = new AudioFileFilter();
				 jfc.addChoosableFileFilter(audioFileFilter);
				 jfc.setFileFilter(audioFileFilter);

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
		audioListBtnPane.add(addAudioBtn);

		// 播放按钮
		playAudioBtn = new JButton("\u64AD\u653E");
		playAudioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!audioList.isSelectionEmpty()) {
					currentAudioIndex = audioList.getSelectedIndex();
					audioPlayer.playMedia(jListUtil.get(audioList.getSelectedIndex()).getAbsolutePath());
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		audioListBtnPane.add(playAudioBtn);

		fadeInPlayBtn = new JButton("\u6E10\u5F3A\u64AD\u653E");
		fadeInPlayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!audioList.isSelectionEmpty()) {
					currentAudioIndex = audioList.getSelectedIndex();
					JSlider volumeSlider = ActivitiesController.getMainFrame().getAudioPlayerCtrlPane()
							.getAudioPlayerVolumeSlider();
					volumeSlider.setValue(20);
					audioPlayer.playMedia(jListUtil.get(audioList.getSelectedIndex()).getAbsolutePath());
					new Thread(new Runnable() {
						@Override
						public void run() {
							fadeIn = true;
							try {
								while (volumeSlider.getValue() < 80 && fadeIn) {
									volumeSlider.setValue(volumeSlider.getValue() + 1);
									Thread.sleep(100);
								}	
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							fadeIn = false;
						}
					}).start();
					
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		audioListBtnPane.add(fadeInPlayBtn);

		fadeOutPlayBtn = new JButton("\u6E10\u5F31\u505C\u6B62");
		fadeOutPlayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						JSlider volumeSlider = ActivitiesController.getMainFrame().getAudioPlayerCtrlPane()
								.getAudioPlayerVolumeSlider();
						fadeIn = false;
						try {
							while (volumeSlider.getValue() > 0 && !fadeIn) {
								volumeSlider.setValue(volumeSlider.getValue() - 1);
								Thread.sleep(100);
							}
							audioPlayer.stop();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}).start();;
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		audioListBtnPane.add(fadeOutPlayBtn);

		// 列表项目上移按钮
		moveUpAudioBtn = new JButton("\u4E0A\u79FB");
		moveUpAudioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jListUtil.moveUp(audioList.getSelectedIndex());
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});

		audioListBtnPane.add(moveUpAudioBtn);

		// 列表项目下移按钮
		moveDownAudioBtn = new JButton("\u4E0B\u79FB");
		moveDownAudioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jListUtil.moveDown(audioList.getSelectedIndex());
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		audioListBtnPane.add(moveDownAudioBtn);
	}

	public int getCurrentAudioIndex() {
		return currentAudioIndex;
	}

	public void setCurrentAudioIndex(int currentaudioIndex) {
		this.currentAudioIndex = currentaudioIndex;
	}

	public CustomJList<String> getAudioList() {
		return audioList;
	}
	
	

}
