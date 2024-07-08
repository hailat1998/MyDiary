package com.hd1998.mydiary

import com.hd1998.mydiary.domain.model.Dairy
import java.text.SimpleDateFormat
import java.util.Locale

val dairyList = listOf(
    Dairy(
        title = "Morning Reflections",
        text = "Today I woke up early and went for a walk. The sunrise was beautiful.",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("01 07 2024, 06:30:00")!!
    ),
    Dairy(
        title = "Workday Challenges",
        text = "Had a tough day at work. Lots of meetings and deadlines.",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("02 07 2024, 17:45:00")!!
    ),
    Dairy(
        title = "Evening Relaxation",
        text = "Spent the evening reading a book and drinking tea.",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("03 07 2024, 20:00:00")!!
    ),
    Dairy(
        title = "Weekend Getaway",
        text = "Went on a weekend trip to the mountains. The view was breathtaking.",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("04 07 2024, 14:30:00")!!
    ),
    Dairy(
        title = "Cooking Experiment",
        text = "Tried a new recipe today. It turned out delicious!",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("05 07 2024, 19:00:00")!!
    ),
    Dairy(
        title = "Workout Routine",
        text = "Completed my workout routine. Feeling energized.",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("06 07 2024, 07:00:00")!!
    ),
    Dairy(
        title = "Movie Night",
        text = "Watched a great movie with friends. Had a lot of fun.",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("07 07 2024, 21:00:00")!!
    ),
    Dairy(
        title = "Work Achievement",
        text = "Received praise from my boss for a project well done.",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("08 07 2024, 16:00:00")!!
    ),
    Dairy(
        title = "Gardening Time",
        text = "Spent the afternoon gardening. The flowers are blooming beautifully.",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("09 07 2024, 15:00:00")!!
    ),
    Dairy(
        title = "Family Dinner",
        text = "Had a lovely dinner with my family. Great conversations and food.",
        date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("10 07 2024, 19:30:00")!!
    )
)