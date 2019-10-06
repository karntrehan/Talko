package com.karntrehan.talko.messages

import android.database.sqlite.SQLiteException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.karntrehan.talko.architecture.TestingUtils
import com.karntrehan.talko.messages.landing.MessagesContract
import com.karntrehan.talko.messages.landing.MessagesVM
import com.karntrehan.talko.messages.landing.models.MessageModel
import com.karntrehan.talko.messages.landing.models.MessagesAndUsersJsonModel
import com.karntrehan.talko.messages.utils.TrampolineSchedulerRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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

        whenever(repo.currentUserId()).doReturn(1)

    }

    @Test
    fun `Test first load success`() {
        //Mock a first load from repo
        whenever(repo.isFirstLoad()).doReturn(true)

        //Mock a subject from repo
        val subject = PublishSubject.create<Boolean>()
        whenever(repo.loadMessagesIntoMemory(any())).doReturn(subject)

        //
        whenever(repo.messages(any(), any())).doReturn(Single.just(emptyList()))

        //Trigger
        viewModel.messages()

        verify(loadingObs).onChanged(true)

        verify(repo).loadMessagesIntoMemory(any())

        //Mock a success from the mock for local source insertion
        subject.onNext(true)

        verify(repo).messages(20, 0)

        verify(loadingObs).onChanged(false)
    }

    @Test
    fun `Test first load error`() {
        //Mock a first load from repo
        whenever(repo.isFirstLoad()).doReturn(true)

        //Mock a subject from repo
        val subject = PublishSubject.create<Boolean>()
        whenever(repo.loadMessagesIntoMemory(any())).doReturn(subject)

        //Trigger
        viewModel.messages()

        verify(loadingObs).onChanged(true)

        verify(repo).loadMessagesIntoMemory(any())

        //Mock an SQLite error from the mock for local source insertion
        val error = SQLiteException()
        subject.onError(error)

        verify(repo, never()).messages(20, 0)

        verify(errorObs).onChanged(error)
    }

    @Test
    fun `Test single received message with attachment`() {

    }

    //region region: Utils
    private fun mockLocalData(): MessagesAndUsersJsonModel {
        return gson.fromJson(
            TestingUtils.getResponseFromJson("messages"),
            MessagesAndUsersJsonModel::class.java
        )
    }

    //endregion

}