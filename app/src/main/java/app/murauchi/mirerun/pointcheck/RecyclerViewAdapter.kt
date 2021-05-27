package app.murauchi.mirerun.pointcheck

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class RecyclerViewAdapter(
    private val context: Context,
    private var pointList: OrderedRealmCollection<Record>?,
    private val autoUpdate: Boolean
    ) : RealmRecyclerViewAdapter<Record, RecyclerViewAdapter.ViewHolder>(pointList, autoUpdate) {

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_point_data_cell, viewGroup, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = pointList?.size ?: 0 //リストの要素数を返す

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record: Record = pointList?.get(position) ?: return //要素番号を返す？

            holder.typeText.text = record.type
            holder.amountText.text = record.amount
            holder.limitText.text = record.limit.toString()

        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val typeText: TextView = view.findViewById(R.id.typeTextView)
            val amountText: TextView = view.findViewById(R.id.amountTextView)
            val limitText: TextView = view.findViewById(R.id.limitTextView)
        }

}

