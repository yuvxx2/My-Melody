package com.example.melodyflashcards.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.melodyflashcards.R
import com.example.melodyflashcards.data.Flashcard
import com.example.melodyflashcards.data.FlashcardDatabase
import com.example.melodyflashcards.data.FlashcardRepository

/**
 * Fragment for creating new flashcards
 */
class CreateFlashcardFragment : Fragment() {

    private lateinit var createViewModel: CreateFlashcardViewModel
    private lateinit var frontEditText: EditText
    private lateinit var backEditText: EditText
    private lateinit var categorySpinner: Spinner
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_flashcard, container, false)
        
        // Initialize ViewModel
        val dao = FlashcardDatabase.getDatabase(requireContext()).flashcardDao()
        val repository = FlashcardRepository(dao)
        val factory = CreateFlashcardViewModelFactory(repository)
        createViewModel = ViewModelProvider(this, factory)[CreateFlashcardViewModel::class.java]
        
        // Initialize views
        frontEditText = view.findViewById(R.id.front_edit_text)
        backEditText = view.findViewById(R.id.back_edit_text)
        categorySpinner = view.findViewById(R.id.category_spinner)
        val saveButton: Button = view.findViewById(R.id.save_button)
        val clearButton: Button = view.findViewById(R.id.clear_button)
        
        // Set up category spinner
        createViewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }
        
        // Set up save button
        saveButton.setOnClickListener {
            saveFlashcard()
        }
        
        // Set up clear button
        clearButton.setOnClickListener {
            clearFields()
        }
        
        return view
    }
    
    private fun saveFlashcard() {
        val front = frontEditText.text.toString().trim()
        val back = backEditText.text.toString().trim()
        
        if (front.isEmpty() || back.isEmpty()) {
            Toast.makeText(requireContext(), "Front and back cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        
        val category = if (categorySpinner.selectedItem != null) {
            categorySpinner.selectedItem.toString()
        } else {
            "General"
        }
        
        val flashcard = Flashcard(
            front = front,
            back = back,
            category = category
        )
        
        createViewModel.insertFlashcard(flashcard)
        Toast.makeText(requireContext(), "Flashcard saved!", Toast.LENGTH_SHORT).show()
        clearFields()
    }
    
    private fun clearFields() {
        frontEditText.text.clear()
        backEditText.text.clear()
        categorySpinner.setSelection(0)
    }
}

/**
 * ViewModel for the Create Flashcard screen
 */
class CreateFlashcardViewModel(private val repository: FlashcardRepository) : androidx.lifecycle.ViewModel() {
    
    val allCategories = repository.allCategories
    
    fun insertFlashcard(flashcard: Flashcard) {
        androidx.lifecycle.viewModelScope.launch {
            repository.insert(flashcard)
        }
    }
}

/**
 * Factory for creating the CreateFlashcardViewModel with dependencies
 */
class CreateFlashcardViewModelFactory(private val repository: FlashcardRepository) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateFlashcardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateFlashcardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}