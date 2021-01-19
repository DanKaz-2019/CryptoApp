package ru.baiganov.cryptoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.baiganov.cryptoapp.api.ApiFactory
import ru.baiganov.cryptoapp.database.AppDatabase
import ru.baiganov.cryptoapp.pojo.CoinPriceInfo
import ru.baiganov.cryptoapp.pojo.CoinPriceInfoRawData
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getIPriceList()

    init {
        loadData()
    }

    fun getDetailInfo(fSymbol: String): LiveData<CoinPriceInfo> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSymbol)
    }

    private fun loadData() {
        val disposable = ApiFactory.apiService.getTopInfoCoins(limit = 50)
                .map { it.data?.map { it.coinInfo?.name }?.joinToString(",") }
                .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
                .map { getPriceListFromRaData(it) }
                .delaySubscription(10, TimeUnit.SECONDS)
                .repeat()
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    db.coinPriceInfoDao().insertPriceList(it)
                }, {
                    Log.d("ERROR", "Failure: ${it.message}")
                })
        compositeDisposable.addAll(disposable)
    }

    private fun getPriceListFromRaData(
            coinPriceInfoRawData: CoinPriceInfoRawData
    ): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currency in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                        currencyJson.getAsJsonObject(currency),
                        CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}