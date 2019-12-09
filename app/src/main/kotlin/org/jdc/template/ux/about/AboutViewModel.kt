package org.jdc.template.ux.about

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.vikingsen.inject.viewmodel.ViewModelInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jdc.template.Analytics
import org.jdc.template.BuildConfig
import org.jdc.template.ext.saveBodyToFile
import org.jdc.template.model.db.main.individual.Individual
import org.jdc.template.model.db.main.type.IndividualAffiliationType
import org.jdc.template.model.repository.IndividualRepository
import org.jdc.template.model.webservice.colors.ColorService
import org.jdc.template.model.webservice.colors.dto.ColorsDto
import org.jdc.template.work.WorkScheduler
import org.threeten.bp.LocalDate
import retrofit2.Response
import timber.log.Timber
import java.io.File

class AboutViewModel
@ViewModelInject constructor(
        private val analytics: Analytics,
        private val application: Application,
        private val individualRepository: IndividualRepository,
        private val colorService: ColorService,
        private val workScheduler: WorkScheduler,
        @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    var appVersion = BuildConfig.VERSION_NAME
    var appBuildDateTime = BuildConfig.BUILD_TIME

    fun logAnalytics() {
        analytics.logEvent(Analytics.EVENT_VIEW_ABOUT)
    }

    /**
     * Creates sample data WITH using injection
     */
    fun createSampleDataWithInjection() = viewModelScope.launch {
        // clear any existing items
        individualRepository.deleteAllIndividuals()

        val individual1 = Individual()
        individual1.firstName = "Jeff"
        individual1.lastName = "Campbell"
        individual1.forceSensitive = true
        individual1.individualAffiliateType = IndividualAffiliationType.JEDI
        individual1.birthdate = LocalDate.of(1970, 1, 1)

        val individual1a = Individual()
        individual1a.firstName = "Ty"
        individual1a.lastName = "Campbell"
        individual1a.forceSensitive = false
        individual1a.individualAffiliateType = IndividualAffiliationType.RESISTANCE
        individual1a.birthdate = LocalDate.of(1970, 1, 1)

        val individual2 = Individual()
        individual2.firstName = "John"
        individual2.lastName = "Miller"
        individual2.forceSensitive = true
        individual2.individualAffiliateType = IndividualAffiliationType.SITH
        individual2.birthdate = LocalDate.of(1970, 1, 2)

        individualRepository.saveNewAffiliate("Campbell", listOf(individual1, individual1a))
        individualRepository.saveNewAffiliate("Miller", listOf(individual2))
    }

    /**
     * Simple web service call
     */
    fun testQueryWebServiceCall() = viewModelScope.launch {
        val response = colorService.colors()

        if (response.isSuccessful) {
            processWebServiceResponse(response)
        } else {
            Timber.e("Failed to get colors from webservice [${response.errorBody()}]")
        }
    }

    /**
     * Simple web service call using the full url (instead of just an endpoint)
     */
    fun testFullUrlQueryWebServiceCall() = viewModelScope.launch {
        val response = colorService.colorsByFullUrl(ColorService.FULL_URL)

        if (response.isSuccessful) {
            processWebServiceResponse(response)
        } else {
            Timber.e("Search FAILED [${response.errorBody()}]")
        }
    }

    /**
     * Web service call that saves response to file, then processes the file (best for large JSON payloads)
     */
    fun testSaveQueryWebServiceCall() = viewModelScope.launch {
        val response = colorService.colorsToFile()

        if (response.isSuccessful) {
            // delete any existing file
            val outputFile = File(application.externalCacheDir, "ws-out.json")
            if (outputFile.exists()) {
                outputFile.delete()
            }

            // save the response body to file
            response.saveBodyToFile(outputFile)

            // show the output of the file
            val fileContents = outputFile.readText()
            Timber.i("Output file: [$fileContents]")
        } else {
            Timber.e("Search FAILED [${response.errorBody()}]")
        }
    }

    private fun processWebServiceResponse(response: Response<ColorsDto>) {
        if (response.isSuccessful) {
            Timber.i("Search SUCCESS")
            response.body()?.let {
                processSearchResponse(it)
            }
        } else {
            Timber.e("Search FAILURE: code (%d)", response.code())
        }
    }

    private fun processSearchResponse(colorsDto: ColorsDto) {
        for (dtoResult in colorsDto.colors) {
            Timber.i("Result: %s", dtoResult.colorName)
        }
    }

    /**
     * Sample for creating a scheduled simple worker
     */
    fun workManagerSimpleTest() = viewModelScope.launch {
        workScheduler.scheduleSimpleWork("test1")
        workScheduler.scheduleSimpleWork("test2")

        delay(3000)

        workScheduler.scheduleSimpleWork("test3")
    }

    /**
     * Sample for creating a scheduled sync worker
     */
    fun workManagerSyncTest() = viewModelScope.launch {
        workScheduler.scheduleSync()
        workScheduler.scheduleSync(true)

        delay(3000)

        workScheduler.scheduleSync()
    }

    /**
     * Table change listener tests
     */
    fun testTableChange() = viewModelScope.launch {
        // Sample tests
        if (individualRepository.getIndividualCount() == 0L) {
            Timber.e("No data.. cannot perform test")
            return@launch
        }

        // Make some changes
        val originalName: String

        val individualList = individualRepository.getAllIndividuals()
        if (individualList.isNotEmpty()) {
            val individual = individualList[0]
            originalName = individual.firstName
            Timber.i("ORIGINAL NAME = %s", originalName)

            // change name
            individual.firstName = "Bobby"
            individualRepository.saveIndividual(individual)

            // restore name
            individual.firstName = originalName
            individualRepository.saveIndividual(individual)
        } else {
            Timber.e("Cannot find individual")
        }
    }

    fun testStuff() = viewModelScope.launch {
        individualRepository.getAllMembers().forEach {
            Timber.i("Member Info: $it")
        }
    }
}