package com.fridgerecipe.android.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    // 알림 설정 상태
    var allNotificationsEnabled by remember { mutableStateOf(true) }
    var expiryNotificationEnabled by remember { mutableStateOf(true) }
    var notificationTime by remember { mutableStateOf("09:00") }
    var expiryDayD3 by remember { mutableStateOf(true) }
    var expiryDayD1 by remember { mutableStateOf(true) }
    var expiryDayD0 by remember { mutableStateOf(false) }
    var recipeNotificationEnabled by remember { mutableStateOf(false) }

    // 앱 설정 상태
    var selectedTheme by remember { mutableStateOf("system") }
    var selectedStorage by remember { mutableStateOf("fridge") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 알림 설정 섹션
            SectionHeader(title = "알림 설정")

            ListItem(
                headlineContent = { Text("전체 알림") },
                trailingContent = {
                    Switch(
                        checked = allNotificationsEnabled,
                        onCheckedChange = { allNotificationsEnabled = it }
                    )
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            ListItem(
                headlineContent = { Text("소비기한 알림") },
                trailingContent = {
                    Switch(
                        checked = expiryNotificationEnabled,
                        onCheckedChange = { expiryNotificationEnabled = it },
                        enabled = allNotificationsEnabled
                    )
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            ListItem(
                headlineContent = { Text("알림 시간") },
                trailingContent = {
                    Text(
                        text = notificationTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // 소비기한 알림 일수
            ListItem(
                headlineContent = { Text("소비기한 알림 일수") },
                supportingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = expiryDayD3,
                            onCheckedChange = { expiryDayD3 = it },
                            enabled = allNotificationsEnabled && expiryNotificationEnabled
                        )
                        Text("D-3", fontSize = 13.sp)
                        Checkbox(
                            checked = expiryDayD1,
                            onCheckedChange = { expiryDayD1 = it },
                            enabled = allNotificationsEnabled && expiryNotificationEnabled
                        )
                        Text("D-1", fontSize = 13.sp)
                        Checkbox(
                            checked = expiryDayD0,
                            onCheckedChange = { expiryDayD0 = it },
                            enabled = allNotificationsEnabled && expiryNotificationEnabled
                        )
                        Text("당일", fontSize = 13.sp)
                    }
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            ListItem(
                headlineContent = { Text("레시피 추천 알림") },
                trailingContent = {
                    Switch(
                        checked = recipeNotificationEnabled,
                        onCheckedChange = { recipeNotificationEnabled = it },
                        enabled = allNotificationsEnabled
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()

            // 앱 설정 섹션
            SectionHeader(title = "앱 설정")

            ListItem(
                headlineContent = { Text("테마") },
                supportingContent = {
                    Column {
                        Spacer(modifier = Modifier.height(4.dp))
                        listOf("system" to "시스템", "light" to "라이트", "dark" to "다크").forEach { (value, label) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = selectedTheme == value,
                                    onClick = { selectedTheme = value }
                                )
                                Text(label, fontSize = 14.sp)
                            }
                        }
                    }
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            ListItem(
                headlineContent = { Text("기본 보관장소") },
                supportingContent = {
                    Column {
                        Spacer(modifier = Modifier.height(4.dp))
                        listOf("fridge" to "냉장", "freezer" to "냉동", "room" to "상온").forEach { (value, label) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = selectedStorage == value,
                                    onClick = { selectedStorage = value }
                                )
                                Text(label, fontSize = 14.sp)
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()

            // 정보 섹션
            SectionHeader(title = "정보")

            ListItem(
                headlineContent = { Text("앱 버전") },
                trailingContent = {
                    Text(
                        text = "0.1.0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            ListItem(
                headlineContent = { Text("이용약관") }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            ListItem(
                headlineContent = { Text("개인정보처리방침") }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}
