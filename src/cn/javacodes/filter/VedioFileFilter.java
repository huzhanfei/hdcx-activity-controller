package cn.javacodes.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class VedioFileFilter extends FileFilter {

	/*
	 * .3g2;.3gp;.3gp2;.3gpp;.amv;.asf;.avi;.bik;.bin;.divx;.drc;.vc;.flv;.gvi;.
	 * iso;.m1v;.m2v;.m2t;.m2ts;.m4v;.mkv;.mov;.mp2;.mp2v;.mp4;.mp4v;.mpe;.mpeg;
	 * .mpeg1;.mpeg2;.mpeg4;.mpg;.mpv2;.mts;.mtv;.mxf;.mxg;.nsv;.nuv;.ogg;.ogm;.
	 * ogv;.ogx;.ps;.rec;.rm;.rmvb;.rpl;.thp;.tod;.ts;.tts;.txd;.vob;.vro;.webm;
	 * .wm;.wmv;.wtv;.xesc
	 */
	@Override
	public boolean accept(File f) {
		String name = f.getName();
		String expandedName = ".3g2;.3gp;.3gp2;.3gpp;.amv;.asf;.avi;.bik;.bin;.divx;.drc;.vc;"
		+".flv;.gvi;.iso;.m1v;.m2v;.m2t;.m2ts;.m4v;.mkv;.mov;.mp2;.mp2v;.mp4;.mp4v;.mpe;.mpeg;"
				+".mpeg1;.mpeg2;.mpeg4;.mpg;.mpv2;.mts;.mtv;.mxf;.mxg;.nsv;.nuv;.ogg;.ogm;.ogv;"
		+".ogx;.ps;.rec;.rm;.rmvb;.rpl;.thp;.tod;.ts;.tts;.txd;.vob;.vro;.webm;.wm;.wmv;.wtv;.xesc";
		String[] exs = expandedName.split(";");
		boolean metchEx = false;		
		for (int i = 0; i < exs.length && !metchEx; i++) {
			metchEx = name.toLowerCase().endsWith(exs[i]);
		}		
		return f.isDirectory() || metchEx;
	}

	@Override
	public String getDescription() {
		return "ÊÓÆµÎÄ¼þ";
	}
	
	


}
