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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode = true

            HealthTrackerTheme(darkMode = darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val viewModel: HealthViewModel = viewModel()
                    AppNavHost(navController, viewModel, darkMode)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: HealthViewModel,
    darkMode: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = "welcome",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("welcome") {
            WelcomeScreen(navController, darkMode)
        }
        composable("home") {
            HomeScreen(navController, viewModel, darkMode)
        }
        composable("add_record") {
            AddRecordScreen(navController, viewModel, darkMode)
        }
        composable("stats") {
            StatsScreen(navController, viewModel, darkMode)
        }
        composable("settings") {
            SettingsScreen(navController, viewModel, darkMode)
        }
    }
}

@Composable
fun BackgroundWithImage(
    darkMode: Boolean,
    content: @Composable () -> Unit
) {
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
                .background(if (darkMode) Color(0x99000000) else Color.Transparent),
            verticalArrangement = Arrangement.Top
        ) {
            content()
        }
    }
}

@Composable
fun WelcomeScreen(
    navController: NavHostController,
    darkMode: Boolean
) {
    BackgroundWithImage(darkMode = darkMode) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Health Tracker",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))
            Text(
                "Matric: 207546",
                color = Color.White,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(40.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text("Get Started")
            }
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HealthViewModel,
    darkMode: Boolean
) {
    BackgroundWithImage(darkMode = darkMode) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Matric: 207546",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            BasicTextField(
                value = viewModel.healthData.userName,
                onValueChange = { viewModel.updateUserName(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
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
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("add_record") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add New Health Record")
            }

            Spacer(modifier = Modifier.height(16.dp))

            StepCounterCard(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            ExerciseSection()
            Spacer(modifier = Modifier.height(16.dp))
            HeartRateCard()
            Spacer(modifier = Modifier.height(16.dp))
            WaterTrackerCard(viewModel)
            Spacer(modifier = Modifier.height(16.dp))

            BottomNavigationBar(navController)
        }
    }
}

@Composable
fun AddRecordScreen(
    navController: NavHostController,
    viewModel: HealthViewModel,
    darkMode: Boolean
) {
    var title by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    BackgroundWithImage(darkMode = darkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Add New Record",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))

            BasicTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text("Record Title (e.g. Running)", color = Color.Gray)
                    }
                    innerTextField()
                }
            )
            Spacer(Modifier.height(10.dp))

            BasicTextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text("Value (e.g. 30 mins)", color = Color.Gray)
                    }
                    innerTextField()
                }
            )
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (title.isNotEmpty() && value.isNotEmpty()) {
                        viewModel.addNewRecord(title, value)
                        navController.navigate("stats")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save & View in Stats")
            }
            Spacer(Modifier.height(10.dp))

            Button(onClick = { navController.navigate("home") }) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun StepCounterCard(viewModel: HealthViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "${viewModel.healthData.steps}",
                fontSize = 72.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text("/6000 Steps", fontSize = 18.sp, color = LightGray)

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
        shape = RoundedCornerShape(100.dp),
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
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Track your heart rate for better health.",
                    color = LightGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

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
        shape = RoundedCornerShape(16.dp),
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

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                                    viewModel.addWater(300)
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
            modifier = Modifier.clickable { navController.navigate("home") }
        ) {
            Text("🏠", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
            Text("Home", fontSize = 12.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("stats") }
        ) {
            Text("📊", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
            Text("Stats", fontSize = 12.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { navController.navigate("settings") }
        ) {
            Text("⚙️", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
            Text("Settings", fontSize = 12.sp)
        }
    }
}

@Composable
fun StatsScreen(
    navController: NavHostController,
    viewModel: HealthViewModel,
    darkMode: Boolean
) {
    BackgroundWithImage(darkMode = darkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Your Health Stats",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            StatItem("Steps Today", "${viewModel.healthData.steps} / 6000")
            StatItem("Water Drank", "${viewModel.healthData.waterAmount} / 2000 ml")
            StatItem("User Name", viewModel.healthData.userName.ifEmpty { "Not set" })

            Spacer(Modifier.height(20.dp))
            Text("Your Added Records", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))

            viewModel.recordList.forEach { record ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(record.title, fontWeight = FontWeight.Bold)
                        Text(record.value)
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: HealthViewModel,
    darkMode: Boolean
) {
    BackgroundWithImage(darkMode = darkMode) {
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
    Card {
        Column(Modifier.padding(16.dp)) {
            Text(label, color = Color.LightGray)
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
    Spacer(Modifier.height(12.dp))
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