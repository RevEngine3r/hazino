package r.finance.hazino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val transactionListRepository: TransactionListRepository
) : ViewModel() {

    private val _selectedListId = MutableStateFlow(1L)
    val selectedListId: StateFlow<Long> = _selectedListId

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions: StateFlow<List<TransactionEntity>> =
        selectedListId.flatMapLatest { listId ->
            transactionRepository.getAllTransactions(listId)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val transactionLists: StateFlow<List<TransactionListEntity>> =
        transactionListRepository.allTransactionLists
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun selectList(listId: Long) {
        _selectedListId.value = listId
    }

    fun addTransaction(amount: Double, dateTime: LocalDateTime, caption: String) {
        viewModelScope.launch {
            transactionRepository.addTransaction(
                TransactionEntity(
                    amount = amount,
                    dateTime = dateTime,
                    caption = caption,
                    listId = selectedListId.value
                )
            )
        }
    }

    fun addTransactionList(name: String) {
        viewModelScope.launch {
            val currentList = transactionLists.value
            val newList = TransactionListEntity(
                name = name,
                listOrder = currentList.size
            )
            transactionListRepository.addTransactionList(newList)
        }
    }

    fun updateTransactionList(transactionList: TransactionListEntity) {
        viewModelScope.launch {
            transactionListRepository.updateTransactionList(transactionList)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
        }
    }

    fun deleteAllTransactions() {
        viewModelScope.launch {
            transactionRepository.deleteAllTransactions(selectedListId.value)
        }
    }

    fun deleteTransactionList(listId: Long) {
        viewModelScope.launch {
            if (_selectedListId.value == listId) {
                val otherLists = transactionLists.value.filter { it.id != listId }
                if (otherLists.isNotEmpty()) {
                    _selectedListId.value = otherLists.first().id
                }
            }
            transactionListRepository.deleteTransactionList(listId)
        }
    }
}