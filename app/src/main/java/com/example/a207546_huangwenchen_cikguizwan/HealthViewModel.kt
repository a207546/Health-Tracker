package com.example.a207546_huangwenchen_cikguizwan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HealthViewModel : ViewModel() {
    var healthData by mutableStateOf(HealthData())
        private set

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
}