package com.boardaround.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.boardaround.data.entities.Article

@Composable
fun NewsArticleItem(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmAction by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .width(200.dp)
            .height(280.dp)
            .padding(vertical = 4.dp)
            .clickable { confirmAction = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        CustomImageCard(
            item = null,
            image = article.urlToImage.toString(),
            contentDescription = "${article.title} image",
            cardShape = CardDefaults.shape,
            modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)
        )

        Column(modifier = Modifier.padding(16.dp)) {
            CustomTitle(
                text = article.title.toString(),
                textStyle = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            CustomTitle(
                text = article.description.toString(),
                textStyle = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                alignment = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CustomTitle(
                    text = article.source?.name.toString(),
                    textStyle = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    alignment = TextAlign.Start
                )

                val publicationDate = article.publishedAt?.substringBefore("T")
                CustomTitle(
                    text = publicationDate.toString(),
                    textStyle = MaterialTheme.typography.bodySmall,
                    alignment = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        CustomAlertDialog(
            isVisible = confirmAction,
            title = "Confirm action",
            description = "Are you sure you want to abandon? You will be taken to a new screen",
            onConfirm = {
                confirmAction = false
                onClick()
            },
            onDismiss = { confirmAction = false }
        )
    }
}
