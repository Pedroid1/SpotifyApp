package com.pedroid.data.remote.interceptors

import com.pedroid.domain.session.SessionManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthInterceptorTest {

    @Test
    fun `interceptor adds Authorization header with token`() {
        val sessionManager = mockk<SessionManager>()
        every { sessionManager.getAccessToken() } returns "abc123"

        val interceptor = AuthInterceptor(sessionManager)

        val originalRequest = Request.Builder()
            .url("https://api.test.com")
            .build()

        val chain = mockk<Interceptor.Chain>()
        every { chain.request() } returns originalRequest

        val capturedRequestSlot = slot<Request>()
        every { chain.proceed(capture(capturedRequestSlot)) } answers {
            Response.Builder()
                .request(capturedRequestSlot.captured)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body("{}".toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }

        interceptor.intercept(chain)

        val finalRequest = capturedRequestSlot.captured
        val header = finalRequest.header("Authorization")
        assertEquals("Bearer abc123", header)
    }

    @Test
    fun `interceptor adds Authorization header with null token`() {
        val sessionManager = mockk<SessionManager>()
        every { sessionManager.getAccessToken() } returns null

        val interceptor = AuthInterceptor(sessionManager)

        val originalRequest = Request.Builder()
            .url("https://api.test.com")
            .build()

        val chain = mockk<Interceptor.Chain>()
        every { chain.request() } returns originalRequest

        val capturedRequestSlot = slot<Request>()
        every { chain.proceed(capture(capturedRequestSlot)) } answers {
            Response.Builder()
                .request(capturedRequestSlot.captured)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body("{}".toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }

        interceptor.intercept(chain)

        val finalRequest = capturedRequestSlot.captured
        val header = finalRequest.header("Authorization")
        assertEquals("Bearer null", header)
    }
}
