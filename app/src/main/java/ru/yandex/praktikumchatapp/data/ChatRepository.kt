package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import kotlin.time.Duration.Companion.milliseconds

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        return api.getReply().retryWhen { cause, attempt ->
            if (cause is Exception && attempt < RETRIES) {
                val delay = INIT_DELAY * (attempt + 1)
                delay(delay.milliseconds)
                true
            } else
                false

        }
    }

    companion object {
        private const val INIT_DELAY = 10L
        private const val RETRIES = 5
    }
}