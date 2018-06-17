package com.guard.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.customview
 * 最终要实现的目标
 * @description: https://www.jianshu.com/p/7be162740a95
 * https://mp.weixin.qq.com/s/zRdYyYX0jRC4Toywv1zm9Q
 * @date: 2018/6/11
 * @time: 19:57
 */
class MyGridView : ViewGroup {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)

        //计算出所用的childView的宽和高
        measureChildren(sizeWidth, sizeHeight)

        var width: Int
        var height: Int

        val cCount = childCount

        var cWidth: Int
        var cHeight: Int
        var cParams: MarginLayoutParams? = null

        var lHeight = 0
        var rHeight = 0

        var tWidth = 0
        var bWidth = 0
        for (i in 0 until cCount) {
            val childView = getChildAt(i)
            cWidth = childView.measuredWidth
            cHeight = childView.measuredHeight
            cParams = childView.layoutParams as MarginLayoutParams

            //上面的两个childView
            if ((i == 0) or (i == 1)) {
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin
            }
            if ((i == 2) or (i == 3)) {
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin
            }
            if ((i == 0) or (i == 2)) {
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin
            }
            if ((i == 1) or (i == 3)) {
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin
            }

            width = Math.max(tWidth, bWidth)
            height = Math.max(lHeight, rHeight)

            /**
             * 如果是wrap_content设置为我们计算的值
             * 否则:直接设置为父容器
             */
            val tempWidth = if (widthMode == MeasureSpec.EXACTLY)
                sizeWidth
            else
                width
            val tempHeight = if (heightMode == MeasureSpec.EXACTLY)
                sizeHeight
            else
                height

            setMeasuredDimension(tempWidth, tempHeight)
        }





        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        var cWidth: Int
        var cHeight: Int
        var cParams: MarginLayoutParams
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            cWidth = childView.measuredWidth
            cHeight = childView.measuredHeight
            cParams = childView.layoutParams as MarginLayoutParams
            var cl: Int = 0
            var ct: Int = 0
            var cr: Int = 0
            var cb: Int = 0
            when (i) {
                0 -> {
                    cl = cParams.leftMargin
                    ct = cParams.topMargin
                }
                1 -> {
                    cl = width - cWidth - cParams.leftMargin - cParams.rightMargin
                    ct = cParams.topMargin
                }
                2 -> {
                    cl = cParams.leftMargin
                    ct = height - cHeight - cParams.bottomMargin
                }
                3 -> {
                    cl = width - cWidth - cParams.leftMargin - cParams.rightMargin
                    ct = height - cHeight - cParams.bottomMargin
                }
            }
            cr = cl + cWidth
            cb = ct + cHeight
            childView.layout(cl, ct, cr, cb)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}