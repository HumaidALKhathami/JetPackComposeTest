package com.example.jetpackcomposetest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposetest.flickrresponse.PhotoArg

@Composable
fun PostDetailsScreen(photo: PhotoArg) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Row {
                ProfileImage(url = photo.url_s)
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = photo.title)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = photo.datetaken)
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(text = photo.description)
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "")
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "")
                Icon(imageVector = Icons.Default.Share, contentDescription = "")
                Icon(imageVector = Icons.Default.Warning, contentDescription = "")
            }
        }
    }
}