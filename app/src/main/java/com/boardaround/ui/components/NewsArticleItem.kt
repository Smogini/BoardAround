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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.boardaround.data.entities.Article
import kotlin.text.isNullOrBlank
import androidx.compose.ui.res.painterResource
import com.boardaround.R


@Composable
fun NewsArticleItem(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(280.dp)
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            if (!article.urlToImage.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(article.urlToImage)
                        .crossfade(true)
                        .error(R.drawable.placeholder_image)
                        .fallback(R.drawable.placeholder_image)
                        .build(),
                    contentDescription = "Immagine notizia: ${article.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = article.title ?: "Nessun titolo",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = article.description ?: "Nessuna descrizione",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!article.source?.name.isNullOrBlank()) {
                        Text(
                            text = article.source!!.name!!,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    } else {
                        Spacer(Modifier.weight(1f, fill = false))
                    }

                    if (!article.publishedAt.isNullOrBlank()) {
                        Text(
                            text = formatDate(article.publishedAt),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// Funzione helper semplice per formattare la data (puoi renderla più sofisticata)
// Formato atteso da NewsAPI: "2023-10-27T10:30:00Z"
@Composable // O rendila una funzione non Composable se non usa nulla da Compose
private fun formatDate(publishedAt: String?): String {
    if (publishedAt.isNullOrBlank()) return ""
    return try {
        // Estrae solo la parte della data YYYY-MM-DD
        publishedAt.substringBefore("T")
        // Se vuoi una formattazione più complessa, usa OffsetDateTime e DateTimeFormatter
        // val odt = OffsetDateTime.parse(publishedAt)
        // val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) // o .ofPattern("dd MMM yyyy")
        // odt.format(formatter)
    } catch (e: Exception) {
        publishedAt // Restituisci la stringa originale in caso di errore di parsing
    }
}