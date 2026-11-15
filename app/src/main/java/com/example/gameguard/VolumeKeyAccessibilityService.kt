package com.example.gameguard

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.widget.Toast

class VolumeKeyAccessibilityService : AccessibilityService() {
    private var lastUpPress = 0L
    private var lastDownPress = 0L
    private val doubleTapMs = 450L

    override fun onAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent?) = Unit
    override fun onInterrupt() = Unit

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN || event.repeatCount != 0) {
            return event.keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
                event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
        }
        val now = System.currentTimeMillis()
        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                val isDouble = now - lastUpPress <= doubleTapMs
                lastUpPress = if (isDouble) 0L else now
                if (isDouble) {
                    val ok = GuardController.setBlocked(this, true)
                    showMessage(if (ok) "تم تفعيل وضع اللعب 🔒" else "يلزم إعداد التطبيق كمالك للجهاز")
                }
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val isDouble = now - lastDownPress <= doubleTapMs
                lastDownPress = if (isDouble) 0L else now
                if (isDouble) {
                    val ok = GuardController.setBlocked(this, false)
                    showMessage(if (ok) "تم إلغاء وضع اللعب" else "يلزم إعداد التطبيق كمالك للجهاز")
                }
                return true
            }
        }
        return super.onKeyEvent(event)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}