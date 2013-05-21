package pro.trousev.cleer.android;

import pro.trousev.cleer.android.service.RusTag;
import pro.trousev.cleer.android.userInterface.MainActivity;
import pro.trousev.cleer.android.userInterface.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

//SEE https://developer.android.com/guide/topics/ui/notifiers/notifications.html
public class CleerAndroidNotificationManager {
	private Context context = null;
	private NotificationManager mNotificationManager = null;
	private static int currentDisposableNotificationId = Constants.DISPOSABLE_NOTIFICATON_ID;
	private static Notification playerNotification = null;
	
	public CleerAndroidNotificationManager(Context c) {
		context = c;
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	private void postNotification(String title, String text, int id) {
		Notification notification = buildNotification(title, text);
		postNotification(notification, id);
	}
	
	private void postNotification(Notification notification, int id) {
		mNotificationManager.notify(id, notification);
	}

	private Notification buildNotification(String title, String text) {
		RusTag rusTag = new RusTag();
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.application_icon)
				.setContentTitle(title).setContentText(rusTag.change(text));
		Intent notificationIntent = new Intent(context, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(notificationIntent);
		PendingIntent notificationPendingIntent = stackBuilder
				.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(notificationPendingIntent);
		return mBuilder.build();
	}
	
	public Notification getPlayerNotification() {
		return playerNotification;
	}

	public void postGeneralNotification(String text) {
		postNotification(Constants.GENERAL_NOTIFICATION_TITLE, text,
				Constants.GENERAL_NOTIFICATON_ID);
	}

	public void postPlayerNotification(String text) {
		playerNotification = buildNotification(Constants.PLAYER_NOTIFICATION_TITLE, text);
		playerNotification.flags |= (Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT);
		postNotification(playerNotification, Constants.PLAYER_NOTIFICATION_ID);
	}

	public void postDisposableNotification(String text) {
		postNotification(Constants.DISPOSABLE_NOTIFICATON_TITLE, text,
				currentDisposableNotificationId);
		currentDisposableNotificationId++;
	}
	
	public void cancelAll() {
		mNotificationManager.cancelAll();
	}
	
	public void cancelPlayerNotification() {
		mNotificationManager.cancel(Constants.PLAYER_NOTIFICATION_ID);
	}
	
	public void cancelGeneralNotification() {
		mNotificationManager.cancel(Constants.GENERAL_NOTIFICATON_ID);
	}
	
	public void cancelDisposableNotification() {
		for (int i = Constants.DISPOSABLE_NOTIFICATON_ID; i <= currentDisposableNotificationId; i++)
			mNotificationManager.cancel(i);
	}
}
