package com.example.a207546_huangwenchen_cikguizwan
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// 用于添加健康记录
data class HealthRecord(
    val id: Int,
    val title: String,
    val value: String
)

class HealthViewModel : ViewModel() {
    var healthData by mutableStateOf(HealthData())
        private set

    //  Project 1 新增：列表数据，可 Add，可跨页面显示
    var recordList by mutableStateOf(listOf<HealthRecord>())
        private set

    private var nextId = 1

    fun updateUserName(name: String) {
        healthData = healthData.copy(userName = name)
    }

    fun addWater(amount: Int) {
        val newAmount = healthData.waterAmount + amount
        healthData = healthData.copy(waterAmount = newAmount)
    }

    fun resetWater() {
        healthData = healthData.copy(waterAmount = 300)
    }

    //  Project 1 新增：添加记录
    fun addNewRecord(title: String, value: String) {
        val newRecord = HealthRecord(nextId++, title, value)
        recordList = recordList + newRecord
    }
}