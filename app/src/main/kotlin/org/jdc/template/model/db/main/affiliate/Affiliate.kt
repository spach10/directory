package org.jdc.template.model.db.main.affiliate

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Affiliate(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = ""
)
