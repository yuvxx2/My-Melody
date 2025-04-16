package com.example.melodyflashcards.data

import androidx.lifecycle.LiveData

/**
 * Repository class that abstracts access to the flashcard database
 */
class FlashcardRepository(private val flashcardDao: FlashcardDao) {
    
    val allFlashcards: LiveData<List<Flashcard>> = flashcardDao.getAllFlashcards()
    val allCategories: LiveData<List<String>> = flashcardDao.getAllCategories()
    
    suspend fun insert(flashcard: Flashcard): Long {
        return flashcardDao.insertFlashcard(flashcard)
    }
    
    suspend fun update(flashcard: Flashcard) {
        flashcardDao.updateFlashcard(flashcard)
    }
    
    suspend fun delete(flashcard: Flashcard) {
        flashcardDao.deleteFlashcard(flashcard)
    }
    
    fun getFlashcardsByCategory(category: String): LiveData<List<Flashcard>> {
        return flashcardDao.getFlashcardsByCategory(category)
    }
    
    fun getFlashcardCountByCategory(category: String): LiveData<Int> {
        return flashcardDao.getFlashcardCountByCategory(category)
    }
}