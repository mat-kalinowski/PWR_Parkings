package com.example.parkingipwr

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class MenuView(context: Context, attributeSet: AttributeSet)
    : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private var viewHeight = 1210
    private val viewWidth = 700
    private val verticalRectWidth = viewWidth/70
    private val verticalRectHeight = (viewHeight * 0.9).toInt()

    private val rectWidth = (viewWidth * 0.4).toInt()
    private val rectHeight = (0.4 * rectWidth).toInt()

    val triangleWidth = (viewWidth * 0.05).toInt()

    val textPadding = rectWidth/10


    init {
        holder.addCallback(this)
    }



    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    override fun surfaceCreated(holder: SurfaceHolder?) {
        setBackgroundColor(context.resources.getColor(R.color.colorBackground))
        var canvas = holder!!.lockCanvas()
        draw(canvas)
        holder!!.unlockCanvasAndPost(canvas)
    }



    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return
        val paint = Paint()

        paint.color = resources.getColor(R.color.colorAccent)

        val verticalRect = Rect(viewWidth/2 - verticalRectWidth/2, viewHeight - verticalRectHeight,
                                    viewWidth/2 + verticalRectWidth/2, viewHeight  )
        val topRect = Rect(viewWidth/2, viewHeight - verticalRectHeight,
                            viewWidth/2 + rectWidth, viewHeight - verticalRectHeight + rectHeight)

        val pathTop = Path()
        pathTop.setFillType(Path.FillType.EVEN_ODD);
        pathTop.moveTo(topRect.right.toFloat(), topRect.top.toFloat())
        pathTop.lineTo(topRect.right.toFloat() + triangleWidth, topRect.top.toFloat() + rectHeight/2)
        pathTop.lineTo(topRect.right.toFloat(), topRect.bottom.toFloat())
        pathTop.lineTo(topRect.right.toFloat(), topRect.top.toFloat())
        pathTop.close()

        val bottomRect = Rect(viewWidth/2 - rectWidth, viewHeight - verticalRectHeight + rectHeight,
            viewWidth/2, viewHeight - verticalRectHeight + 2*rectHeight)


        val pathBottom = Path()
        pathBottom.setFillType(Path.FillType.EVEN_ODD);
        pathBottom.moveTo(bottomRect.left.toFloat(), bottomRect.top.toFloat())
        pathBottom.lineTo(bottomRect.left.toFloat() - triangleWidth, bottomRect.top.toFloat() + rectHeight/2)
        pathBottom.lineTo(bottomRect.left.toFloat(), bottomRect.bottom.toFloat())
        pathBottom.lineTo(bottomRect.left.toFloat(), bottomRect.top.toFloat())
        pathBottom.close()


        canvas.drawRect(verticalRect, paint)
        canvas.drawRect(topRect, paint)
        canvas.drawRect(bottomRect, paint)
        canvas.drawRect(verticalRect, paint)




        paint.color = resources.getColor(R.color.colorAccent)
        paint.setStyle(Paint.Style.FILL_AND_STROKE)
        paint.setAntiAlias(true)
        canvas.drawPath(pathTop, paint)
        canvas.drawPath(pathBottom, paint)

        paint.color = resources.getColor(R.color.white)
        paint.textSize = 40f

        canvas.drawText("Obecny stan parkingów", topRect.left.toFloat() + textPadding, topRect.top.toFloat() + textPadding, paint)


    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("touch", "touch " + event.x.toString() + "   " + event.y.toString())
            }
        }
        return true
    }



}
