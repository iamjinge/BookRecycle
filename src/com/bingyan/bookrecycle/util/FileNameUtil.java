package com.bingyan.bookrecycle.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FileNameUtil {

	public static String urlToFilename(File dir, String url) {
		try {
			return dir.getAbsolutePath() + File.separator
					+ URLEncoder.encode(url.replace("*", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String urlToFilename(String dirPath, String url) {
		try {
			return dirPath + File.separator
					+ URLEncoder.encode(url.replace("*", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
