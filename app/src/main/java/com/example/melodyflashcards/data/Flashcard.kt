package com.example.melodyflashcards.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a flashcard in the database
 */
@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val front: String,
    val back: String,
    val category: String,
    val createdDate: Long = System.currentTimeMillis(),
    val lastReviewed: Long? = null,
    val difficulty: Int = 0 // 0-Easy, 1-Medium, 2-Hard
)