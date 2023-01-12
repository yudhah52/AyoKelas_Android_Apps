package com.yhezra.ayokelas.ui.detailclassroom

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.yhezra.ayokelas.ui.ClassroomAddUpdateActivity
import com.yhezra.ayokelas.R
import com.yhezra.ayokelas.databinding.ActivityDetailClassroomBinding
import com.yhezra.ayokelas.db.ClassroomHelper
import com.yhezra.ayokelas.db.DatabaseContract
import com.yhezra.ayokelas.entity.Classroom

class DetailClassroomActivity : AppCompatActivity() {

    private var isTeacher = false
    private var isMember = false
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityDetailClassroomBinding
    private var classroom: Classroom? = null
    private lateinit var classroomHelper: ClassroomHelper
    private var position: Int = 0

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
        binding = ActivityDetailClassroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        supportActionBar?.title = "Detail Classroom"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setupWithNavController(navController)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        classroomHelper = ClassroomHelper.getInstance(applicationContext)
        classroomHelper.open()

        classroom = intent.getParcelableExtra(EXTRA_CLASSROOM)

        if (classroom?.userEmail == user?.email) {
            isTeacher = true
        }
        if (!(classroom?.members == "")) {
            val members = classroom?.members?.split(";")
            for (i in members!!) {
                if (i.split("|")[1] == user?.email) {
                    isMember = true
                    break
                }
            }
        }

        position = intent.getIntExtra(ClassroomAddUpdateActivity.EXTRA_POSITION, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isTeacher) {
            menuInflater.inflate(R.menu.menu_change, menu)
        } else if (isMember) {
            menuInflater.inflate(R.menu.menu_leave_class, menu)
        } else {
            menuInflater.inflate(R.menu.menu_join, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        when (item.itemId) {
            R.id.action_change -> {
                val intent =
                    Intent(this@DetailClassroomActivity, ClassroomAddUpdateActivity::class.java)
                intent.putExtra(ClassroomAddUpdateActivity.EXTRA_POSITION, position)
                intent.putExtra(ClassroomAddUpdateActivity.EXTRA_CLASSROOM, classroom)
                startActivityForResult(intent, ClassroomAddUpdateActivity.REQUEST_UPDATE)
                finish()
            }
            R.id.action_join_class -> {
                if (classroom?.members == "") {
                    classroom?.members += "${user?.displayName}|${user?.email}"
                } else {
                    classroom?.members += ";${user?.displayName}|${user?.email}"
                }

                val intent = Intent()
                intent.putExtra(EXTRA_CLASSROOM, classroom)
                intent.putExtra(EXTRA_POSITION, position)

                val values = ContentValues()
                values.put(DatabaseContract.ClassroomColumns.MEMBERS, classroom?.members)

                val result = classroomHelper.update(classroom?.id.toString(), values)
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    Toast.makeText(
                        this@DetailClassroomActivity,
                        "Berhasil Terdaftar ke dalam Kelas",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@DetailClassroomActivity,
                        "Gagal Terdaftar ke dalam Kelas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.action_leave_class -> {
                val members = classroom?.members?.split(";")
                var updatedMember = String()
                var a = 0
                for (i in members!!) {
                    if (i.split("|")[1] == user?.email) {
                        for (j in members!!) {
                            if (j != i) {
                                updatedMember += if(a!=0) ";${j}" else "${j}"
                                a+=1
                            }
                        }
                        break
                    }
                }
                classroom?.members = updatedMember

                val intent = Intent()
                intent.putExtra(EXTRA_CLASSROOM, classroom)
                intent.putExtra(EXTRA_POSITION, position)

                val values = ContentValues()
                values.put(DatabaseContract.ClassroomColumns.MEMBERS, classroom?.members)

                val result = classroomHelper.update(classroom?.id.toString(), values)
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    Toast.makeText(
                        this@DetailClassroomActivity,
                        "Berhasil Keluar Kelas",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@DetailClassroomActivity,
                        "Gagal Keluar Kelas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}