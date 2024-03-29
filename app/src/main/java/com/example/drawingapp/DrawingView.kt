package com.example.drawingapp

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import java.lang.ProcessBuilder.Redirect.Type


class DrawingView(context:Context,attrs:AttributeSet):View(context,attrs) {

      private var mDrawPath:CustomPth?=null
      private var mCanvasBitmap:Bitmap?=null
      private var mDrawPaint: Paint?=null
      private var mCanvasPaint:Paint?=null
      private var mBrusheSize:Float=0.toFloat()
      private var color= Color.BLACK
      private var canvas:Canvas?=null
      private val paths=ArrayList<CustomPth>()
      private val undoPaths=ArrayList<CustomPth>()

       init {
           setUpDrawing()
       }

      private fun setUpDrawing(){

          mDrawPaint=Paint()
          mDrawPath=CustomPth(color,mBrusheSize)
          mDrawPaint!!.color=color
          mDrawPaint!!.style=Paint.Style.STROKE
          mDrawPaint!!.strokeJoin=Paint.Join.ROUND
          mDrawPaint!!.strokeCap=Paint.Cap.ROUND
          mCanvasPaint=Paint(Paint.DITHER_FLAG)
         // mBrusheSize=20.toFloat()
          paths


      }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas=Canvas(mCanvasBitmap!!)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas!!.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)

        for(path in paths){
            mDrawPaint!!.strokeWidth=path.brushThickness
            mDrawPaint!!.color=path.color
            canvas.drawPath(path, mDrawPaint!!)
        }

        if(!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth=mDrawPath!!.brushThickness
            mDrawPaint!!.color=mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val touchx=event?.x
        val touchy=event?.y

        when(event?.action){

            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness - mBrusheSize
                mDrawPath!!.reset()
                if (touchx != null) {
                    if (touchy != null) {
                        mDrawPath!!.moveTo(touchx, touchy)
                    }
                }
            }
            MotionEvent.ACTION_MOVE ->{
                if (touchx != null) {
                    if (touchy != null) {
                        mDrawPath!!.lineTo(touchx,touchy)
                    }
                }
            }
           MotionEvent.ACTION_UP->{
               paths.add(mDrawPath!!)
               mDrawPath=CustomPth(color,mBrusheSize)
           }
            else-> return false
        }
        invalidate()
        return true
    }

    fun setSizeForBrush(newSize:Float){
        mBrusheSize=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        newSize,resources.displayMetrics)
        mDrawPaint!!.strokeWidth=mBrusheSize
    }

    fun setColor(newColor:String){
        color=Color.parseColor(newColor)
        mDrawPaint!!.color=color
    }

    fun onClickUndo(){
        if(paths.size>0){
            undoPaths.add(paths.removeAt(paths.size-1))
            invalidate()
        }
    }


     internal inner class CustomPth(var color:Int,var brushThickness:Float):Path(){

     }



}