package com.dndcharacterbuilder.ui.bitmaputils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View

fun getBitmapFromView (view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    view.layout(view.left, view.top, view.right, view.bottom)
    view.draw(Canvas(bitmap))
    return bitmap
}

// TODO RenderScript is deprecated, but migration guide is unclear
fun Bitmap.blur (context: Context, radius: Float): Bitmap {
    val rs = RenderScript.create(context)
    val alloc = Allocation.createFromBitmap(rs, this)
    ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)).apply {
        setRadius(radius)
        setInput(alloc)
        forEach(alloc)
    }
    alloc.copyTo(this)
    return this
}

fun Bitmap.tint (alpha: Int, color: Int): Bitmap {
    val bitmap = this
    val paint = Paint()
    paint.setARGB(alpha, Color.red(color), Color.green(color), Color.blue(color))
    paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_OVER))
    Canvas(this).apply {
        drawBitmap(bitmap, 0f, 0f, null)
        drawPaint(paint)
    }
    return this
}
