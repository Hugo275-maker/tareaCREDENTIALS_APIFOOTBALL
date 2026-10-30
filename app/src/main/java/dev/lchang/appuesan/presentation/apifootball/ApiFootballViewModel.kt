package dev.lchang.appuesan.presentation.apifootball

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.lchang.appuesan.data.remote.apifootball.Country
import dev.lchang.appuesan.data.remote.apifootball.RetrofitInstance
import dev.lchang.appuesan.data.remote.apifootball.TeamWrapper
import dev.lchang.appuesan.data.remote.apifootball.toErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApiFootballViewModel: ViewModel(){

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries
    private val _selectedCountry = MutableStateFlow<Country?>(null)
    val selectedCountry: StateFlow<Country?> = _selectedCountry
    private val _teams = MutableStateFlow<List<TeamWrapper>>(emptyList())
    val teams: StateFlow<List<TeamWrapper>> = _teams
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _errorMessages = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _errorMessages

    init {
        loadCountries()
    }

    fun loadCountries(){
        if (!RetrofitInstance.isConfigured) {
            _errorMessages.value = "Falta API_BASE_URL o API_KEY en local.properties"
            return
        }
        viewModelScope.launch {
            _isLoading.value  = true
            _errorMessages.value = null
            try {
                val response = RetrofitInstance.api.getCountries()
                val apiError = response.errors.toErrorMessage()
                if (apiError != null) {
                    _errorMessages.value = apiError
                } else {
                    _countries.value = response.response.sortedBy { it.name }
                }
            }catch (e: Exception){
                _errorMessages.value = e.message
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTeamsByCountry(country: Country){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessages.value = null
            try {
                val response = RetrofitInstance.api.getTeamsByCountry(country.name)
                val apiError = response.errors.toErrorMessage()
                if (apiError != null) {
                    _teams.value = emptyList()
                    _errorMessages.value = apiError
                } else {
                    _teams.value = response.response
                }
            }catch (e: Exception){
                _errorMessages.value = e.message
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun onCountrySelected(country: Country){
        _selectedCountry.value = country
        loadTeamsByCountry(country)
    }

    // Reintenta la última operación que falló
    fun retry(){
        val country = _selectedCountry.value
        if (_countries.value.isEmpty()) loadCountries()
        else if (country != null) loadTeamsByCountry(country)
    }


}
