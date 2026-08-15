package ru.yandex.praktikumchatapp.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.yandex.praktikumchatapp.data.ChatRepository

class ChatViewModel(
    val isWithReplies: Boolean = true
) : ViewModel() {

    private val repository = ChatRepository()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private val _shouldShowKeyboard = MutableStateFlow(false)

    val shouldShowKeyboard: StateFlow<Boolean> = _shouldShowKeyboard

    // TODO Задание 4: замените messages и shouldShowKeyboard на state

    init {
        viewModelScope.launch {
            while (isWithReplies) {
                repository.getReplyMessage().collect { response ->
                    _shouldShowKeyboard.value = true
                    val currentMessages = _messages.value
                    _messages.value =
                        (currentMessages + Message.OtherMessage(response))

                }
            }
        }
    }

    fun sendMyMessage(messageText: String) {
        val currentMessages = _messages.value
        _messages.value = currentMessages + Message.MyMessage(messageText)
    }
}