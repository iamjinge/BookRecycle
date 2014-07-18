package com.bingyan.bookrecycle.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bingyan.bookrecycle.util.FileNameUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileCache {
    private static String mSdRootPath;
    private static String mDataRootPath = null;
    private final static String FOLDER_NAME = "BookRecycle/cache/image";
    
    public FileCache(Context context){
    	mSdRootPath = Environment.getExternalStorageDirectory().getPath();
        mDataRootPath = context.getCacheDir().getPath();
    }
    
//    获取储存Image的目录
    private String getStorageDirectory(){
    	return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
    }
    
//    保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录 
    public void savaBitmap(String fileName, Bitmap bitmap) throws IOException{
        if(bitmap == null){
            return;
        }
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if(!folderFile.exists()){
            folderFile.mkdir();
        }
        Log.e("", fileName);
        String filePath = FileNameUtil.urlToFilename(path, fileName);
        File file = new File(filePath);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }
      
//     从手机或者sd卡获取Bitmap 
    public Bitmap getBitmap(String fileName){
    	String filePath = FileNameUtil.urlToFilename(getStorageDirectory(), fileName);
        return BitmapFactory.decodeFile(filePath);
    }
      
//    判断文件是否存在
    public boolean isFileExists(String fileName){
    	String filePath = FileNameUtil.urlToFilename(getStorageDirectory(), fileName);
        return new File(filePath).exists();
    }
     
//    获取文件的大小 
    public long getFileSize(String fileName) {
    	String filePath = FileNameUtil.urlToFilename(getStorageDirectory(), fileName);
        return new File(filePath).length();
    }
      
//    删除SD卡或者手机的缓存图片和目录
    public void deleteFile() {
        File dirFile = new File(getStorageDirectory());
        if(! dirFile.exists()){
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }
        
        dirFile.delete();
    }
}
