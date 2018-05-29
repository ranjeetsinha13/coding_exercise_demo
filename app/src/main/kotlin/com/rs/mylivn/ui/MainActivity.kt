package com.rs.mylivn.ui

import android.Manifest
import android.annotation.TargetApi
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import com.rs.mylivn.*
import com.rs.mylivn.entity.ImageItem
import com.rs.mylivn.permissions.PermissionRequestCallback
import com.rs.mylivn.viewmodels.ImagesViewModel
import com.rs.mylivn.viewmodels.ImagesViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment_container.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    override val TAG: String = MainActivity::class.java.name

    override fun getLayout() = R.layout.main_activity

    override fun getActivityTitle() = R.string.app_name

    @Inject
    lateinit var viewModelFactory: ImagesViewModelFactory
    private lateinit var viewModel: ImagesViewModel
    var isLoading: Boolean = true

    private val permissionRequestCallback = object : PermissionRequestCallback {
        override fun onPermissionGranted(permissions: List<String>) {
            Log.i(TAG, "Permission granted [ $permissions ]")
            // Do initial setup;
            this@MainActivity.doInitialSetup()
            loadImages()

        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onPermissionDenied(permissions: List<String>) {
            Log.e(TAG, "Permission denied [ $permissions ]")

            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show Rationale
                Log.i(TAG, "Displaying permission rationale to provide additional context.")

                main_layout.getSnackBar(getString(R.string.permission_rationale), Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, {
                    requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, callback =
                    this)
                }).show()
            } else {
                main_layout.getSnackBar(getString(R.string.permission_rationale), Snackbar.LENGTH_INDEFINITE)
                        .setAction(resources.getString(R.string.settings), {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                            startActivity(intent)
                        }).show()


            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        App.appComponent.inject(this)

        checkWriteSDCardPermission()

        images_data.layoutManager = GridLayoutManager(this, 3)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImagesViewModel::class.java)


        //viewModel.addImage("https://www.pexels.com/photo/scenic-view-of-beach-248797/")
        //viewModel.deleteImage("59c08988-1f6a-4e56-96bf-8a08d07438b3")

    }

    private fun loadImages() {
        viewModel.getImages().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
            Log.d(TAG, "images....$it")
            showImages(it)
        }, { throwable: Throwable? ->
            throwable?.printStackTrace()
            this.toast(getString(R.string.error_with_fetching_data_message))
        }
        )

    }

    private fun showImages(images: List<ImageItem>) {
        // Handle loading of images here
        progress_toolbar.visibility = View.GONE
        images_data.adapter = ImagesRecyclerViewAdapter(this, images as MutableList<ImageItem>, viewModel)
        val itemTouchHelper = ItemTouchHelper(DragManageAdapter(
                images_data.adapter as ImagesRecyclerViewAdapter,
                ItemTouchHelper.UP.or(ItemTouchHelper.DOWN.or(ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))),
                ItemTouchHelper.UP.or(ItemTouchHelper.DOWN.or(ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)))))
        itemTouchHelper.attachToRecyclerView(images_data)

    }


    private fun checkWriteSDCardPermission() {
        // Request Needed Permissions;
        requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, callback = permissionRequestCallback)
    }


}