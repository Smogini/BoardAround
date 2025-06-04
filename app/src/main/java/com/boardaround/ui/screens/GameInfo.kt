package com.boardaround.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.data.entities.Game
import com.boardaround.data.entities.UserComment
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomImageCard
import com.boardaround.ui.components.CustomTitle
import com.boardaround.viewmodel.GameViewModel

@Composable
fun ShowGameInfo(navController: NavController, gameViewModel: GameViewModel, context: Context) {
    val gameToShow by gameViewModel.selectedGame.collectAsState()

    ScreenTemplate(
        title = gameToShow?.name.orEmpty(),
        currentRoute = Route.GameInfo,
        navController = navController
    ) {
        item {
            if (gameToShow != null) {
                GameDetailsTabbedView(gameToShow!!, gameViewModel, context)
            }
        }
    }
}

@Composable
fun GameDetailsTabbedView(
    game: Game,
    gameViewModel: GameViewModel,
    context: Context
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val userGames by gameViewModel.userGames.collectAsState()
    val isGameAdded = userGames.any { it.gameId == game.id }

    val tabTitles = listOf("Information", "Description", "Reviews")

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    TabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = { Text(title) }
            )
        }
    }

    Spacer(modifier = Modifier.size(5.dp))

    when (selectedTabIndex) {
        0 -> GameCard(game, isGameAdded, gameViewModel, context)
        1 -> {
            CustomTitle(
                text = game.description ?: "No description available",
                fontWeight = FontWeight.Normal,
                alignment = TextAlign.Start,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
        2 -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(game.userComments ?: emptyList()) { comment ->
                    UserCommentCard(comment = comment, modifier = Modifier.heightIn(min = 150.dp))
                }
            }

        }
    }
}

@Composable
private fun GameCard(
    game: Game,
    isGameAdded: Boolean,
    gameViewModel: GameViewModel,
    context: Context
) {
    var alreadyAdded by remember { mutableStateOf(isGameAdded) }

    LaunchedEffect(isGameAdded) {
        alreadyAdded = isGameAdded
    }

    CustomImageCard(
        item = null,
        isSelected = true,
        image = game.imageUrl.toString(),
        contentDescription = game.name + "'s image",
        cardSize = 200
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp, bottom = 10.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow(Icons.Default.Casino, "Editor", game.publisher)
            Spacer(Modifier.height(4.dp))
            InfoRow(Icons.Default.People, "Players", "${game.minPlayers} - ${game.maxPlayers}")
            Spacer(Modifier.height(4.dp))
            InfoRow(Icons.Default.Timer, "Game time", "${game.playingTime} min")
            Spacer(Modifier.height(4.dp))
            InfoRow(Icons.Default.VerifiedUser, "Ratings", "${game.userRatings?.ratings?.usersRated}")
            Spacer(Modifier.height(4.dp))
            InfoRow(Icons.Default.StarRate, "Average", "${game.userRatings?.ratings?.average}")
        }
    }

    CustomButton(
        text = if (alreadyAdded) "Remove from your library" else "Add to your library",
        backgroundColor = 
            if (alreadyAdded) MaterialTheme.colorScheme.tertiary
            else MaterialTheme.colorScheme.primary,
        leadingIcon = {
            CustomClickableIcon(
                title =
                    if (isGameAdded) "remove"
                    else "add",
                icon =
                    if (isGameAdded) Icons.Default.Remove
                    else Icons.Default.Add,
                iconColor =
                    if (isGameAdded) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.primary
            )
        },
        onClick = {
            if (alreadyAdded) {
                Toast.makeText(context, "Removed from your library", Toast.LENGTH_SHORT).show()
                gameViewModel.removeSavedGame(game.id)
            } else {
                Toast.makeText(context, "Added to your library", Toast.LENGTH_SHORT).show()
                gameViewModel.saveGame(game.id, game.name, game.imageUrl.toString())
            }
            gameViewModel.getUserGames()
        }
    )
}

@Composable
private fun InfoRow(
    leadingIcon: ImageVector,
    label: String,
    value: String?,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )

        CustomTitle(
            text = label,
            textStyle = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            alignment = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )

        CustomTitle(
            text = value ?: "-",
            textStyle = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            alignment = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun UserCommentCard(
    comment: UserComment,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 3
) {
    var isExpanded by remember { mutableStateOf(false) }
    var canExpand by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f, label = "rotation"
    )

    val formattedRating =
        if (comment.rating == "N/A") comment.rating
        else comment.rating + "/10"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .animateContentSize()
            .clickable { if (canExpand || isExpanded) isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            CustomTitle(
                text = "${comment.username} • $formattedRating",
                textStyle = MaterialTheme.typography.titleSmall,
                modifier = Modifier.basicMarquee()
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = comment.description.trim(),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface),
                maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { layoutResult -> canExpand = layoutResult.hasVisualOverflow }
            )

            AnimatedVisibility(canExpand || isExpanded) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        modifier = Modifier
                            .rotate(rotationAngle)
                            .size(24.dp)
                    )
                }
            }
        }
    }
}
