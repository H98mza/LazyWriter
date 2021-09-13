package pdm.hamza.lazywriter.messagesList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import pdm.hamza.lazywriter.R
import pdm.hamza.lazywriter.database.Message

class MessagesListAdapter(private val context: Context, private val data: MutableList<Message>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return data[position].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val newView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_messages, parent, false)

        val messageTextView = newView.findViewById<TextView>(R.id.messageTextView)
        messageTextView.text = data[position].text

        return newView
    }
}
