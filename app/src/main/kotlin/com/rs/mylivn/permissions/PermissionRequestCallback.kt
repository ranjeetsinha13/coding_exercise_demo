package com.rs.mylivn.permissions

interface PermissionRequestCallback {

    fun onPermissionGranted(permissions: List<String>)

    fun onPermissionDenied(permissions: List<String>)

}