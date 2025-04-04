package com.boardaround.ui.components

import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.theme.BottomBar
import com.boardaround.ui.theme.ButtonColor

@Composable
fun BottomBar(navController: NavController) {
    var showExtraFabs by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = showExtraFabs, label = "Extra Fabs Position")

    val verticalOffset by transition.animateDp(
        label = "Vertical Offset",
        transitionSpec = { tween(durationMillis = 300, easing = EaseOutQuad) }
    ) { visible ->
        if (visible) (-10).dp else 50.dp
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BottomAppBar(
            containerColor = BottomBar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(50.dp))
                .align(Alignment.BottomCenter)
                .wrapContentHeight(),
            actions = {
                CustomButtonIcon(
                    "Homepage",
                    icon = Icons.Filled.Home,
                    iconColor = ButtonColor,
                    onClick = {
                        navController.navigate(Route.Homepage) {
                            launchSingleTop = true
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                CustomButtonIcon(
                    title = "Location",
                    icon = Icons.Filled.AddLocation,
                    iconColor = ButtonColor,
                    onClick = {
                        navController.navigate(Route.Map) {
                            launchSingleTop = true
                        }
                    }
                )

                CustomButtonIcon(
                    title = "Account",
                    icon = Icons.Filled.AccountCircle,
                    iconColor = ButtonColor,
                    onClick = {
                        navController.navigate(Route.MyProfile) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 50.dp)
        ) {

            if (showExtraFabs) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomFloatingActionButton(
                        onClick = {
                            navController.navigate(Route.NewEvent)
                            showExtraFabs = false
                        },
                        modifier = Modifier
                            .size(55.dp)
                            .offset(x = (-20).dp, y = verticalOffset),
                        icon = Icons.Filled.Create
                    )
                    CustomFloatingActionButton(
                        onClick = {
                            navController.navigate(Route.NewPost)
                            showExtraFabs = false
                        },
                        modifier = Modifier
                            .size(55.dp)
                            .offset(x = 20.dp, y = verticalOffset),
                        icon = Icons.Filled.PostAdd
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomFloatingActionButton(
                onClick = {
                    showExtraFabs = !showExtraFabs
                },
                modifier = Modifier.size(60.dp)
            )
        }
    }
}