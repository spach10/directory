package org.jdc.template.model.db.main.directoryitem

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DirectoryItemDao {
    @Query("SELECT id, lastName, firstName, profilePicture FROM Individual ORDER BY lastName, firstName, profilePicture")
    fun findAllDirectItemsFlow(): Flow<List<DirectoryItem>>
}
