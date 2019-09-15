package chris.meyering.todolist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Toast
import chris.meyering.todolist.MainActivity
import chris.meyering.todolist.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
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

    private fun shakeAnim(v: View) {
        val shake: Animation = AnimationUtils.loadAnimation(this, R.anim.shake)
        v.startAnimation(shake)
    }

    private fun initViews() {
        btn_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        btn_register.setOnClickListener {
            val email = et_register_email.text.toString()
            val pass = et_register_password.text.toString()
            val pass2 = et_register_password_verify.text.toString()
            if (pass.isEmpty()) {
                til_register_password.error = "Field can not be empty."
                shakeAnim(til_register_password)
            } else {
                til_register_password.isErrorEnabled = false
            }
            if (pass2.isEmpty()) {
                til_register_password_verify.error = "Field can not be empty."
                shakeAnim(til_register_password_verify)
            } else {
                til_register_password_verify.isErrorEnabled = false
            }
            if (pass.isNotEmpty() && pass2.isNotEmpty()) {
                if (pass == pass2) {
                    if (isEmailValid(email)) {
                        til_register_password_verify.isErrorEnabled = false
                        register(email, pass)
                    } else {
                        til_register_email.error = "Invaild email address!"
                        shakeAnim(til_register_email)
                    }
                } else {
                    til_register_password_verify.error = "Passwords do not match."
                    til_register_password.error = "Passwords do not match."
                    shakeAnim(til_register_password)
                    shakeAnim(til_register_password_verify)
                }
            }
        }
    }

    private fun register(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                mAuth.currentUser?.sendEmailVerification()
                Log.d("RegisterActivity", "createUserWithEmailAndPassword:success")
                startActivity(Intent(baseContext, MainActivity::class.java))
            } else {
                Log.d("RegisterActivity", "createUserWithEmailAndPassword:failure", it.exception)
                when (it.exception) {
                    is FirebaseAuthWeakPasswordException ->
                        Snackbar.make(findViewById(R.id.activity_register_root), "Password is not strong enough", Snackbar.LENGTH_SHORT).show()
                    is FirebaseAuthInvalidCredentialsException ->
                        Toast.makeText(baseContext, "Email is invalid", Toast.LENGTH_SHORT).show()
                    is FirebaseAuthUserCollisionException ->
                        Snackbar.make(findViewById(R.id.activity_register_root), "Email already in use.", Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.login)) {
                                startActivity(Intent(baseContext, LoginActivity::class.java))
                            }
                            .show()


                }
//                Toast.makeText(baseContext, "Failed to authenticate.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //credit https://gist.github.com/ironic-name/f8e8479c76e80d470cacd91001e7b45b#gistcomment-2797638
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }



    //FIREBASE HELP https://firebase.google.com/docs/auth/android/start/

}
