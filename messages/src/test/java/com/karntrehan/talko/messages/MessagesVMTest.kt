package com.karntrehan.talko.messages

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.karntrehan.talko.architecture.TestingUtils
import com.karntrehan.talko.messages.landing.MessagesContract
import com.karntrehan.talko.messages.landing.MessagesVM
import com.karntrehan.talko.messages.landing.models.MessageModel
import com.karntrehan.talko.messages.landing.models.MessagesAndUsersJsonModel
import com.karntrehan.talko.messages.utils.TrampolineSchedulerRule
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MessagesVMTest {

    //Ensure livedata executions happen immediately
    @get:Rule
    var ruleInstant = InstantTaskExecutorRule()

    //Ensure rx executions happen immediately
    @get:Rule
    var ruleRx = TrampolineSchedulerRule()

    //Class to test
    private lateinit var viewModel: MessagesVM
    //Mock the repo
    private val repo = mock<MessagesContract.Repo>()

    //Mock the loading and error observers
    private val loadingObs = mock<Observer<Boolean>>()
    private val errorObs = mock<Observer<Throwable>>()

    //Mock the messages livedata observers
    private val messagesObs = mock<Observer<List<MessageModel>>>()

    private val gson = Gson()

    @Before
    fun setUp() {
        //Setup the viewmodel with mocked repo and test scheduler
        viewModel = MessagesVM(repo = repo)

        //Attach observers to loading and error
        viewModel.loading.observeForever(loadingObs)
        viewModel.error.observeForever(errorObs)

        //Attach observers to pagination and characters list
        viewModel.messages.observeForever(messagesObs)

    }

    //region region: Utils
    private fun pm(message: String) {
        println("\nMessages verified: $message")
    }

    private fun mockLocalData(): MessagesAndUsersJsonModel {
        return gson.fromJson(
            TestingUtils.getResponseFromJson("messages"),
            MessagesAndUsersJsonModel::class.java
        )
    }

    //endregion

}