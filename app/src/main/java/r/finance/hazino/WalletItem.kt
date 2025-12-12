package r.finance.hazino


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import r.finance.hazino.ui.theme.customWalletItemColorScheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WalletItem(
    amount: Double,
    dateTime: LocalDateTime,
    caption: String,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteSheet by remember { mutableStateOf(false) }

    val myColors = customWalletItemColorScheme

    val (backgroundColor, borderColor, icon) = when {
        amount > 0 -> Triple(
            myColors.walletItemBackgroundGreen,
            myColors.walletItemBorderGreen,
            Icons.Default.ArrowDownward
        )

        amount < 0 -> Triple(
            myColors.walletItemBackgroundRed,
            myColors.walletItemBorderRed,
            Icons.Default.ArrowUpward
        )

        else -> Triple(
            myColors.walletItemBackgroundGray,
            myColors.walletItemBorderGray,
            Icons.Default.Remove
        )
    }

    // Format date/time (e.g., "Oct 21, 2025 • 14:30")
    val formattedDateTime = dateTime.format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd • HH:mm")
    )

    // Format amount with + for positive, - for negative
    val formattedAmount = if (amount > 0) "+$amount" else "$amount"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon
            Icon(
                imageVector = icon,
                contentDescription = if (amount > 0)
                    stringResource(R.string.w_income) else
                    stringResource(R.string.w_expense),
                modifier = Modifier.size(24.dp)
            )

            // Main content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = caption.ifEmpty {
                        if (amount > 0)
                            stringResource(R.string.w_income) else
                            stringResource(R.string.w_expense)
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = formattedDateTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Amount
            Text(
                text = formattedAmount,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End,
                modifier = Modifier.widthIn(min = 80.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete",
                modifier = Modifier.clickable { showDeleteSheet = true }
            )
        }
    }

    if (showDeleteSheet) {
        QuickTransactionDeleteSheet(
            onDelete = {
                onDeleteClick()
                showDeleteSheet = false
            },
            onDismiss = { showDeleteSheet = false }
        )
    }
}