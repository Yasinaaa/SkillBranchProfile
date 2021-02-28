package ru.skillbranch.skillarticles.data.remote.interceptors

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.NetworkManager.api
import ru.skillbranch.skillarticles.data.remote.req.RefreshReq

class TokenAuthenticator : Authenticator {

    private val prefManager = PrefManager

    override fun authenticate(route: Route?, response: Response): Request? {

        return if (response.code == 401) {

            val res = api.refreshAccessToken(RefreshReq(prefManager.refreshToken)).execute()
            return if (res.isSuccessful) {

                val bearer = "Bearer ${res.body()!!.accessToken}"

                prefManager.accessToken = bearer
                prefManager.refreshToken = res.body()!!.refreshToken
                response.request.newBuilder()
                    .header("Authorization", bearer)
                    .build()

            } else null

        } else null
    }
}