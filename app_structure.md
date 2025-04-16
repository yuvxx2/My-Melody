# My Melody Flashcards App - Implementation Guide

## Core Components

### 1. Database Structure

```kotlin
// Flashcard.kt - Entity class
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val front: String,
    val back: String,
    val category: String,
    val createdDate: Long = System.currentTimeMillis(),
    val lastReviewed: Long? = null,
    val difficulty: Int = 0 // 0-Easy, 1-Medium, 2-Hard
)

// FlashcardDao.kt - Data Access Object
interface FlashcardDao {
    @Insert
    suspend fun insertFlashcard(flashcard: Flashcard): Long
    
    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)
    
    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)
    
    @Query("SELECT * FROM flashcards")
    suspend fun getAllFlashcards(): List<Flashcard>
    
    @Query("SELECT * FROM flashcards WHERE category = :category")
    suspend fun getFlashcardsByCategory(category: String): List<Flashcard>
}

// FlashcardDatabase.kt
@Database(entities = [Flashcard::class], version = 1)
abstract class FlashcardDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
}
```

### 2. UI Screens

#### Main Activity Layout (activity_main.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/pastel_background"
    tools:context=".MainActivity">

    <ImageView
        android:id="@+id/headerImage"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:adjustViewBounds="true"
        android:src="@drawable/melody_header"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/titleText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="My Melody Flashcards"
        android:textColor="@color/primary_pink"
        android:textSize="24sp"
        android:fontFamily="casual"
        android:layout_marginTop="16dp"
        app:layout_constraintTop_toBottomOf="@id/headerImage"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <androidx.cardview.widget.CardView
        android:id="@+id/createCardButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:cardCornerRadius="16dp"
        app:cardElevation="4dp"
        app:cardBackgroundColor="@color/secondary_pink"
        app:layout_constraintTop_toBottomOf="@id/titleText">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="16dp"
            android:text="Create New Flashcard"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="18sp" />
    </androidx.cardview.widget.CardView>

    <androidx.cardview.widget.CardView
        android:id="@+id/studyButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:cardCornerRadius="16dp"
        app:cardElevation="4dp"
        app:cardBackgroundColor="@color/accent_pink"
        app:layout_constraintTop_toBottomOf="@id/createCardButton">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="16dp"
            android:text="Study Flashcards"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="18sp" />
    </androidx.cardview.widget.CardView>

    <androidx.cardview.widget.CardView
        android:id="@+id/categoriesButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:cardCornerRadius="16dp"
        app:cardElevation="4dp"
        app:cardBackgroundColor="@color/pastel_blue"
        app:layout_constraintTop_toBottomOf="@id/studyButton">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="16dp"
            android:text="Categories"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="18sp" />
    </androidx.cardview.widget.CardView>

    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/melody_footer"
        android:layout_marginBottom="16dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Flashcard Creation Screen (fragment_create_card.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/pastel_background">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp">

        <ImageView
            android:id="@+id/createHeaderImage"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:src="@drawable/melody_pencil"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent" />

        <TextView
            android:id="@+id/createTitle"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Create New Flashcard"
            android:textColor="@color/primary_pink"
            android:textSize="22sp"
            android:fontFamily="casual"
            android:layout_marginTop="8dp"
            app:layout_constraintTop_toBottomOf="@id/createHeaderImage"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/frontCardLayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            app:boxBackgroundColor="@color/white"
            app:boxCornerRadiusBottomEnd="16dp"
            app:boxCornerRadiusBottomStart="16dp"
            app:boxCornerRadiusTopEnd="16dp"
            app:boxCornerRadiusTopStart="16dp"
            app:layout_constraintTop_toBottomOf="@id/createTitle">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/frontCardText"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="Front of Card"
                android:inputType="textMultiLine"
                android:minLines="3" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/backCardLayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            app:boxBackgroundColor="@color/white"
            app:boxCornerRadiusBottomEnd="16dp"
            app:boxCornerRadiusBottomStart="16dp"
            app:boxCornerRadiusTopEnd="16dp"
            app:boxCornerRadiusTopStart="16dp"
            app:layout_constraintTop_toBottomOf="@id/frontCardLayout">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/backCardText"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="Back of Card"
                android:inputType="textMultiLine"
                android:minLines="3" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/categoryLayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            app:boxBackgroundColor="@color/white"
            app:boxCornerRadiusBottomEnd="16dp"
            app:boxCornerRadiusBottomStart="16dp"
            app:boxCornerRadiusTopEnd="16dp"
            app:boxCornerRadiusTopStart="16dp"
            app:layout_constraintTop_toBottomOf="@id/backCardLayout">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/categoryText"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="Category" />
        </com.google.android.material.textfield.TextInputLayout>

        <Button
            android:id="@+id/saveButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:background="@drawable/rounded_button"
            android:text="Save Flashcard"
            android:textColor="@color/white"
            android:padding="12dp"
            app:layout_constraintTop_toBottomOf="@id/categoryLayout" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</ScrollView>
