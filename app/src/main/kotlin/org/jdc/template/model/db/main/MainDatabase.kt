package org.jdc.template.model.db.main

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.dbtools.android.room.DatabaseViewQuery
import org.jdc.template.model.db.converter.DateTimeTextConverter
import org.jdc.template.model.db.main.converter.MainDatabaseConverters
import org.jdc.template.model.db.main.directoryitem.DirectoryItem
import org.jdc.template.model.db.main.directoryitem.DirectoryItemDao
import org.jdc.template.model.db.main.affiliate.Affiliate
import org.jdc.template.model.db.main.affiliate.AffiliateDao
import org.jdc.template.model.db.main.individual.Individual
import org.jdc.template.model.db.main.individual.IndividualDao

@Database(
    entities = [
        Individual::class,
        Affiliate::class
    ],
    views = [
        DirectoryItem::class
    ],
    version = 1
)
@TypeConverters(MainDatabaseConverters::class, DateTimeTextConverter::class, DateTimeTextConverter::class)
abstract class MainDatabase : RoomDatabase() {

    abstract val individualDao: IndividualDao
    abstract val affiliateDao: AffiliateDao
    abstract val directoryItemDao: DirectoryItemDao

    companion object {
        // if no WAL support - only allow 1 single-threaded write operations (assign this in the Room.databaseBuilder.setTransactionExecutor(...) (MainDatabaseWrapper.kt))
        // val TRANSACTION_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()

        const val DATABASE_NAME = "main.db"
        val DATABASE_VIEW_QUERIES = listOf(
            DatabaseViewQuery(DirectoryItem.VIEW_NAME, DirectoryItem.VIEW_QUERY)
        )
    }
}