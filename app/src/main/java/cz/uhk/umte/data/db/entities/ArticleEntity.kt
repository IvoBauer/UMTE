package cz.uhk.umte.data.db.entities

import java.util.Date

class ArticleEntity(
    val title: String?,
    val summary: String? = "Summary not found.",
    val pubDate: Date?,
    val uri: String? = "https://kinsta.com/wp-content/uploads/2018/08/funny-404-page.jpg"
)