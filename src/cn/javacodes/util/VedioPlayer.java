package cn.javacodes.util;

import javax.swing.SwingUtilities;

import cn.javacodes.launcher.ActivitiesController;
import cn.javacodes.view.MainFrame;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class VedioPlayer {

	@SuppressWarnings("serial")
	private final static EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent(){};
	
		
	static{
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    	if (ActivitiesController.getMainFrame().getState() == 1) {
                    		ActivitiesController.getMainFrame().getExpansionWindow().setVisible(true);
						}
                    	MainFrame frame = ActivitiesController.getMainFrame();
                    	frame.setTitle(ActivitiesController.TITLE + " - 正在播放：" 
                    			+ frame.getVedioListUtil().get(frame.getVedioListPane().getCurrentVedioIndex()).getAbsolutePath());
                    }
                });
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    	if (ActivitiesController.getMainFrame().getState() == 1) {
                    		ActivitiesController.getMainFrame().getExpansionWindow().setVisible(false);
						}
                    	ActivitiesController.getMainFrame().setTitle(ActivitiesController.TITLE);
                    }
                });
            }
            
            
            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            	SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    	if (ActivitiesController.getMainFrame().getState() == 1) {
                    		ActivitiesController.getMainFrame().getExpansionWindow().setVisible(false);
						}
                    	ActivitiesController.getMainFrame().setTitle(ActivitiesController.TITLE); 
                    }
                    
                });
            	
            	
            }
            
            
            @Override
            public void paused(MediaPlayer mediaPlayer) {
            	SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    	MainFrame frame = ActivitiesController.getMainFrame();
                    	frame.setTitle(ActivitiesController.TITLE + " - 暂停播放：" 
                    			+ frame.getVedioListUtil().get(frame.getVedioListPane().getCurrentVedioIndex()).getAbsolutePath());
                    }
                    
                });
            	
            	
            }
        });
        
        mediaPlayerComponent.getMediaPlayer().enableMarquee(true);
        
	}
	
	private VedioPlayer(){}
	
	public static EmbeddedMediaPlayerComponent getMediaPlayerComponent(){
			return mediaPlayerComponent;			
	}


	
	
}
