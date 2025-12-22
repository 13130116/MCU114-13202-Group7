package com.example.stepcounter // 請確保這與您的套件名稱相符

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {

    // 使用一個私有的、可變的 State 來儲存計數值
    private val _count = mutableStateOf(0)

    // 對外暴露一個不可變的 State，讓 UI 只能讀取而不能直接修改
    val count: State<Int> = _count

    // 提供一個公開的函數，讓 UI 可以觸發計數增加的邏輯
    fun increment() {
        _count.value++
    }
}
