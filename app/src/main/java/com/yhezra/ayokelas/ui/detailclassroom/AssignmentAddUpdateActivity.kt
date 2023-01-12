package com.yhezra.ayokelas.ui.detailclassroom

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.yhezra.ayokelas.R
import com.yhezra.ayokelas.databinding.ActivityAssignmentAddUpdateBinding
import com.yhezra.ayokelas.db.ClassroomHelper
import com.yhezra.ayokelas.db.DatabaseContract
import com.yhezra.ayokelas.entity.Classroom
import java.text.SimpleDateFormat
import java.util.*

class AssignmentAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var isEditAssignment = false
    private var classroom: Classroom? = null
    private var position: Int = 0
    private lateinit var classroomHelper: ClassroomHelper

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAssignmentAddUpdateBinding

    companion object {
        const val EXTRA_CLASSROOM = "extra_classroom"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        classroomHelper = ClassroomHelper.getInstance(applicationContext)
        classroomHelper.open()

        classroom = intent.getParcelableExtra(EXTRA_CLASSROOM)
        position = intent.getIntExtra(EXTRA_POSITION, 0)

        val actionBarTitle: String
        val btnTitle: String

        if (isEditAssignment) {
            actionBarTitle = "ModifyAssignment"
            btnTitle = "Update"
        } else {
            actionBarTitle = "Add Assignment"
            btnTitle = "Add"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnSubmit.text = btnTitle
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val task = binding.edTask.text.toString().trim()
            val dueDate = binding.edDueDate.text.toString().trim()

            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            if (task.isEmpty()) {
                binding.edTask.error = "Field can not be blank"
                return
            } else if (dueDate.isEmpty()) {
                binding.edDueDate.error = "Field can not be blank"
            }

            if (classroom?.assignments == "" || classroom?.assignments == null) {
                classroom?.assignments = String()
                classroom?.assignments += "$task|$dueDate"
            } else {
                classroom?.assignments += ";$task|$dueDate"
            }

            val intent = Intent()
            intent.putExtra(EXTRA_CLASSROOM, classroom)
            intent.putExtra(EXTRA_POSITION, position)

            // contentvalues untuk menampung data
            val values = ContentValues()
            values.put(DatabaseContract.ClassroomColumns.ASSIGNMENTS, classroom?.assignments)

            val result = classroomHelper.update(classroom?.id.toString(), values)

            if (result > 0) {
                setResult(RESULT_UPDATE, intent)
                Toast.makeText(
                    this@AssignmentAddUpdateActivity,
                    "Berhasil menambahkan tugas ke dalam kelas",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this@AssignmentAddUpdateActivity,
                    "Gagal menambahkan tugas ke dalam kelas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEditAssignment) {
            menuInflater.inflate(R.menu.menu_delete, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                val result = classroomHelper.deleteById(classroom?.id.toString()).toLong()
                if (result > 0) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_POSITION, position)
                    setResult(RESULT_DELETE, intent)
                    Toast.makeText(
                        this@AssignmentAddUpdateActivity,
                        "Tugas berhasil dihapus dari kelas",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@AssignmentAddUpdateActivity,
                        "Gagal menghapus tugas dari kelas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }

}