package com.bingyan.bookrecycle.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpUtil {
	public final static String TAG = "mytag_HttpUtil";
	public Context context;

	public final static int HTTP_METHOD_GET = 1;
	public final static int HTTP_METHOD_POST = 2;

	private String mUrl;
	private int mMethod;
	private List<NameValuePair> mParams;
	private OnfinishedListener mOnfinishedListener;
	private HttpClient mHttpClient;
	private int mResponedType;

	private HttpHandler mHttpHandler;
	private Thread mThread;

	public final static int RESPONED_ERROR = 0;
	public final static int RESPONED_TYPE_STRING = 1;
	public final static int RESPONED_TYPE_BITMAP = 2;
	public final static int RESPONED_TYPE_JSON = 3;

	public HttpUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mHttpHandler = new HttpHandler();
		mHttpClient = new DefaultHttpClient();
		HttpConnectionParams
				.setConnectionTimeout(mHttpClient.getParams(), 3000);
		HttpConnectionParams.setSoTimeout(mHttpClient.getParams(), 5000);
		Log.i(TAG, "created HttpUtil");
	}

	public void setHttpParams(String url, List<NameValuePair> params,
			int httpMethod, int responedType,
			OnfinishedListener onfinishedListener) {
		// TODO Auto-generated constructor stub
		Log.i(TAG, "set param");
		mMethod = httpMethod;
		mUrl = url;
		mParams = params;
		mResponedType = responedType;
		mOnfinishedListener = onfinishedListener;
		mThread = new Thread(new HttpRunable());
	}

	public void start() {
		if (mThread != null && !mThread.isAlive()) {
			mThread.start();
		}
	}

	private HttpResponse post() {
		HttpPost httpPost = new HttpPost(mUrl);
		HttpResponse httpResponse = null;
		try {
			if (mParams != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(mParams, HTTP.UTF_8));
			}
			httpResponse = mHttpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpResponse;
	}

	private HttpResponse get() {
		HttpGet httpGet = new HttpGet(mUrl);
		HttpResponse httpResponse = null;
		try {
			Log.i(TAG, "get start execute");
			httpResponse = mHttpClient.execute(httpGet);
			Log.i(TAG, "execute over" + httpResponse);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "get return" + httpResponse);
		return httpResponse;
	}

	public final static Bitmap getBitmapFromUrl(String urlString) {
		Bitmap bitmap = null;
		HttpURLConnection urlConnection = null;
		BufferedInputStream inputStream = null;
		try {
			Log.i(TAG, "http getBitmap start");
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			inputStream = new BufferedInputStream(
					urlConnection.getInputStream(), 8 * 1024);
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, "http getBitmap error");
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return bitmap;
	}

	public void setOnfinishedListener(OnfinishedListener onfinishedListener) {
		mOnfinishedListener = onfinishedListener;
	}

	public interface OnfinishedListener {
		public void onSuccessed(Object object);
		public void onError(String errMsg);
	}

	class HttpRunable implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i(TAG, "run start");
			HttpResponse httpResponse = null;
			switch (mMethod) {
			case HTTP_METHOD_POST:
				httpResponse = post();
				break;
			case HTTP_METHOD_GET:
				httpResponse = get();
				break;
			default:
				break;
			}
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				switch (mResponedType) {
				case RESPONED_TYPE_STRING:
					break;
				case RESPONED_TYPE_JSON:
					JSONObject json = null;
					try {
						InputStream inputStream = httpResponse.getEntity()
								.getContent();
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(inputStream));
						// Log.i(TAG,"readline"+bufferedReader.readLine());
						StringBuffer stringBuffer = new StringBuffer();
						for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
								.readLine()) {
							stringBuffer.append(s);
						}
						json = new JSONObject(stringBuffer.toString());
						Log.i(TAG, "json:" + json);
						Message msg = mHttpHandler.obtainMessage(
								RESPONED_TYPE_JSON, json);
						mHttpHandler.sendMessage(msg);
						return;
					} catch (IllegalStateException e2) {
						e2.printStackTrace();
					} catch (IOException e2) {
						e2.printStackTrace();
					} catch (JSONException e2) {
						e2.printStackTrace();
					}

					break;
				case RESPONED_TYPE_BITMAP:
					break;
				default:
					break;
				}
				Message msg = mHttpHandler.obtainMessage(RESPONED_ERROR, null);
				mHttpHandler.sendMessage(msg);
			}
		}
	}

	class HttpHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg == null || msg.obj == null)
				return;
			switch (msg.what) {
			case RESPONED_TYPE_STRING:
				break;
			case RESPONED_TYPE_JSON:
				JSONObject json = (JSONObject) msg.obj;
				mOnfinishedListener.onSuccessed(json);
				break;
			case RESPONED_TYPE_BITMAP:
				break;
			default:
				mOnfinishedListener.onError("net error ");
				break;
			}
		}
	}
}