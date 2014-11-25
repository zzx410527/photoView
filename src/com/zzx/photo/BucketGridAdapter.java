/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.zzx.photo;

import java.util.ArrayList;

import com.zzx.photo.bean.BucketEntry;
import com.zzx.photo.bean.MediaChooserConstants;


import net.tsz.afinal.FinalBitmap;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class BucketGridAdapter extends ArrayAdapter<BucketEntry> {

	private Context mContext;
	private ArrayList<BucketEntry> mBucketEntryList;
	private boolean mIsFromVideo;
	private int mWidth;
	private FinalBitmap fb;

	public BucketGridAdapter(Context context, int resource, ArrayList<BucketEntry> categories, boolean isFromVideo) {
		super(context, resource, categories);
		mBucketEntryList = categories;
		mContext = context;
		mIsFromVideo = isFromVideo;
		fb = FinalBitmap.create(context);
	}

	public int getCount() {
		return mBucketEntryList.size();
	}

	@Override
	public BucketEntry getItem(int position) {
		return mBucketEntryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addLatestEntry(String url) {
		int count = mBucketEntryList.size();
		boolean success = false;
		for (int i = 0; i < count; i++) {
			if (mBucketEntryList.get(i).bucketName.equals(MediaChooserConstants.folderName)) {
				mBucketEntryList.get(i).bucketUrl = url;
				success = true;
				break;
			}
		}

		if (!success) {
			BucketEntry latestBucketEntry = new BucketEntry(0, MediaChooserConstants.folderName, url);
			mBucketEntryList.add(0, latestBucketEntry);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
			mWidth = display.getWidth(); // deprecated

			LayoutInflater viewInflater;
			viewInflater = LayoutInflater.from(mContext);
			convertView = viewInflater.inflate(R.layout.view_grid_bucket_item_media_chooser, parent, false);

			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewFromMediaChooserBucketRowView);
			holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextViewFromMediaChooserBucketRowView);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FrameLayout.LayoutParams imageParams = (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
		imageParams.width = mWidth / 2;
		imageParams.height = mWidth / 2;

		holder.imageView.setLayoutParams(imageParams);
		BucketEntry bucketEntry = mBucketEntryList.get(position);
		fb.display(holder.imageView, bucketEntry.bucketUrl);

		holder.nameTextView.setText(bucketEntry.bucketName+"("+bucketEntry.count+")");
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView nameTextView;
	}
}
