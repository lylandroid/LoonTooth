package net.mindlee.loontooth.gui;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 
 * 基础类，用于定义一些所有Activity都用到的行为
 * 
 * @author 李伟
 * 
 */
public class BaseActivity extends Activity {
	private static final String TAG = BaseActivity.class.getSimpleName();
	private long lastBackTime = 0;
	private static long TIME_DIFF = 2 * 1000;

	public enum ViewInfo {
		FOCUSED_ITEM(0);
		private int value;

		// 构造方法
		private ViewInfo(int value) {
			this.value = value;
		}

		// get set 方法
		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long now = new Date().getTime();
			if (now - lastBackTime < TIME_DIFF) {
				return super.onKeyDown(keyCode, event);
			} else {
				lastBackTime = now;
				Toast.makeText(this, "再点一次将退出", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

}
