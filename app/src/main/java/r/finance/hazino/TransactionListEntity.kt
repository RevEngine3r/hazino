package r.finance.hazino

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_lists")
data class TransactionListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)
