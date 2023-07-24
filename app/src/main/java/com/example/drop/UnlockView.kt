package com.example.drop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class UnlockView(context: Context,attrs:AttributeSet?):View(context,attrs) {
    private var mRadius = dp2px(40)
    private var mSpace = dp2px(10)
    private var startCx = 0f
    private var startCy = 0f
    private val mDotPaint:Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = resources.getColor(R.color.water,null)
            style = Paint.Style.FILL
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //宽
        var mWidth = 0
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        mWidth = when(widthMode){
            MeasureSpec.EXACTLY -> widthSize
            else ->6*mRadius+4*mSpace
        }

        //高
        var mHeight = 0
        val heightMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(widthMeasureSpec)
        mHeight= when(heightMode){
            MeasureSpec.EXACTLY -> heightSize
            else ->6*mRadius+4*mSpace
        }
    }

    //计算
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mRadius = (Math.min(height,width)-4*mSpace)/6
        startCx = (width-squareSize())/2+mSpace+mRadius.toFloat()
        startCy = (width-squareSize())/2+mSpace+mRadius.toFloat()

    }

    override fun onDraw(canvas: Canvas?) {
        drawNineDot(canvas)
    }

    private fun drawNineDot(canvas: Canvas?){
        for (i in 0 until 3)
            for (j in 0 until 3)
            {
                val cx = startCx+j*(2*mRadius+mSpace)
                val cy = startCy+i*(2*mRadius+mSpace)
                canvas?.drawCircle(cx,cy,mRadius.toFloat(),mDotPaint)
            }
    }

    private fun squareSize() = Math.min(height,width)

}






fun View.dp2px(dp:Int):Int{
    return (context.resources.displayMetrics.density*dp).toInt()
}