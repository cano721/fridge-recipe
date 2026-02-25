package com.fridgerecipe.android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("마이페이지") },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item {
                // Profile card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("로그인이 필요합니다", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { /* TODO: Navigate to login */ }) {
                            Text("로그인하기")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item { SettingsItem(Icons.Default.Notifications, "알림 설정") }
            item { SettingsItem(Icons.Default.DarkMode, "다크 모드") }
            item { SettingsItem(Icons.Default.Restaurant, "식이 제한 설정") }
            item { SettingsItem(Icons.Default.Favorite, "즐겨찾기 레시피") }
            item { SettingsItem(Icons.Default.Help, "도움말") }
            item { SettingsItem(Icons.Default.Info, "버전 정보", trailing = "v1.0.0") }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SettingsItem(Icons.AutoMirrored.Filled.ExitToApp, "로그아웃")
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    trailing: String? = null,
) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        trailingContent = if (trailing != null) {
            { Text(trailing, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
    )
}
