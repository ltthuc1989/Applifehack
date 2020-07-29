package com.applifehack.knowledge.util

import android.util.Log

class MeasureTime {
    private var startTime :Long =0
    private var endTime :Long =0

    fun start(){
       startTime=System.currentTimeMillis()
    }
    fun end(){
        endTime=System.currentTimeMillis()
        Log.d("MeasureTime","Elapsed Time in nanosecond: ${(endTime - startTime)}")
    }



}