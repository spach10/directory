package org.jdc.template.model.webservice.individuals.dto

import kotlinx.serialization.Serializable
import org.jdc.template.model.db.main.type.IndividualAffiliationType

@Serializable
class IndividualDto(
        val firstName: String,
        val lastName: String,
        val birthdate: String,
        val profilePicture: String,
        val forceSensitive: Boolean,
        val affiliation: IndividualAffiliationType
)