package com.example.znotodo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val greenPaint = Paint().apply {
        color = Color.parseColor("#4CAF50")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val redPaint = Paint().apply {
        color = Color.parseColor("#F44336")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val axisPaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 2f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val legendTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        isAntiAlias = true
    }

    private val legendGreenPaint = Paint().apply {
        color = Color.parseColor("#4CAF50")
        style = Paint.Style.FILL
    }

    private val legendRedPaint = Paint().apply {
        color = Color.parseColor("#F44336")
        style = Paint.Style.FILL
    }

    private val data = listOf(
        4 to 2, 3 to 1, 5 to 1, 2 to 2, 6 to 2
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val legendHeight = 60f
        val padding = 40f
        val usableHeight = height - legendHeight - 2 * padding
        val barWidth = width / (data.size * 3f)
        val spacing = barWidth

        val maxVal = data.flatMap { listOf(it.first, it.second) }.maxOrNull()?.toFloat() ?: 1f

        val bottom = height - legendHeight - padding

        data.forEachIndexed { i, (completed, pending) ->
            val baseX = i * (2 * barWidth + spacing) + spacing
            val completedHeight = completed / maxVal * usableHeight
            val pendingHeight = pending / maxVal * usableHeight

            canvas.drawRect(
                baseX,
                bottom - completedHeight,
                baseX + barWidth,
                bottom,
                greenPaint
            )

            canvas.drawRect(
                baseX + barWidth,
                bottom - pendingHeight,
                baseX + 2 * barWidth,
                bottom,
                redPaint
            )
        }

        drawAxes(canvas, padding, bottom)
        drawLegend(canvas)
    }

    private fun drawAxes(canvas: Canvas, padding: Float, bottom: Float) {
        val left = padding
        val right = width - padding
        val top = padding

        // Trục Y
        canvas.drawLine(left, top, left, bottom, axisPaint)

        // Trục X
        canvas.drawLine(left, bottom, right, bottom, axisPaint)
    }

    private fun drawLegend(canvas: Canvas) {
        val squareSize = 30f
        val spacing = 20f
        val startX = 40f
        val startY = height - 30f

        // Completed
        canvas.drawRect(startX, startY - squareSize, startX + squareSize, startY, legendGreenPaint)
        canvas.drawText("Complete", startX + squareSize + spacing, startY - 5f, legendTextPaint)

        // Unfinished
        val secondX = startX + 200f
        canvas.drawRect(secondX, startY - squareSize, secondX + squareSize, startY, legendRedPaint)
        canvas.drawText("Unfinished", secondX + squareSize + spacing, startY - 5f, legendTextPaint)
    }
}
