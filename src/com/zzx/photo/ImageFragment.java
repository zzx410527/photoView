package com.zzx.photo;
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




import java.util.ArrayList;



import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;



public class ImageFragment extends Fragment {
	private ArrayList<String> mSelectedItems = new ArrayList<String>();
	private ArrayList<MediaModel> mGalleryModelList;
	private GridView mImageGridView;
	private View mView;
//	private OnImageSelectedListener mCallback;
	private GridViewAdapter mImageAdapter;
	private Cursor mImageCursor;
	private ArrayList<String> mUrlList = new ArrayList<String>();

	// Container Activity must implement this interface
	public interface OnImageSelectedListener {
		public void onImageSelected(int count);
	}

//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//
//		// This makes sure that the container activity has implemented
//		// the callback interface. If not, it throws an exception
//		try {
//			mCallback = (OnImageSelectedListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString() + " must implement OnImageSelectedListener");
//		}
//	}

	public ImageFragment(){
		setRetainInstance(true);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(mView == null){
			mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);

			mImageGridView = (GridView) mView.findViewById(R.id.gridViewFromMediaChooser);


			if (getArguments() != null) {
				initPhoneImages(getArguments().getString("name"),getArguments().getString("bucketId"));
			}else{
				initPhoneImages();
			}

		}else{
			((ViewGroup) mView.getParent()).removeView(mView);
			if(mImageAdapter == null || mImageAdapter.getCount() == 0){
				Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
			}
		}

		return mView;
	}


	private void initPhoneImages(String bucketName,String bucketId){
		try {
			final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
			String searchParams = null;
			String bucket = bucketName;
//			searchParams = "bucket_display_name = \"" + bucket + "\"";
			searchParams = "bucket_id  = \""+bucketId+ "\"";
			final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,MediaStore.Images.Media.DISPLAY_NAME};
			mImageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, searchParams, null, orderBy + " DESC");

			setAdapter(mImageCursor);
			mImageCursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initPhoneImages() {
		try {
			final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
			final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,MediaStore.Images.Media.DISPLAY_NAME};
			mImageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
			setAdapter(mImageCursor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void setAdapter(Cursor imagecursor) {

		if(imagecursor.getCount() > 0){
			///storage/sdcard0/Toast/download/T1bRVvBCWv1RXrhCrK_l.jpg
			///storage/sdcard1/Toast/download/4_1.png
			mGalleryModelList = new ArrayList<MediaModel>();
			for (int i = 0; i < imagecursor.getCount(); i++) {
				imagecursor.moveToPosition(i);
				int dataColumnIndex       = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
				int nameColumuIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
				String url = imagecursor.getString(dataColumnIndex).toString();
				String name = imagecursor.getString(nameColumuIndex);
				MediaModel galleryModel   = new MediaModel(url, false,name);
				mGalleryModelList.add(galleryModel);
				mUrlList.add(url);
			}


			mImageAdapter = new GridViewAdapter(getActivity(), 0, mGalleryModelList, false);
			mImageGridView.setAdapter(mImageAdapter);
		}else{
			Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
		}

		mImageGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// update the mStatus of each category in the adapter
				GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
				MediaModel galleryModel = (MediaModel) adapter.getItem(position);

				String nameString = mUrlList.get(position);
				Intent intent = new Intent(getActivity(),ViewPagerActivity.class);
				intent.putExtra("imageurl", mUrlList);
				intent.putExtra("currentPosition", position);
				startActivity(intent);
				

				// inverse the status
				galleryModel.status = ! galleryModel.status;

				adapter.notifyDataSetChanged();


//				if (mCallback != null) {
//					mCallback.onImageSelected(mSelectedItems.size());
//					Intent intent = new Intent();
//					intent.putStringArrayListExtra("list", mSelectedItems);
//					getActivity().setResult(Activity.RESULT_OK, intent);
//				}

			}
		});
	}

	public ArrayList<String> getSelectedImageList() {
		return mSelectedItems;
	}

	public void addItem(String item) {
		if(mImageAdapter != null){
			MediaModel model = new MediaModel(item, false,"");
			mGalleryModelList.add(0, model);
			mImageAdapter.notifyDataSetChanged();
		}else{
			initPhoneImages();
		}
	}
}
