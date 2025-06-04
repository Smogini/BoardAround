package com.boardaround.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.boardaround.R
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
            .width(230.dp)
            .height(320.dp)
            .padding(vertical = 8.dp)
            .clickable { confirmAction = true }
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                clip = true
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RectangleShape
    ) {
        CustomImageCard(
            item = null,
            image = article.urlToImage ?: R.drawable.placeholder_image,
            contentDescription = "${article.title}'s image",
            modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            thickness = 2.dp
        )

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            CustomTitle(
                text = article.title.orEmpty(),
                textStyle = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            CustomTitle(
                text = article.description.orEmpty(),
                textStyle = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                maxLines = 3,
                alignment = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                CustomTitle(
                    text = article.source?.name.orEmpty(),
                    textStyle = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    alignment = TextAlign.Start
                )

                val publicationDate = article.publishedAt?.substringBefore("T").orEmpty()
                CustomTitle(
                    text = publicationDate,
                    textStyle = MaterialTheme.typography.bodySmall,
                    alignment = TextAlign.End,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        CustomAlertDialog(
            isVisible = confirmAction,
            title = "Confirm action",
            description =
                "Are you sure you want to abandon? You will be taken to:\n${article.url}",
            onConfirm = {
                confirmAction = false
                onClick()
            },
            onDismiss = { confirmAction = false }
        )
    }
}
