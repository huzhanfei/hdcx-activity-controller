package cn.javacodes.launcher;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import cn.javacodes.view.MainFrame;
import cn.javacodes.view.PasswordDialog;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 * 黑大创协黑龙江大学学生创业协会活动电子场控系统V1.0
 * 最后修改时间:2016-05-16
 * @author 胡湛霏
 * @since 2016-05-13
 */
public class ActivitiesController {

	private static MainFrame mainFrame = null;
	
	public static final String TITLE = "黑龙江大学学生创业协会活动电子场控系统V1.0";

	public static void main(String[] args) {
		if (new NativeDiscovery().discover()) {		
			JDialog passwordDialog = new PasswordDialog();
			passwordDialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(mainFrame,
					"未检测到本地VLC播放器,请先安装64位版本VLC播放器(最低版本2.1.0)!"
							, "环境错误", JOptionPane.ERROR_MESSAGE);
		}
	}



	public synchronized static MainFrame getMainFrame() {
		if (mainFrame == null) {	
			mainFrame = new MainFrame();
			return mainFrame;
		}else{
			return mainFrame;
		}
	}
}
