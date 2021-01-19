package ru.baiganov.cryptoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_coin_price_list.*
import ru.baiganov.cryptoapp.adapters.CoinInfoAdapter
import ru.baiganov.cryptoapp.adapters.OnCoinClickListener
import ru.baiganov.cryptoapp.pojo.CoinPriceInfo

class CoinPriceListActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_price_list)
        val adapter = CoinInfoAdapter()
        adapter.onCoinClickListener = object : OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
                startActivity(CoinDetailActivity.newIntent(this@CoinPriceListActivity, coinPriceInfo.fromSymbol))
            }
        }
        rvCoinPriceList.adapter = adapter
        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(CoinViewModel::class.java)

        viewModel.priceList.observe(this, Observer {
            adapter.coinInfoList = it
        })
    }
}