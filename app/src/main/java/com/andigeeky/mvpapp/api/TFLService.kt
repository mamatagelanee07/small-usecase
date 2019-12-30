package com.andigeeky.mvpapp.api

import com.andigeeky.mvpapp.lines.vo.Line
import retrofit2.http.GET


/**
 * REST API access points
 */
interface TFLService {
    @GET("Line/Mode/tube")
    suspend fun getLines(): ApiResponse<List<Line>>
}
