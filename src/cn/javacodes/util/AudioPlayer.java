package cn.javacodes.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Random;

import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import cn.javacodes.launcher.ActivitiesController;
import cn.javacodes.view.AudioListPane;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class AudioPlayer {

	private final static EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

	private final static EmbeddedMediaPlayer audioPlayer = mediaPlayerComponent.getMediaPlayer();

	public final static int NORMAL = 0;

	public final static int LIST_LOOP = 1;

	public final static int SINGLE_LOOP = 2;

	public final static int LIST_ORDER = 3;

	public final static int RANDOM = 4;

	public static int playMode = 0;

	static {
		JWindow audioPlayerWindow = new JWindow();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		audioPlayerWindow.setLocation(0, screenSize.height);
		audioPlayerWindow.setVisible(true);
		audioPlayerWindow.add(mediaPlayerComponent);
		audioPlayer.setVolume(80);
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void playing(MediaPlayer mediaPlayer) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {

						ActivitiesController.getMainFrame().getAudioPlayerCtrlPane().getAudioPlayBtn()
								.setText("\u2016");
					}
				});
			}

			@Override
			public void finished(MediaPlayer mediaPlayer) {
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						ActivitiesController.getMainFrame().getAudioPlayerCtrlPane().getAudioPlayBtn().setText("\u25B6");
						AudioListPane audioListPane = ActivitiesController.getMainFrame().getAudioListPane();
						JListUtilities<String, File> jlu = audioListPane.getjListUtil();

						int current = audioListPane.getCurrentAudioIndex();
						int listSize = jlu.getSize();
						switch (playMode) {
						case NORMAL:
							break;
						case LIST_LOOP:
							if (current < listSize - 1) {
								audioPlayer.playMedia(jlu.get(current + 1).getAbsolutePath());
								audioListPane.setCurrentAudioIndex(current + 1);
								jlu.getList().setSelectedIndex(current + 1);
							} else if (current == listSize - 1) {
								audioPlayer.playMedia(jlu.get(0).getAbsolutePath());
								audioListPane.setCurrentAudioIndex(0);
								jlu.getList().setSelectedIndex(0);
							}
							break;
						case SINGLE_LOOP:
							audioPlayer.playMedia(jlu.get(current).getAbsolutePath());
							break;
						case LIST_ORDER:
							if (current < listSize - 1) {
								audioPlayer.playMedia(jlu.get(current + 1).getAbsolutePath());
								audioListPane.setCurrentAudioIndex(current + 1);
								jlu.getList().setSelectedIndex(current + 1);
							}
							break;
						case RANDOM:
							Random rd = new Random();
							int index = rd.nextInt(listSize);
							audioPlayer.playMedia(jlu.get(index).getAbsolutePath());
							audioListPane.setCurrentAudioIndex(index);
							jlu.getList().setSelectedIndex(index);
							break;
						default:
							break;
						}
						
						
					}
				}).start();;



			}

			@Override
			public void paused(MediaPlayer mediaPlayer) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ActivitiesController.getMainFrame().getAudioPlayerCtrlPane().getAudioPlayBtn()
								.setText("\u25B6");
					}
				});
			}

			@Override
			public void stopped(MediaPlayer mediaPlayer) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ActivitiesController.getMainFrame().getAudioPlayerCtrlPane().getAudioPlayBtn()
								.setText("\u25B6");

					}
				});
			}

		});
	}

	public static EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
		return mediaPlayerComponent;
	}

}
