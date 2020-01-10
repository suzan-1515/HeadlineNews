package com.cognota.feed.list.adapter

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class CirclePagerIndicatorDecoration : ItemDecoration() {
    private val colorActive = Color.WHITE
    private val colorInactive = Color.parseColor("#BBAFAC")
    private val colorTransition = Color.GRAY
    private var lastActivePosition = 0

    private val DP: Float = Resources.getSystem().displayMetrics.density

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private val mIndicatorHeight = (DP * 15).toInt()

    /**
     * Inactive Indicator radiius.
     */
    private val itemRadius = DP * 4
    /**
     * Padding between indicators.
     */
    private val mIndicatorItemPadding = DP * 8

    /**
     * Some more natural animation interpolation
     */
    private val mInterpolator: Interpolator = AccelerateDecelerateInterpolator()

    private val mPaint = Paint()
    private val mInactivePaint = Paint()

    init {
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true

        mInactivePaint.strokeCap = Paint.Cap.ROUND
        mInactivePaint.style = Paint.Style.STROKE
        mInactivePaint.strokeWidth = 2f
        mInactivePaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val itemCount = parent.adapter!!.itemCount
        if (itemCount > 1) {
            val totalLength = itemRadius * 2 * itemCount
            val paddingBetweenItems =
                0.coerceAtLeast(itemCount - 1) * mIndicatorItemPadding
            val indicatorTotalWidth = totalLength + paddingBetweenItems
            val indicatorStartX = (parent.width - indicatorTotalWidth) / 2f
            // center vertically in the allotted space
            val indicatorPosY = parent.height - mIndicatorHeight / 1f
            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)
            // find active page (which should be highlighted)
            val layoutManager =
                parent.layoutManager as LinearLayoutManager?
            val activePosition = layoutManager!!.findFirstVisibleItemPosition()
            if (activePosition == RecyclerView.NO_POSITION) {
                return
            }
            // find offset of active page (if the user is scrolling)
            val activeChild: View? = layoutManager.findViewByPosition(activePosition)
            val left: Int = activeChild?.left ?: 0
            val width: Int = activeChild?.width ?: 0
            // on swipe the active item will be positioned from [-width, 0]
// interpolate offset for smooth animation
            val progress: Float =
                mInterpolator.getInterpolation(left * -1 / width.toFloat())
            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress)
        }
        // center horizontally, calculate width and subtract half from center
    }

    private fun drawInactiveIndicators(
        c: Canvas,
        indicatorStartX: Float,
        cenY: Float,
        itemCount: Int
    ) {
        mInactivePaint.color = colorActive
        // width of item indicator including padding
        val itemWidth = itemRadius * 2 + mIndicatorItemPadding
        var cenX = indicatorStartX + itemRadius
        for (i in 0 until itemCount) { // draw the circle for every item
            c.drawCircle(cenX, cenY, itemRadius, mInactivePaint)
            cenX += itemWidth
        }
    }

    private fun drawHighlights(
        c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
        highlightPosition: Int, progress: Float
    ) { // width of item indicator including padding
        var highlightPosition = highlightPosition
        val itemWidth = itemRadius * 2 + mIndicatorItemPadding
        if (progress == 0f) { // no swipe, draw a normal indicator
            mPaint.color = colorActive
            lastActivePosition = highlightPosition
        } else {
            if (lastActivePosition > highlightPosition) {
                highlightPosition = lastActivePosition
            }
            mPaint.color = colorTransition
        }
        val highlightStart = indicatorStartX + itemWidth * highlightPosition
        c.drawCircle(highlightStart + itemRadius, indicatorPosY, itemRadius, mPaint)
    }

}