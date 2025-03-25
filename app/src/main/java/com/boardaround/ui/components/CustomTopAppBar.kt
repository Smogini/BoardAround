package com.boardaround.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.ui.theme.Background
import com.boardaround.ui.theme.BottomBar
import com.boardaround.ui.theme.Divider
import com.boardaround.ui.theme.PrimaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navController: NavController,
) {
    Column {
        HorizontalDivider(
            color = Divider,
            thickness = 4.dp
        )
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    color = PrimaryText,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            actions = {
//                val pagesWithoutNotifications = setOf(
//                    Route.Login,
//                    Route.Register,
//                    Route.EditMyProfile)
//                val currentBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentRoute = currentBackStackEntry?.destination?.route

//                if (currentRoute !in pagesWithoutNotifications) {
//                    CustomButtonIcon(
//                        "Empty notifications",
//                        Icons.Filled.NotificationsNone,
//                        BottomBar,
//                        onClick = { /* TODO */ }
//                    )
//                    if (currentRoute == "myProfile") {
//                        CustomButtonIcon(
//                            "Settings",
//                            Icons.Filled.Settings,
//                            BottomBar,
//                            onClick = {
//                                navController.navigate("editMyProfile") {
//                                    launchSingleTop = true
//                                }
//                            }
//                        )
//                    }
//                }
//                if (currentRoute == "editMyProfile") {
//                    CustomButtonIcon(
//                        "Return",
//                        Icons.Filled.Cancel,
//                        Errors,
//                        onClick = {
//                            navController.navigate("myProfile") {
//                                launchSingleTop = true
//                            }
//                        }
//                    )
//                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Background,
                actionIconContentColor = BottomBar
            ),
            modifier = Modifier
                .padding(10.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
        )
    }
}