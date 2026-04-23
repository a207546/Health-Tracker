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
//   Lab4 新增导入
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //  控制白天/黑夜模式（默认 true = 黑夜）
            val darkMode = false   //   false

            // 主题自动根据 darkMode 变化
            HealthTrackerTheme(darkMode = darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    // 【Lab4 新增】导航控制器（管理页面跳转）

                    val navController = rememberNavController()


                    // Lab4 新增ViewModel（管理数据，旋转不丢失）

                    val viewModel: HealthViewModel = viewModel()


                    // Lab4 新增页面路由总控制器

                    AppNavHost(navController, viewModel, darkMode)
                }
            }
        }
    }
}

// ------------------------------
// Lab4 新增导航：管理3个页面跳转
// ------------------------------
@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: HealthViewModel,
    darkMode: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = "home", // 默认打开首页
        modifier = Modifier.fillMaxSize()
    ) {
        composable("home") { // 路由名：home
            HomeScreen(navController, viewModel, darkMode)
        }
        composable("stats") { // 路由名：stats（数据页）
            StatsScreen(navController, viewModel, darkMode)
        }
        composable("settings") { // 路由名：settings（设置页）
            SettingsScreen(navController, viewModel, darkMode)
        }
    }
}

// 背景布局
@Composable
fun BackgroundWithImage(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "app background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            content()
        }
    }
}

// 主内容
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HealthViewModel,
    darkMode: Boolean
) {
    BackgroundWithImage {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Matric: 207546",
                color = MaterialTheme.colorScheme.onSurface, //  自动白天/黑夜变色
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            BasicTextField(

                //  【Lab4 新增】数据从 ViewModel 读取

                value = viewModel.healthData.userName,
                onValueChange = { viewModel.updateUserName(it) },

                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface) // 自动变色
                    .padding(12.dp),
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                decorationBox = { innerTextField ->
                    if (viewModel.healthData.userName.isEmpty()) {
                        Text("Enter your name", color = Color.LightGray)
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            var showMessage by remember { mutableStateOf("") }
            Button(
                onClick = {
                    showMessage = if (viewModel.healthData.userName.isNotEmpty()) {
                        "Hello, ${viewModel.healthData.userName}! Welcome to Health Tracker"
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
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            // 传入 ViewModel 让数据共享

            StepCounterCard(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            ExerciseSection()
            Spacer(modifier = Modifier.height(16.dp))
            HeartRateCard()
            Spacer(modifier = Modifier.height(16.dp))
            WaterTrackerCard(viewModel)
            Spacer(modifier = Modifier.height(16.dp))

            // ------------------------------
            // Lab4 新增底部导航可跳转页面
            // ------------------------------
            BottomNavigationBar(navController)
        }
    }
}

@Composable
fun StepCounterCard(viewModel: HealthViewModel) {
    var expanded by remember { mutableStateOf(false) } // 动画控制状态

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // 展开收起
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        //形状
        shape = RoundedCornerShape(100.dp),
        // 卡片颜色（自动支持白天黑夜）
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,//水平居中
            verticalArrangement = Arrangement.Center //垂直居中
        ) {

            // Lab4 新增数据来自 ViewModel

            Text(
                "${viewModel.healthData.steps}",
                fontSize = 72.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text("/6000 Steps", fontSize = 18.sp, color = LightGray)

            // 展开后显示
            if (expanded) {
                Text("Daily average: 0", fontSize = 14.sp, color = LightGray)
            }
        }
    }
}

@Composable
fun ExerciseSection() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        //形状
        shape = RoundedCornerShape(100.dp),
        //卡片颜色
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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

//展开/收起动画
@Composable
fun HeartRateCard() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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

//展开/收起
@Composable
fun WaterTrackerCard(viewModel: HealthViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val dailyGoal = 2000

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        // 形状
        shape = RoundedCornerShape(16.dp),
        //卡片颜色
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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

                    // Lab4 新增数据来自 ViewModel

                    Text(
                        text = "${viewModel.healthData.waterAmount} ml / $dailyGoal ml",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                if (viewModel.healthData.waterAmount < dailyGoal) {
                                    viewModel.addWater(300) // 更新数据到 ViewModel
                                }
                            }
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+300ml", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

// 底部导航栏
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("home") } // 👈 跳转首页
        ) {
            Text("🏠", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
            Text("Home", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("stats") } // 👈 跳转统计页
        ) {
            Text("📊", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
            Text("Stats", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("settings") } // 👈 跳转设置页
        ) {
            Text("⚙️", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
            Text("Settings", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}


// Lab4 新增第二个页面健康数据统计

@Composable
fun StatsScreen(navController: NavHostController, viewModel: HealthViewModel, darkMode: Boolean) {
    BackgroundWithImage {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Your Health Stats",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            // 数据都来自同一个 ViewModel
            StatItem("Steps Today", "${viewModel.healthData.steps} / 6000")
            StatItem("Water Drank", "${viewModel.healthData.waterAmount} / 2000 ml")
            StatItem("User Name", viewModel.healthData.userName.ifEmpty { "Not set" })

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text("Back to Home")
            }
        }
    }
}


// Lab4 新增第三个页面：设置页

@Composable
fun SettingsScreen(navController: NavHostController, viewModel: HealthViewModel, darkMode: Boolean) {
    BackgroundWithImage {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Settings",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { viewModel.resetWater() }, modifier = Modifier.fillMaxWidth()) {
                Text("Reset Water Tracker")
            }
            Button(onClick = { navController.navigate("home") }) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, color = Color.LightGray)
            Text(value, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    HealthTrackerTheme(darkMode = true) {
        val navController = rememberNavController()
        val viewModel: HealthViewModel = viewModel()
        HomeScreen(navController, viewModel, true)
    }
}