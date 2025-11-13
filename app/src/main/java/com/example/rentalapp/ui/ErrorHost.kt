package com.example.rentalapp.ui

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ErrorHost {
    private val _error = MutableSharedFlow<Message>()
    val error = _error.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.Default)

    fun emit(message: Message) {
        Timber.d("Emitting error: $message")
        scope.launch {
            _error.emit(message)
        }
    }
}

// TODO: ローカライズ
sealed interface Message
sealed interface Error : Message {
    data class String(val msg: kotlin.String) : Error
    data object Unknown : Error
}
sealed interface Info : Message{
    data class String(val msg: kotlin.String) : Info
}


fun commonExceptionHandler(messageHost: ErrorHost) = CoroutineExceptionHandler { _, e ->
    val error: Message = if (e.message.isNullOrBlank()) {
        Error.Unknown
    } else {
        Timber.e(e.message!!)
        Error.String(e.message!!)
    }
    messageHost.emit(error)
}