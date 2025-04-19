package com.example.melodyflashcards.ui.home

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.melodyflashcards.R
import kotlin.Int
import kotlin.String

public class CategoriesFragmentDirections private constructor() {
  private data class ActionCategoriesToStudy(
    public val categoryId: Int = -1,
    public val categoryName: String = "General",
  ) : NavDirections {
    public override val actionId: Int = R.id.action_categories_to_study

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("categoryId", this.categoryId)
        result.putString("categoryName", this.categoryName)
        return result
      }
  }

  public companion object {
    public fun actionCategoriesToStudy(categoryId: Int = -1, categoryName: String = "General"):
        NavDirections = ActionCategoriesToStudy(categoryId, categoryName)
  }
}
