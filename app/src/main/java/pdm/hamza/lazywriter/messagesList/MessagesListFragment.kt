package pdm.hamza.lazywriter.messagesList

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_messages_list.*
import pdm.hamza.lazywriter.R
import pdm.hamza.lazywriter.database.DBHelper
import pdm.hamza.lazywriter.database.Message

class MessagesListFragment(
    private val messages: MutableList<Message>,
    private val refresh: () -> Unit
) :
    Fragment(R.layout.fragment_messages_list),
    AdapterView.OnItemClickListener,
    AdapterView.OnItemLongClickListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    constructor() : this(mutableListOf(), {})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messagesList.adapter = MessagesListAdapter(requireContext(), messages)
        messagesList.onItemClickListener = this
        messagesList.onItemLongClickListener = this

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(messages[position].text.contains("#GPS#")) {
            val permission1 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            if (permission1 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), 1)
                return
            }

            val permission2 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            if (permission2 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                return
            }

            var coordinates = "NOT_FOUND"
            fusedLocationClient.lastLocation
                .addOnFailureListener {
                    Log.d("bbb", it.toString())
                }
                .addOnSuccessListener { location : Location? ->
                    coordinates = location?.latitude.toString() + ", " + location?.longitude.toString()

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(
                            Intent.EXTRA_TEXT,
                            messages[position].text.replace("#GPS#", coordinates))
                        type = "text/plain"
                    }

                    startActivity(Intent.createChooser(intent, null))
                }


        } else {
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(
                    Intent.EXTRA_TEXT,
                    messages[position].text)
                type = "text/plain"
            }

            startActivity(Intent.createChooser(intent, null))
        }


    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        // Opzione per la cancellazione
        MaterialAlertDialogBuilder(requireContext())
            .setItems(arrayOf(getString(R.string.deleteMessagePrompt))) { _, which ->
                when(which) {
                    // Ask for confirmation
                    0 -> MaterialAlertDialogBuilder(requireContext(),
                        R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog
                    )
                        .setTitle(R.string.deleteMessage)
                        .setMessage(R.string.deleteMessageConfirm)
                        .setNeutralButton(R.string.cancel, null)
                        .setPositiveButton(R.string.yesDelete) { _, _ ->
                            DBHelper(requireContext()).deleteMessage(id)
                            refresh()
                        }
                        .show()
                }
            }
            .show()

        return true
    }
}