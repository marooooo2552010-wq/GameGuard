package com.example.gameguard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pad = (20 * resources.displayMetrics.density).toInt()
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(pad, pad, pad, pad)
        }
        fun text(value: String, size: Float = 17f) = TextView(this).apply {
            this.text = value
            textSize = size
            gravity = Gravity.CENTER
            setPadding(0, pad / 2, 0, pad / 2)
        }
        layout.addView(text("🎮 Game Guard", 28f))
        layout.addView(text("مرتين على رفع الصوت (+): قفل شريط الإشعارات\nمرتين على خفض الصوت (-): إلغاء القفل"))
        layout.addView(text(
            if (GuardController.isDeviceOwner(this))
                "حالة الإدارة: تم إعداد مالك الجهاز"
            else
                "حالة الإدارة: لم يتم إعداد مالك الجهاز"
        ))
        layout.addView(Button(this).apply {
            this.text = "فتح إعدادات إمكانية الوصول"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        })
        layout.addView(Button(this).apply {
            this.text = "اختبار تفعيل منع شريط الإشعارات"
            setOnClickListener {
                val ok = GuardController.setBlocked(this@MainActivity, true)
                android.widget.Toast.makeText(
                    this@MainActivity,
                    if (ok) "تم التفعيل" else "لم ينجح: اضبط التطبيق كمالك للجهاز أولاً",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        })
        layout.addView(Button(this).apply {
            this.text = "إلغاء منع شريط الإشعارات"
            setOnClickListener { GuardController.setBlocked(this@MainActivity, false) }
        })
        layout.addView(text(
            "مهم: أندرويد لا يسمح للتطبيق العادي بمنع لوحة الإشعارات بالكامل. " +
            "يلزم إعداد Device Owner عبر ADB، وهذا الإجراء غالباً يتطلب جهازاً جديداً أو إعادة ضبط المصنع. " +
            "أثناء تشغيل خدمة الاختصارات سيتم استهلاك ضغطات الصوت؛ استخدم أزرار الصوت من لوحة النظام عند الحاجة.",
            14f
        ))
        setContentView(layout)
    }
}