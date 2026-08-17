package ru.yandex.praktikumchatapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.yandex.praktikumchatapp.data.ChatRepository

class ChatViewModel(
    val isWithReplies: Boolean = true
) : ViewModel() {

    private val repository = ChatRepository()
    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state


    init {
        viewModelScope.launch {
            while (isWithReplies) {
                repository.getReplyMessage().collect { response ->
                    _state.value = _state.value.copy(
                        shouldShowKeyboard = true,
                        messages = _state.value.messages + Message.OtherMessage(response)
                    )
                }
            }
        }
    }

    fun sendMyMessage(messageText: String) {
        _state.value = _state.value.copy(
            messages = _state.value.messages + Message.MyMessage(messageText)
        )
    }
}