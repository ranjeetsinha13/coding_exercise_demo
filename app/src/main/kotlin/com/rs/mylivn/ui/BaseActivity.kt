package com.rs.mylivn.ui

import android.os.Bundle
import com.rs.mylivn.permissions.PermissionCompatActivity


abstract class BaseActivity : PermissionCompatActivity() {

    protected abstract val TAG: String

    protected abstract fun getLayout(): Int

    protected abstract fun getActivityTitle(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        setTitle(getActivityTitle())


    }

}