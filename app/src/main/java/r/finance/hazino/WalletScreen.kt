package r.finance.hazino


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: WalletViewModel,
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val transactionLists by viewModel.transactionLists.collectAsStateWithLifecycle()
    val selectedListId by viewModel.selectedListId.collectAsStateWithLifecycle()

    val selectedList = transactionLists.find { it.id == selectedListId }

    var showBottomSheet by remember { mutableStateOf(false) }
    var amountInput by remember { mutableStateOf("") }
    var captionInput by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showDeleteSheet by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showAddListDialog by remember { mutableStateOf(false) }

    if (showAddListDialog) {
        AddListDialog(
            onDismiss = { showAddListDialog = false },
            onAdd = { listName ->
                viewModel.addTransactionList(listName)
                showAddListDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedList?.name ?: "Wallet") },
                actions = {
                    IconButton(onClick = { showDeleteSheet = true }) {
                        Icon(
                            Icons.Default.DeleteSweep,
                            contentDescription = "Delete all transactions in this list"
                        )
                    }
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        transactionLists.forEach { list ->
                            DropdownMenuItem(
                                text = { Text(list.name) },
                                onClick = {
                                    viewModel.selectList(list.id)
                                    showMenu = false
                                }
                            )
                        }
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Add New List") },
                            onClick = {
                                showAddListDialog = true
                                showMenu = false
                            }
                        )
                        if (transactionLists.size > 1) {
                            DropdownMenuItem(
                                text = { Text("Delete Current List") },
                                onClick = {
                                    viewModel.deleteTransactionList(selectedListId)
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Reset form and show bottom sheet
                    amountInput = ""
                    captionInput = ""
                    error = null
                    showBottomSheet = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add transaction")
            }
        }
    ) { innerPadding ->
        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No transactions yet")
            }
        } else {
            WalletList(
                transactions = transactions,
                onDeleteClick = { transaction ->
                    viewModel.deleteTransaction(transaction)
                },
                onTransactionClick = { transaction ->
                    //viewModel.deleteTransaction(transaction) // Todo: Edit.
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }

        // The Add Transaction Bottom Sheet
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    error = null
                },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.surface, // Standard surface color
                contentColor = MaterialTheme.colorScheme.onSurface, // Standard on-surface color
            ) {
                AddTransactionSheetContent(
                    amount = amountInput,
                    onAmountChange = { amountInput = it },
                    caption = captionInput,
                    onCaptionChange = { captionInput = it },
                    error = error,
                    onAddIncome = {
                        val amountValue = it.toDoubleOrNull()
                        if (amountValue != null && captionInput.isNotBlank()) {
                            viewModel.addTransaction(
                                amount = amountValue, // Positive for income
                                dateTime = LocalDateTime.now(),
                                caption = captionInput.trim()
                            )
                            scope.launch {
                                bottomSheetState.hide()
                                showBottomSheet = false
                                // Reset states after successful add
                                amountInput = ""
                                captionInput = ""
                                error = null
                            }
                        } else {
                            error = "Please enter a valid amount and description."
                        }
                    },
                    onAddExpense = {
                        val amountValue = it.toDoubleOrNull()
                        if (amountValue != null && captionInput.isNotBlank()) {
                            viewModel.addTransaction(
                                amount = -amountValue, // Negative for expense
                                dateTime = LocalDateTime.now(),
                                caption = captionInput.trim()
                            )
                            scope.launch {
                                bottomSheetState.hide()
                                showBottomSheet = false
                                // Reset states after successful add
                                amountInput = ""
                                captionInput = ""
                                error = null
                            }
                        } else {
                            error = "Please enter a valid amount and description."
                        }
                    }
                )
            }
        }
    }

    if (showDeleteSheet) {
        QuickTransactionDeleteSheet(
            caption = "Confirm delete all transactions?",
            onDelete = {
                viewModel.deleteAllTransactions()
                showDeleteSheet = false
            },
            onDismiss = {
                showDeleteSheet = false
            }
        )
    }
}