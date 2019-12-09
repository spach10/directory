package org.jdc.template.model.db.main.affiliate

import androidx.room.ColumnInfo
import androidx.room.Relation
import org.jdc.template.model.db.main.individual.Individual

data class AffiliateMembers(
        var id: Long = 0L,
        @ColumnInfo(name = "name")
        var affiliateName: String = "",

        @Relation(parentColumn = "id", entityColumn = "affiliateId")
        var members: List<Individual> = emptyList(),

        @Relation(parentColumn = "id", entityColumn = "affiliateId", entity = Individual::class, projection = ["firstName"])
        var memberNames: List<String> = emptyList()
)


