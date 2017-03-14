package cn.javacodes.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cn.javacodes.launcher.ActivitiesController;
import cn.javacodes.util.AudioPlayer;
import cn.javacodes.util.MediaTimeUtil;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

@SuppressWarnings("serial")
public class AudioPlayerCtrlPane extends JPanel {
	
	private EmbeddedMediaPlayer audioPlayer = AudioPlayer.getMediaPlayerComponent().getMediaPlayer();
	
	private MediaTimeUtil audioTimeUtil;
	
	private JPanel audioPlayerBtnAndProgressPane;
	
	private JPanel audioProgressPane;
	
	private JProgressBar audioProgressBar;
	
	private JLabel audioProgressBarRightLabel;
	
	private JLabel audioProgressBarLeftLabel;
	
	private JPanel audioPlayerBtnPane;
	
	private JButton audioStopBtn;
	
	private JButton audioPreBtn;
	
	private JButton audioPlayBtn;
	
	private JButton audioNextBtn;
	
	private JButton audioChangeModeBtn;
	
	private JSlider audioPlayerVolumeSlider;
	
	private JPanel audioVolumeSliderPane;
	
	/**
	 * Create the panel.
	 */
	public AudioPlayerCtrlPane() {
		
		setBorder(new TitledBorder(null, "\u97F3\u9891\u64AD\u653E\u5668", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));

		audioPlayerBtnAndProgressPane = new JPanel();
		add(audioPlayerBtnAndProgressPane, BorderLayout.CENTER);
		audioPlayerBtnAndProgressPane.setLayout(new BorderLayout(0, 0));

		audioProgressPane = new JPanel();
		audioPlayerBtnAndProgressPane.add(audioProgressPane, BorderLayout.NORTH);
		audioProgressPane.setLayout(new BorderLayout(0, 0));

		audioProgressBar = new JProgressBar();
		audioProgressPane.add(audioProgressBar, BorderLayout.CENTER);

		audioProgressBarRightLabel = new JLabel("00:00");
		audioProgressPane.add(audioProgressBarRightLabel, BorderLayout.EAST);

		audioProgressBarLeftLabel = new JLabel("00:00");
		audioProgressPane.add(audioProgressBarLeftLabel, BorderLayout.WEST);
		
		
		audioProgressBar.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				audioPlayer.setTime((long) (((float) x / audioProgressBar.getWidth()) * audioPlayer.getLength()));
			}

		});

		audioProgressBar.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				audioPlayer.setTime((long) (((float) x / audioProgressBar.getWidth()) * audioPlayer.getLength()));

			}
		});

		audioTimeUtil = new MediaTimeUtil();

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					while (true) {
						long totalTime = audioPlayer.getLength();
						long currentTime = audioPlayer.getTime();
						if (totalTime <= 0) {
							audioProgressBar.setValue(0);
							audioProgressBarLeftLabel.setText("00:00");
							audioProgressBarRightLabel.setText("00:00");
						} else {
							audioTimeUtil.timeCalculator(currentTime, totalTime);
							audioProgressBarLeftLabel
									.setText(audioTimeUtil.getCurrentMin() + ":" + audioTimeUtil.getCurrentSec());
							audioProgressBarRightLabel
									.setText(audioTimeUtil.getTotalMin() + ":" + audioTimeUtil.getTotalSec());
							double percent = (double) currentTime / totalTime;
							audioProgressBar.setValue((int) (percent * 100));
						}
						Thread.sleep(200);
					}
				} catch (Exception e) {

				}

			}

		}).start();

		audioPlayerBtnPane = new JPanel();
		audioPlayerBtnAndProgressPane.add(audioPlayerBtnPane, BorderLayout.CENTER);
		audioPlayerBtnPane.setLayout(new GridLayout(0, 5, 0, 0));

		audioStopBtn = new JButton("\u25A0");
		audioStopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				audioPlayer.stop();
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		audioPlayerBtnPane.add(audioStopBtn);

		audioPreBtn = new JButton("\u300A");
		audioPreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ActivitiesController.getMainFrame().getAudioListPane().getCurrentAudioIndex() > 0) {
					audioPlayer
							.playMedia(ActivitiesController.getMainFrame().getAudioListPane().getjListUtil().get(ActivitiesController.getMainFrame().getAudioListPane().getCurrentAudioIndex() - 1).getAbsolutePath());
					ActivitiesController.getMainFrame().getAudioListPane().setCurrentAudioIndex(ActivitiesController.getMainFrame().getAudioListPane().getCurrentAudioIndex() - 1);
					ActivitiesController.getMainFrame().getAudioListPane().getjListUtil().getList().setSelectedIndex(ActivitiesController.getMainFrame().getAudioListPane().getCurrentAudioIndex());
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
				
			}
		});
		audioPlayerBtnPane.add(audioPreBtn);

		audioPlayBtn = new JButton("\u25B6");
		audioPlayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (audioPlayer.isPlaying()) {
					audioPlayer.pause();
				} else {
					audioPlayer.play();
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		audioPlayerBtnPane.add(audioPlayBtn);

		audioNextBtn = new JButton("\u300B");
		audioNextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ActivitiesController.getMainFrame().getAudioListPane().getCurrentAudioIndex() >= 0
						&& ActivitiesController.getMainFrame().getAudioListPane().getCurrentAudioIndex() < ActivitiesController.getMainFrame().getAudioListPane().getjListUtil().getSize() - 1) {
					audioPlayer
							.playMedia(ActivitiesController.getMainFrame().getAudioListPane().getjListUtil().get(ActivitiesController.getMainFrame().getAudioListPane().getCurrentAudioIndex() + 1).getAbsolutePath());
					ActivitiesController.getMainFrame().getAudioListPane().setCurrentAudioIndex(ActivitiesController.getMainFrame().getAudioListPane().getCurrentAudioIndex() + 1);
					ActivitiesController.getMainFrame().getAudioListPane().getjListUtil().getList().setSelectedIndex(ActivitiesController.getMainFrame().getAudioListPane().getCurrentAudioIndex());
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		audioPlayerBtnPane.add(audioNextBtn);

		audioChangeModeBtn = new JButton("ÆÕ");
		audioChangeModeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (AudioPlayer.playMode) {
				case AudioPlayer.NORMAL:
					audioChangeModeBtn.setText("ÁÐ");
					AudioPlayer.playMode = AudioPlayer.LIST_LOOP;
					break;
				case AudioPlayer.LIST_LOOP:
					audioChangeModeBtn.setText("µ¥");
					AudioPlayer.playMode = AudioPlayer.SINGLE_LOOP;
					break;
				case AudioPlayer.SINGLE_LOOP:
					audioChangeModeBtn.setText("Ë³");
					AudioPlayer.playMode = AudioPlayer.LIST_ORDER;
					break;
				case AudioPlayer.LIST_ORDER:
					audioChangeModeBtn.setText("Ëæ");
					AudioPlayer.playMode = AudioPlayer.RANDOM;
					break;
				case AudioPlayer.RANDOM:
					audioChangeModeBtn.setText("ÆÕ");
					AudioPlayer.playMode = AudioPlayer.NORMAL;
					break;
				default:
					break;
				}
				ActivitiesController.getMainFrame().getContentPane().requestFocus();
			}
		});
		audioPlayerBtnPane.add(audioChangeModeBtn);
		
		audioVolumeSliderPane = new JPanel();
		audioPlayerBtnAndProgressPane.add(audioVolumeSliderPane, BorderLayout.SOUTH);
		audioVolumeSliderPane.setLayout(new BorderLayout(0, 0));

		audioPlayerVolumeSlider = new JSlider();
		audioPlayerVolumeSlider.setValue(80);
		audioVolumeSliderPane.add(audioPlayerVolumeSlider);
		
		
		JLabel audioVolumeCnlabel = new JLabel("\u97F3\u91CF");
		audioVolumeCnlabel.setHorizontalAlignment(SwingConstants.CENTER);
		audioVolumeCnlabel.setPreferredSize(new Dimension(40, 20));
		audioVolumeSliderPane.add(audioVolumeCnlabel, BorderLayout.WEST);

		JLabel audioVolumeLabel = new JLabel("80 %");
		audioVolumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		audioVolumeLabel.setPreferredSize(new Dimension(40, 20));
		audioVolumeSliderPane.add(audioVolumeLabel, BorderLayout.EAST);
		
		audioPlayerVolumeSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				audioPlayer.setVolume(audioPlayerVolumeSlider.getValue());
				audioVolumeLabel.setText(audioPlayerVolumeSlider.getValue() + "%");
			}
		});
		
		

		audioPlayerVolumeSlider.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				audioPlayerVolumeSlider.setValue((int) (e.getX()
						* ((float) audioPlayerVolumeSlider.getMaximum() / audioPlayerVolumeSlider.getWidth())));
			}
		});
		
	}

	public JButton getAudioPlayBtn() {
		return audioPlayBtn;
	}

	public JSlider getAudioPlayerVolumeSlider() {
		return audioPlayerVolumeSlider;
	}
	
	
	

}
