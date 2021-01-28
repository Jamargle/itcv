package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class ProfileDetailsPresenterTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val viewState = mockk<ProfileDetailsViewState>(relaxed = true)
    private val presenter = ProfileDetailsPresenter(viewState, dispatchers)

    @Test
    fun `on onViewReady displays profile name`() {
        val args = mockk<ProfileDetailsArgs>()
        val expectedUserName = "Some username"
        every { args.userName } returns expectedUserName
        every { args.memberSince } returns Date()

        presenter.onViewReady(args)

        verify { viewState.displayProfileName(expectedUserName) }
    }

    @Test
    fun `on onViewReady displays profile start date`() {
        val args = mockk<ProfileDetailsArgs>()
        val expectedMemberSince = Date(994560000000)
        every { args.userName } returns "Some username"
        every { args.memberSince } returns expectedMemberSince

        presenter.onViewReady(args)

        verify { viewState.displayMemberSince("08 - Jul - 2001") }
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
