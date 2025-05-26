package com.boardaround.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.GifBox
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.data.entities.Game
import com.boardaround.navigation.Route
import com.boardaround.navigation.navigateSingleTop
import com.boardaround.ui.components.CustomClickableIcon
import com.boardaround.ui.components.CustomImageCard
import com.boardaround.ui.components.ExpandableSection
import com.boardaround.viewmodel.GameViewModel

@Composable
fun ShowGameInfo(navController: NavController, gameViewModel: GameViewModel) {
    val gameToShow by gameViewModel.selectedGame.collectAsState()

    gameViewModel.getUserGames()

    ScreenTemplate(
        title = gameToShow?.name.toString(),
        currentRoute = Route.GameInfo,
        navController = navController
    ) {
        item {
            if (gameToShow != null) {
                GameDetailsTabbedView(gameToShow!!, gameViewModel, navController)
            }
        }
    }
}

@Composable
fun GameDetailsTabbedView(game: Game, gameViewModel: GameViewModel, navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val userGames by gameViewModel.userGames.collectAsState()
    val isGameAdded = userGames.any { it.gameId == game.id }

    /* TODO: aggiungere le recensioni dall'api */
    val tabTitles = listOf("Game info", "Description", "Reviews")

    TabRow(selectedTabIndex = selectedTabIndex) {
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
        0 -> GameCard(game, isGameAdded, gameViewModel, navController)
        1 -> {
            Text(
                text = game.description ?: "No description available",
                modifier = Modifier.padding(16.dp)
            )
        }
        2 -> {
            Text(
                text = "No review available",
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun GameCard(
    game: Game,
    isGameAdded: Boolean,
    gameViewModel: GameViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    CustomImageCard(
        item = game,
        image = game.imageUrl.toString(),
        contentDescription = game.name,
        cardSize = 200
    )

    Spacer(Modifier.height(16.dp))

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow(Icons.Default.Casino, "Editor", game.publisher)
            InfoRow(Icons.Default.People, "N° of players", "${game.minPlayers} - ${game.maxPlayers}")
            InfoRow(Icons.Default.Timer, "Game time", "${game.playingTime} min")
        }
    }

    Spacer(Modifier.height(16.dp))

    ExpandableSection(
        title = "Expansions",
        icon = Icons.Default.GifBox,
        itemList = game.expansions ?: emptyList(),
        labelProvider = { it.title },
        onItemClick = { expansion ->
            gameViewModel.getGameInfo(expansion.id)
            navController.navigateSingleTop(Route.GameInfo)
        }
    )

    var text by remember { mutableStateOf("Add to your library") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
            if (isGameAdded) {
                text = "Remove from your library"
                gameViewModel.removeSavedGame(game.id)
                Toast.makeText(context, "Removed from your library", Toast.LENGTH_SHORT).show()
            } else {
                text = "Add to your library"
                gameViewModel.saveGame(game.id, game.name, game.imageUrl.toString())
//                gameViewModel.unlockAchievement(5)
                Toast.makeText(context, "Game added to your library", Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, "You've unlocked an achievement", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Text(text)
        CustomClickableIcon(
            title = text,
            icon = Icons.Default.Add,
            iconColor = MaterialTheme.colorScheme.primary,
            onClick = {}
        )
    }
}

@Composable
fun InfoRow(
    leadingIcon: ImageVector,
    label: String,
    value: String?,
    onClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        CustomClickableIcon(
            title = label,
            icon = leadingIcon,
            iconColor = MaterialTheme.colorScheme.primary,
            onClick = {  }
        )
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.weight(1f))
        Text(value ?: "-", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}
