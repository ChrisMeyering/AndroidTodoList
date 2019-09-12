package chris.meyering.todolist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import chris.meyering.todolist.R
import kotlinx.android.synthetic.main.activity_new_item.*

class NewItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)
        fab_save_todo_item.setOnClickListener {

        }
    }
}
