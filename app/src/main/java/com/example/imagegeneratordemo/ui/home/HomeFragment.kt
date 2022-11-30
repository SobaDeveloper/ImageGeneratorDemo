package com.example.imagegeneratordemo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.imagegeneratordemo.R
import com.example.imagegeneratordemo.databinding.FragmentHomeBinding
import com.example.imagegeneratordemo.ui.common.ViewState
import com.example.imagegeneratordemo.ui.dialogs.ImageViewerDialogFragment
import com.example.imagegeneratordemo.utils.ImageExtensions.base64ToBitmap
import com.example.imagegeneratordemo.utils.gone
import com.example.imagegeneratordemo.utils.invisible
import com.example.imagegeneratordemo.utils.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLiveData()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        setAdapters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tvSubject.setAdapter(null)
        binding.tvDescription.setAdapter(null)
        binding.tvStyle.setAdapter(null)
        _binding = null
    }

    private fun setAdapters() {
        val promptSubjects = resources.getStringArray(R.array.prompt_subject)
        val subjectAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, promptSubjects)
        binding.tvSubject.setAdapter(subjectAdapter)

        val promptDescs = resources.getStringArray(R.array.prompt_description)
        val descAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, promptDescs)
        binding.tvDescription.setAdapter(descAdapter)

        val promptStyles = resources.getStringArray(R.array.prompt_style)
        val styleAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, promptStyles)
        binding.tvStyle.setAdapter(styleAdapter)
    }

    private fun setLiveData() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> startLoadingState()
                is ViewState.Success -> {
                    val imageUrl = it.data.imageList[0].url
                    stopLoadingState()
                    binding.ivImage.apply {
                        load(imageUrl.base64ToBitmap()) {
                            crossfade(true)
                        }
                    }
                }
                is ViewState.Error -> {
                    Snackbar.make(binding.scrollView, it.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startLoadingState() {
        binding.ivImage.invisible()
        binding.ivImagePlaceholder.gone()
        binding.lottiePainting.apply {
            visible()
            playAnimation()
        }
    }

    private fun stopLoadingState() {
        binding.ivImage.visible()
        binding.lottiePainting.apply { pauseAnimation() }
    }

    private fun setListeners() {
        binding.btnToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.btnToggleFree) {
                    binding.freeTextInputLayout.visible()
                    binding.textInputLayoutGroup.gone()
                } else if (checkedId == R.id.btnTogglePreset) {
                    binding.freeTextInputLayout.gone()
                    binding.textInputLayoutGroup.visible()
                }
            }
        }

        binding.btnGenerateImage.setOnClickListener {
            var prompt = ""
            when (binding.btnToggleGroup.checkedButtonId) {
                R.id.btnToggleFree -> {
                    prompt = binding.etPrompt.text.toString()
                }
                R.id.btnTogglePreset -> {
                    prompt = setPresetPrompt()
                }
            }

            if (prompt.isNotEmpty()) {
                binding.ivImagePlaceholder.gone()
                viewModel.getImages(prompt = prompt)
            } else Snackbar.make(
                binding.scrollView,
                getString(R.string.please_input_a_valid_prompt),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        binding.ivImage.setOnClickListener {
            viewModel.currentImage.apply {
                showImageViewerDialog(this.first, this.second)
            }
        }
    }

    private fun showImageViewerDialog(imageUrl: String, imagePrompt: String) =
        ImageViewerDialogFragment.newInstance(imageUrl, imagePrompt)
            .show(childFragmentManager, "")

    private fun setPresetPrompt(): String {
        val prompt1 = binding.tvSubject.text
        val prompt2 = binding.tvDescription.text
        val promptStyle = binding.tvStyle.text
        return "$prompt1 $prompt2, $promptStyle"
    }
}