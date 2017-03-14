package cn.javacodes.util;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class CustomJList<K> extends JList<K> {
	
	public CustomJList(){
		this.setFixedCellWidth(150);
	}

	@Override
	public int locationToIndex(Point location) {
		int index = super.locationToIndex(location);
		if (index != -1 && !getCellBounds(index, index).contains(location)) {
			return -1;
		} else {
			return index;
		}
	}

	public static <K, V> void addMouseRightBtnClickedMenu(JListUtilities<K, V> jlu, MouseEvent e) {
		CustomJList<K> list = (CustomJList<K>) jlu.getList();

		if (list.locationToIndex(e.getPoint()) == -1 && !e.isShiftDown() && !isMenuShortcutKeyDown(e)) {
			list.clearSelection();
		} else {
			list.setSelectedIndex(list.locationToIndex(e.getPoint()));
		}

		if (e.getButton() == MouseEvent.BUTTON3) {
			JPopupMenu popMenu = new JPopupMenu();
			JMenuItem deleteItem = new JMenuItem("删除选中项");
			deleteItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jlu.remove(list.getSelectedIndex());
				}
			});
			JMenuItem clearItem = new JMenuItem("清空列表");
			clearItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jlu.removeAll();
				}
			});
			if (list.isSelectionEmpty()) {
				deleteItem.setEnabled(false);
			}
			if (jlu.getSize() == 0) {
				clearItem.setEnabled(false);
			}
			popMenu.add(deleteItem);
			popMenu.add(clearItem);
			popMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private static boolean isMenuShortcutKeyDown(InputEvent event) {
		return (event.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0;
	}
}
