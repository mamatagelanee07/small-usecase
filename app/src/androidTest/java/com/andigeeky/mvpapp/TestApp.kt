package com.andigeeky.mvpapp

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [TFLTestRunner].
 */
class TestApp : Application()
