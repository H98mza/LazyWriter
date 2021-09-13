package pdm.hamza.lazywriter

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_message.*
import pdm.hamza.lazywriter.database.DBHelper

class AddMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_message)
        supportActionBar?.title = resources.getString(R.string.addMessage)
    }

    fun cancel(view: View) {
        finish()
    }

    fun addMessage(view: View) {
        val text = messageText.text.toString().trim()

        if (text.length > 0) {
            DBHelper(this).addMessage(text)
            Toast.makeText(this, getString(R.string.messageAdded), Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, getString(R.string.noEmptyMessage), Toast.LENGTH_SHORT).show()
        }
    }
}