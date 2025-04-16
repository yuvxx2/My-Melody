package com.example.melodyflashcards.ui.study

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.melodyflashcards.R
import com.example.melodyflashcards.data.Flashcard
import com.example.melodyflashcards.data.FlashcardDatabase
import com.example.melodyflashcards.data.FlashcardRepository

/**
 * Fragment for studying flashcards with flip animation
 */
class StudyFlashcardsFragment : Fragment() {

    private lateinit var studyViewModel: StudyFlashcardsViewModel
    private lateinit var frontCardView: CardView
    private lateinit var backCardView: CardView
    private lateinit var frontTextView: TextView
    private lateinit var backTextView: TextView
    private lateinit var categoryNameTextView: TextView
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    
    private var isFrontVisible = true
    private lateinit var frontAnimation: AnimatorSet
    private lateinit var backAnimation: AnimatorSet
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_study_flashcards, container, false)
        
        // Initialize ViewModel
        val dao = FlashcardDatabase.getDatabase(requireContext()).flashcardDao()
        val repository = FlashcardRepository(dao)
        val factory = StudyFlashcardsViewModelFactory(repository)
        studyViewModel = ViewModelProvider(this, factory)[StudyFlashcardsViewModel::class.java]
        
        // Initialize views
        frontCardView = view.findViewById(R.id.flashcard_front)
        backCardView = view.findViewById(R.id.flashcard_back)
        frontTextView = view.findViewById(R.id.front_text)
        backTextView = view.findViewById(R.id.back_text)
        categoryNameTextView = view.findViewById(R.id.category_name)
        nextButton = view.findViewById(R.id.next_button)
        prevButton = view.findViewById(R.id.prev_button)
        
        // Set up animations
        setupAnimations()
        
        // Get category from arguments
        val args = arguments?.let { StudyFlashcardsFragmentArgs.fromBundle(it) }
        val categoryId = args?.categoryId ?: -1
        
        if (categoryId != -1) {
            // Load flashcards for specific category
            studyViewModel.loadFlashcardsByCategory(args?.categoryName ?: "General")
            categoryNameTextView.text = "Category: ${args?.categoryName ?: "General"}"
        } else {
            // Load all flashcards
            studyViewModel.loadAllFlashcards()
            categoryNameTextView.text = "Category: All"
        }
        
        // Observe flashcards
        studyViewModel.currentFlashcard.observe(viewLifecycleOwner) { flashcard ->
            updateCardContent(flashcard)
        }
        
        // Set up card flip on click
        val cardContainer = view.findViewById<View>(R.id.card_container)
        cardContainer.setOnClickListener {
            flipCard()
        }
        
        // Set up navigation buttons
        nextButton.setOnClickListener {
            studyViewModel.nextCard()
            resetCardToFront()
        }
        
        prevButton.setOnClickListener {
            studyViewModel.previousCard()
            resetCardToFront()
        }
        
        return view
    }
    
    private fun setupAnimations() {
        frontAnimation = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_front) as AnimatorSet
        backAnimation = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_back) as AnimatorSet
        
        // Set pivot points for rotation
        val scale = requireContext().resources.displayMetrics.density * 8000
        frontCardView.cameraDistance = scale
        backCardView.cameraDistance = scale
        
        // Initially hide back view
        backCardView.visibility = View.GONE
    }
    
    private fun updateCardContent(flashcard: Flashcard?) {
        if (flashcard == null) {
            frontTextView.text = "No flashcards available"
            backTextView.text = "Create some flashcards first!"
            nextButton.isEnabled = false
            prevButton.isEnabled = false
        } else {
            frontTextView.text = flashcard.front
            backTextView.text = flashcard.back
            nextButton.isEnabled = studyViewModel.hasNextCard()
            prevButton.isEnabled = studyViewModel.hasPreviousCard()
        }
    }
    
    private fun flipCard() {
        if (isFrontVisible) {
            // Flip to back
            frontAnimation.setTarget(frontCardView)
            backAnimation.setTarget(backCardView)
            frontAnimation.start()
            backAnimation.start()
            backCardView.visibility = View.VISIBLE
            frontCardView.visibility = View.GONE
        } else {
            // Flip to front
            frontAnimation.setTarget(backCardView)
            backAnimation.setTarget(frontCardView)
            backAnimation.start()
            frontAnimation.start()
            frontCardView.visibility = View.VISIBLE
            backCardView.visibility = View.GONE
        }
        isFrontVisible = !isFrontVisible
    }
    
    private fun resetCardToFront() {
        if (!isFrontVisible) {
            // Reset to front without animation
            frontCardView.visibility = View.VISIBLE
            backCardView.visibility = View.GONE
            isFrontVisible = true
        }
    }
}

/**
 * ViewModel for the Study Flashcards screen
 */
class StudyFlashcardsViewModel(private val repository: FlashcardRepository) : androidx.lifecycle.ViewModel() {
    
    private val _flashcards = androidx.lifecycle.MutableLiveData<List<Flashcard>>()
    private val _currentIndex = androidx.lifecycle.MutableLiveData<Int>(0)
    private val _currentFlashcard = androidx.lifecycle.MediatorLiveData<Flashcard?>()
    
    val currentFlashcard: androidx.lifecycle.LiveData<Flashcard?> = _currentFlashcard
    
    init {
        _currentFlashcard.addSource(_flashcards) { flashcards ->
            updateCurrentFlashcard(flashcards, _currentIndex.value ?: 0)
        }
        
        _currentFlashcard.addSource(_currentIndex) { index ->
            updateCurrentFlashcard(_flashcards.value ?: emptyList(), index)
        }
    }
    
    private fun updateCurrentFlashcard(flashcards: List<Flashcard>, index: Int) {
        _currentFlashcard.value = if (flashcards.isNotEmpty() && index >= 0 && index < flashcards.size) {
            flashcards[index]
        } else {
            null
        }
    }
    
    fun loadAllFlashcards() {
        repository.allFlashcards.observeForever { flashcards ->
            _flashcards.value = flashcards
            _currentIndex.value = 0
        }
    }
    
    fun loadFlashcardsByCategory(category: String) {
        repository.getFlashcardsByCategory(category).observeForever { flashcards ->
            _flashcards.value = flashcards
            _currentIndex.value = 0
        }
    }
    
    fun nextCard() {
        val currentIndex = _currentIndex.value ?: 0
        val flashcards = _flashcards.value ?: emptyList()
        
        if (currentIndex < flashcards.size - 1) {
            _currentIndex.value = currentIndex + 1
        }
    }
    
    fun previousCard() {
        val currentIndex = _currentIndex.value ?: 0
        
        if (currentIndex > 0) {
            _currentIndex.value = currentIndex - 1
        }
    }
    
    fun hasNextCard(): Boolean {
        val currentIndex = _currentIndex.value ?: 0
        val flashcards = _flashcards.value ?: emptyList()
        return currentIndex < flashcards.size - 1
    }
    
    fun hasPreviousCard(): Boolean {
        val currentIndex = _currentIndex.value ?: 0
        return currentIndex > 0
    }
}

/**
 * Factory for creating the StudyFlashcardsViewModel with dependencies
 */
class StudyFlashcardsViewModelFactory(private val repository: FlashcardRepository) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudyFlashcardsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudyFlashcardsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}