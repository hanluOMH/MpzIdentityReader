package org.multipaz.identityreader

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
private fun SettingsItem(
    leadingIcon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            leadingIcon()
        }
        val localTextStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
            fontSize = MaterialTheme.typography.labelLarge.fontSize
        )
        CompositionLocalProvider(LocalTextStyle provides localTextStyle) {
            text()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDialog(
    settingsModel: SettingsModel,
    onDismissed: () -> Unit,
    onUseWithoutGoogleAccountClicked: () -> Unit,
    onSignInToGoogleClicked: () -> Unit,
) {
    val signedIn = settingsModel.signedIn.collectAsState()

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismissed() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Top)
            ) {
                Box() {
                    IconButton(onClick = { onDismissed() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                        text = "Google Account",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Top)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Top)
                    ) {
                        if (signedIn.value != null) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Start),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                signedIn.value!!.ProfilePicture(size = 48.dp)
                                Column() {
                                    Text(
                                        text = signedIn.value!!.displayName ?: "Google User",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        // TODO: usually the email address but for some G accounts it's a numerical ID
                                        //   Not really sure how to do deal with this.
                                        text = signedIn.value!!.id,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }

                            SettingsItem(
                                text = { Text("Use without a Google account") },
                                leadingIcon = { Icon(Icons.Outlined.NoAccounts, contentDescription = null) },
                                onClick = { onUseWithoutGoogleAccountClicked() }
                            )
                        } else {
                            SettingsItem(
                                text = { Text("Sign into your Google account") },
                                leadingIcon = { Icon(Icons.Outlined.AccountCircle, contentDescription = null) },
                                onClick = { onSignInToGoogleClicked() }
                            )
                        }
                    }
                }
            }
        }
    }
}
