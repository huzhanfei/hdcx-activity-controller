package cn.javacodes.launcher;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import cn.javacodes.view.MainFrame;
import cn.javacodes.view.PasswordDialog;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 * �ڴ�Э��������ѧѧ����ҵЭ�����ӳ���ϵͳV1.0
 * ����޸�ʱ��:2016-05-16
 * @author ��տ��
 * @since 2016-05-13
 */
public class ActivitiesController {

	private static MainFrame mainFrame = null;
	
	public static final String TITLE = "��������ѧѧ����ҵЭ�����ӳ���ϵͳV1.0";

	public static void main(String[] args) {
		if (new NativeDiscovery().discover()) {		
			JDialog passwordDialog = new PasswordDialog();
			passwordDialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(mainFrame,
					"δ��⵽����VLC������,���Ȱ�װ64λ�汾VLC������(��Ͱ汾2.1.0)!"
							, "��������", JOptionPane.ERROR_MESSAGE);
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
