package com.andigeeky.mvpapp.api

import com.andigeeky.mvpapp.lines.vo.Line

/**
 * Fake API implementation that does not implement anything.
 * Designed for tests to fake
 */
open class FakeTFLService(
    var linesImpl: suspend () -> ApiResponse<List<Line>> = notImplemented1()
) : TFLService {

    override suspend fun getLines()= linesImpl()

    companion object {
        private fun <R> notImplemented1(): suspend () -> R {
            return {
                TODO("")
            }
        }
    }
}

