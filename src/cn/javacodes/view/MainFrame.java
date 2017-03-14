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
 * @author ��տ��
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

	// ��ǰ״̬,0����������ʾ,1������չ����ʾ
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
	 * ���ý�����ʽ
	 */
	{
		changeSkin(UIManager.getSystemLookAndFeelClassName());
	}

	private void changeSkin(String className) {
		// Metal��javax.swing.plaf.metal.MetalLookAndFeel
		// Nimbus��javax.swing.plaf.nimbus.NimbusLookAndFeel
		// CDE/Motif��com.sun.java.swing.plaf.motif.MotifLookAndFeel
		// Windows��com.sun.java.swing.plaf.windows.WindowsLookAndFeel
		// Windows
		// Classic��com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel
		try {
			// ����UI�۸�
			UIManager.setLookAndFeel(className);
			// ���������UI�۸�
			SwingUtilities.updateComponentTreeUI(this);

			Enumeration<Object> keys = UIManager.getDefaults().keys();
			Object key = null;
			Object value = null;
			while (keys.hasMoreElements()) {
				key = keys.nextElement();
				value = UIManager.get(key);
				/** ����ȫ�ֵ����� */
				if (value instanceof Font) {
					UIManager.put(key, new Font(Font.DIALOG, Font.PLAIN, 12));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���������ƴ���
	 */
	public MainFrame() {
		// ����Ĭ��״̬Ϊ����״̬
		state = 0;
		// ����Ĭ������Ϊ80%
		vedioPlayer.setVolume(80);
		// ����Ĭ�ϴ��ڹر��¼�
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ����Ĭ��λ�ü��ߴ�
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 1366;
		int height = 700;
		setBounds(((int) screenSize.getWidth() - width) >> 1, ((int) screenSize.getHeight() - height) >> 1, width,
				height);
		// ���ô�����С�ߴ�
		setMinimumSize(new Dimension(1366, 700));
		// ���ñ���
		setTitle(ActivitiesController.TITLE);
		// ��ʼ������
		initialize();

	}

	/**
	 * ��ʼ������
	 */
	private void initialize() {
		// ������岢����
		createPane();
		// �������
		creatComponent();
		// ɨ�豾����ʾ�豸��������ʾ�豸�������״̬
		scanGraphicsDevices();
		// Ϊ���ڼ��������¼�����
		addEventListener();
	}

	/**
	 * ����¼�����
	 */
	private void addEventListener() {
		// Ϊ��Ƶ����������¼�����
		addVedioProgressBarEventListener();
		// Ϊ��չ�����ӻ���֡������������¼�����
		addFpxSliderEventListener();
		// Ϊ�л���Ļ��ť����¼�����
		addChangeScreenBtnEvent();
		// Ϊ��Ƶ���������ư�ť��������������¼�����
		addVedioPlayerCtrlBtnsEventListener();
		// Ϊ������ť����¼�����
		addSoundOffBtnEventListener();
		// Ϊ��չ��ǽֽ��ť����¼�����
		addExpScreenWallpaperBtnEventListener();
		// Ϊ������Ӽ����¼�����
		addKeyBoardListenerEvent();
	}

	/**
	 * �������
	 */
	private void creatComponent() {
		// ��Ƶ������
		vedioProgressBar = new JProgressBar();
		vedioProgressBarPane.add(vedioProgressBar, BorderLayout.CENTER);

		// ��Ƶ����������ǩ(��ǰʱ��)
		vedioProgressBarLeftLabel = new JLabel("00:00");
		vedioProgressBarPane.add(vedioProgressBarLeftLabel, BorderLayout.WEST);

		// ��Ƶ�������Ҳ��ǩ(��ʱ��)
		vedioProgressBarRightLabel = new JLabel("00:00");
		vedioProgressBarPane.add(vedioProgressBarRightLabel, BorderLayout.EAST);

		// ��չ������
		expansionWindow = new JWindow();
		expansionWindow.setVisible(false);
		expansionWindowContentPane = expansionWindow.getContentPane();
		expansionWindowContentPane.setLayout(new BorderLayout(0, 0));

		// ��չ�����ӻ���֡��������
		fpxSlider = new JSlider();
		fpxSlider.setMinimum(5);
		fpxSlider.setMaximum(30);
		fpxSlider.setValue(25);
		fpxSlider.setPreferredSize(new Dimension(60, 20));
		lblFpx = new JLabel("25fpx");
		fpxSliderPane.add(fpxSlider);
		fpxSliderPane.add(lblFpx);

		// ��չ��ǽֽ��ť
		expansionScreenWallpaperBtn = new JButton("\u6269\u5C55\u5C4F\u5899\u7EB8");
		quickBtnPane.add(expansionScreenWallpaperBtn);
		changeScreenBtn = new JButton("\u5207\u6362\u4E3B\u5C4F\u5E55/\u6269\u5C55\u5C4F");

		// ��չ��ǽֽ����
		expansionScreenWallpaperWindow = new JWindow();

		// �л���Ļ��ť
		changeScreenBtnPane = new JPanel();
		expansionScreenCtrlBtnPane.add(changeScreenBtnPane, BorderLayout.CENTER);
		changeScreenBtnPane.add(changeScreenBtn);

		// ��Ƶ����������������
		vedioPlayerVolumeSlider = new JSlider();
		vedioPlayerVolumeSlider.setPreferredSize(new Dimension(150, 20));
		vedioPlayerVolumeSliderPane.add(vedioPlayerVolumeSlider);
		vedioPlayerVolumeSlider.setValue(80);

		// ��Ƶ�������������ֱ�ǩ
		vedioVolumeCnlabel = new JLabel("\u97F3\u91CF");
		vedioPlayerVolumeSliderPane.add(vedioVolumeCnlabel);

		// ��Ƶ������������С��ǩ
		vedioVolumeLabel = new JLabel("80 %");
		vedioVolumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		vedioVolumeLabel.setPreferredSize(new Dimension(40, 20));
		vedioPlayerVolumeSliderPane.add(vedioVolumeLabel);

		// ��Ƶ��һ����ť
		vedioPreBtn = new JButton("\u300A");
		vedioPlayerCtrlBtnPane.add(vedioPreBtn);
		// ��Ƶ����/��ͣ��ť
		vedioPlayBtn = new JButton("\u25B6 / \u2016");
		vedioPlayerCtrlBtnPane.add(vedioPlayBtn);
		// ��Ƶ��һ����ť
		vedioNextBtn = new JButton("\u300B");
		vedioPlayerCtrlBtnPane.add(vedioNextBtn);
		// ��Ƶֹͣ��ť
		vedioStopBtn = new JButton("\u25A0");
		vedioPlayerCtrlBtnPane.add(vedioStopBtn);

		// ������ť
		soundOffBtn = new JButton("\u9759\u97F3");
		quickBtnPane.add(soundOffBtn);
	}

	/**
	 * ������ť�¼�����
	 */
	private void addSoundOffBtnEventListener() {
		soundOffBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vedioPlayer.isMute() || AudioPlayer.getMediaPlayerComponent().getMediaPlayer().isMute()) {
					soundOffBtn.setText("����");
				} else {
					soundOffBtn.setText("�ָ�");
				}

				vedioPlayer.mute();
				AudioPlayer.getMediaPlayerComponent().getMediaPlayer().mute();
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
	}

	/**
	 * ��չ��ǽֽ��ť�¼�����
	 */
	private void addExpScreenWallpaperBtnEventListener() {
		// Ϊ��չ��ǽֽ��ť����¼�����
		expansionScreenWallpaperBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gds.length > 1) {
					if (expansionScreenWallpaperWindow.isVisible()) {
						expansionScreenWallpaperWindow.setVisible(false);
					} else {
						if (imgFile == null) {
							JFileChooser jfc = new JFileChooser("ѡ�񱳾�ͼƬ");
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
	 * ��Ƶ���������ư�ť�¼�����
	 */
	private void addVedioPlayerCtrlBtnsEventListener() {
		// Ϊ��Ƶ��һ����ť����¼�����
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

		// Ϊ��Ƶ����/��ͣ��ť����¼�����
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

		// Ϊ��Ƶ��һ����ť����¼�����

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

		// Ϊ��Ƶֹͣ��ť����¼�����
		vedioStopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				expansionWindow.setVisible(false);
				vedioPlayer.stop();
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});

		// Ϊ��Ƶ��������������¼�����
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
	 * �л���Ļ��ť�¼�����
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
	 * ɨ����ʾ�豸�������л���Ļ��ť,��չ�����ӻ���֡������������չ��ǽֽ��ť�����״̬
	 */
	private void scanGraphicsDevices() {
		// ��ȡ��ǰϵͳ��ʾ�豸
		gds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

		// �����⵽��չ��, �������л���Ļ����չ��ǽֽ��ťΪ����
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

		// �����⵽��չ��, ��������չ�������߳�
		if (gds.length > 1) {
			expScreenMonitorPane = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					g.drawImage(expImg, 0, 0, this.getWidth(), this.getHeight(), null);
					g.setColor(Color.RED);
					g.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
					g.drawString("��չ����ػ���", 15, 40);
				}

			};
			rect = new Rectangle(gds[0].getDisplayMode().getWidth(), 0, gds[1].getDisplayMode().getWidth(),
					gds[1].getDisplayMode().getHeight());

			try {
				robot = new Robot();
			} catch (AWTException e1) {
				e1.printStackTrace();
			}

			// ʵʱ�����չ���߳�
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							expImg = robot.createScreenCapture(rect);
							expScreenMonitorPane.repaint();
							Thread.sleep(1000 / fpx);

							// �������չ��״̬����ͣ����
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
	 * ��չ��֡�����û������¼�����
	 */
	private void addFpxSliderEventListener() {
		// ��չ��֡�����û�����״̬�ı��¼�
		fpxSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				fpx = fpxSlider.getValue();
				lblFpx.setText(fpxSlider.getValue() + "fpx");
			}
		});

		// ��չ��֡�����û�����������¼�
		fpxSlider.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				fpxSlider.setValue((int) (e.getX() * ((float) fpxSlider.getMaximum() / fpxSlider.getWidth())));
			}
		});
	}

	/**
	 * ��Ƶ�������¼�����
	 */
	private void addVedioProgressBarEventListener() {

		// ������¼�
		vedioProgressBar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				vedioPlayer.setTime((long) (((float) x / vedioProgressBar.getWidth()) * vedioPlayer.getLength()));
			}

		});

		// �����ק�¼�
		vedioProgressBar.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				vedioPlayer.setTime((long) (((float) x / vedioProgressBar.getWidth()) * vedioPlayer.getLength()));

			}
		});

		// �����߳�ʵʱ��Ⲣ�޸Ľ�����
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
	 * �������
	 */
	private void createPane() {

		contentPane = getContentPane();
		// ���������������
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(mainPane);
		mainPane.setLayout(new BorderLayout(0, 0));
		// ������Ƶ�����������
		vedioPlayerPane = new JPanel();
		vedioPlayerPane.setBorder(new TitledBorder(null, "\u89C6\u9891\u64AD\u653E\u5668", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		mainPane.add(vedioPlayerPane, BorderLayout.CENTER);
		vedioPlayerPane.setLayout(new BorderLayout(0, 0));

		// ������Ƶ���������
		vedioPane = new JPanel();
		vedioPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		vedioPlayerPane.add(vedioPane, BorderLayout.CENTER);
		vedioPane.setLayout(new BorderLayout(0, 0));
		vedioPane.add(VedioPlayer.getMediaPlayerComponent(), BorderLayout.CENTER);

		// ������Ƶ�������
		vedioCtrlPane = new JPanel();
		vedioPlayerPane.add(vedioCtrlPane, BorderLayout.SOUTH);
		vedioCtrlPane.setLayout(new BorderLayout(0, 0));

		// ������Ƶ���������������
		vedioProgressBarPane = new JPanel();
		vedioCtrlPane.add(vedioProgressBarPane, BorderLayout.NORTH);
		vedioProgressBarPane.setLayout(new BorderLayout(0, 0));

		// ������Ƶ��������ť���
		vedioPlayerBtnPane = new JPanel();
		vedioCtrlPane.add(vedioPlayerBtnPane, BorderLayout.SOUTH);
		vedioPlayerBtnPane.setLayout(new BorderLayout(0, 0));

		// ������չ�����ư�ť���
		expansionScreenCtrlBtnPane = new JPanel();
		expansionScreenCtrlBtnPane.setLayout(new BorderLayout(0, 0));
		vedioPlayerBtnPane.add(expansionScreenCtrlBtnPane, BorderLayout.WEST);

		// ��չ�����ӻ���֡���������
		fpxSliderPane = new JPanel();
		expansionScreenCtrlBtnPane.add(fpxSliderPane, BorderLayout.EAST);

		// �Ҳ�������(������ʹ��)
		rightOutterPane = new JPanel();
		mainPane.add(rightOutterPane, BorderLayout.EAST);
		rightOutterPane.setLayout(new GridLayout(0, 2, 0, 0));

		// �Ҳ��ڲ������(����ʹ��)
		leftInnerPane = new JPanel();
		rightOutterPane.add(leftInnerPane);
		leftInnerPane.setLayout(new GridLayout(2, 1, 0, 0));

		// ��Ƶ�б����
		vedioListPane = new VedioListPane();
		leftInnerPane.add(vedioListPane);
		// ��ȡ���ڲ�����Ƶ�б��JListUtilities����(�Զ���)
		vedioListUtil = vedioListPane.getjListUtil();

		// �ļ��б����
		fileListPane = new FileListPane();
		leftInnerPane.add(fileListPane);

		// �Ҳ��ڲ������
		rightInnerPane = new JPanel();
		rightOutterPane.add(rightInnerPane);
		rightInnerPane.setLayout(new BorderLayout(0, 0));

		// ��Ƶ���
		audioPane = new JPanel();
		rightInnerPane.add(audioPane, BorderLayout.CENTER);
		audioPane.setLayout(new BorderLayout(0, 0));

		// ��Ƶ�������
		audioPlayerCtrlPane = new AudioPlayerCtrlPane();
		audioPane.add(audioPlayerCtrlPane, BorderLayout.SOUTH);

		// ��Ƶ�б����
		audioListPane = new AudioListPane();
		audioPane.add(audioListPane, BorderLayout.CENTER);

		// ���ٰ�ť���
		quickBtnPane = new JPanel();
		rightInnerPane.add(quickBtnPane, BorderLayout.SOUTH);
		quickBtnPane.setLayout(new GridLayout(0, 2, 0, 0));

		// ��Ƶ�����������������
		vedioPlayerVolumeSliderPane = new JPanel();
		vedioPlayerBtnPane.add(vedioPlayerVolumeSliderPane, BorderLayout.EAST);
		vedioPlayerVolumeSliderPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// ��Ƶ���������ư�ť���
		vedioPlayerCtrlBtnPane = new JPanel();
		vedioPlayerBtnPane.add(vedioPlayerCtrlBtnPane, BorderLayout.CENTER);

	}

	/**
	 * ��Ӽ����¼�����
	 */
	private void addKeyBoardListenerEvent() {
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

			@Override
			public void eventDispatched(AWTEvent event) {
				if (((KeyEvent) event).getID() == KeyEvent.KEY_PRESSED) {
					switch (((KeyEvent) event).getKeyCode()) {
					// �ҷ������Ƶ���
					case KeyEvent.VK_RIGHT:
						vedioPlayer.skip(10000);
						break;

					// �������Ƶ����
					case KeyEvent.VK_LEFT:
						vedioPlayer.skip(-10000);
						break;

					// �ո����ͣ�򲥷���Ƶ
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
