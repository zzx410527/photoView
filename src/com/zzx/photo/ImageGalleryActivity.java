package com.zzx.photo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class ImageGalleryActivity extends FragmentActivity {
	private ImageFragment imageFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_grally);
		imageFragment = new ImageFragment();
		if(getIntent()!=null&&(getIntent().getBooleanExtra("isFromBucket", false))){
			Bundle bundle = new Bundle();
			bundle.putString("name", getIntent().getStringExtra("name"));
			bundle.putString("bucketId", getIntent().getStringExtra("bucketId"));
			imageFragment.setArguments(bundle);
		}
		setFragment(imageFragment);
		
	}
	
	private void setFragment(android.support.v4.app.Fragment f)
	{
		getSupportFragmentManager().beginTransaction()
		.add(R.id.fl_content_container, f).commit();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}
