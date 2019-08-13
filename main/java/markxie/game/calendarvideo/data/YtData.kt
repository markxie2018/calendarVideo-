package markxie.game.calendarvideo.data

import com.google.gson.annotations.SerializedName

data class YtRowData(
    @SerializedName("nextPageToken")
    val nextPageToken: String? = "",
    val items: List<YtItem>,
    val pageInfo: PageInfo

)

data class YtItem(
    val snippet: Snippet
)

data class Snippet(
    @SerializedName("title")
    val title: String,
//    @SerializedName("description")
//    val description: String,
    @SerializedName("publishedAt")
    val date: String,
    val resourceId: ResourceId
)


data class ResourceId(
    @SerializedName("videoId")
    val id: String
)

data class PageInfo(
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("resultsPerPage")
    val resultsPerPage: Int
)