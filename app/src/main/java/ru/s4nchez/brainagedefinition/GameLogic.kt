package ru.s4nchez.brainagedefinition

import android.os.Handler

class GameLogic(private val gameView: GameView) {

    private var handler: Handler = Handler()

    private var numbers = ArrayList<Int>()
    private var count = MIN_COUNT
    private var lastIndex = DEFAULT_LAST_INDEX

    companion object {
        private val NUMBERS_SET = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        private const val TIME_BEFORE_HIDE = 1000L
        private const val TIME_BEFORE_NEW_GAME = 2000L
        private const val MIN_COUNT = 3
        private const val MAX_COUNT = 7
        private const val DEFAULT_LAST_INDEX = -1
    }

    private fun prepareData(newCount: Int) {
        count = newCount
        lastIndex = DEFAULT_LAST_INDEX
        val numbersSet = ArrayList(NUMBERS_SET)
        numbersSet.shuffle()
        numbers.clear()
        for (i in 0 until count) {
            numbers.add(numbersSet[i])
        }
        numbers.sort()
    }

    private fun runNewGame() {
        gameView.disableView()
        gameView.setNumbers(numbers)

        Thread(Runnable {
            Thread.sleep(TIME_BEFORE_HIDE)
            handler.post {
                gameView.hideNumbers()
                gameView.enableView()
            }
        }).start()
    }

    fun start() {
        prepareData(MIN_COUNT)
        runNewGame()
    }

    fun handleClick(number: Int) {
        val index = numbers.indexOf(number)

        if (lastIndex + 1 == index && index + 1 != numbers.size) {
            lastIndex++
            gameView.showNumber(number)
            return
        }

        if (lastIndex + 1 != index) {
            setTimeoutBeforeNewGame(Math.max(MIN_COUNT, count - 1))
            gameView.showGameOver()
            gameView.showNumbers()
            return
        }

        if (index + 1 >= numbers.size) {
            setTimeoutBeforeNewGame(Math.min(MAX_COUNT, count + 1))
            gameView.showNumber(number)
            gameView.showSuccess()
            return
        }
    }

    private fun setTimeoutBeforeNewGame(newCount: Int) {
        gameView.disableView()
        Thread(Runnable {
            Thread.sleep(TIME_BEFORE_NEW_GAME)
            gameView.enableView()
            prepareData(newCount)
            runNewGame()
        }).start()
    }

    interface GameView {
        fun setNumbers(numbers: ArrayList<Int>)
        fun hideNumbers()
        fun showNumbers()
        fun showNumber(number: Int)
        fun showGameOver()
        fun showSuccess()
        fun disableView()
        fun enableView()
    }
}