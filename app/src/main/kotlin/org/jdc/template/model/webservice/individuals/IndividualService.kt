package org.jdc.template.model.webservice.individuals

import org.jdc.template.model.webservice.individuals.dto.IndividualsDto
import retrofit2.Response
import retrofit2.http.GET

interface IndividualService {
    @GET("/mobile/interview/directory")
    suspend fun colors(): Response<IndividualsDto>

    companion object {
        const val BASE_URL = "https://ldscdn.org"
        const val SUB_URL = "/mobile/interview/directory"
        const val FULL_URL = "$BASE_URL$SUB_URL"
    }
}