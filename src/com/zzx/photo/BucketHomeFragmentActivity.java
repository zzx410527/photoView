package com.zzx.photo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.zzx.photo.bean.MediaChooserConstants;

public class BucketHomeFragmentActivity extends FragmentActivity {

	private static Uri fileUri;
	private ArrayList<String> mSelectedImage = new ArrayList<String>();
	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home_media_chooser);

		setFragment(new BucketImageFragment());

	}

	private void setFragment(android.support.v4.app.Fragment f) {
		getSupportFragmentManager().beginTransaction().add(R.id.fl_container, f).commit();
	}

	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), MediaChooserConstants.folderName);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MediaChooserConstants.MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else if (type == MediaChooserConstants.MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == MediaChooserConstants.BUCKET_SELECT_IMAGE_CODE) {
				addMedia(mSelectedImage, data.getStringArrayListExtra("list"));

			} else if (requestCode == MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
				final AlertDialog alertDialog = MediaChooserConstants.getDialog(BucketHomeFragmentActivity.this).create();
				alertDialog.show();

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// Do something after 2000ms
						String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
						android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
						BucketImageFragment bucketImageFragment = (BucketImageFragment) fragmentManager.findFragmentByTag("tab1");
						if (bucketImageFragment != null) {
							bucketImageFragment.getAdapter().addLatestEntry(fileUriString);
							bucketImageFragment.getAdapter().notifyDataSetChanged();
						}
						alertDialog.dismiss();
					}
				}, 5000);

			}
		}
	}

	private void addMedia(ArrayList<String> list, ArrayList<String> input) {
		for (String string : input) {
			list.add(string);
		}
	}

}
