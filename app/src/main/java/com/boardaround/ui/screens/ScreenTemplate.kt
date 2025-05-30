package com.boardaround.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.BottomBar
import com.boardaround.ui.components.CustomFloatingActionButton
import com.boardaround.ui.components.CustomTopAppBar
import com.boardaround.viewmodel.NotificationViewModel
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ScreenTemplate(
    title: String,
    currentRoute: Route = Route.Splash,
    navController: NavController,
    userViewModel: UserViewModel? = null,
    showBottomBar: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    var showExtraFab by remember { mutableStateOf(false) }
    val verticalOffset by animateDpAsState(if (showExtraFab) 0.dp else (-10).dp, label = "fabAnimation")

    Scaffold(
        topBar = { CustomTopAppBar(title, navController, userViewModel, currentRoute) },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (showBottomBar) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset {
                        IntOffset(x = 0, y = (verticalOffset - 20.dp).roundToPx())
                    }
                ) {
                    AnimatedVisibility(
                        showExtraFab,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                        modifier = Modifier.offset {
                            IntOffset(x = 0, y = (verticalOffset - 20.dp).roundToPx())
                        }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomFloatingActionButton(
                                onClick = {
                                    showExtraFab = false
                                    navController.navigateSingleTop(Route.NewEvent)
                                },
                                modifier = Modifier.size(50.dp),
                                icon = Icons.Filled.Create
                            )
                            CustomFloatingActionButton(
                                onClick = {
                                    showExtraFab = false
                                    navController.navigateSingleTop(Route.NewPost)
                                },
                                modifier = Modifier.size(50.dp),
                                icon = Icons.Filled.PostAdd
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    CustomFloatingActionButton(
                        onClick = { showExtraFab = !showExtraFab },
                        modifier = Modifier.size(50.dp)
                            .offset { IntOffset(x = 0, y = (-20).dp.roundToPx()) }
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { contentPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                content()
            }
        }

        if (showBottomBar) BottomBar(navController)
    }
}