package r.finance.hazino

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // This makes it a singleton
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "wallet_database" // Your database name from the companion object
        ).build()
    }

    @Provides
    @Singleton // Not strictly needed if AppDatabase is a singleton, but good practice
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }
}


/*
*
* Add form dialog to input custom transaction details
* Implement undo delete with Snackbar
* Add search/filter by caption or date
*
* */