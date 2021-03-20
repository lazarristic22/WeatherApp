package com.example.weatherapp.ui.helpScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherapp.databinding.HelpFragmentBinding

class HelpFragment : Fragment() {

    private lateinit var binding: HelpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HelpFragmentBinding.inflate(inflater, container, false)
        setWebViewClient()
        setupChromeClient()
        setUpViews()
        return binding.root
    }

    private fun setupChromeClient() {
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress < 100) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.progress = newProgress
                } else {
                    binding.progressBar.visibility = View.INVISIBLE

                }
            }
        }
    }

    private fun setWebViewClient() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.swipeRefreshLayout.isEnabled = true
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setUpViews() {
        binding.webView.canGoBackOrForward(20)
        binding.webView.settings.apply {
            // I enabled this here so i could route to your site that requires JS
            javaScriptEnabled = true
            allowFileAccess = true
            domStorageEnabled = true
            allowContentAccess = true
            allowFileAccessFromFileURLs=true
            setAppCacheEnabled(true)
            cacheMode = WebSettings.LOAD_NO_CACHE

        }
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        binding.webView.loadUrl(URL)
        binding.swipeRefreshLayout.setOnRefreshListener {
            SwipeRefreshLayout.OnRefreshListener { binding.webView.reload() }
        }

    }

    companion object {
        const val URL = "https://www.gecko.rs/"
    }

}