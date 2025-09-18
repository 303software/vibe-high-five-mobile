package com.highfive.app.networking

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

class CustomAPIException(val apiError: CustomAPIError, response: HttpResponse, responseText: String) : ResponseException(response, responseText)