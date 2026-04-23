package com.example.a207546_huangwenchen_cikguizwan

// Data Class 用于存储健康数据
data class HealthData(
    val userName: String = "",
    val steps: Int = 0,
    val waterAmount: Int = 300,
    val heartRate: Int = 0,
    val exerciseMinutes: Int = 0
)