package com.boardaround.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.boardaround.R
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.viewmodel.UserViewModel

@Composable
fun ShowProfileScreen(
    context: Context,
    navController: NavController,
    userViewModel: UserViewModel
) {
    val userToShow by userViewModel.selectedUser.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()

    val myFriends by userViewModel.userFriends.collectAsState(initial = emptyList())
    val isFriend = myFriends.any { it.username == userToShow?.username }

    ScreenTemplate(
        title = "Profile of ${userToShow?.username}",
        currentRoute = Route.Profile,
        navController
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(userToShow?.profilePic)
                            .placeholder(R.drawable.default_profile)
                            .error(R.drawable.default_profile)
                            .build()
                    ),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = userToShow?.name ?: "Name not available",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userToShow?.email ?: "Email not available",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userToShow?.dob ?: "Date of birth not available",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (userToShow != null && userToShow != currentUser) {
                if (isFriend) {
                    CustomButton(
                        onClick = { userViewModel.removeFriend(userToShow!!.uid) },
                        text = "Remove friend",
                    )
                } else {
                    CustomButton(
                        onClick = {
                            val toUserUID = userToShow!!.uid
                            userViewModel.sendFriendshipRequest(currentUser!!.uid, toUserUID)
                            Toast.makeText(context, "Friend request sent", Toast.LENGTH_SHORT).show()
                        },
                        text = "Add friend",
                    )
                }
            }
        }
    }
}
