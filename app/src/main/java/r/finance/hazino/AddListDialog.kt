package r.finance.hazino

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddListDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var listName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New List") },
        text = {
            Column {
                Text("Enter a name for the new list:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = listName,
                    onValueChange = { listName = it },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (listName.isNotBlank()) {
                        onAdd(listName)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
