package ru.baiganov.cryptoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_coin_detail.*

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)
        if (!intent.hasExtra(EXTRA_FROM_SYMBOLS)) {
            finish()
            return
        }
        val fromSymbol: String = intent.getStringExtra(EXTRA_FROM_SYMBOLS).toString()
        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(CoinViewModel::class.java)
        viewModel.getDetailInfo(fromSymbol).observe(this, Observer {
            tvPrice.text = it.price.toString()
            tvMinPrice.text = it.lowDay.toString()
            tvMaxPrice.text = it.highDay.toString()
            tvLastMarket.text = it.lastMarket
            tvLastUpdate.text = it.getFormattedTime()
            tvFromSymbol.text = it.fromSymbol
            tvToSymbol.text = it.toSymbol
            Glide.with(applicationContext)
                .load(it.getFullImageUrl())
                .into(ivLogoCoin)
        })
    }

    companion object {
        private const val EXTRA_FROM_SYMBOLS = "fSym"

        fun newIntent(context: Context, fromSymbol: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOLS, fromSymbol)
            return intent
        }
    }
}