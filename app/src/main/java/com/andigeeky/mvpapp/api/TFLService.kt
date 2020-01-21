package com.andigeeky.mvpapp.api

import com.andigeeky.mvpapp.api.vo.ApiResponse
import com.andigeeky.mvpapp.lines.data.vo.Line
import com.andigeeky.mvpapp.testing.OpenForTesting
import retrofit2.http.GET


/**
 * REST API access points
 */
@OpenForTesting
interface TFLService {
    @GET("Line/Mode/tube")
    suspend fun getLines(): ApiResponse<List<Line>>
}
