package com.example.movingobject

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import java.nio.channels.FileLock
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mBall by lazy { findViewById<CardView>(R.id.ball) }

    companion object {
        private const val TAG = "MainActivity"
    }

    private var isFromLeft = true
    private var isFromTop = true

    private val colorsList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorsArray = resources.obtainTypedArray(R.array.colors_list)
        for (i in 0 until colorsArray.length()) {
            colorsList.add(colorsArray.getColor(i, 0))
        }
        colorsArray.recycle()

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                mBall.x = getXY(mBall.x, mBall.y).first
                mBall.y = getXY(mBall.x, mBall.y).second
            }
        }, 1000, 30)

    }

    private fun getXY(x: Float, y: Float): Pair<Float, Float> {

        hasReachedRight(x)
        hasReachedLeft(x)
        hasReachedTop(y)
        hasReachedBottom(y)

        val offsetX = if (isFromLeft) {
            10f
        } else {
            -10f
        }

        val offsetY = if (isFromTop) {
            10f
        } else {
            -10f
        }

        return Pair(offsetX + mBall.x, offsetY + mBall.y)
    }

    private fun hasReachedRight(x: Float) = if (x + mBall.width >= Resources.getSystem().displayMetrics.widthPixels) {
        isFromLeft = false
        changeBackground()
        true
    } else {
        false
    }

    private fun hasReachedLeft(x: Float) = if (x <= 0) {
        isFromLeft = true
        changeBackground()
        true
    } else {
        false
    }

    private fun hasReachedTop(y: Float) = if (y <= 0) {
        isFromTop = true
        changeBackground()
        true
    } else {
        false
    }

    private fun hasReachedBottom(y: Float) = if (y + (mBall.height * 2) >= Resources.getSystem().displayMetrics.heightPixels) {
        isFromTop = false
        changeBackground()
        true
    } else {
        false
    }

    private fun changeBackground() {
        mBall.setCardBackgroundColor(colorsList.random())
    }

}