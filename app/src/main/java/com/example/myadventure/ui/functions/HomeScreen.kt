package com.example.myadventure.ui.functions
// Compose와 Android 관련 import
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

// 프로젝트 관련 import
import com.example.myadventure.R
import com.example.myadventure.ui.compose.Feature
import com.example.myadventure.ui.compose.FeatureCardRow
import com.example.myadventure.ui.compose.PointDisplay
import com.example.myadventure.ui.profile.UserPreferences


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferences = UserPreferences.getInstance(context)

    // 사용자 정보 로드
    val userName by remember { mutableStateOf(userPreferences.getUserName()) }
    val profileImageUriString by remember { mutableStateOf(userPreferences.getProfileImageUri()) }
    val profileImageUri = profileImageUriString?.let { Uri.parse(it) }

    // pointsFlow 구독하여 포인트 상태 유지
    val points by userPreferences.pointsFlow.collectAsState(initial = userPreferences.getPoints())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("홈 화면") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 사용자 프로필 섹션
                UserProfileSection(userName, profileImageUri)

                Spacer(modifier = Modifier.height(16.dp))

                // 포인트 표시
                PointDisplay(points = points)

                // 주요 기능 카드 표시
                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FeatureCardRow(
                        navController = navController,
                        firstFeature = Feature("mission_screen", "미션", R.drawable.ic_mission),
                        secondFeature = Feature("diary_screen", "다이어리", R.drawable.ic_diary)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FeatureCardRow(
                        navController = navController,
                        firstFeature = Feature("garden_screen", "정원", R.drawable.ic_garden),
                        secondFeature = Feature("map_screen", "지도", R.drawable.ic_map)
                    )
                }
            }
        }
    )
}

@Composable
fun UserProfileSection(userName: String, profileImageUri: Uri?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = profileImageUri?.let { rememberAsyncImagePainter(it) }
                ?: painterResource(id = R.drawable.ic_profile),
            contentDescription = "프로필 사진",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .padding(end = 16.dp)
        )
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}