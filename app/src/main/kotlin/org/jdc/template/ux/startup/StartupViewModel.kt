package org.jdc.template.ux.startup

import androidx.lifecycle.viewModelScope
import com.vikingsen.inject.viewmodel.ViewModelInject
import kotlinx.coroutines.launch
import org.jdc.template.Analytics
import org.jdc.template.BuildConfig
import org.jdc.template.model.db.converter.DateTimeTextConverter
import org.jdc.template.model.db.main.individual.Individual
import org.jdc.template.model.repository.IndividualRepository
import org.jdc.template.model.webservice.individuals.IndividualService
import org.jdc.template.model.webservice.individuals.dto.IndividualsDto
import org.jdc.template.ui.viewmodel.BaseViewModel
import retrofit2.Response
import timber.log.Timber

class StartupViewModel
@ViewModelInject constructor(
        private val analytics: Analytics,
        private val individualRepository: IndividualRepository,
        private val individualService: IndividualService
) : BaseViewModel<StartupViewModel.Event>() {
    private val debugStartup = false
    private var currentProgressCount = 0

    init {
//        @Suppress("ConstantConditionIf") // used for debugging
//        if (!debugStartup) {
            startup()
//        }
    }

    private fun startup() = viewModelScope.launch {
        analytics.logEvent(Analytics.EVENT_LAUNCH_APP, mapOf(Analytics.PARAM_BUILD_TYPE to BuildConfig.BUILD_TYPE))

        // Make REST call and get individuals
        val response = individualService.individuals()
        processWebServiceResponse(response)

        sendEvent(Event.StartupFinished)
    }

    private suspend fun processWebServiceResponse(response: Response<IndividualsDto>) {
        if (response.isSuccessful) {
            response.body()?.let { individualsDto ->
                for (individual in individualsDto.individuals) {
                    showProgress( "Syncing individuals")

                    val individualToInsert = Individual()
                    individualToInsert.firstName = individual.firstName
                    individualToInsert.lastName = individual.lastName
                    individualToInsert.birthdate = DateTimeTextConverter().fromStringToLocalDate(individual.birthdate)
                    individualToInsert.profilePicture = individual.profilePicture
                    individualToInsert.forceSensitive = individual.forceSensitive
                    individualToInsert.individualAffiliateType = individual.affiliation
                    individualRepository.saveIndividual(individualToInsert)
                }
            }
        } else {
            Timber.e("Search FAILURE: code (%d)", response.code())
        }
    }

    fun debugResumeStartup() {
        startup()
    }

    private fun showProgress(message: String) {
        Timber.i("Startup progress: [%s]", message)
        currentProgressCount += 1
        sendEvent(Event.StartupProgress(currentProgressCount, message))
    }

    companion object {
        const val TOTAL_STARTUP_PROGRESS = 3
    }

    sealed class Event {
        class StartupProgress(val progress: Int, val message: String = "", val indeterminate: Boolean = false) : Event()
        object StartupFinished : Event()
    }
}