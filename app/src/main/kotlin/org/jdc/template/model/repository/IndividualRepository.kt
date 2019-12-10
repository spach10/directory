package org.jdc.template.model.repository

import androidx.room.withTransaction
import org.jdc.template.model.db.main.MainDatabaseWrapper
import org.jdc.template.model.db.main.affiliate.Affiliate
import org.jdc.template.model.db.main.individual.Individual
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndividualRepository
@Inject constructor(
    private val mainDatabaseWrapper: MainDatabaseWrapper
){
    private fun mainDatabase() = mainDatabaseWrapper.getDatabase()

    private fun individualDao() = mainDatabase().individualDao
    private fun householdDao() = mainDatabase().affiliateDao
    private fun directoryItemDao() = mainDatabase().directoryItemDao

    fun getDirectoryListFlow() = directoryItemDao().findAllDirectItemsFlow()
    suspend fun getIndividual(individualId: Long) = individualDao().findById(individualId)
    fun getIndividualFlow(individualId: Long) = individualDao().findByIdFlow(individualId)
    suspend fun getAllIndividuals() = individualDao().findAll()
    suspend fun getIndividualCount() = individualDao().findCount()
    suspend fun getIndividualFirstName(individualId: Long) = individualDao().findFirstName(individualId)

    suspend fun saveIndividual(individual: Individual) {
        if (isPreviouslyInserted(individual)) return

        if (individual.id <= 0) {
            val newId = individualDao().insert(individual)
            individual.id = newId
        } else {
            individualDao().update(individual)
        }
    }

    suspend fun saveAffiliate(affiliate: Affiliate) {
        if (affiliate.id <= 0) {
            val newId = householdDao().insert(affiliate)
            affiliate.id = newId
        } else {
            householdDao().update(affiliate)
        }
    }

    suspend fun saveNewAffiliate(lastName: String, individuals: List<Individual>) {
        mainDatabase().withTransaction {
            val affiliate = Affiliate()
            affiliate.name = lastName
            saveAffiliate(affiliate)

            individuals.forEach { individual ->
                individual.affiliateId = affiliate.id
                saveIndividual(individual)
            }
        }
    }

    suspend fun deleteIndividual(individualId: Long) = individualDao().deleteById(individualId)

    suspend fun deleteAllIndividuals() {
        mainDatabase().withTransaction {
            individualDao().deleteAll()
            householdDao().deleteAll()
        }
    }

    suspend fun getAllMembers() = householdDao().findAllMembers()

    private suspend fun isPreviouslyInserted(individualToInsert: Individual) : Boolean {
        val individuals = getAllIndividuals()
        individuals.map {
            if (it.equals(individualToInsert))
                return true
        }
        return false
    }
}