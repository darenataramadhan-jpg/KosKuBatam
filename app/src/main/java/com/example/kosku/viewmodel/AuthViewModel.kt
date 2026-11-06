package com.example.kosku.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kosku.data.DummyData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthState(
    val isLoggedIn: Boolean = false,
    val userName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            delay(900) // simulasi proses autentikasi
            val ok = email.trim().equals(DummyData.VALID_EMAIL, ignoreCase = true) &&
                    password == DummyData.VALID_PASSWORD
            _state.value = if (ok) {
                AuthState(isLoggedIn = true, userName = "Mahasiswa TRPL")
            } else {
                AuthState(error = "Email atau password salah")
            }
        }
    }

    fun clearError() = _state.update { it.copy(error = null) }

    fun logout() { _state.value = AuthState() }
}
