package com.example.miniproj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.miniproj.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var originalStudent: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalStudent = intent.getSerializableExtra("STUDENT_DATA") as? Student
        originalStudent?.let {
            binding.edtName.setText(it.name)
            binding.edtClass.setText(it.className)
            binding.edtGpa.setText(it.gpa.toString())
        }

        binding.btnSave.setOnClickListener {
            val newName = binding.edtName.text.toString().trim()
            val newClass = binding.edtClass.text.toString().trim()
            val newGpa = binding.edtGpa.text.toString().toDoubleOrNull()

            if (newName.isEmpty() || newClass.isEmpty() || newGpa == null || newGpa !in 0.0..4.0) {
                Toast.makeText(this, "Vui lòng nhập thông tin hợp lệ (GPA từ 0.0 đến 4.0)!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedStudent = originalStudent?.copy(
                name = newName,
                className = newClass,
                gpa = newGpa
            ) ?: return@setOnClickListener

            val resultIntent = Intent().apply {
                putExtra("UPDATED_STUDENT", updatedStudent)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}