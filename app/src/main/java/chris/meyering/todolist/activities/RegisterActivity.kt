package chris.meyering.todolist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import chris.meyering.todolist.MainActivity
import chris.meyering.todolist.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_login
import kotlinx.android.synthetic.main.activity_login.btn_register
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        initViews()
    }

    private fun initViews() {
        btn_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        btn_register.setOnClickListener {
            val email = et_register_email.text.toString()
            val pass = et_register_password.text.toString()
            val pass2 = et_register_password_verify.text.toString()
            if (pass == pass2) {
                if (isEmailValid(email)) {
                    til_register_password_verify.isErrorEnabled = false

                    register(email, pass)
                } else {
                    til_register_email.error = "Invaild email address!"
                }
            } else {
                til_register_password_verify.error = "Passwords do not match."
            }
        }
    }

    private fun register(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                startActivity(Intent(baseContext, MainActivity::class.java))
            } else {
                Toast.makeText(baseContext, "Failed to authenticate.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //credit https://gist.github.com/ironic-name/f8e8479c76e80d470cacd91001e7b45b#gistcomment-2797638
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }



    //FIREBASE HELP https://firebase.google.com/docs/auth/android/start/

}
