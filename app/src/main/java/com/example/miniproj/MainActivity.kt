package com.example.miniproj

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.miniproj.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "TAG_LIFECYCLE"

    private var currentStudent = Student(
        id = "2415053122129",
        name = "Nguyễn Minh Nghĩa",
        className = "126TLTTD02",
        email = "2415053122129@sv.ute.udn.vn",
        gpa = 2.89
    )

    private val editProfileLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updated = result.data?.getSerializableExtra("UPDATED_STUDENT") as? Student
            updated?.let {
                currentStudent = it
                bindStudentData(currentStudent)
                Toast.makeText(this, "Đã cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val galleryLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.imgAvatar.setImageURI(it)
            Toast.makeText(this, "Đã đổi ảnh đại diện!", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraPermissionLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Đã cấp quyền Camera thành công!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Bạn đã từ chối quyền Camera!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: Activity đang được khởi tạo")
        bindStudentData(currentStudent)

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java).apply {
                putExtra("STUDENT_DATA", currentStudent)
            }
            editProfileLauncher.launch(intent)
        }

        binding.btnChangeAvatar.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnCallHotline.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:0905123456")
            }
            startActivity(dialIntent)
        }

        binding.btnRequestCamera.setOnClickListener {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun bindStudentData(student: Student) {
        binding.tvName.text = student.name
        binding.tvDetails.text = "MSSV: ${student.id} - Lớp: ${student.className}"
        binding.tvGpaBadge.text = "GPA: ${student.gpa}"
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity hiển thị trên màn hình")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Activity sẵn sàng tương tác")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity bị che khuất một phần")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity ẩn hoàn toàn")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Activity được mở lại từ trạng thái Stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity bị hủy khỏi bộ nhớ")
    }
}