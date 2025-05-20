package org.example.project.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.example.project.model.Character

@Composable
fun CharacterCard(character: Character) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .fillMaxHeight()
            ) {
                KamelImage(
                    resource = asyncPainterResource(character.image),
                    contentDescription = character.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    onLoading = {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    },
                    onFailure = {
                        Text("⚠️", Modifier.align(Alignment.Center))
                    }
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = character.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 7,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Raza: ${character.race} | Ki: ${character.ki}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Afiliación: ${character.affiliation}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
