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

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;


public class GridViewAdapter extends ArrayAdapter<MediaModel> {

	private Context mContext;
	private List<MediaModel> mGalleryModelList;
	private int mWidth;
	private boolean mIsFromVideo;
	private FinalBitmap fb;


	public GridViewAdapter(Context context, int resource, List<MediaModel> categories, boolean isFromVideo) {
		super(context, resource, categories);
		mGalleryModelList = categories;
		mContext          = context;
		mIsFromVideo      = isFromVideo;
		fb = FinalBitmap.create(context);
	}

	public int getCount() {
		return mGalleryModelList.size();
	}

	@Override
	public MediaModel getItem(int position) {
		return mGalleryModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay(); 
			mWidth = display.getWidth();  // deprecated

			LayoutInflater viewInflater;
			viewInflater = LayoutInflater.from(getContext());
			convertView = viewInflater.inflate(R.layout.view_grid_item_media_chooser, parent, false);

			holder = new ViewHolder();
			holder.checkBoxTextView   = (CheckedTextView) convertView.findViewById(R.id.checkTextViewFromMediaChooserGridItemRowView);
			holder.imageView          = (ImageView) convertView.findViewById(R.id.imageViewFromMediaChooserGridItemRowView);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LayoutParams imageParams = (LayoutParams) holder.imageView.getLayoutParams();
		imageParams.width  = mWidth/2;
		imageParams.height = mWidth/2;

		holder.imageView.setLayoutParams(imageParams);
//		DisplayImageOptions image_options = new DisplayImageOptions.Builder()
//		.cacheOnDisc()
//		.cacheInMemory()
//		.resetViewBeforeLoading()
//		.imageScaleType(ImageScaleType.EXACT)
//		.build();
//		ImageLoader.getInstance().displayImage(mGalleryModelList.get(position).url, holder.imageView,image_options);
		fb.display(holder.imageView, mGalleryModelList.get(position).url);
		holder.checkBoxTextView.setChecked(mGalleryModelList.get(position).status);
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		CheckedTextView checkBoxTextView;
	}

}
