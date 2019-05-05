//DEPS com.google.code.gson:gson:2.8.5
import com.google.gson.Gson

data class Event(val event_type: String, val data: String, val timestamp: Long)

val words = listOf("boring",
        "fuel",
        "halting",
        "stay",
        "delightful",
        "scientific",
        "mint",
        "part",
        "smart",
        "pear",
        "enthusiastic",
        "fair",
        "boat",
        "branch",
        "blushing",
        "thumb",
        "stitch",
        "whirl",
        "probable"
)

val types = listOf(
        "text",
        "bla"
)

while (true) {
    println(Gson().toJson(Event(types.random(), words.random(), System.currentTimeMillis())))
    Thread.sleep(1_00)
}