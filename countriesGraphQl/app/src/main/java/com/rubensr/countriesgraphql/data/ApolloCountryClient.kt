package com.rubensr.countriesgraphql.data

import com.apollographql.apollo3.ApolloClient
import com.rubensr.CountriesQuery
import com.rubensr.CountryQuery
import com.rubensr.countriesgraphql.domain.CountryClient
import com.rubensr.countriesgraphql.domain.DetailedCountry
import com.rubensr.countriesgraphql.domain.SimpleCountry

class ApolloCountryClient (
    private val apolloClient: ApolloClient
): CountryClient{
    override suspend fun getCountries(): List<SimpleCountry> {
        return apolloClient
            .query(CountriesQuery())
            .execute()
            .data
            ?.countries
            ?.map { country -> country.toSimpleCountry() }
            .orEmpty()
    }

    override suspend fun getCountry(code: String): DetailedCountry? {
        return apolloClient
            .query(CountryQuery(code))
            .execute()
            .data
            ?.country
            ?.toDetailedCountry()
    }

}