package cn.javacodes.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class AudioFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		String name = f.getName();
		String expandedName = ".3ga;.669;.a52;.aac;.ac3;.adt;.adts;.aif;.aifc;"
				+ ".aiff;.amr;.aob;.ape;.awb;.caf;.dts;.flac;.it;.kar;.m4a;.m4b;"
				+ ".m4p;.m5p;.mid;.mka;.mlp;.mod;.mpa;.mp1;.mp2;.mp3;.mpc;.mpga;"
				+ ".mus;.oga;.ogg;.oma;.opus;.qcp;.ra;.rmi;.s3m;.sid;.spx;.thd;"
				+ ".tta;.voc;.vqf;.w64;.wav;.wma;.wv;.xa;.xm";
		String[] exs = expandedName.split(";");	
		boolean metchEx = false;
		for (int i = 0; i < exs.length && !metchEx; i++) {
			metchEx = name.toLowerCase().endsWith(exs[i]);
		}
		return f.isDirectory() || metchEx;
	}

	@Override
	public String getDescription() {
		return "ÒôÆµÎÄ¼þ";
	}

}
