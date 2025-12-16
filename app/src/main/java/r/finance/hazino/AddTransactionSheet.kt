package r.finance.hazino

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionSheetContent(
    amount: String,
    onAmountChange: (String) -> Unit,
    caption: String,
    onCaptionChange: (String) -> Unit,
    error: String?,
    onAddIncome: (String) -> Unit, // Callback for Income
    onAddExpense: (String) -> Unit // Callback for Expense
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Amount Input Row (Borderless, Transparent)
        BasicTextField(
            value = amount,
            onValueChange = onAmountChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    if (amount.isEmpty()) {
                        Text(
                            text = "0.00",
                            style = TextStyle(
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Description Input Row (Borderless, Transparent)
        BasicTextField(
            value = caption,
            onValueChange = onCaptionChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    if (caption.isEmpty()) {
                        Text(
                            text = "Description",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Income and Expense Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onAddIncome(amount) },
                enabled = amount.isNotBlank() && caption.isNotBlank(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF4CAF50) // Green-ish color for Income
                ),
                modifier = Modifier.weight(1f) // Distribute width equally
            ) {
                Text("Income")
            }

            TextButton(
                onClick = { onAddExpense(amount) },
                enabled = amount.isNotBlank() && caption.isNotBlank(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFFE91E63) // Red color for Expense
                ),
                modifier = Modifier.weight(1f) // Distribute width equally
            ) {
                Text("Expense")
            }
        }
    }
}