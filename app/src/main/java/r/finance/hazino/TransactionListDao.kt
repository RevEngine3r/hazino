package r.finance.hazino

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionListDao {
    @Query("SELECT * FROM transaction_lists ORDER BY listOrder")
    fun getTransactionLists(): Flow<List<TransactionListEntity>>

    @Insert
    suspend fun insert(transactionList: TransactionListEntity)

    @Update
    suspend fun update(transactionList: TransactionListEntity)

    @Query("DELETE FROM transaction_lists WHERE id = :listId")
    suspend fun delete(listId: Long)
}
