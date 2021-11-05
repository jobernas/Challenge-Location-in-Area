package eu.jobernas.locationarea

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import eu.jobernas.locationarea.databinding.ActivityMainBinding
import eu.jobernas.locationarea.location.LocationService

class MainActivity: AppCompatActivity(),
            View.OnClickListener {

    /**
     * Vars
     */
    private var binding: ActivityMainBinding? = null

    /**
     * Super Methods
     **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding = binding
        binding.messageTextView.text = getString(R.string.lb_searching_for_areas)
        binding.handleServiceButton.apply {
            text = if (isMyServiceRunning(LocationService::class.java))
                getString(R.string.lb_stop_service) else getString(R.string.lb_start_service)
            setOnClickListener(this@MainActivity)
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    /**
     * View.OnClickListener
     *
     * @param view
     */
    override fun onClick(view: View?) {
        if (isMyServiceRunning(LocationService::class.java))
            stopService(Intent())
        else
            startService(Intent())
    }

    /**
     * Private Methods
     */


}