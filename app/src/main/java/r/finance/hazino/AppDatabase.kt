package r.finance.hazino

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}

class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
        return dateTime?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDateTime(epochMilli: Long?): LocalDateTime? {
        return epochMilli?.let {
            Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC).toLocalDateTime()
        }
    }
}