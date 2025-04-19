package com.example.melodyflashcards.ui.home

import android.os.Bundle
import androidx.core.os.bundleOf
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
            require(categoryName.isNotBlank()) { "Category name cannot be blank" }
            return StudyFlashcardsFragmentArgs(
                categoryName = categoryName
            ).toNavDirections()
        }
    }
}

/**
 * Arguments for the StudyFlashcardsFragment
 */
class StudyFlashcardsFragmentArgs(
    val categoryName: String = "General",
    val categoryId: Int = NO_CATEGORY_ID
) {
    fun toNavDirections(): NavDirections {
        val navDirections = ActionOnlyNavDirections(R.id.action_categories_to_study)
        navDirections.arguments = bundleOf(
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_ID to categoryId
        )
        return navDirections
    }

    companion object {
        private const val ARG_CATEGORY_NAME = "categoryName"
        private const val ARG_CATEGORY_ID = "categoryId"
        private const val NO_CATEGORY_ID = -1

        fun fromBundle(bundle: Bundle): StudyFlashcardsFragmentArgs {
            val categoryName = bundle.getString(ARG_CATEGORY_NAME) ?: "General"
            val categoryId = bundle.getInt(ARG_CATEGORY_ID, NO_CATEGORY_ID)
            return StudyFlashcardsFragmentArgs(categoryName, categoryId)
        }
    }
}