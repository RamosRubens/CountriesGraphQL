package com.rubensr.countriesgraphql.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubensr.countriesgraphql.domain.DetailedCountry
import com.rubensr.countriesgraphql.domain.GetCountriesUseCase
import com.rubensr.countriesgraphql.domain.GetCountryUseCase
import com.rubensr.countriesgraphql.domain.SimpleCountry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getCountryUseCase: GetCountryUseCase
): ViewModel() {

    private val _state = MutableStateFlow(CountriesState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { loading -> loading.copy(
                isLoading = true
            ) }

            _state.update { countries -> countries.copy(
                countries = getCountriesUseCase.execute(),
                isLoading = false
            ) }
        }
    }

    fun selectCountry(code: String) = viewModelScope.launch {
        _state.update { country -> country.copy(
            selectedCountry = getCountryUseCase.execute(code)
        ) }
    }

    fun dismissCountryDialog(){
        _state.update {country -> country.copy(
            selectedCountry = null
        )}
    }

    data class CountriesState (
        val countries: List<SimpleCountry> = emptyList(),
        val isLoading: Boolean = false,
        val selectedCountry: DetailedCountry ?= null
    )
}