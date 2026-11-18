package com.example.livesearch

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.livesearch.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etSearchQuery.doOnTextChanged { text, _, _, _ ->
            viewModel.onQueryChanged(text?.toString().orEmpty())
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.searchResults.collect { results ->
                        val currentQuery = binding.etSearchQuery.text.toString().trim()
                        binding.tvSearchResults.text = when {
                            currentQuery.length < 3 -> "Введіть щонайменше 3 символи для пошуку"
                            results.isEmpty() -> "Нічого не знайдено за запитом «$currentQuery»"
                            else -> results.joinToString(separator = "\n") { "• $it" }
                        }
                    }
                }

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.visibility =
                            if (isLoading) View.VISIBLE else View.INVISIBLE
                    }
                }
            }
        }
    }
}