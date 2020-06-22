package guinea.diego.kotlinapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface api {
    @GET("https://apifluttercsharp20200521230519.azurewebsites.net/api/Diegoes")
    fun getdata(): Call<List<Post>>

    @GET("https://apifluttercsharp20200521230519.azurewebsites.net/api/Diegoes/{id}")
    fun getvalor(@Path("id")id:Int): Call<Post>
}
class Post(
    var id:Int,
    var name:String
)