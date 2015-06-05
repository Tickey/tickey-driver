package com.tickey.app.utility;

import android.app.Activity;
import android.util.Log;

import com.tickey.BuildConfig;

public class MyLog {

	/**
	 * Send a {@link #VERBOSE} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int v(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			return Log.v(tag, msg);
		} else {
			return 0;
		}
	}

	/**
	 * Send a {@link #VERBOSE} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int v(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG) {
			return Log.v(tag, msg, tr);
		} else {
			return 0;
		}
	}
	
	public static int v(Activity activity, String msg) {
		if (BuildConfig.DEBUG) {
			return Log.v(activity.getClass().getSimpleName(), msg);
		} else {
			return 0;
		}
	}
	
	/**
	 * Send a {@link #DEBUG} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int d(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			return Log.d(tag, msg);
		} else {
			return 0;
		}
	}

	/**
	 * Send a {@link #DEBUG} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int d(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG) {
			return Log.d(tag, msg, tr);
		} else {
			return 0;
		}
	}

	/**
	 * Send an {@link #INFO} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int i(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			return Log.i(tag, msg);
		} else {
			return 0;
		}
	}

	/**
	 * Send a {@link #INFO} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int i(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG) {
			return Log.i(tag, msg, tr);
		} else {
			return 0;
		}
	}

	/**
	 * Send a {@link #WARN} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int w(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			return Log.w(tag, msg);
		} else {
			return 0;
		}
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int w(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG) {
			return Log.w(tag, tr);
		} else {
			return 0;
		}
	}

	/*
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag Used to identify the source of a log message. It usually
	 * identifies the class or activity where the log call occurs.
	 * 
	 * @param tr An exception to log
	 */
	public static int w(String tag, Throwable tr) {
		if (BuildConfig.DEBUG) {
			return Log.w(tag, tr);
		} else {
			return 0;
		}
	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int e(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			return Log.e(tag, msg);
		} else {
			return 0;
		}
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int e(String tag, String msg, Throwable tr) {
		if (BuildConfig.DEBUG) {
			return Log.e(tag, msg, tr);
		} else {
			return 0;
		}
	}

}
