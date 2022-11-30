package com.example.imagegeneratordemo.ui.dialogs

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import coil.load
import com.example.imagegeneratordemo.R
import com.example.imagegeneratordemo.databinding.DialogImageViewerBinding
import com.example.imagegeneratordemo.utils.ImageExtensions.base64ToBitmap
import com.google.android.material.snackbar.Snackbar

class ImageViewerDialogFragment : DialogFragment() {

    private var _binding: DialogImageViewerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Material_Light_NoActionBar
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogImageViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogFadeInAnimation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bitmap: Bitmap? = arguments?.getString(ARG_IMAGE_URL)?.base64ToBitmap()
        val imageDesc = arguments?.getString(ARG_IMAGE_DESC) ?: getString(R.string.blank_prompt)

        bitmap?.let {
            binding.expandedImage.apply {
                this.load(it) { crossfade(true) }
            }
            binding.downloadButton.setOnClickListener {
                MediaStore.Images.Media.insertImage(
                    requireContext().contentResolver,
                    bitmap,
                    imageDesc,
                    null
                )
                Snackbar.make(
                    binding.parentLayout,
                    getString(R.string.image_saved),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }

        binding.tvDescription.text = imageDesc

        binding.closeButton.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_IMAGE_URL = "image_url"
        const val ARG_IMAGE_DESC = "image_desc"

        fun newInstance(imageUrl: String, imageDesc: String): ImageViewerDialogFragment {
            val dialogFragment = ImageViewerDialogFragment()

            val args = Bundle()
            args.putCharSequence(ARG_IMAGE_URL, imageUrl)
            args.putCharSequence(ARG_IMAGE_DESC, imageDesc)
            dialogFragment.arguments = args
            return dialogFragment
        }
    }
}
