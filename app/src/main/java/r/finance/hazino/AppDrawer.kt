package r.finance.hazino

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AppDrawer(
    viewModel: WalletViewModel,
    onCloseDrawer: () -> Unit
) {
    val transactionLists by viewModel.transactionLists.collectAsStateWithLifecycle()
    val selectedListId by viewModel.selectedListId.collectAsStateWithLifecycle()
    var showAddListSheet by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<TransactionListEntity?>(null) }
    var listToRename by remember { mutableStateOf<TransactionListEntity?>(null) }

    if (showAddListSheet) {
        AddListSheet(
            onDismiss = { showAddListSheet = false },
            onAdd = { listName ->
                viewModel.addTransactionList(listName)
                showAddListSheet = false
            }
        )
    }

    listToDelete?.let { list ->
        DeleteListSheet(
            listName = list.name,
            onDismiss = { listToDelete = null },
            onDelete = {
                viewModel.deleteTransactionList(list.id)
                listToDelete = null
            }
        )
    }

    listToRename?.let { list ->
        RenameListSheet(
            listName = list.name,
            onDismiss = { listToRename = null },
            onRename = { newName ->
                viewModel.updateTransactionList(list.copy(name = newName))
                listToRename = null
            }
        )
    }

    ModalDrawerSheet {
        Text(
            stringResource(R.string.app_name),
            modifier = Modifier.padding(horizontal = 13.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        ReorderableList(
            items = transactionLists,
            onMove = { from, to ->
                val updatedLists = transactionLists.toMutableList().apply {
                    add(to, removeAt(from))
                }
                updatedLists.forEachIndexed { index, list ->
                    viewModel.updateTransactionList(list.copy(listOrder = index))
                }
            },
            key = { it.id },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 25.dp)
        ) { list ->
            NavigationDrawerItem(
                label = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(list.name)
                        if (transactionLists.size > 1) {
                            Row {
                                IconButton(onClick = { listToRename = list }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Rename list",
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                                IconButton(onClick = { listToDelete = list }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete list",
                                        modifier = Modifier.size(32.dp),
                                    )
                                }
                            }
                        }
                    }
                },
                selected = list.id == selectedListId,
                onClick = {
                    viewModel.selectList(list.id)
                    onCloseDrawer()
                },
                icon = {}
            )
        }
    }
    NavigationDrawerItem(
        label = { Text("Add New List") },
        selected = false,
        onClick = { showAddListSheet = true },
        icon = { Icon(Icons.Default.Add, contentDescription = "Add new list") }
    )
}

