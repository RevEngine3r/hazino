package r.finance.hazino

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(private val dao: TransactionDao) {
    fun getAllTransactions(listId: Long): Flow<List<TransactionEntity>> =
        dao.getAllTransactions(listId)

    suspend fun addTransaction(transaction: TransactionEntity) {
        dao.insertTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        dao.deleteTransaction(transaction)
    }

    suspend fun deleteAllTransactions(listId: Long) {
        dao.deleteAllTransactions(listId)
    }
}