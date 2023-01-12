package com.yhezra.ayokelas

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
import com.yhezra.ayokelas.databinding.ActivityClassroomAddUpdateBinding
import com.yhezra.ayokelas.db.ClassroomHelper
import com.yhezra.ayokelas.db.DatabaseContract
import com.yhezra.ayokelas.entity.Classroom
import java.text.SimpleDateFormat
import java.util.*

class ClassroomAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var classroom:Classroom? = null
    private var position: Int = 0
    private lateinit var classroomHelper: ClassroomHelper

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityClassroomAddUpdateBinding

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
        binding = ActivityClassroomAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        classroomHelper = ClassroomHelper.getInstance(applicationContext)
        classroomHelper.open()

        classroom = intent.getParcelableExtra(EXTRA_CLASSROOM)
        if (classroom != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            classroom = Classroom()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"

            classroom?.let {
                binding.edClassName.setText(it.title)
                binding.edDesc.setText(it.description)
                binding.edTeacher.setText(it.teacher)
            }

        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSubmit.text = btnTitle

        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val title = binding.edClassName.text.toString().trim()
            val description = binding.edDesc.text.toString().trim()
            val teacher = binding.edTeacher.text.toString().trim()

            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            if (title.isEmpty()) {
                binding.edClassName.error = "Field can not be blank"
                return
            }

            classroom?.title = title
            classroom?.description = description
            classroom?.teacher = teacher
            classroom?.user = user?.email.toString().trim()

            val intent = Intent()
            intent.putExtra(EXTRA_CLASSROOM, classroom)
            intent.putExtra(EXTRA_POSITION, position)

            // contentvalues untuk menampung data
            val values = ContentValues()
            values.put(DatabaseContract.ClassroomColumns.TITLE, title)
            values.put(DatabaseContract.ClassroomColumns.DESCRIPTION, description)
            values.put(DatabaseContract.ClassroomColumns.TEACHER, teacher)
            values.put(DatabaseContract.ClassroomColumns.USER, user?.email.toString())

            //Jika merupakan edit maka setresultnya UPDATE, dan jika bukan maka setresultnya ADD

            if (isEdit) {
                val result = classroomHelper.update(classroom?.id.toString(), values)
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@ClassroomAddUpdateActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
                }
            } else {
                classroom?.date = getCurrentDate()
                values.put(DatabaseContract.ClassroomColumns.DATE, getCurrentDate())
                val result = classroomHelper.insert(values)

                if (result > 0) {
                    classroom?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@ClassroomAddUpdateActivity, "Gagal menambah data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus Kelas ini?"
            dialogTitle = "Hapus Kelas"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = classroomHelper.deleteById(classroom?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@ClassroomAddUpdateActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}