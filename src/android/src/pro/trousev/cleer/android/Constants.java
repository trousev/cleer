package pro.trousev.cleer.android;

public final class Constants {
	public static final String LOG_TAG = "CleerAndroid";
	// FIXME: Add path to database
	public static final String PACKAGE_NAME = "pro.trousev.cleer.android.userInterface";
	// FIXME: replace it with method
	public static final String DATABASE_PATH = String.format(
			"//data//data//%s//databases//Library.db", PACKAGE_NAME);
	public static final String PLAYER_NOTIFICATION_TITLE = "Cleer Player";
	public static final String GENERAL_NOTIFICATION_TITLE = "Cleer";
	public static final String DISPOSABLE_NOTIFICATON_TITLE = "Cleer Player";
	public static final int GENERAL_NOTIFICATON_ID = 2;
	public static final int PLAYER_NOTIFICATION_ID = 1;
	public static final int DISPOSABLE_NOTIFICATON_ID = 3;
	public static final int PROGRESSBAR_TIMER_RATE = 1000; // in milliseconds
}
