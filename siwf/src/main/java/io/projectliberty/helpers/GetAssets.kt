package io.projectliberty.helpers

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "SIWF.GetAssets"

@Serializable
data class Assets(
    val colors: Colors,
    val content: Content,
    val images: Images
)

@Serializable
data class Colors(
    val primary: String,
    val light: String,
    val dark: String
)

@Serializable
data class Content(
    val title: String
)

@Serializable
data class Images(
    val logoPrimary: String,
    val logoLight: String,
    val logoDark: String
)

// Only one JSON Parser instance is needed
private val jsonParser = Json { ignoreUnknownKeys = true }

suspend fun getRemoteAssets(): Assets? = withContext(Dispatchers.IO) {
    try {
        val urlString = "https://projectlibertylabs.github.io/siwf/v2/assets/assets.json"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.connect()

        return@withContext if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val json = connection.inputStream.bufferedReader().use { it.readText() }
            connection.disconnect()
            jsonParser.decodeFromString<Assets>(json)
        } else {
            Log.e("fetchAssets", "Error: ${connection.responseCode}")
            connection.disconnect()
            null
        }
    } catch (e: Exception) {
        Log.e("fetchAssets", "Exception: ${e.message}")
        null
    }
}

fun getLocalAssets(): Assets {
    return Assets(
        colors = Colors(
            primary = "#54B2AB",
            light = "#ffffff",
            dark = "#000000"
        ),
        content = Content(
            title = "Continue with Frequency"
        ),
        images = Images(
            logoPrimary = "iVBORw0KGgoAAAANSUhEUgAAAGMAAABkCAMAAACl4H4fAAAAdVBMVEUAAAD///////////////////////////////////////////////////////////////////////////////////9gtrCq2NWAxcCVzsvV6+pVsav0+vpqu7XK5+W/4t/f8O/K5uX1+vqf09C13drU6+rq9fVQSSDuAAAAFXRSTlMA3yAQQIC/789wYK+fkH8wUKCwj2/UUKURAAACfklEQVR4Xs3ZCW7bMBCG0Z+rSGqxk3TkLem63P+IRVy3RA0lQ4kcoN8FHjiEAEKD/yxtU3Kvpb3doXXGxl7Rv4XeWYNG2cnTW/nY4EBmUvR+atKoyXoqye+xteSpNJWwJRsyIKNoT2vrNVY1dbShCeXpQNtSGoV96GhrXURRT1TTE/iMp7qCBpNWVJvSgkRGOEIMyYQwYhS1Khgs11O7Biw2UcsmLGSXgR+Hsr7fI7b8vg+Xsl7uDWVw30CNDepxV6J6g5mWUQ0MZloTCRjkkIMmEaMzyA0yBg3sMeoNMvkYUka+Ed1JGflGEpUZ8/mdPtNi8WaEQuOF1udxbUdSRv7YR1HD5VGJGSp/HGIGaQBJ2IgAemFjAKCEDQUYEjbIwIobe0RxI2KsNp4ZY4SvM54P8yfGeKwzzt/my4UzAtR243y80pyh0K018pCuAm90oLVGHtKtAzFtMr5chyRj5CGJGudTFkqNVXeeh7TG6KDKjXm+LHViDIXAGkys4dHXGscza4xVxnw6E9eIWGHMh6/EF7HfbBw/UlEWhjOYIfEZ/s3AD4l/xD0wBjMktkcAkTGYIbGlgrcoPyT+LQrFGMyQmAJec6XGkTY0Xg0rauxwzQsaAb+Lgka6GaYTMzqNW07MGPAnI2Zo/O1ByBiQM52MoZGDEzEc869vg8H9GbUCRsJdfXNjwH1G8Ub17sA2NiwWck0Nh8UeGho9ljOhmaEMsytijOqFFGPILNZ+Mr8+GUJsBym0c855A76xihhRVBTcbddfStAoz206hMOqdL+a8BprS+sGFixyIopP2NrelwkWNWnHHUY5g+p28e3TeGfRKGNdH+4A1UdrUNIvSTqR1zCLoUEAAAAASUVORK5CYII=",
            logoLight = "iVBORw0KGgoAAAANSUhEUgAAAGMAAABkCAMAAACl4H4fAAAAb1BMVEUAAAD///////////////////////////////////////////////////////////////////////////////////+/v78QEBCAgIBAQEBgYGDv7+8AAACvr68gICCgoKB/f39wcHCQkJDPz8/f39+gYSqoAAAAFXRSTlMA3yAQQIC/z+9wYK+fkH8wUKCwj2+qSpY8AAACfklEQVR4Xs3ZiWrkMAzH4b/vOJlkpu0qc/TY8/2fcRmyFCrSWokt2N8LfEgmEGz8Z1nf9+Fef/JHtM75PBj6WBqCd2iUnyJ9VswNBnKToa8zk0VNPpKkeMLe+kjSTI89+bQAeoqNtLXBYlNTRzuaIM8m2pcRj/Kto711GaKeqKYnlHOR6koWhayh2oxVIThSIhQQTigjzlCrksN6A7XrgNUmatmElfw6MF9k/eKIl5/35UXWGzeMA+9AjQ0awOqpucG35YyCwbY1kYJB4cOBk4rROXbgCgYd2BgaBjk2RlODn4jtFAx2Ij3JjPP8Rb9ptYylJDTeaHtxIY6kYLCPfVQ1AluVhmHYx6FhkAXQKxsZwKBsHAAYZcMAjpQNcvDqxglZ3cgYq43ngjEi1hnPl/P3gvFYZ8w/zy8vJSPB7Dfm610uGgbdBoMtaZGLRgfaYLAl/etChXYZr8uSVAy2JC1jvjFBYGw6c7YkodHByI3zMgLvVjAMktDgyY2Ioda4zkVjrDLOt5lKjcgVxvnHM5XLOO02rq8kysPJDb4kYU7+z8CXJM0AeBAZfEnyHgFkkcGXJK8X/4vyJcmzAGAEBl+SvIR7QWpcaUcj7nlV44h7iIpGwlJWNHosuU7N6CyWEBQMfpnh1AyL9x4UDH476rrmBh8DCCpGYHd9Cga/GfUKRg/W0Nw4gOdM2ah+O/CNDY+VQlMjYLWHhsaA9VxqZhgnfSvihsaDFDe0Htb+sKvPCkL3DZIjqZKIDuXGKmKEqKz4tl1/KMlCXtg1RMCm7LCZiBZb67ctLHksqSmxx95OUSZ41GRDaRgTHKo75s+nicGjUc6HITHADNnLJvgL2V6WVVX1ltEAAAAASUVORK5CYII=",
            logoDark = "iVBORw0KGgoAAAANSUhEUgAAAGMAAABkCAQAAAA4N0amAAAC30lEQVR42u2cUXHrMBAAF4IgCEIgCEIgmMEzA4tBzcBl4DIQBEMQBEG4169oMmnrpJIl30x3EWx1ajKaufDHT1gcAwP+5sAVx4XTY3CMrETkRzdWPA7DyXBMBORlA+M5zscwEZEiIxOWbjgCUs3AleYMBKS6kYFmODYkqzHFEpDDXbEcyERCGjlxCJYNaWrEUpl/JKS5iZGKvCHdfKMKhoB0dcNSiCUi3Y1YzRHlITlCcUiO0B1iiMjp3DC8xIqc0oUXmJDTOvEkDnleJ76qo7CnO+Bqe6lLFPaMGHZZkJNnCCs7DIiCjJ3BMkQFGbuDNSFKMgTPN1hEUUbC8CWLqgxh2TkLJRmC4YFFYYZ/PIukIGP3fgxIzYwk7pdehFccuWOrmxGFNgbIXBClGYLjxqw4w3NjU5wRyZ8YijMECwCD8owRAFblGQsAROUZEcAgyjMEAw79GVcY0Z8xwtw/wwhlzhB6ZhiZJMkglPnRMcPJmyT5pDxjg9gjw0mQTHFGhNQuI49RpkpGAmmTkcfoES8U2izjmsdIZUYeI70ZTpaccGDGcVc8j9HxGemgf7jp0+dZhDIjbFUzMi0zAqy9M4K4Chlzv4wkS04ocYaxT0YSL6biF/Vr+4wg1+pPbqZZRh6j2pryJ4WCMar74PZelFE6RuV+AMBYlFE6RuUONR4/S8eoXAvFD26+bIzK3bjha2cEoZUzN5zijAtkgtKMjTtGpRkDdxiSwoyEhXu8woyFB4zCDAuPvCvLWPgSQ1KVYfkGryjD8y2GqCQjYvgBpyRjYIdVQcbCLoZYI6P/5oA7eYbjSfyJMzwv8H7SjJWXMGwnzIiYgqWgggwF2007Gap2zay4L7z8bf5Vj8ibyV0NGKowI92cqcioddG6/y3ZsByCb3gOngOxrE0uteVwBuKho+TIqEwJDDTnSqia4OiGxRefS8RjoD8XRsKvTsDjOBkGh2dl2/3rr4x1f2brP+G5DGplZB6rAAAAAElFTkSuQmCC"
        )
    )
}
