package com.example.melodyflashcards.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.melodyflashcards.R
import com.example.melodyflashcards.data.Flashcard
import com.example.melodyflashcards.data.FlashcardDatabase
import com.example.melodyflashcards.data.FlashcardRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Fragment for displaying and managing flashcard categories
 */
class CategoriesFragment : Fragment() {

    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        
        // Initialize ViewModel
        val dao = FlashcardDatabase.getDatabase(requireContext()).flashcardDao()
        val repository = FlashcardRepository(dao)
        val factory = CategoriesViewModelFactory(repository)
        categoriesViewModel = ViewModelProvider(this, factory)[CategoriesViewModel::class.java]
        
        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.categories_recycler_view)
        adapter = CategoryAdapter(emptyList()) { category ->
            // Navigate to study fragment with selected category
            val action = CategoriesFragmentDirections.actionCategoriesToStudy(category)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        // Observe categories
        categoriesViewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            adapter.updateCategories(categories)
        }
        
        // Set up add category button
        val addButton: FloatingActionButton = view.findViewById(R.id.add_category_button)
        addButton.setOnClickListener {
            showAddCategoryDialog()
        }
        
        return view
    }
    
    private fun showAddCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null)
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add New Category")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val categoryNameInput = dialogView.findViewById<TextView>(R.id.category_name_input)
                val categoryName = categoryNameInput.text.toString().trim()
                
                if (categoryName.isNotEmpty()) {
                    categoriesViewModel.addCategory(categoryName)
                    Toast.makeText(requireContext(), "Category added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Category name cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

/**
 * Adapter for displaying categories in a RecyclerView
 */
class CategoryAdapter(private var categories: List<String>, private val onCategoryClick: (String) -> Unit) : 
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    
    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.category_name)
        val cardCount: TextView = view.findViewById(R.id.card_count)
        val studyButton: View = view.findViewById(R.id.study_button)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category
        holder.cardCount.text = "0 cards" // This should be updated with actual count
        
        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
        
        holder.studyButton.setOnClickListener {
            onCategoryClick(category)
        }
    }
    
    override fun getItemCount() = categories.size
    
    fun updateCategories(newCategories: List<String>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}

/**
 * ViewModel for the Categories screen
 */
class CategoriesViewModel(private val repository: FlashcardRepository) : androidx.lifecycle.ViewModel() {
    
    val allCategories = repository.allCategories
    
    fun addCategory(categoryName: String) {
        // Create a dummy flashcard to ensure the category exists
        val dummyFlashcard = Flashcard(
            front = "Category: $categoryName",
            back = "This is a placeholder card. Add more cards to this category.",
            category = categoryName
        )
        
        androidx.lifecycle.viewModelScope.launch {
            repository.insert(dummyFlashcard)
        }
    }
}

/**
 * Factory for creating the CategoriesViewModel with dependencies
 */
class CategoriesViewModelFactory(private val repository: FlashcardRepository) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}