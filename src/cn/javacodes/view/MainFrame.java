package cn.javacodes.view;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JWindow;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cn.javacodes.filter.ImgFileFilter;
import cn.javacodes.launcher.ActivitiesController;
import cn.javacodes.util.AudioPlayer;
import cn.javacodes.util.JListUtilities;
import cn.javacodes.util.MediaTimeUtil;
import cn.javacodes.util.VedioPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

/**
 * @author 胡湛霏
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel mainPane;

	private Container contentPane;

	private EmbeddedMediaPlayer vedioPlayer = VedioPlayer.getMediaPlayerComponent().getMediaPlayer();

	private AudioListPane audioListPane;

	private JListUtilities<String, File> vedioListUtil;

	private MediaTimeUtil vedioTimeUtil;

	// 当前状态,0代表主屏显示,1代表扩展屏显示
	private int state = 0;

	private JWindow expansionWindow;

	private File imgFile;

	private JPanel expScreenMonitorPane;

	private VedioListPane vedioListPane;

	private Robot robot;

	private BufferedImage expImg;

	private Rectangle rect;

	private int fpx = 25;

	private AudioPlayerCtrlPane audioPlayerCtrlPane;

	private FileListPane fileListPane;

	private JButton soundOffBtn;

	private JButton vedioStopBtn;

	private JButton vedioNextBtn;

	private JButton vedioPlayBtn;

	private JButton vedioPreBtn;

	private JPanel vedioPlayerCtrlBtnPane;

	private JLabel vedioVolumeLabel;

	private JLabel vedioVolumeCnlabel;

	private JSlider vedioPlayerVolumeSlider;

	private JPanel vedioPlayerVolumeSliderPane;

	private JPanel changeScreenBtnPane;

	private JPanel fpxSliderPane;

	private JWindow expansionScreenWallpaperWindow;

	private GraphicsDevice[] gds;

	private JButton changeScreenBtn;

	private JButton expansionScreenWallpaperBtn;

	private JLabel lblFpx;

	private JSlider fpxSlider;

	private Container expansionWindowContentPane;

	private JLabel vedioProgressBarRightLabel;

	private JLabel vedioProgressBarLeftLabel;

	private JProgressBar vedioProgressBar;

	private JPanel quickBtnPane;

	private JPanel audioPane;

	private JPanel rightInnerPane;

	private JPanel leftInnerPane;

	private JPanel rightOutterPane;

	private JPanel expansionScreenCtrlBtnPane;

	private JPanel vedioPlayerBtnPane;

	private JPanel vedioProgressBarPane;

	private JPanel vedioCtrlPane;

	private JPanel vedioPane;

	private JPanel vedioPlayerPane;

	/**
	 * 设置界面样式
	 */
	{
		changeSkin(UIManager.getSystemLookAndFeelClassName());
	}

	private void changeSkin(String className) {
		// Metal：javax.swing.plaf.metal.MetalLookAndFeel
		// Nimbus：javax.swing.plaf.nimbus.NimbusLookAndFeel
		// CDE/Motif：com.sun.java.swing.plaf.motif.MotifLookAndFeel
		// Windows：com.sun.java.swing.plaf.windows.WindowsLookAndFeel
		// Windows
		// Classic：com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel
		try {
			// 设置UI观感
			UIManager.setLookAndFeel(className);
			// 更新组件树UI观感
			SwingUtilities.updateComponentTreeUI(this);

			Enumeration<Object> keys = UIManager.getDefaults().keys();
			Object key = null;
			Object value = null;
			while (keys.hasMoreElements()) {
				key = keys.nextElement();
				value = UIManager.get(key);
				/** 设置全局的字体 */
				if (value instanceof Font) {
					UIManager.put(key, new Font(Font.DIALOG, Font.PLAIN, 12));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建主控制窗口
	 */
	public MainFrame() {
		// 设置默认状态为主屏状态
		state = 0;
		// 设置默认音量为80%
		vedioPlayer.setVolume(80);
		// 设置默认窗口关闭事件
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置默认位置及尺寸
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 1366;
		int height = 700;
		setBounds(((int) screenSize.getWidth() - width) >> 1, ((int) screenSize.getHeight() - height) >> 1, width,
				height);
		// 设置窗口最小尺寸
		setMinimumSize(new Dimension(1366, 700));
		// 设置标题
		setTitle(ActivitiesController.TITLE);
		// 初始化窗口
		initialize();

	}

	/**
	 * 初始化窗口
	 */
	private void initialize() {
		// 创建面板并布局
		createPane();
		// 创建组件
		creatComponent();
		// 扫描本地显示设备并根据显示设备设置组件状态
		scanGraphicsDevices();
		// 为窗口及组件添加事件监听
		addEventListener();
	}

	/**
	 * 添加事件监听
	 */
	private void addEventListener() {
		// 为视频进度条添加事件监听
		addVedioProgressBarEventListener();
		// 为扩展屏监视画面帧数滑动条添加事件监听
		addFpxSliderEventListener();
		// 为切换屏幕按钮添加事件监听
		addChangeScreenBtnEvent();
		// 为视频播放器控制按钮及音量控制添加事件监听
		addVedioPlayerCtrlBtnsEventListener();
		// 为静音按钮添加事件监听
		addSoundOffBtnEventListener();
		// 为扩展屏墙纸按钮添加事件监听
		addExpScreenWallpaperBtnEventListener();
		// 为程序添加键盘事件监听
		addKeyBoardListenerEvent();
	}

	/**
	 * 创建组件
	 */
	private void creatComponent() {
		// 视频进度条
		vedioProgressBar = new JProgressBar();
		vedioProgressBarPane.add(vedioProgressBar, BorderLayout.CENTER);

		// 视频进度条左侧标签(当前时间)
		vedioProgressBarLeftLabel = new JLabel("00:00");
		vedioProgressBarPane.add(vedioProgressBarLeftLabel, BorderLayout.WEST);

		// 视频进度条右侧标签(总时间)
		vedioProgressBarRightLabel = new JLabel("00:00");
		vedioProgressBarPane.add(vedioProgressBarRightLabel, BorderLayout.EAST);

		// 扩展屏窗口
		expansionWindow = new JWindow();
		expansionWindow.setVisible(false);
		expansionWindowContentPane = expansionWindow.getContentPane();
		expansionWindowContentPane.setLayout(new BorderLayout(0, 0));

		// 扩展屏监视画面帧数滑动条
		fpxSlider = new JSlider();
		fpxSlider.setMinimum(5);
		fpxSlider.setMaximum(30);
		fpxSlider.setValue(25);
		fpxSlider.setPreferredSize(new Dimension(60, 20));
		lblFpx = new JLabel("25fpx");
		fpxSliderPane.add(fpxSlider);
		fpxSliderPane.add(lblFpx);

		// 扩展屏墙纸按钮
		expansionScreenWallpaperBtn = new JButton("\u6269\u5C55\u5C4F\u5899\u7EB8");
		quickBtnPane.add(expansionScreenWallpaperBtn);
		changeScreenBtn = new JButton("\u5207\u6362\u4E3B\u5C4F\u5E55/\u6269\u5C55\u5C4F");

		// 扩展屏墙纸窗口
		expansionScreenWallpaperWindow = new JWindow();

		// 切换屏幕按钮
		changeScreenBtnPane = new JPanel();
		expansionScreenCtrlBtnPane.add(changeScreenBtnPane, BorderLayout.CENTER);
		changeScreenBtnPane.add(changeScreenBtn);

		// 视频播放器音量滑动条
		vedioPlayerVolumeSlider = new JSlider();
		vedioPlayerVolumeSlider.setPreferredSize(new Dimension(150, 20));
		vedioPlayerVolumeSliderPane.add(vedioPlayerVolumeSlider);
		vedioPlayerVolumeSlider.setValue(80);

		// 视频播放器音量文字标签
		vedioVolumeCnlabel = new JLabel("\u97F3\u91CF");
		vedioPlayerVolumeSliderPane.add(vedioVolumeCnlabel);

		// 视频播放器音量大小标签
		vedioVolumeLabel = new JLabel("80 %");
		vedioVolumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		vedioVolumeLabel.setPreferredSize(new Dimension(40, 20));
		vedioPlayerVolumeSliderPane.add(vedioVolumeLabel);

		// 视频上一曲按钮
		vedioPreBtn = new JButton("\u300A");
		vedioPlayerCtrlBtnPane.add(vedioPreBtn);
		// 视频播放/暂停按钮
		vedioPlayBtn = new JButton("\u25B6 / \u2016");
		vedioPlayerCtrlBtnPane.add(vedioPlayBtn);
		// 视频下一曲按钮
		vedioNextBtn = new JButton("\u300B");
		vedioPlayerCtrlBtnPane.add(vedioNextBtn);
		// 视频停止按钮
		vedioStopBtn = new JButton("\u25A0");
		vedioPlayerCtrlBtnPane.add(vedioStopBtn);

		// 静音按钮
		soundOffBtn = new JButton("\u9759\u97F3");
		quickBtnPane.add(soundOffBtn);
	}

	/**
	 * 静音按钮事件监听
	 */
	private void addSoundOffBtnEventListener() {
		soundOffBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vedioPlayer.isMute() || AudioPlayer.getMediaPlayerComponent().getMediaPlayer().isMute()) {
					soundOffBtn.setText("静音");
				} else {
					soundOffBtn.setText("恢复");
				}

				vedioPlayer.mute();
				AudioPlayer.getMediaPlayerComponent().getMediaPlayer().mute();
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
	}

	/**
	 * 扩展屏墙纸按钮事件监听
	 */
	private void addExpScreenWallpaperBtnEventListener() {
		// 为扩展屏墙纸按钮添加事件监听
		expansionScreenWallpaperBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gds.length > 1) {
					if (expansionScreenWallpaperWindow.isVisible()) {
						expansionScreenWallpaperWindow.setVisible(false);
					} else {
						if (imgFile == null) {
							JFileChooser jfc = new JFileChooser("选择背景图片");
							jfc.setMultiSelectionEnabled(false);
							ImgFileFilter imgFileFilter = new ImgFileFilter();
							jfc.addChoosableFileFilter(imgFileFilter);
							jfc.setFileFilter(imgFileFilter);
							try {
								UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
								SwingUtilities.updateComponentTreeUI(jfc);
							} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
									| UnsupportedLookAndFeelException e1) {
								e1.printStackTrace();
							}
							jfc.showOpenDialog(MainFrame.this);
							if (jfc.getSelectedFile() != null) {
								imgFile = jfc.getSelectedFile();
							} else {
								return;
							}
						}
						int x = gds[0].getDisplayMode().getWidth();
						int width = gds[1].getDisplayMode().getWidth();
						int height = gds[1].getDisplayMode().getHeight();
						expansionScreenWallpaperWindow.setBounds(x, 0, width, height);
						JPanel expansionScreenWallpaperWindowContentPane = new JPanel() {
							@Override
							public void paintComponent(Graphics g) {
								g.drawImage(new ImageIcon(imgFile.getAbsolutePath()).getImage(), 0, 0, this.getWidth(),
										this.getHeight(), null);
							}
						};
						expansionScreenWallpaperWindow.setContentPane(expansionScreenWallpaperWindowContentPane);
						expansionScreenWallpaperWindow.setVisible(true);
					}
				}

				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}

		});
	}

	/**
	 * 视频播放器控制按钮事件监听
	 */
	private void addVedioPlayerCtrlBtnsEventListener() {
		// 为视频上一曲按钮添加事件监听
		vedioPreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vedioListPane.getCurrentVedioIndex() > 0) {
					vedioPlayer
							.playMedia(vedioListUtil.get(vedioListPane.getCurrentVedioIndex() - 1).getAbsolutePath());
					vedioListPane.setCurrentVedioIndex(vedioListPane.getCurrentVedioIndex() - 1);
					vedioListUtil.getList().setSelectedIndex(vedioListPane.getCurrentVedioIndex());
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});

		// 为视频播放/暂停按钮添加事件监听
		vedioPlayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (vedioPlayer.canPause()) {
					vedioPlayer.pause();
				} else {
					vedioPlayer.play();
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});

		// 为视频下一曲按钮添加事件监听

		vedioNextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vedioListPane.getCurrentVedioIndex() >= 0
						&& vedioListPane.getCurrentVedioIndex() < vedioListUtil.getSize() - 1) {
					vedioPlayer
							.playMedia(vedioListUtil.get(vedioListPane.getCurrentVedioIndex() + 1).getAbsolutePath());
					vedioListPane.setCurrentVedioIndex(vedioListPane.getCurrentVedioIndex() + 1);
					vedioListUtil.getList().setSelectedIndex(vedioListPane.getCurrentVedioIndex());
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});

		// 为视频停止按钮添加事件监听
		vedioStopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				expansionWindow.setVisible(false);
				vedioPlayer.stop();
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});

		// 为视频音量滑动条添加事件监听
		vedioPlayerVolumeSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				vedioPlayer.setVolume(vedioPlayerVolumeSlider.getValue());
				vedioVolumeLabel.setText(vedioPlayerVolumeSlider.getValue() + "%");
			}
		});

		vedioPlayerVolumeSlider.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				vedioPlayerVolumeSlider.setValue((int) (e.getX()
						* ((float) vedioPlayerVolumeSlider.getMaximum() / vedioPlayerVolumeSlider.getWidth())));
			}
		});
	}

	/**
	 * 切换屏幕按钮事件监听
	 */
	private void addChangeScreenBtnEvent() {
		changeScreenBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gds.length > 1) {
					long time = vedioPlayer.getTime();
					if (state == 0) {
						state = 1;
						// vedioPane.remove(VedioPlayer.getMediaPlayerComponent());
						vedioPane.removeAll();
						expansionWindowContentPane.removeAll();
						vedioPane.add(expScreenMonitorPane, BorderLayout.CENTER);
						expansionWindowContentPane.add(VedioPlayer.getMediaPlayerComponent(), BorderLayout.CENTER);
						expansionWindow.repaint();
						MainFrame.this.repaint();
						vedioPlayer.setFullScreen(true);
						vedioPlayer.stop();
						if (time >= 0) {
							expansionWindow.setVisible(true);
							vedioPlayer.play();
							vedioPlayer.setTime(time);
						}
						if (MainFrame.this.getExtendedState() == MAXIMIZED_BOTH) {
							MainFrame.this.setExtendedState(NORMAL);
							MainFrame.this.setExtendedState(MAXIMIZED_BOTH);
						} else if (MainFrame.this.getExtendedState() == NORMAL) {
							MainFrame.this.setExtendedState(MAXIMIZED_BOTH);
							MainFrame.this.setExtendedState(NORMAL);
						}

					} else {
						state = 0;
						expansionWindowContentPane.remove(VedioPlayer.getMediaPlayerComponent());
						// vedioPane.remove(expScreenMonitorPane);
						vedioPane.removeAll();
						expansionWindowContentPane.removeAll();
						vedioPane.add(VedioPlayer.getMediaPlayerComponent(), BorderLayout.CENTER);
						vedioPlayer.setFullScreen(false);
						expansionWindow.setVisible(false);
						MainFrame.this.repaint();
						if (MainFrame.this.getExtendedState() == MAXIMIZED_BOTH) {
							MainFrame.this.setExtendedState(NORMAL);
							MainFrame.this.setExtendedState(MAXIMIZED_BOTH);
						} else if (MainFrame.this.getExtendedState() == NORMAL) {
							MainFrame.this.setExtendedState(MAXIMIZED_BOTH);
							MainFrame.this.setExtendedState(NORMAL);
						}
						vedioPlayer.stop();
						if (time >= 0) {
							vedioPlayer.play();
							vedioPlayer.setTime(time);
						}

					}
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
	}

	/**
	 * 扫描显示设备并设置切换屏幕按钮,扩展屏监视画面帧数滑动条及扩展屏墙纸按钮的组件状态
	 */
	private void scanGraphicsDevices() {
		// 获取当前系统显示设备
		gds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

		// 如果检测到扩展屏, 则设置切换屏幕及扩展屏墙纸按钮为可用
		if (gds.length > 1) {
			changeScreenBtn.setEnabled(true);
			expansionScreenWallpaperBtn.setEnabled(true);
			gds[1].setFullScreenWindow(expansionWindow);
			expansionWindow.setVisible(false);
			fpxSlider.setEnabled(true);
		} else {
			changeScreenBtn.setEnabled(false);
			expansionScreenWallpaperBtn.setEnabled(false);
			fpxSlider.setEnabled(false);
		}

		// 如果检测到扩展屏, 则启动扩展屏监视线程
		if (gds.length > 1) {
			expScreenMonitorPane = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					g.drawImage(expImg, 0, 0, this.getWidth(), this.getHeight(), null);
					g.setColor(Color.RED);
					g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
					g.drawString("扩展屏监控画面", 15, 40);
				}

			};
			rect = new Rectangle(gds[0].getDisplayMode().getWidth(), 0, gds[1].getDisplayMode().getWidth(),
					gds[1].getDisplayMode().getHeight());

			try {
				robot = new Robot();
			} catch (AWTException e1) {
				e1.printStackTrace();
			}

			// 实时监控扩展屏线程
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							expImg = robot.createScreenCapture(rect);
							expScreenMonitorPane.repaint();
							Thread.sleep(1000 / fpx);

							// 如果非扩展屏状态则暂停监视
							while (state == 0) {
								Thread.sleep(200);
							}
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}).start();

		}
	}

	/**
	 * 扩展屏帧数设置滑动条事件监听
	 */
	private void addFpxSliderEventListener() {
		// 扩展屏帧数设置滑动条状态改变事件
		fpxSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				fpx = fpxSlider.getValue();
				lblFpx.setText(fpxSlider.getValue() + "fpx");
			}
		});

		// 扩展屏帧数设置滑动条鼠标点击事件
		fpxSlider.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				fpxSlider.setValue((int) (e.getX() * ((float) fpxSlider.getMaximum() / fpxSlider.getWidth())));
			}
		});
	}

	/**
	 * 视频进度条事件监听
	 */
	private void addVedioProgressBarEventListener() {

		// 鼠标点击事件
		vedioProgressBar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				vedioPlayer.setTime((long) (((float) x / vedioProgressBar.getWidth()) * vedioPlayer.getLength()));
			}

		});

		// 鼠标拖拽事件
		vedioProgressBar.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				vedioPlayer.setTime((long) (((float) x / vedioProgressBar.getWidth()) * vedioPlayer.getLength()));

			}
		});

		// 创建线程实时检测并修改进度条
		vedioTimeUtil = new MediaTimeUtil();
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					while (true) {
						long totalTime = vedioPlayer.getLength();
						long currentTime = vedioPlayer.getTime();
						if (totalTime <= 0) {
							vedioProgressBar.setValue(0);
							vedioProgressBarLeftLabel.setText("00:00");
							vedioProgressBarRightLabel.setText("00:00");
						} else {
							vedioTimeUtil.timeCalculator(currentTime, totalTime);
							vedioProgressBarLeftLabel
									.setText(vedioTimeUtil.getCurrentMin() + ":" + vedioTimeUtil.getCurrentSec());
							vedioProgressBarRightLabel
									.setText(vedioTimeUtil.getTotalMin() + ":" + vedioTimeUtil.getTotalSec());
							double percent = (double) currentTime / totalTime;
							vedioProgressBar.setValue((int) (percent * 100));
						}
						Thread.sleep(200);
					}
				} catch (Exception e) {

				}

			}

		}).start();

	}

	/**
	 * 创建面板
	 */
	private void createPane() {

		contentPane = getContentPane();
		// 创建主窗口主面板
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(mainPane);
		mainPane.setLayout(new BorderLayout(0, 0));
		// 创建视频播放器主面板
		vedioPlayerPane = new JPanel();
		vedioPlayerPane.setBorder(new TitledBorder(null, "\u89C6\u9891\u64AD\u653E\u5668", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		mainPane.add(vedioPlayerPane, BorderLayout.CENTER);
		vedioPlayerPane.setLayout(new BorderLayout(0, 0));

		// 创建视频播放器面板
		vedioPane = new JPanel();
		vedioPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		vedioPlayerPane.add(vedioPane, BorderLayout.CENTER);
		vedioPane.setLayout(new BorderLayout(0, 0));
		vedioPane.add(VedioPlayer.getMediaPlayerComponent(), BorderLayout.CENTER);

		// 创建视频控制面板
		vedioCtrlPane = new JPanel();
		vedioPlayerPane.add(vedioCtrlPane, BorderLayout.SOUTH);
		vedioCtrlPane.setLayout(new BorderLayout(0, 0));

		// 创建视频播放器进度条面板
		vedioProgressBarPane = new JPanel();
		vedioCtrlPane.add(vedioProgressBarPane, BorderLayout.NORTH);
		vedioProgressBarPane.setLayout(new BorderLayout(0, 0));

		// 创建视频播放器按钮面板
		vedioPlayerBtnPane = new JPanel();
		vedioCtrlPane.add(vedioPlayerBtnPane, BorderLayout.SOUTH);
		vedioPlayerBtnPane.setLayout(new BorderLayout(0, 0));

		// 创建扩展屏控制按钮面板
		expansionScreenCtrlBtnPane = new JPanel();
		expansionScreenCtrlBtnPane.setLayout(new BorderLayout(0, 0));
		vedioPlayerBtnPane.add(expansionScreenCtrlBtnPane, BorderLayout.WEST);

		// 扩展屏监视画面帧数设置面板
		fpxSliderPane = new JPanel();
		expansionScreenCtrlBtnPane.add(fpxSliderPane, BorderLayout.EAST);

		// 右侧外层面板(纯布局使用)
		rightOutterPane = new JPanel();
		mainPane.add(rightOutterPane, BorderLayout.EAST);
		rightOutterPane.setLayout(new GridLayout(0, 2, 0, 0));

		// 右侧内层左面板(布局使用)
		leftInnerPane = new JPanel();
		rightOutterPane.add(leftInnerPane);
		leftInnerPane.setLayout(new GridLayout(2, 1, 0, 0));

		// 视频列表面板
		vedioListPane = new VedioListPane();
		leftInnerPane.add(vedioListPane);
		// 获取用于操作视频列表的JListUtilities对象(自定义)
		vedioListUtil = vedioListPane.getjListUtil();

		// 文件列表面板
		fileListPane = new FileListPane();
		leftInnerPane.add(fileListPane);

		// 右侧内层右面板
		rightInnerPane = new JPanel();
		rightOutterPane.add(rightInnerPane);
		rightInnerPane.setLayout(new BorderLayout(0, 0));

		// 音频面板
		audioPane = new JPanel();
		rightInnerPane.add(audioPane, BorderLayout.CENTER);
		audioPane.setLayout(new BorderLayout(0, 0));

		// 音频控制面板
		audioPlayerCtrlPane = new AudioPlayerCtrlPane();
		audioPane.add(audioPlayerCtrlPane, BorderLayout.SOUTH);

		// 音频列表面板
		audioListPane = new AudioListPane();
		audioPane.add(audioListPane, BorderLayout.CENTER);

		// 快速按钮面板
		quickBtnPane = new JPanel();
		rightInnerPane.add(quickBtnPane, BorderLayout.SOUTH);
		quickBtnPane.setLayout(new GridLayout(0, 2, 0, 0));

		// 视频播放器音量控制面板
		vedioPlayerVolumeSliderPane = new JPanel();
		vedioPlayerBtnPane.add(vedioPlayerVolumeSliderPane, BorderLayout.EAST);
		vedioPlayerVolumeSliderPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// 视频播放器控制按钮面板
		vedioPlayerCtrlBtnPane = new JPanel();
		vedioPlayerBtnPane.add(vedioPlayerCtrlBtnPane, BorderLayout.CENTER);

	}

	/**
	 * 添加键盘事件监听
	 */
	private void addKeyBoardListenerEvent() {
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

			@Override
			public void eventDispatched(AWTEvent event) {
				if (((KeyEvent) event).getID() == KeyEvent.KEY_PRESSED) {
					switch (((KeyEvent) event).getKeyCode()) {
					// 右方向键视频快进
					case KeyEvent.VK_RIGHT:
						vedioPlayer.skip(10000);
						break;

					// 左方向键视频快退
					case KeyEvent.VK_LEFT:
						vedioPlayer.skip(-10000);
						break;

					// 空格键暂停或播放视频
					case KeyEvent.VK_SPACE:
						if (vedioPlayer.isPlaying()) {
							vedioPlayer.pause();
						} else {
							vedioPlayer.play();
						}
						break;

					case KeyEvent.VK_UP:
						if (!(audioListPane.getAudioList().hasFocus() || fileListPane.getFileList().hasFocus()
								|| vedioListPane.getVedioList().hasFocus())) {
							vedioPlayerVolumeSlider.setValue(vedioPlayerVolumeSlider.getValue() + 5);
						}
						break;
					case KeyEvent.VK_DOWN:
						if (!(audioListPane.getAudioList().hasFocus() || fileListPane.getFileList().hasFocus()
								|| vedioListPane.getVedioList().hasFocus())) {
							vedioPlayerVolumeSlider.setValue(vedioPlayerVolumeSlider.getValue() - 5);
						}
						break;
					default:
						break;
					}
				}

			}
		}, AWTEvent.KEY_EVENT_MASK);
	}

	public synchronized int getState() {
		return state;
	}

	public synchronized JWindow getExpansionWindow() {
		return expansionWindow;
	}

	public VedioListPane getVedioListPane() {
		return vedioListPane;
	}

	public JListUtilities<String, File> getVedioListUtil() {
		return vedioListUtil;
	}

	public AudioPlayerCtrlPane getAudioPlayerCtrlPane() {
		return audioPlayerCtrlPane;
	}

	public AudioListPane getAudioListPane() {
		return audioListPane;
	}

}
