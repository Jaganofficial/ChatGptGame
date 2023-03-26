package com.lineup.chatgptgame

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lineup.chatgptgame.recyclerview.MyAdapter
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setBackgroundColor(Color.BLACK)
        val myAdapter = MyAdapter(generateRandomColors(25))
        recyclerView.adapter = myAdapter
        val lifecount = findViewById<TextView>(R.id.lifecount)
        val remainingTime = findViewById<TextView>(R.id.time)
        val callback = MyItemTouchHelperCallback(myAdapter, lifecount, this)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Code to be executed every 1 second
                val timeLeft = millisUntilFinished / 1000
                remainingTime.text = "Time left: $timeLeft seconds"
            }

            override fun onFinish() {
                // Code to be executed when the countdown is finished
                val gameDialog = GameDialogue(
                    context = this@MainActivity,
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.tryagain),
                    "Restart",
                    Color.RED
                ,"Wanna Play again?") {
                    recreate()
                }
                gameDialog.show()
            }
        }
        val gameDialog = GameDialogue(
            this,
            ContextCompat.getDrawable(this, R.drawable.play),
            "Start",
            Color.GREEN
        ,"Let's play!") {
            it.hide()
            countDownTimer.start()
        }
        supportActionBar?.hide()
        gameDialog.show()
    }

    fun generateRandomColors(size: Int): ArrayList<String> {
        val colors = ArrayList<String>()
        val random = Random()
        for (i in 0 until size) {
            colors.add(if (random.nextBoolean()) "yellow" else "red")
        }
        return colors
    }

    class MyItemTouchHelperCallback(
        private val adapter: MyAdapter,
        var lifeCount: TextView,
        val context: Context
    ) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        var LifeCount: Int = 3

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val data = adapter.getData()
            if (direction == ItemTouchHelper.RIGHT) {
                if (data[position] == "red") {
                    LifeCount--
                    this.lifeCount.text = "Remaining Life : ${LifeCount.toString()}"
                    adapter.notifyItemChanged(position)
                } else {
                    data.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            } else if (direction == ItemTouchHelper.LEFT) {
                if (data[position] == "yellow") {
                    LifeCount--
                    this.lifeCount.text = "Remaining Life : ${LifeCount.toString()}"
                    adapter.notifyItemChanged(position)
                } else {
                    data.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }
            if (LifeCount <= 0) {
                val gameDialog = GameDialogue(
                    context = context,
                    ContextCompat.getDrawable(context, R.drawable.tryagain),
                    "Try Again",
                    Color.RED
                ,"Wanna Play again?") {
                    (context as MainActivity).recreate()
                }
                gameDialog.show()
            }
        }
    }
}

class GameDialogue(
    context: Context,
    Image: Drawable?,
    buttonText: String,
    textColor: Int,
    text:String,
    buttonClick: (Dialog) -> Unit
) :
    Dialog(context) {

    private var startButton: Button
    private var gameImage: ImageView
    private var textView:TextView
    init {
        setContentView(R.layout.game_pop_up)
        startButton = findViewById(R.id.start)
        startButton.setBackgroundColor(textColor)
        startButton.text = buttonText
        gameImage = findViewById(R.id.imageView)
        gameImage.setImageDrawable(Image)
        textView = findViewById(R.id.textView)
        textView.text=text
        textView.setTextColor(textColor)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        startButton.setOnClickListener {
            buttonClick(this)
        }
    }
}

