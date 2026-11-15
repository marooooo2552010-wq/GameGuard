package com.example.gameguard

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context

object GuardController {
    fun setBlocked(context: Context, blocked: Boolean): Boolean {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admin = ComponentName(context, GuardAdminReceiver::class.java)
        if (!dpm.isDeviceOwnerApp(context.packageName)) return false
        return try {
            dpm.setStatusBarDisabled(admin, blocked)
        } catch (_: SecurityException) {
            false
        } catch (_: Exception) {
            false
        }
    }

    fun isDeviceOwner(context: Context): Boolean {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return dpm.isDeviceOwnerApp(context.packageName)
    }
}