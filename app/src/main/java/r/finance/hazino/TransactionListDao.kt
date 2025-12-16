package r.finance.hazino

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionListDao {
    @Query("SELECT * FROM transaction_lists ORDER BY listOrder")
    fun getTransactionLists(): Flow<List<TransactionListEntity>>

    @Insert
    suspend fun insert(transactionList: TransactionListEntity)

    @Query("UPDATE transaction_lists SET name = :name, listOrder = :listOrder WHERE id = :id")
    suspend fun update(id: Long, name: String, listOrder: Int)

    @Query("DELETE FROM transaction_lists WHERE id = :listId")
    suspend fun delete(listId: Long)
}
