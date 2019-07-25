package ru.s4nchez.brainagedefinition

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.collections.ArrayList

class GameView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var numbers = ArrayList<Number>()
    private var random = Random()
    private var clickListener: ClickListener? = null

    private val numberPaint = Paint()
    private val circlePaint = Paint()
    private val numberBounds = Rect()

    companion object {
        private const val CELL_SIZE = 175
        private const val FONT_SIZE = 72.0f
        private const val STROKE_WIDTH = 20.0f
    }

    init {
        numberPaint.textSize = 3 * FONT_SIZE
        numberPaint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        numberPaint.color = Color.BLACK
        circlePaint.color = Color.BLACK
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = STROKE_WIDTH
    }

    fun setListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    fun hideNumbers() {
        numbers.forEach { it.isVisible = false }
        invalidate()
    }

    fun showNumbers() {
        numbers.forEach { it.isVisible = true }
        invalidate()
    }

    fun showNumber(number: Int) {
        numbers.forEach { if (it.number == number) it.isVisible = true }
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = resolveSize(calculateDesiredWidth(), widthMeasureSpec)
        val measureHeight = resolveSize(calculateDesiredHeight(), heightMeasureSpec)
        setMeasuredDimension(measureWidth, measureHeight)
    }

    private fun calculateDesiredWidth(): Int {
        return suggestedMinimumWidth + paddingLeft + paddingRight
    }

    private fun calculateDesiredHeight(): Int {
        return suggestedMinimumHeight + paddingTop + paddingBottom
    }

    fun setNumbers(numbers: List<Int>) {
        this.numbers.clear()
        numbers.forEach {

            val pair = generateLeft()
            val left = pair.first
            val right = left + CELL_SIZE
            val top = pair.second
            val bottom = top + CELL_SIZE

            this.numbers.add(Number(
                    number = it,
                    left = left,
                    right = right,
                    top = top,
                    bottom = bottom,
                    isVisible = true))
        }
        invalidate()
    }

    private fun generateLeft(): Pair<Int, Int> {
        while (true) {
            val left = random.nextInt(width - CELL_SIZE)
            val top = random.nextInt(height - CELL_SIZE)
            numbers.find {
                left + CELL_SIZE >= it.left &&
                        left <= it.right &&
                        top + CELL_SIZE >= it.top &&
                        top <= it.bottom
            } ?: return Pair(left, top)
        }
    }

    override fun onDraw(canvas: Canvas) {
        numbers.forEach {
            numberBounds.set(it.left, it.top, it.right, it.bottom)
            if (it.isVisible) {
                val text = it.number.toString()
                val textWidth = numberPaint.measureText(text)
                canvas.drawText(
                        text,
                        it.left.toFloat() + (CELL_SIZE - textWidth) / 2,
                        it.top.toFloat() + CELL_SIZE - 6, // Костыль, чтобы отцентровать по вертикали
                        numberPaint)
            } else {
                val half = CELL_SIZE.toFloat() / 2
                canvas.drawCircle(
                        it.left.toFloat() + half,
                        it.top.toFloat() + half,
                        half - STROKE_WIDTH / 2,
                        circlePaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return true
        var isEventHandled = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> isEventHandled = true
            MotionEvent.ACTION_UP -> {
                handleClick(event.x, event.y)
                performClick()
                isEventHandled = true
            }
            MotionEvent.ACTION_CANCEL -> isEventHandled = true
        }
        return isEventHandled
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun handleClick(x: Float, y: Float) {
        numbers.forEach {
            if (x >= it.left && x <= it.right && y >= it.top && y <= it.bottom) {
                clickListener?.onClick(it.number)
                return
            }
        }
    }

    private fun l(msg: Any) {
        Log.d("sssss", msg.toString())
    }

    data class Number(
            val number: Int,
            val left: Int,
            val right: Int,
            val top: Int,
            val bottom: Int,
            var isVisible: Boolean
    )

    interface ClickListener {
        fun onClick(number: Int)
    }
}