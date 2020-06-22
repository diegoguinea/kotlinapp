package guinea.diego.kotlinapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val analytic: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Hola diego ya lo hiciste")
        analytic.logEvent("InitScreen",bundle)

        setUp()
        controlsesion()
    }
    private fun controlsesion(){
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = pref.getString("email",null)
        val provider = pref.getString("provider", null)

        if(email != null && provider != null){

            showHome(email,ProviderType.valueOf(provider))
        }

    }

    private fun setUp(){
        title = "Autenticación"

        btnRegistrar.setOnClickListener{
            if(EmaileditText.text.isNotEmpty() && PswdeditText.text.isNotEmpty()) {
                print(EmaileditText.text.toString() + " contra: " + PswdeditText.text.toString())
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(EmaileditText.text.toString(), PswdeditText.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                        }else{
                            showAlert()
                        }
                }
            }else{
                showAlert()
            }
        }

        btnLogin.setOnClickListener{
            if(EmaileditText.text.isNotEmpty() && PswdeditText.text.isNotEmpty()) {
                print(EmaileditText.text.toString() + " contra: " + PswdeditText.text.toString())
                FirebaseAuth.getInstance().signInWithEmailAndPassword(EmaileditText.text.toString(), PswdeditText.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                        }else{
                            showAlert()
                        }
                    }
            }else{
                showAlert()
            }

        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido algun error respecto al usuario o contraseña")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun showHome(email: String,provider: ProviderType){
        val homeIntent = Intent(this, homeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

}
