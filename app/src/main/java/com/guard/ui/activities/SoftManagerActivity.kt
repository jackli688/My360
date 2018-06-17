package com.guard.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.AbsListView
import android.widget.ListView
import com.guard.R
//import com.guard.R.id.soft_move
import kotlinx.android.synthetic.main.activity_softmanager.*


class SoftManagerActivity : AppCompatActivity() {
    val Tag = SoftManagerActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        startActivity(Intent(this, RequestPermissionActivity::class.java))
        setContentView(R.layout.activity_softmanager)
//        soft_move.setOnClickListener({
//            Log.d(Tag, "SoftManagerActivity onClick click")
//            val view = showText.parent as View
////            view.scrollBy(-100, -100)
//            view.scrollTo(-100, -100)
////            showText.scrollBy(0, -30)
//            //向左移动
//            soft_move.offsetLeftAndRight(-100)
//            //向上移动
//            soft_move.offsetTopAndBottom(-100)
//        })

//        soft_move.setOnLongClickListener {
//            Log.d(Tag, "SoftManagerActivity onLongClick click")
//            true
//        }
//
//        val myGestureListenerImp = MyGestureListenerImp()
//        val myGesture = MyGesture(this, myGestureListenerImp)
//
//
//        soft_move.setOnTouchListener { _, event ->
//            myGesture.onTouchEvent(event)
//            if (event != null) {
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        Log.d(Tag, "SoftManagerActivity press down")
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        Log.d(Tag, "SoftManagerActivity press move")
//
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        Log.d(Tag, "SoftManagerActivity press up")
//
//                    }
//                }
//            }
//            false
//        }
//
//
//        val listView = ListView(this)
//        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
//            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
//
//            }
//
//            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })
    }


    inner class MyGesture : GestureDetector {
        internal constructor(context: Context, listener: OnGestureListener) : super(context, listener) {

        }
    }

    //双击操作
    inner class MyGestureListenerImp : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            return super.onDoubleTap(e)
        }
    }
}
