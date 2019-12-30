package com.andigeeky.mvpapp.api
import okhttp3.Request
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class CallDelegate<TIn, TOut>(
    protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
}

class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, ApiResponse<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<ApiResponse<T>>) = proxy.enqueue(object: Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback.onResponse(this@ResultCall, Response.success(ApiResponse.create(response)))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            callback.onResponse(this@ResultCall, Response.success(ApiResponse.create(t)))
        }
    })

    override fun cloneImpl() = ResultCall(proxy.clone())
}

class ResultAdapter(
    private val type: Type
): CallAdapter<Type, Call<ApiResponse<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> =
        ResultCall(call)
}

class ApiResponseCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                ApiResponse::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    ResultAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }
}
