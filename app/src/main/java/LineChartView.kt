package com.example.znotodo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val completedPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val pendingPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val dotPaint = Paint().apply {
        color = Color.BLACK
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

    private val legendBluePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private val legendRedPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    // Demo data (có thể thay bằng setData sau này)
    private val completedData = listOf(5f, 6f, 4f, 5f, 7f, 6f)
    private val pendingData = listOf(3f, 4f, 3f, 2f, 3f, 4f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (completedData.isEmpty() || pendingData.isEmpty()) return

        val padding = 40f
        val legendHeight = 60f
        val usableWidth = width - 2 * padding
        val usableHeight = height - 2 * padding - legendHeight

        val max = (completedData + pendingData).maxOrNull() ?: 1f
        val min = (completedData + pendingData).minOrNull() ?: 0f
        val stepX = usableWidth / (completedData.size - 1)

        drawAxes(canvas, padding, usableWidth, usableHeight, legendHeight)
        drawLine(canvas, completedData, completedPaint, stepX, padding, usableHeight, max, min, legendHeight)
        drawLine(canvas, pendingData, pendingPaint, stepX, padding, usableHeight, max, min, legendHeight)
        drawLegend(canvas)
    }

    private fun drawLine(
        canvas: Canvas,
        data: List<Float>,
        paint: Paint,
        stepX: Float,
        padding: Float,
        usableHeight: Float,
        max: Float,
        min: Float,
        legendHeight: Float
    ) {
        for (i in 0 until data.size - 1) {
            val x1 = padding + i * stepX
            val y1 = padding + (1 - (data[i] - min) / (max - min)) * usableHeight
            val x2 = padding + (i + 1) * stepX
            val y2 = padding + (1 - (data[i + 1] - min) / (max - min)) * usableHeight

            canvas.drawLine(x1, y1, x2, y2, paint)
            canvas.drawCircle(x1, y1, 6f, dotPaint)
            if (i == data.size - 2) canvas.drawCircle(x2, y2, 6f, dotPaint)
        }
    }

    private fun drawAxes(canvas: Canvas, padding: Float, width: Float, height: Float, legendHeight: Float) {
        val left = padding
        val bottom = this.height - padding - legendHeight
        val top = padding
        val right = this.width - padding

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
        canvas.drawRect(startX, startY - squareSize, startX + squareSize, startY, legendBluePaint)
        canvas.drawText("Complete", startX + squareSize + spacing, startY - 5f, legendTextPaint)

        // Unfinished
        val secondX = startX + 200f
        canvas.drawRect(secondX, startY - squareSize, secondX + squareSize, startY, legendRedPaint)
        canvas.drawText("Unfinished", secondX + squareSize + spacing, startY - 5f, legendTextPaint)
    }
}
