package edu.utsa.cs3443.skyboltecommerceapp.Fragments.Searching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities.Companion.hideBottomNavigation
import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentCameraScannerBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraScannerFragment : Fragment()
{
    private lateinit var binding: FragmentCameraScannerBinding
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraScannerBinding.inflate(inflater)

        cameraExecutor = Executors.newSingleThreadExecutor()

        startCamera()

        return binding.root
    }

    val barLauncher = registerForActivityResult(ScanContract()) {result ->
        if(result.contents != null)
        {
            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Here are your results")
                setMessage("Do you wish to accept the results?")
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton("Delete") { dialog, _ ->
                    Utilities.showToast(requireContext(), "You're a giga-chad")
                    dialog.dismiss()
                }
            }

            alertDialog.create()
            alertDialog.show()
        }

        findNavController().navigateUp()
    }

    private fun startCamera()
    {
        val sOption = ScanOptions()
        sOption.setPrompt("Volume up to flash on")
        sOption.setBeepEnabled(true)
        sOption.setOrientationLocked(false)
        sOption.setCaptureActivity(CaptureActivity::class.java)
        barLauncher.launch(sOption)
        hideBottomNavigation()
    }
}