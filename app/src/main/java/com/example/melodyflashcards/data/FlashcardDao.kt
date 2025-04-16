package com.example.melodyflashcards.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object for Flashcard entities
 */
@Dao
interface FlashcardDao {
    @Insert
    suspend fun insertFlashcard(flashcard: Flashcard): Long
    
    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)
    
    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)
    
    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards(): LiveData<List<Flashcard>>
    
    @Query("SELECT * FROM flashcards WHERE category = :category")
    fun getFlashcardsByCategory(category: String): LiveData<List<Flashcard>>
    
    @Query("SELECT DISTINCT category FROM flashcards")
    fun getAllCategories(): LiveData<List<String>>
    
    @Query("SELECT COUNT(*) FROM flashcards WHERE category = :category")
    fun getFlashcardCountByCategory(category: String): LiveData<Int>
}