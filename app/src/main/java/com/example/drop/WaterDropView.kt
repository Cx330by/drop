package com.example.drop

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build

import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.getMode
import android.view.View.MeasureSpec.getSize
import kotlin.math.cos
import kotlin.math.sin

class WaterDropView(context: Context, attrs: AttributeSet): View(context,attrs) {
    private var mDefaultColor = context.getColor(R.color.bottle)
    private var mWaterColor = context.getColor(R.color.water)
    private var mStrokeColor: Int
    private var mRadius = dp2px(500).toInt()
    private var mPath = Path()
    private var mCircleStrokeWidth = dp2px(8).toInt()
    private var mCircleCx = 0f
    private var mCircleCy = 0f



    private var mStartX = 0f
    private var mStartY= 0f
    private var mEndX = 0f
    private var mEndY= 0f

    private val mWaveHeight = dp2px(50)
    private var firstStretchPointX = 0f
    private var mCurrentWaterLineWidth = 0f

        set(value) {
            field = value

        }

    private var mAngle = 90f
        set(value) {
            field = value
            mCurrentWaterLineWidth = sin(value)*mRadius*2
            invalidate()
        }




    private val mCircleStrokePaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = mDefaultColor
            style = Paint.Style.STROKE
            strokeWidth = mCircleStrokeWidth.toFloat()
        }
    }
    private val mCirclePaint:Paint by lazy {
    Paint().apply {
        isAntiAlias = true
        color = mWaterColor
        style = Paint.Style.FILL
    }
}
    init {
    context.obtainStyledAttributes(attrs,R.styleable.WaterDropView).apply {
        mWaterColor = getColor(R.styleable.WaterDropView_water_color,mDefaultColor)
        mStrokeColor = getColor(R.styleable.WaterDropView_stroke_color,mDefaultColor)
        recycle()
    }
}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var mWidth = 0
        var mHeight = 0

        mWidth = when(getMode(widthMeasureSpec)){
            MeasureSpec.EXACTLY -> getSize(widthMeasureSpec)
            else -> (2*mRadius)
        }
        mHeight = when(getMode(widthMeasureSpec)){
            MeasureSpec.EXACTLY -> getSize(widthMeasureSpec)
            else -> (3*mRadius)
        }
        setMeasuredDimension(mWidth,mHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!(2*mRadius == width && 3*mRadius == height)){
            mRadius = width/2
            if (3*mRadius < height){

            }else{
                mRadius = height/3
            }
        }

       mRadius -= mCircleStrokeWidth
        mCircleCx = width.toFloat()/2
        mCircleCy = height - mRadius.toFloat()-mCircleStrokeWidth
    }

    override fun onDraw(canvas: Canvas?) {
        drawCircleStroke(canvas)
        drawWater(canvas)
    }

    private fun drawCircleStroke(canvas: Canvas?){
        canvas?.drawCircle(mCircleCx,mCircleCy,mRadius.toFloat(),mCircleStrokePaint)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE){
            ValueAnimator.ofFloat(0f,mCurrentWaterLineWidth).apply {
                duration = 500
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
                addUpdateListener {
                    firstStretchPointX =it.animatedValue as Float
                    invalidate()
                }
                start()
            }
        }
    }

    private  fun drawWater(canvas: Canvas?){
        mStartX = mRadius*(1- sin(mAngle.toDouble())).toFloat()
        mStartY = height - mCircleStrokeWidth - mRadius*(1- cos(mAngle))
        mEndX = mRadius*(1+ sin(mAngle.toDouble())).toFloat()
        mEndY = mStartY
        mPath.reset()
        mPath.moveTo(mStartX,mStartY)
        mPath.addArc(mCircleCx - mRadius,mCircleCy-mRadius,
            mCircleCx+mRadius,mCircleCy+mRadius,
            90-mAngle,2*mAngle)

        mPath.cubicTo(
            firstStretchPointX,mWaveHeight-mWaveHeight,
            firstStretchPointX+ sin(mAngle)*mRadius,mStartY+mWaveHeight,
            mStartX,mStartY)


        mPath.close()

        canvas?.drawPath(mPath,mCirclePaint)
    }

    private fun startDegreeAnimation(){
    }










    //TOOLS
    private fun dp2px(dp: Int) = context.resources.displayMetrics.density * dp
    fun colorToAlphaColor(alpha:Int,color:Int):Int{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color))
        }else{
            color
        }
    }


}