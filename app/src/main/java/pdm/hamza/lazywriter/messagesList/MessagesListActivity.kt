package pdm.hamza.lazywriter.messagesList

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.activity_messages_list.*
import pdm.hamza.lazywriter.AddMessageActivity
import pdm.hamza.lazywriter.R
import pdm.hamza.lazywriter.database.DBHelper

class MessagesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_list)

        fabAdd?.setOnClickListener{
            val intent = Intent(this, AddMessageActivity::class.java)
            startActivity(intent)
        }

       refreshList()
    }

    private fun refreshList() {
        val messages = DBHelper(this).listMessages()

        supportFragmentManager.commit {
            setReorderingAllowed(true)

            val nextFragment =
                if (messages.size <= 0)
                    Fragment(R.layout.fragment_no_messages)
                else
                    MessagesListFragment(messages, ::refreshList)

            replace(R.id.messagesListFragmentContainer, nextFragment)
        }

        supportActionBar?.title = resources.getString(R.string.myMessages) + " (" + messages.size + "/20)"
        if (messages.size >= 20) {
            fabAdd.hide()
        } else {
            fabAdd.show()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }
}