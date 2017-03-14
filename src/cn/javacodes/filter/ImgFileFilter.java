package cn.javacodes.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImgFileFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		String name = f.getName();
		String expandedName = ".jpg;.png;.bmp;.jpeg;.gif";
		String[] exs = expandedName.split(";");
		boolean metchEx = false;		
		for (int i = 0; i < exs.length && !metchEx; i++) {
			metchEx = name.toLowerCase().endsWith(exs[i]);
		}		
		return f.isDirectory() || metchEx;
	}

	@Override
	public String getDescription() {
		return "Í¼Æ¬ÎÄ¼þ(*.jpg;*.png;*.bmp;*.jpeg;*.gif)";
	}
	
}
