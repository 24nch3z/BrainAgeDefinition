package ru.s4nchez.brainagedefinition

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GameView.ClickListener, GameLogic.GameView {

    private var game: GameLogic = GameLogic(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game_view.setListener(this)
        button.setOnClickListener {
            button.isEnabled = false
            button.alpha = 0.3f
            game.start()
        }
    }

    override fun onClick(number: Int) {
        game.handleClick(number)
    }

    override fun setNumbers(numbers: ArrayList<Int>) {
        game_view.setNumbers(numbers)
    }

    override fun hideNumbers() {
        game_view.hideNumbers()
    }

    override fun showNumbers() {
        game_view.showNumbers()
    }

    override fun showSuccess() {
        Toast.makeText(this, "Win", Toast.LENGTH_LONG).show()
    }

    override fun showGameOver() {
        Toast.makeText(this, "Game over", Toast.LENGTH_LONG).show()
    }

    override fun showNumber(number: Int) {
        game_view.showNumber(number)
    }

    override fun disableView() {
        game_view.isEnabled = false
    }

    override fun enableView() {
        game_view.isEnabled = true
    }
}
