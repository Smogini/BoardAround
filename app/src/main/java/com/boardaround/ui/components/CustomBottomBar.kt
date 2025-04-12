package com.boardaround.ui.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.theme.ButtonColor

@Composable
fun BottomBar(navController: NavController) {
    var showExtraFabs by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) } // Stato per il menu a tendina
    val transition = updateTransition(targetState = showExtraFabs, label = "Extra Fabs Position")
    val verticalOffset by transition.animateDp(
        label = "Vertical Offset",
        transitionSpec = { tween(durationMillis = 300) }
    ) { visible -> if (visible) (-10).dp else 50.dp }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.onBackground,
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
                    iconColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        navController.navigate(Route.Homepage) {
                            launchSingleTop = true
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Pulsante che fa comparire il menu a tendina
                Box {
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Menu a tendina che appare quando expanded è true
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Token") },
                            onClick = {
                                navController.navigate(Route.Homepage) {
                                    launchSingleTop = true
                                }
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Lancio dei dadi") },
                            onClick = {
                                navController.navigate(Route.Dice) {
                                    launchSingleTop = true
                                }
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Segna punti") },
                            onClick = {
                                navController.navigate(Route.ScoreBoard) {
                                    launchSingleTop = true
                                }
                                expanded = false
                            }
                        )
                    }
                }

                CustomButtonIcon(
                    title = "Account",
                    icon = Icons.Filled.AccountCircle,
                    iconColor = MaterialTheme.colorScheme.primary,
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
