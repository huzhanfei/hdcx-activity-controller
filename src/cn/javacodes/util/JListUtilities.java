package cn.javacodes.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class JListUtilities<K, V> {

	private JList<K> list;

	private Map<K, V> dataMap;

	private DefaultListModel<K> data;

	public JListUtilities(JList<K> list) {
		this.list = list;
		dataMap = new HashMap<K, V>();
		data = new DefaultListModel<K>();
		list.setModel(data);
	}

	public void moveUp(int index) {
		if (index > 0) {
			K tempElement = data.remove(index - 1);
			data.insertElementAt(tempElement, index);
			list.setModel(data);
			list.setSelectedIndex(index - 1);
		}
	}

	public void moveDown(int index) {
		if (index < data.getSize() - 1 && index >= 0) {
			K tempElement = data.remove(index);
			data.insertElementAt(tempElement, index + 1);
			list.setModel(data);
			list.setSelectedIndex(index + 1);
		}
	}

	@SuppressWarnings("unchecked")
	public void add(K key, V value) {
		if (dataMap.get(key) == null) {
			dataMap.put(key, value);
			data.addElement(key);
			list.setModel(data);
		} else if (!dataMap.get(key).equals(value) && key instanceof String) {
			String otherkey = ((String) key) + "(another)";
			dataMap.put((K)otherkey, value);
			data.addElement((K)otherkey);
			list.setModel(data);
		}

	}

	public void remove(int index) {
		if (index >= 0) {
			dataMap.remove(data.remove(index));
			list.setModel(data);
		}
	}

	public void removeAll() {
		dataMap.clear();
		data.clear();
		list.setModel(data);
	}

	public V get(int index) {
		if (index >= 0) {
			return dataMap.get(data.getElementAt(index));
		} else {
			return null;
		}

	}

	public int getSize() {
		return data.getSize();
	}

	public JList<K> getList() {
		return list;
	}


	

}
