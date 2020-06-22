package guinea.diego.kotlinapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

enum class ProviderType{
    BASIC
}

    const val TAG = "TAG-FOR-LOG"

class homeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setUp(email?:"",provider?:"")


        val pref = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        pref.putString("email", email)
        pref.putString("provider", provider)
        pref.apply()



    }



    private fun setUp(email: String,provider: String){
        title = "Inicio"
        emailTextview.text = email
        paswdTextview.text = provider



        btncerrarsesion.setOnClickListener {
            val pref = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            pref.clear()
            pref.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        /*btnsubir.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("message")

            //myRef.setValue(apiText.text.toString())
        }*/
        btnbajar.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("message")
            myRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(String::class.java)
                    Log.d(TAG, "Value is: $value")
                    datostextView.text = value.toString()
                }

                override fun onCancelled(error: DatabaseError) {

                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
        btnmodificar.setOnClickListener {

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("message")

            myRef.setValue(modificartext.text.toString())
        }
    }
    /* Llamada a una api que yo mismo he creado

    fun api(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://apifluttercsharp20200521230519.azurewebsites.net/api/Diegoes")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val service = retrofit.create<api>(api::class.java)

        service.getdata().enqueue(object : Callback<List<Post>>{
            override fun  onReponse(
                call: Call<List<Post>>?, response: Response<List<Post>>?
            ){
                val posts = response?.body()
                Log.i(TAG_LOGS, Gson().toJson(posts))
            }

            override fun onFailure(call: Call<List<Post>>?, t: Throwable?) {
                t?.printStackTrace()
            }

        })
    }*/
}
