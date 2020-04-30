package com.example.assignmentandroidserver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.assignmentandroidserver.network.API
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btnSignUp.setOnClickListener(View.OnClickListener {
            hideError()
            if(edtPassword.text.toString()!=edtPasswordRepeat.text.toString()){
                showError("Không khớp !")
            }
            else{
                signUpRequest(edtUserName.text.toString(),edtPassword.text.toString())
            }
        })
    }

    private fun signUpRequest(username: String, password: String) {
        API.service.signUp(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                    Toast.makeText(this,"Đăng kí thành công !",Toast.LENGTH_SHORT)
                    var i = Intent(this,LoginActivity::class.java)
                    startActivity(i)
            }, { ex ->
                showError(ex.toString())
            })
    }

    private fun hideError() {
        tvErrorMsg.visibility = View.GONE
    }

    private fun showError(errorMsg: String) {
        tvErrorMsg.visibility = View.VISIBLE
        tvErrorMsg.text = errorMsg
    }
}
