package com.andigeeky.mvpapp.api

import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class ApiResponseTest {
    @Test
    fun `test returns empty response if Response body is null` () {
        val emptyResponse = Response.success<String>(
            null
        )
        val response = ApiResponse.create<String>(emptyResponse)
        assert(response is ApiEmptyResponse<*>)
    }

    @Test
    fun `test returns success response if Response is successful` () {
        val headers = okhttp3.Headers.of("link", "link")
        val success = Response.success("foo", headers)
        val successResponse = ApiResponse.create<String>(success)
        assert(successResponse is ApiSuccessResponse<*>)
    }

    @Test
    fun `test returns error response if Response is unsuccessful` () {
        val error = Response.error<String>(
            500,
            ResponseBody.create(MediaType.parse("application/txt"), "blah")
        )
        val errorResponse = ApiResponse.create<String>(error)
        assert(errorResponse is ApiErrorResponse<*>)
    }
}
