package ru.baiganov.cryptoapp.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.baiganov.cryptoapp.BuildConfig
import ru.baiganov.cryptoapp.pojo.CoinInfoListOfData
import ru.baiganov.cryptoapp.pojo.CoinPriceInfoRawData

interface ApiService {

    @GET("top/totalvolfull")
    fun getTopInfoCoins(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_LIMIT) limit: Int = 10,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String = CURRENCY
    ): Single<CoinInfoListOfData>

    @GET("pricemultifull")
    fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = API_KEY,
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String = CURRENCY,
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms: String
    ): Single<CoinPriceInfoRawData>

    companion object {
        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"
        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"
        private const val QUERY_PARAM_FROM_SYMBOLS = "fsyms"

        private const val CURRENCY = "USD"
        private const val API_KEY = BuildConfig.MY_API_KEY
    }
}