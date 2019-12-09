package org.jdc.template.model.db.main.affiliate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AffiliateDao {
    @Insert
    suspend fun insert(affiliate: Affiliate): Long

    @Insert
    suspend fun update(affiliate: Affiliate)

    @Query("DELETE FROM Affiliate")
    suspend fun deleteAll()

    @Query("SELECT * FROM Affiliate")
    suspend fun findAllMembers(): List<AffiliateMembers>
}
