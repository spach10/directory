package org.jdc.template.model.db.main.individual

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jdc.template.model.db.main.type.IndividualAffiliationType
import org.threeten.bp.OffsetDateTime

@Entity
data class Individual(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var birthdate: OffsetDateTime,
    var profilePicture: String,
    var forceSensitive: Boolean = false,
    var individualAffiliateType: IndividualAffiliationType
) {

    fun getFullName() = "$firstName $lastName"
}
