package r.finance.hazino

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionListRepository @Inject constructor(private val dao: TransactionListDao) {
    val allTransactionLists: Flow<List<TransactionListEntity>> = dao.getTransactionLists()

    suspend fun addTransactionList(transactionList: TransactionListEntity) {
        dao.insert(transactionList)
    }

    suspend fun updateTransactionList(transactionList: TransactionListEntity) {
        dao.update(transactionList)
    }

    suspend fun deleteTransactionList(listId: Long) {
        dao.delete(listId)
    }
}
