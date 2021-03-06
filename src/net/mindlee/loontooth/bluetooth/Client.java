package net.mindlee.loontooth.bluetooth;

import java.util.ArrayList;
import java.util.List;

import net.mindlee.loontooth.gui.MainActivity;
import net.mindlee.loontooth.util.MyPopWindow;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * 客户端管理
 * 
 * @author 李伟
 * 
 */
public class Client {
	
	private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
	private MainActivity activity;

	public Client(MainActivity context) {
		activity = context;
	}

	public List<BluetoothDevice> getDeviceList() {
		return deviceList;
	}

	// 广播接收器
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothTools.ACTION_NOT_FOUND_SERVER.equals(action)) {
				// 未发现设备
				Log.v("客户端", "没有发现设备");
				activity.DisplayToast("没有发现设备");

			} else if (BluetoothTools.ACTION_FOUND_DEVICE.equals(action)) {
				// 获取到设备对象
				BluetoothDevice device = (BluetoothDevice) intent.getExtras()
						.get(BluetoothTools.DEVICE);
				deviceList.add(device);
				MyPopWindow.getDeviceSearchAdapter().addDevice(device);
				MyPopWindow.getDeviceSearchAdapter().notifyDataSetChanged();

				Log.v("客户端", "发现设备" + device.getName());
				activity.DisplayToast("发现设备" + device.getName());

			} else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {

				Log.v("客户端", "连接成功");
				activity.DisplayToast("连接成功");
				// 连接成功

			} else if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
				// 接收数据
				TransmitBean data = (TransmitBean) intent.getExtras()
						.getSerializable(BluetoothTools.DATA);

				String msg = "请求发送 " + data.getMsg() + "\n大小：" + data.getSize();

				Log.w("客户端", msg);
				activity.DisplayToast(msg);
			}
		}
	};

	public void onStart(Context context) {
		Log.d("Client客户端", "onStart");
		deviceList.clear();

		// 开启后台service
		Intent startService = new Intent(context, ClientService.class);
		context.startService(startService);

		Intent startSearchIntent = new Intent(
				BluetoothTools.ACTION_START_DISCOVERY);
		context.sendBroadcast(startSearchIntent);

		// 注册BoradcasrReceiver
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothTools.ACTION_NOT_FOUND_SERVER);
		intentFilter.addAction(BluetoothTools.ACTION_FOUND_DEVICE);
		intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
		intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);

		context.registerReceiver(broadcastReceiver, intentFilter);
		activity.DisplayToast("客户端已开启");
	}

	public void onStop(Context context) {
		// 关闭后台Service
		Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
		context.sendBroadcast(startService);

		context.unregisterReceiver(broadcastReceiver);
	}
}
