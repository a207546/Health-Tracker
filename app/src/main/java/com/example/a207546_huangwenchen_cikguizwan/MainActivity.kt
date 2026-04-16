package com.example.a207546_huangwenchen_cikguizwan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //  控制白天/黑夜模式（默认 true = 黑夜）
            val darkMode = true   //   false

            // 主题自动根据 darkMode 变化
            HealthTrackerTheme(darkMode = darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BackgroundWithImage()
                }
            }
        }
    }
}

// 背景布局
@Composable
fun BackgroundWithImage() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "app background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
            ,
            verticalArrangement = Arrangement.Top
        ) {
            MainContent()
        }
    }
}

// 主内容
@Composable
fun MainContent() {
    var userName by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Matric: 207546",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        BasicTextField(
            value = userName,
            onValueChange = { userName = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF333333))
                .padding(12.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            decorationBox = { innerTextField ->
                if (userName.isEmpty()) {
                    Text("Enter your name", color = Color.LightGray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                showMessage = if (userName.isNotEmpty()) {
                    "Hello, $userName! Welcome to Health Tracker"
                } else {
                    "Please enter your name first"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Secondary)
        ) {
            Text("Submit", color = Color.White)
        }

        if (showMessage.isNotEmpty()) {
            Text(
                text = showMessage,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        StepCounterCard()
        Spacer(modifier = Modifier.height(16.dp))
        ExerciseSection()
        Spacer(modifier = Modifier.height(16.dp))
        HeartRateCard()
        Spacer(modifier = Modifier.height(16.dp))
        WaterTrackerCard()
        Spacer(modifier = Modifier.height(16.dp))
        BottomNavigationBar()
    }
}



@Composable
fun StepCounterCard() {
    var expanded by remember { mutableStateOf(false) } // 动画控制状态

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // 卡片展开/收起动画
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp), // Material3标准阴影
        //可直接修改的形状参数：当前为16dp圆角，可自行调整数值/形状
        shape = RoundedCornerShape(100.dp),
        // 👇 单独设置此卡片颜色（你可自己改色值）
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,//水平居中
            verticalArrangement = Arrangement.Center //垂直居中
        ) {
            Text(
                "0",
                fontSize = 72.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text("/6000 Steps", fontSize = 18.sp, color = LightGray)

            // 展开后显示额外内容（居中显示）
            if (expanded) {
                Text("Daily average: 0", fontSize = 14.sp, color = LightGray)
            }
        }
    }
}

// 修改点4：替换为Material3 Card组件
// 修改点5：添加点击展开/收起动画
@Composable
fun ExerciseSection() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        //可直接修改的形状参数：当前为16dp圆角，可自行调整数值/形状
        shape = RoundedCornerShape(100.dp),
        // 👇 单独设置此卡片颜色
        colors = CardDefaults.cardColors(containerColor = Color(0xFF383838))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏃", color = Color.White, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Exercise",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
            }

            // 展开显示运动数据
            if (expanded) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ExerciseItem("0.0", "Km")
                    ExerciseItem("0h 0m", "Min")
                    ExerciseItem("0.0", "Kcal")
                }
            }
        }
    }
}

@Composable
fun ExerciseItem(value: String, unit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(unit, fontSize = 14.sp, color = LightGray)
    }
}

//替换为Material3 Card组件
//添加点击展开/收起动画
@Composable
fun HeartRateCard() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        // 👇 可直接修改的形状参数：当前为16dp圆角，可自行调整数值/形状
        shape = RoundedCornerShape(16.dp),
        // 👇 单独设置此卡片颜色
        colors = CardDefaults.cardColors(containerColor = Color(0xFF424242))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("❤️", color = Color.White, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Track heart rate",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Text("✕", fontSize = 18.sp, color = LightGray)
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Track your heart rate for better health.",
                    color = LightGray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Measure",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 14.dp)
                    )
                }
            }
        }
    }
}

//替换为Material3 Card组件
//添加点击展开/收起动画
@Composable
fun WaterTrackerCard() {
    var expanded by remember { mutableStateOf(false) }
    var waterAmount by remember { mutableIntStateOf(300) }
    val dailyGoal = 2000

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        // 👇 可直接修改的形状参数：当前为16dp圆角，可自行调整数值/形状
        shape = RoundedCornerShape(16.dp),
        // 👇 单独设置此卡片颜色
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A4A4A))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💧", color = Color.White, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Water Tracker",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            // 展开显示饮水数据
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$waterAmount ml / $dailyGoal ml",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF333333))
                            .clickable {
                                if (waterAmount < dailyGoal) {
                                    waterAmount += 300
                                }
                            }
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+300ml", color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

// 底部导航栏
@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(DarkSurface),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🏠", fontSize = 24.sp, color = Color.White)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📊", fontSize = 24.sp, color = LightGray)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("⚙️", fontSize = 24.sp, color = LightGray)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    HealthTrackerTheme(darkMode = true) {
        BackgroundWithImage()
    }
}