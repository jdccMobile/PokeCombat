package com.jdccmobile.pokecombat.data.pokeApi

import com.jdccmobile.pokecombat.data.pokeApi.pokedexResponse.PokedexResult
import com.jdccmobile.pokecombat.data.pokeApi.pokemonInfoResponse.PokemonInfoResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiClient {
    @GET("pokemon")
    suspend fun getAllPokemons(): Response<PokedexResult>

    @GET("pokemon/{pokemonId}")
    suspend fun getPokemonInfo(@Path("pokemonId") pokemonId : Int): Response<PokemonInfoResult>
}