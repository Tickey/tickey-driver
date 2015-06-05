package com.tickey.app.utility;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;


public abstract class NNAsyncTask extends AsyncTask<String, Integer, Boolean>{

public abstract boolean onLoad();
public abstract boolean onPostLoad();
private ProgressDialog dialog;
private Context context;
private boolean showLoadingDialog;
public boolean result;


public void onPreload() { }

public NNAsyncTask(Context context, boolean showLoadingDialog) {
	this.context = context;
	this.showLoadingDialog = showLoadingDialog;
}


@Override
protected void onPreExecute() {
	  if (dialog != null) {
		  dialog.dismiss();
		  dialog = null;
	  }
	 
	  if (showLoadingDialog && context != null) {
    	 dialog = new ProgressDialog(context);
    	 dialog.setCancelable(false);
    	 dialog.setCanceledOnTouchOutside(false);
    	 dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});
         dialog.setMessage("Loading...");
         dialog.show();
	  }
	  
	  onPreload();
}

@Override
protected Boolean doInBackground(String... params) {
	// TODO Auto-generated method stub
	result = onLoad();
	return result;
}
@Override
protected void onPostExecute(Boolean result) {
	// TODO Auto-generated method stub
	if (dialog != null && dialog.isShowing()) {
	     dialog.dismiss();
	     dialog = null;
	}
	super.onPostExecute(onPostLoad());
}
	
}
