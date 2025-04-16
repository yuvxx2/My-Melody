package com.example.melodyflashcards.ui.home

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.melodyflashcards.R

/**
 * Navigation directions for the CategoriesFragment
 */
class CategoriesFragmentDirections private constructor() {
    companion object {
        /**
         * Navigate from categories to study fragment with the selected category
         */
        fun actionCategoriesToStudy(categoryName: String): NavDirections {
            return StudyFlashcardsFragmentArgs(categoryName).toNavDirections()
        }
    }
}

/**
 * Arguments for the StudyFlashcardsFragment
 */
class StudyFlashcardsFragmentArgs(val categoryName: String, val categoryId: Int = -1) {
    fun toNavDirections(): NavDirections {
        return ActionOnlyNavDirections(R.id.action_categories_to_study)
    }
    
    companion object {
        fun fromBundle(bundle: Bundle): StudyFlashcardsFragmentArgs {
            val categoryName = bundle.getString("categoryName") ?: "General"
            val categoryId = bundle.getInt("categoryId", -1)
            return StudyFlashcardsFragmentArgs(categoryName, categoryId)
        }
    }
}