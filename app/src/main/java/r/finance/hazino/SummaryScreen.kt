package r.finance.hazino

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    viewModel: WalletViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    val (totalBalance, totalIncome, totalExpense) = remember(transactions) {
        var balance = 0.0
        var income = 0.0
        var expense = 0.0

        transactions.forEach { transaction ->
            balance += transaction.amount
            if (transaction.amount > 0) {
                income += transaction.amount
            } else {
                expense += kotlin.math.abs(transaction.amount)
            }
        }
        Triple(balance, income, expense)
    }

    val formatter = remember { NumberFormat.getCurrencyInstance(Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Summary") })
        },
    )
    { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SummaryCard(
                    title = "Total Income",
                    value = totalIncome,
                    formatter = formatter,
                    color = MaterialTheme.colorScheme.onSurface,
                    Icons.AutoMirrored.Filled.TrendingUp
                )

                SummaryCard(
                    title = "Total Expense",
                    value = totalExpense,
                    formatter = formatter,
                    color = MaterialTheme.colorScheme.onSurface,
                    Icons.AutoMirrored.Filled.TrendingDown
                )

                SummaryCard(
                    title = "Total Balance",
                    value = totalBalance,
                    formatter = formatter,
                    color = if (totalBalance < 0)
                        Color(0xFFE91E63) else Color(0xFF8BC34A),
                    Icons.Default.AccountBalance
                )
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: Double,
    formatter: NumberFormat,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    // Create a simple style for the BasicTextField
    val basicTextStyle = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = color, // Apply the determined color
        textAlign = TextAlign.Center
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent, // Make background transparent
            contentColor = MaterialTheme.colorScheme.onSurface // Default text color for title
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Remove shadow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Padding inside the card
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = icon, contentDescription = "",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Use BasicTextField for the value display
            BasicTextField(
                value = formatter.format(value),
                onValueChange = { }, // No-op, as this is display-only
                // Optional: Disable interaction if needed
                // enabled = false,
                // readOnly = true,
                textStyle = basicTextStyle, // Apply the custom style
                // Remove the default decoration box (background, border, placeholder)
                decorationBox = { innerTextField ->
                    innerTextField() // Just render the text field content directly
                },
                // Ensure the field fills its space and is centered
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}