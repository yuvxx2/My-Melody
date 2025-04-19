package com.example.melodyflashcards.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.melodyflashcards.data.Flashcard
import com.example.melodyflashcards.data.FlashcardDatabase
import com.example.melodyflashcards.data.FlashcardRepository
import com.example.melodyflashcards.databinding.FragmentCreateFlashcardBinding
import kotlinx.coroutines.launch

class CreateFlashcardFragment : Fragment() {
    private var _binding: FragmentCreateFlashcardBinding? = null
    private val binding get() = _binding!!
    private lateinit var createViewModel: CreateFlashcardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateFlashcardBinding.inflate(inflater, container, false)

        setupViewModel()
        setupViews()
        setupObservers()

        return binding.root
    }

    private fun setupViewModel() {
        val dao = FlashcardDatabase.getDatabase(requireContext().applicationContext).flashcardDao()
        val repository = FlashcardRepository(dao)
        val factory = CreateFlashcardViewModelFactory(repository)
        createViewModel = ViewModelProvider(this, factory)[CreateFlashcardViewModel::class.java]
    }

    private fun setupViews() {
        binding.saveButton.setOnClickListener {
            saveFlashcard()
        }

        binding.clearButton.setOnClickListener {
            clearFields()
        }
    }

    private fun setupObservers() {
        createViewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.categorySpinner.adapter = adapter
        }
    }

    private fun saveFlashcard() {
        try {
            val front = binding.frontInput.text.toString().trim()
            val back = binding.backInput.text.toString().trim()

            if (!isValidInput(front, back)) {
                return
            }

            val category = binding.categorySpinner.selectedItem?.toString() ?: "General"

            val flashcard = Flashcard(
                front = front,
                back = back,
                category = category
            )

            createViewModel.insertFlashcard(flashcard)
            Toast.makeText(requireContext(), "Flashcard saved!", Toast.LENGTH_SHORT).show()
            clearFields()
        } catch (_: Exception) {
            Toast.makeText(requireContext(), "Error creating flashcard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidInput(front: String, back: String): Boolean {
        when {
            front.isEmpty() || back.isEmpty() -> {
                Toast.makeText(requireContext(), "Front and back cannot be empty", Toast.LENGTH_SHORT).show()
                return false
            }
            front.length > MAX_TEXT_LENGTH -> {
                Toast.makeText(requireContext(), "Front text is too long", Toast.LENGTH_SHORT).show()
                return false
            }
            back.length > MAX_TEXT_LENGTH -> {
                Toast.makeText(requireContext(), "Back text is too long", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun clearFields() {
        binding.frontInput.text?.clear()
        binding.backInput.text?.clear()
        binding.categorySpinner.setSelection(0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.let { binding ->
            outState.putString(KEY_FRONT_TEXT, binding.frontInput.text.toString())
            outState.putString(KEY_BACK_TEXT, binding.backInput.text.toString())
            outState.putInt(KEY_SPINNER_POSITION, binding.categorySpinner.selectedItemPosition)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { bundle ->
            _binding?.let { binding ->
                binding.frontInput.setText(bundle.getString(KEY_FRONT_TEXT, ""))
                binding.backInput.setText(bundle.getString(KEY_BACK_TEXT, ""))
                binding.categorySpinner.setSelection(bundle.getInt(KEY_SPINNER_POSITION, 0))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MAX_TEXT_LENGTH = 500
        private const val KEY_FRONT_TEXT = "front_text"
        private const val KEY_BACK_TEXT = "back_text"
        private const val KEY_SPINNER_POSITION = "spinner_position"
    }
}

class CreateFlashcardViewModel(private val repository: FlashcardRepository) : androidx.lifecycle.ViewModel() {
    val allCategories = repository.allCategories

    fun insertFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            repository.insert(flashcard)
        }
    }
}

class CreateFlashcardViewModelFactory(
    private val repository: FlashcardRepository
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateFlashcardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateFlashcardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}