```

### 3. Color Resources (colors.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="primary_pink">#FFB6C1</color>
    <color name="secondary_pink">#FFC0CB</color>
    <color name="accent_pink">#FF69B4</color>
    <color name="pastel_background">#FFF0F5</color>
    <color name="text_primary">#FF1493</color>
    <color name="text_secondary">#C71585</color>
    <color name="pastel_blue">#ADD8E6</color>
    <color name="pastel_purple">#D8BFD8</color>
    <color name="white">#FFFFFF</color>
    <color name="black">#000000</color>
</resources>
```

### 4. Study Mode Implementation

```kotlin
// StudyFragment.kt
class StudyFragment : Fragment() {
    private lateinit var binding: FragmentStudyBinding
    private lateinit var viewModel: StudyViewModel
    private var currentPosition = 0
    private var isShowingFront = true
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStudyBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this).get(StudyViewModel::class.java)
        
        // Load flashcards
        viewModel.flashcards.observe(viewLifecycleOwner) { flashcards ->
            if (flashcards.isNotEmpty()) {
                updateCardContent(flashcards[currentPosition])
                binding.progressText.text = "${currentPosition + 1}/${flashcards.size}"
            } else {
                binding.cardContent.text = "No flashcards available"
            }
        }
        
        // Flip card on click
        binding.flashcardView.setOnClickListener {
            flipCard()
        }
        
        // Next card
        binding.nextButton.setOnClickListener {
            viewModel.flashcards.value?.let { flashcards ->
                if (currentPosition < flashcards.size - 1) {
                    currentPosition++
                    isShowingFront = true
                    updateCardContent(flashcards[currentPosition])
                    binding.progressText.text = "${currentPosition + 1}/${flashcards.size}"
                }
            }
        }
        
        // Previous card
        binding.prevButton.setOnClickListener {
            if (currentPosition > 0) {
                currentPosition--
                isShowingFront = true
                viewModel.flashcards.value?.let {
                    updateCardContent(it[currentPosition])
                    binding.progressText.text = "${currentPosition + 1}/${it.size}"
                }
            }
        }
    }
    
    private fun updateCardContent(flashcard: Flashcard) {
        binding.cardContent.text = if (isShowingFront) flashcard.front else flashcard.back
    }
    
    private fun flipCard() {
        val scale = requireContext().resources.displayMetrics.density
        binding.flashcardView.cameraDistance = 8000 * scale
        
        val frontAnim = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_front) as AnimatorSet
        val backAnim = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_back) as AnimatorSet
        
        if (isShowingFront) {
            frontAnim.setTarget(binding.flashcardView)
            frontAnim.start()
            isShowingFront = false
        } else {
            backAnim.setTarget(binding.flashcardView)
            backAnim.start()
            isShowingFront = true
        }
        
        viewModel.flashcards.value?.let {
            updateCardContent(it[currentPosition])
        }
    }
}
```

### 5. Main Activity Implementation

```kotlin
// MainActivity.kt
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Create New Flashcard Button
        findViewById<CardView>(R.id.createCardButton).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateCardFragment())
                .addToBackStack(null)
                .commit()
        }
        
        // Study Flashcards Button
        findViewById<CardView>(R.id.studyButton).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StudyFragment())
                .addToBackStack(null)
                .commit()
        }
        
        // Categories Button
        findViewById<CardView>(R.id.categoriesButton).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CategoriesFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
```

## Implementation Steps

1. **Set up the Android Studio project** following the instructions in project_setup_instructions.md

2. **Create the database components**:
   - Implement the Flashcard entity class
   - Create the FlashcardDao interface
   - Set up the Room database

3. **Design the UI**:
   - Add the color resources to colors.xml
   - Create layouts for main screen, card creation, and study mode
   - Add My Melody/Hello Kitty style images to drawable resources

4. **Implement the core functionality**:
   - MainActivity for navigation
   - CreateCardFragment for adding new flashcards
   - StudyFragment for reviewing flashcards
   - CategoriesFragment for organizing flashcards

5. **Add animations and styling**:
   - Card flip animations
   - Rounded buttons and cards
   - Kawaii design elements

6. **Test and refine**:
   - Test on different screen sizes
   - Ensure smooth navigation
   - Verify database operations

This implementation guide provides the core structure and code samples to create a cute My Melody/Hello Kitty style flashcard app with all the requested features.