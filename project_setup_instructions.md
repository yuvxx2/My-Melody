# My Melody Flashcard App - Project Setup Instructions

## Project Setup in Android Studio

1. **Create a new Android Studio project**
   - Open Android Studio
   - Click on "File" > "New" > "New Project"
   - Select "Empty Activity"
   - Configure your project:
     - Name: "My Melody Flashcards"
     - Package name: "com.example.melodyflashcards"
     - Save location: Select the current directory
     - Language: Kotlin
     - Minimum SDK: API 21 (Android 5.0) or higher

2. **Project Structure**
   - `/app/src/main/java/com/example/melodyflashcards/` - Kotlin source files
     - `MainActivity.kt` - Main entry point
     - `data/` - Data models and database
       - `Flashcard.kt` - Flashcard model
       - `FlashcardDatabase.kt` - Room database setup
       - `FlashcardDao.kt` - Data access object
     - `ui/` - User interface components
       - `create/` - Card creation screens
       - `study/` - Study mode screens
       - `home/` - Home screen
   - `/app/src/main/res/` - Resources
     - `layout/` - XML layouts
     - `drawable/` - Images and graphics
     - `values/` - Colors, strings, styles

## Design Guidelines

### Color Palette
- Primary Pink: #FFB6C1 (Light Pink)
- Secondary Pink: #FFC0CB (Pink)
- Accent: #FF69B4 (Hot Pink)
- Background: #FFF0F5 (Lavender Blush)
- Text Primary: #FF1493 (Deep Pink)
- Text Secondary: #C71585 (Medium Violet Red)

### Typography
- Use rounded, playful fonts
- Consider "Comic Sans MS" or "Quicksand" for headers
- Standard sans-serif for body text

### UI Elements
- Rounded corners (8-16dp radius)
- Soft shadows
- My Melody/Hello Kitty character elements in headers and empty states
- Pastel button styles
- Card-based design for flashcards

## Core Features Implementation

1. **Flashcard Creation**
   - Form with front/back text fields
   - Category selection
   - Optional image attachment
   - Save to local database

2. **Study Mode**
   - Flip animation between front/back
   - Swipe gestures for card navigation
   - Self-assessment options (Easy, Medium, Hard)
   - Progress tracking

3. **Organization**
   - Category creation and management
   - Search functionality
   - Sort and filter options

4. **Settings**
   - Theme options (My Melody vs Hello Kitty)
   - Font size adjustment
   - Study mode preferences

Follow these instructions to set up the project structure in Android Studio and implement the core features with the kawaii aesthetic.