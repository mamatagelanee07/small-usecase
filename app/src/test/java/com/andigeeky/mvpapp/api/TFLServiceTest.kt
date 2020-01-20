package com.andigeeky.mvpapp.api

import com.andigeeky.mvpapp.lines.vo.Line
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class TFLServiceTest {

    private lateinit var service : TFLService
    private lateinit var mockWebServer : MockWebServer

    @Before
    fun setUp(){
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .build()
            .create(TFLService::class.java)
    }

    @Test
    fun `test getLines returns list of lines` () {
        enqueueResponse("lines.json")
        val lines = runBlocking {
            (service.getLines() as ApiSuccessResponse).body
        }
        val request = mockWebServer.takeRequest()
        Assert.assertThat(request.path, CoreMatchers.`is`("/Line/Mode/tube"))

        Assert.assertThat<List<Line>>(lines, IsNull.notNullValue())
        Assert.assertEquals(lines.size, 11)
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
            ?.getResourceAsStream("api-response/$fileName")
        val source = inputStream?.let {
            Okio.buffer(Okio.source(it))
        }
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source?.readString(Charsets.UTF_8))
        )
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }
}
