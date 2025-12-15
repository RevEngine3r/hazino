package r.finance.hazino

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    ModalDrawerSheet {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(transactionLists) { list ->
                NavigationDrawerItem(
                    label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(list.name)
                            if (transactionLists.size > 1) {
                                IconButton(onClick = { listToDelete = list }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete list")
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
}
