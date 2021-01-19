package ru.baiganov.cryptoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.baiganov.cryptoapp.R
import ru.baiganov.cryptoapp.pojo.CoinPriceInfo

class CoinInfoAdapter(): RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    var coinInfoList: List<CoinPriceInfo> = listOf()
    var onCoinClickListener:OnCoinClickListener? = null

    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coin_info, parent, false)
        return CoinInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        holder.bind(coinInfoList[position])
    }

    override fun getItemCount(): Int = coinInfoList.size

    inner class CoinInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivLogoCoin: ImageView = itemView.findViewById(R.id.ivLogoCoin)
        private val tvSymbols: TextView = itemView.findViewById(R.id.tvSymbols)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvTimeUpdate: TextView = itemView.findViewById(R.id.tvTimeUpdate)

        fun bind(coin: CoinPriceInfo) {
            val symbolsTemplate = itemView.context.resources.getString(R.string.symbols_template)
            val lastUpdateTemplate = itemView.context.getString(R.string.last_update_template)
            tvSymbols.text = String.format(symbolsTemplate, coin.fromSymbol, coin.toSymbol)
            tvPrice.text = coin.price.toString()
            tvTimeUpdate.text = String.format(lastUpdateTemplate, coin.getFormattedTime())
            Glide.with(itemView.context)
                .load(coin.getFullImageUrl())
                .into(ivLogoCoin)

            itemView.setOnClickListener {
                onCoinClickListener?.onCoinClick(coin)
            }
        }
    }
}

interface OnCoinClickListener {
    fun onCoinClick(coinPriceInfo: CoinPriceInfo)
}