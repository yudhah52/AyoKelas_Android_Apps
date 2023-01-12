package com.yhezra.ayokelas.ui.navigationdrawer

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.yhezra.ayokelas.databinding.FragmentProfileBinding
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    private lateinit var imageUri: Uri

    companion object {
        const val REQUEST_CAMERA = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        if (user != null) {
            if (user.photoUrl != null) {
                Picasso.get().load(user.photoUrl).into(fragmentProfileBinding.ivProfile)
            } else {
                Picasso.get()
                    .load("https://i.picsum.photos/id/288/3888/2592.jpg?hmac=pTLH4CMKuWqYGdf3jG4X_QBlbsiBuH7KOOnQsiijPks")
                    .into(fragmentProfileBinding.ivProfile)
            }

            fragmentProfileBinding.edName.setText(user.displayName)
            fragmentProfileBinding.edEmail.setText(user.email)

            if (user.isEmailVerified) {
                fragmentProfileBinding.icVerified.visibility = View.VISIBLE
                fragmentProfileBinding.ivVerified.visibility = View.VISIBLE
                fragmentProfileBinding.tvNoteVerified.visibility = View.VISIBLE
                fragmentProfileBinding.icUnverified.visibility = View.INVISIBLE
                fragmentProfileBinding.ivUnverified.visibility = View.INVISIBLE
                fragmentProfileBinding.tvNoteUnverified.visibility = View.INVISIBLE
            } else {
                fragmentProfileBinding.icUnverified.visibility = View.VISIBLE
                fragmentProfileBinding.ivUnverified.visibility = View.VISIBLE
                fragmentProfileBinding.tvNoteUnverified.visibility = View.VISIBLE
                fragmentProfileBinding.icVerified.visibility = View.INVISIBLE
                fragmentProfileBinding.ivVerified.visibility = View.INVISIBLE
                fragmentProfileBinding.tvNoteVerified.visibility = View.INVISIBLE

            }
        }

        fragmentProfileBinding.ivProfile.setOnClickListener {
            intentCamera()
        }

        fragmentProfileBinding.btnSave.setOnClickListener {
            val image = when {
                ::imageUri.isInitialized -> imageUri
                user?.photoUrl == null -> Uri.parse("https://i.picsum.photos/id/288/3888/2592.jpg?hmac=pTLH4CMKuWqYGdf3jG4X_QBlbsiBuH7KOOnQsiijPks")
                else -> user.photoUrl
            }

            val name = fragmentProfileBinding.edName.text.toString().trim()

            if (name.isEmpty()) {
                fragmentProfileBinding.edName.error = "Nama harus diisi"
                fragmentProfileBinding.edName.requestFocus()
                return@setOnClickListener
            }

            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(image)
                .build().also {
                    user?.updateProfile(it)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(activity, "Profile Updated", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
        }

        fragmentProfileBinding.icUnverified.setOnClickListener {
            user?.sendEmailVerification()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(activity, "Email verifikasi telah dikirim", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun intentCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            activity?.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, REQUEST_CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            val imgBitMap = data?.extras?.get("data") as Bitmap
            uploadImage(imgBitMap)
        }
    }

    private fun uploadImage(imgBitMap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val reference =
            FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")

        imgBitMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val image = byteArrayOutputStream.toByteArray()

        reference.putBytes(image)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    reference.downloadUrl.addOnCompleteListener {
                        it.result?.let {
                            imageUri = it
                            fragmentProfileBinding.ivProfile.setImageBitmap(imgBitMap)
                        }
                    }
                }
            }
    }
}