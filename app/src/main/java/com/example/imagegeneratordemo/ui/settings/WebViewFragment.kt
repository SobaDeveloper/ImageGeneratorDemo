package com.example.imagegeneratordemo.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.imagegeneratordemo.databinding.FragmentWebviewBinding

class WebViewFragment : Fragment() {

    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebClient()
        arguments?.getString(ARG_URL)?.let {
            binding.webView.loadUrl(it)
        }

        binding.webView.loadUrl("https://pitch.com/v/DALL-E-prompt-book-v1-tmd33y")
    }

    private fun setupWebClient() {
        binding.webView.webViewClient = PromptWebViewClient()
        binding.webView.settings.apply {
            domStorageEnabled = true
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            setSupportZoom(true)
        }
    }

    private inner class PromptWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }
    }

    companion object {
        private const val ARG_URL = "arg_url"

        fun newInstance(url: String) = WebViewFragment().apply {
            val args = bundleOf(ARG_URL to url)
            arguments = args
        }
    }
}