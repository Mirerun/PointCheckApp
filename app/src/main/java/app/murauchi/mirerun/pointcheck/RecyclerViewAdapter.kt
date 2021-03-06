package app.murauchi.mirerun.pointcheck

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_point_data_cell.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MyRecyclerViewAdapter(
        private val context: Context,
        var pointList: OrderedRealmCollection<Record>?,
        //private val listener: OnItemClickListener,
        private val autoUpdate: Boolean
    ) : RealmRecyclerViewAdapter<Record, MyRecyclerViewAdapter.ViewHolder>(pointList, autoUpdate) {

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_point_data_cell, viewGroup, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = pointList?.size ?: 0 //リストの要素数を返す

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record: Record = pointList?.get(position) ?: return //要素番号を返す？

            /*holder.container.setOnClickListener{
                listener.onItemClick(record)
            }*/
            holder.typeText.text = record.type
            holder.amountText.text = record.amount
            val df = SimpleDateFormat("yyyy-MM-dd")
            val target = LocalDate.parse(df.format(record.limitDate) ,DateTimeFormatter.ofPattern("uuuu-MM-dd") )//DateからStringからLocalDate
            //val localYear = target.year
            holder.limitYearText.text = target.year.toString() //IntからString
            holder.limitMonthText.text = (target.monthValue).toString()
            holder.limitDayText.text = target.dayOfMonth.toString()

        }

        override fun getItem(position: Int): Record? { //take List-position
            return pointList?.get(position)
        }

        /*fun remove(position: Int) {
            val record: Record = pointList?.get(position) ?: return

            record.remove(position)
            notifyItemRemoved(position)

        }*/

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val container: ConstraintLayout = view.container
            val typeText: TextView = view.findViewById(R.id.typeTextView)
            val amountText: TextView = view.findViewById(R.id.amountTextView)
            val limitYearText: TextView = view.findViewById(R.id.limitYearView)
            val limitMonthText: TextView = view.findViewById(R.id.limitMonthView)
            val limitDayText: TextView = view.findViewById(R.id.limitDayView)
        }

        /*interface OnItemClickListener {
            fun onItemClick(item: Record)
        }*/
}




