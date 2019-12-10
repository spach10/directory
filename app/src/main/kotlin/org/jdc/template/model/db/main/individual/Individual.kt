package org.jdc.template.model.db.main.individual

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jdc.template.model.db.main.type.IndividualAffiliationType
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

@Entity
data class Individual(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var birthdate: LocalDate? = null,
    var profilePicture: String = "",
    var forceSensitive: Boolean = false,
    var affiliateId: Long = 0,
    var individualAffiliateType: IndividualAffiliationType? = null
) {

    fun getFullName() = "$firstName $lastName"

    override fun equals(other: Any?): Boolean {
        other?.let {
            if (other is Individual) {
                return this.firstName == other.firstName
                        && this.lastName == other.lastName
                        && this.birthdate == other.birthdate
                        && this.profilePicture == other.profilePicture
                        && this.forceSensitive == other.forceSensitive
                        && this.individualAffiliateType == other.individualAffiliateType
            }
        }
        return false
    }
}
