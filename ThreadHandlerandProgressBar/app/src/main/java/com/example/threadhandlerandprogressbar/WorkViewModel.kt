package com.example.threadhandlerandprogressbar


import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkViewModel : ViewModel() {


    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> get() = _status


    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> get() = _progress

    private var workerThread: Thread? = null


    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {

            MSG_UPDATE_STATUS -> _status.value = msg.obj as String

            MSG_UPDATE_PROGRESS -> _progress.value = msg.arg1
        }
        true
    }


    fun startWork() {

        if (_status.value == "Preparing..." || _status.value == "Working...") return


        _progress.value = 0


        workerThread = Thread {
            try {

                sendMessage(MSG_UPDATE_STATUS, "Preparing...")
                Thread.sleep(1500)
                sendMessage(MSG_UPDATE_STATUS, "Working...")
                for (i in 1..100) {

                    if (Thread.interrupted()) {
                        throw InterruptedException()
                    }

                    Thread.sleep(50)

                    sendMessage(MSG_UPDATE_PROGRESS, i)
                }


                sendMessage(MSG_UPDATE_STATUS, "背景工作結束")

            } catch (e: InterruptedException) {

                sendMessage(MSG_UPDATE_STATUS, "Canceled")
            }
        }
        workerThread?.start()
    }


    fun cancelWork() {
        workerThread?.interrupt()
        _progress.value = 0
    }


    private fun sendMessage(what: Int, obj: Any) {
        val message = Message.obtain()
        message.what = what
        message.obj = obj
        handler.sendMessage(message)
    }


    private fun sendMessage(what: Int, arg1: Int) {
        val message = Message.obtain()
        message.what = what
        message.arg1 = arg1
        handler.sendMessage(message)
    }

    companion object {

        private const val MSG_UPDATE_STATUS = 1
        private const val MSG_UPDATE_PROGRESS = 2
    }
}