package com.bingyan.bookrecycle.image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Queue;

import com.bingyan.bookrecycle.R;
import com.bingyan.bookrecycle.http.HttpUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	public static final String TAG = "ImageLoder";
	public Context mContext;
	private LruCache<String, Bitmap> mLruCache;
	private FileCache mFileCache;

	public ImageLoader(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		initCache(context);
	}

	private void initCache(Context context) {
		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		memClass = memClass > 32 ? 32 : memClass;
		final int cache_size = 1024 * 1024 * memClass / 8;
		mLruCache = new LruCache<String, Bitmap>(cache_size) {
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
		mFileCache = new FileCache(context);
	}

	public void displayImage(ImageView imageView, String url) {
		if (imageView == null || url == null || url.trim().isEmpty())
			return;
		// 先从内存中找
		Bitmap bitmap = mLruCache.get(url);
		if (bitmap != null) {
			Log.i(TAG, "缓存获取");
			imageView.setImageBitmap(bitmap);
			return;
		}
		// 如果该ImageView之前有加载图片的进程 且 不是这张图片 则取消任务
		if (imageView.getTag() != null
				&& ((WeakReference<ImageLoderWorker>) imageView.getTag()).get() != null) {
			ImageLoderWorker worker = ((WeakReference<ImageLoderWorker>) imageView.getTag()).get();
			if (worker.urlString != url) {
				worker.cancel(true);
			}
		}
		
		// 从硬盘或网络找， 开一个线程
		ImageLoderWorker imageLoderWorker = new ImageLoderWorker(imageView);
		imageLoderWorker.execute(url);
		// 将imageview 和 worker 绑定
		imageView.setTag(new WeakReference<ImageLoderWorker>(imageLoderWorker));
	}

	public class ImageLoderWorker extends AsyncTask<String, Void, Bitmap> {
		public WeakReference<ImageView> imageView;
		public String urlString;

		public ImageLoderWorker(ImageView imageView) {
			// TODO Auto-generated constructor stub
			this.imageView = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected void onPreExecute(){
			imageView.get().setBackgroundResource(R.drawable.ic_launcher);
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			// TODO Auto-generated method stub
			this.urlString = urls[0];
			Bitmap bitmap = null;
			// 从硬盘加载
			if (!isCancelled()) {
				bitmap = mFileCache.getBitmap(urlString);
			}
			if (bitmap != null) {
				mLruCache.put(urlString, bitmap);
				Log.i(TAG, "文件获取");
				return bitmap;
			}
			// 网络加载
			if (!isCancelled()) {
				bitmap = HttpUtil.getBitmapFromUrl(urlString);
			}
			if (bitmap != null) {
				Log.i(TAG, "网络获取");
				try {
					mFileCache.savaBitmap(urlString, bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mLruCache.put(urlString, bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// TODO Auto-generated method stub
			if (!isCancelled() && bitmap != null && imageView != null) {
				imageView.get().setImageBitmap(bitmap);
				super.onPostExecute(bitmap);
			}
		}
	}
}
