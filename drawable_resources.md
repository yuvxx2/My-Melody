# My Melody Flashcards App - Drawable Resources Guide

## Required Drawable Resources

To achieve the cute My Melody/Hello Kitty aesthetic, you'll need to create or download the following drawable resources:

### Header and Footer Images

1. **melody_header.xml** - A vector drawable for the app header
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="240dp"
    android:height="80dp"
    android:viewportWidth="240"
    android:viewportHeight="80">
    <path
        android:fillColor="#FFB6C1"
        android:pathData="M0,0h240v80h-240z" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M120,40m-30,0a30,30 0,1 1,60 0a30,30 0,1 1,-60 0" />
    <path
        android:fillColor="#FF69B4"
        android:pathData="M105,30a5,5 0,1 1,10 0a5,5 0,1 1,-10 0" />
    <path
        android:fillColor="#FF69B4"
        android:pathData="M125,30a5,5 0,1 1,10 0a5,5 0,1 1,-10 0" />
    <path
        android:fillColor="#FF69B4"
        android:pathData="M110,45q10,10 20,0" />
    <path
        android:fillColor="#FFB6C1"
        android:pathData="M90,15q-10,10 -10,20q0,10 10,10q5,0 10,-5q5,5 10,5q10,0 10,-10q0,-10 -10,-20q-5,5 -10,5q-5,0 -10,-5z" />
    <path
        android:fillColor="#FFB6C1"
        android:pathData="M150,15q10,10 10,20q0,10 -10,10q-5,0 -10,-5q-5,5 -10,5q-10,0 -10,-10q0,-10 10,-20q5,5 10,5q5,0 10,-5z" />
</vector>
```

2. **melody_footer.xml** - A vector drawable for the app footer
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="120dp"
    android:height="40dp"
    android:viewportWidth="120"
    android:viewportHeight="40">
    <path
        android:fillColor="#FFB6C1"
        android:pathData="M60,20m-20,0a20,20 0,1 1,40 0a20,20 0,1 1,-40 0" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M50,15a3,3 0,1 1,6 0a3,3 0,1 1,-6 0" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M64,15a3,3 0,1 1,6 0a3,3 0,1 1,-6 0" />
    <path
        android:fillColor="#FF69B4"
        android:pathData="M55,25q5,5 10,0" />
</vector>
```

3. **melody_pencil.xml** - A vector drawable for the create card screen
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="100dp"
    android:height="100dp"
    android:viewportWidth="100"
    android:viewportHeight="100">
    <path
        android:fillColor="#FFB6C1"
        android:pathData="M50,50m-40,0a40,40 0,1 1,80 0a40,40 0,1 1,-80 0" />
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M30,30l40,40l-10,10l-40,-40z" />
    <path
        android:fillColor="#FF69B4"
        android:pathData="M30,30l10,10l-5,5l-10,-10z" />
    <path
        android:fillColor="#FFC0CB"
        android:pathData="M20,80l10,-10l10,10z" />
</vector>
```

### Button Styles

1. **rounded_button.xml** - A drawable for rounded buttons
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/accent_pink" />
    <corners android:radius="24dp" />
</shape>
```

2. **card_background.xml** - A drawable for flashcard backgrounds
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/white" />
    <stroke
        android:width="2dp"
        android:color="@color/primary_pink" />
    <corners android:radius="16dp" />
</shape>
```

### Animation Resources

1. **card_flip_front.xml** - Animation for flipping the card front
```xml
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <objectAnimator
        android:duration="300"
        android:propertyName="rotationY"
        android:valueFrom="0"
        android:valueTo="180" />
    <objectAnimator
        android:duration="1"
        android:propertyName="alpha"
        android:startOffset="150"
        android:valueFrom="1.0"
        android:valueTo="0.0" />
</set>
```

2. **card_flip_back.xml** - Animation for flipping the card back
```xml
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <objectAnimator
        android:duration="300"
        android:propertyName="rotationY"
        android:valueFrom="180"
        android:valueTo="0" />
    <objectAnimator
        android:duration="1"
        android:propertyName="alpha"
        android:startOffset="150"
        android:valueFrom="0.0"
        android:valueTo="1.0" />
</set>
```

## Custom Icons

1. **ic_study.xml** - Study mode icon
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="@color/accent_pink"
        android:pathData="M12,3L1,9L12,15L21,10.09V17H23V9M5,13.18V17.18L12,21L19,17.18V13.18L12,17L5,13.18Z" />
</vector>
```

2. **ic_create.xml** - Create flashcard icon
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="@color/accent_pink"
        android:pathData="M19,13H13V19H11V13H5V11H11V5H13V11H19V13Z" />
</vector>
```

3. **ic_category.xml** - Categories icon
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="@color/accent_pink"
        android:pathData="M10,4H4C2.89,4 2,4.89 2,6V18C2,19.11 2.89,20 4,20H10V4M10,4H20V8H10V4M10,10H20V14H10V10M10,16H20V20H10V16Z" />
</vector>
```

## Implementation Instructions

1. Create a `drawable` folder in your Android Studio project under `app/src/main/res/`
2. Add each of these XML files to the drawable folder
3. Reference these drawables in your layouts using `@drawable/resource_name`

## Additional Resources

For more authentic My Melody and Hello Kitty style, consider downloading official character images (with proper licensing) and adding them to your drawable resources. You can use these images for:

- App icon
- Empty state illustrations
- Background patterns
- Achievement badges

Remember to optimize all images for mobile devices to ensure good performance.

## Styling Tips

- Use the provided vector drawables for a consistent look across different device densities
- Combine these drawable resources with the color palette defined in colors.xml
- Apply animations sparingly to avoid overwhelming the user
- Consider adding subtle shadow effects to buttons and cards for depth