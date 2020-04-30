package com.example.assignmentandroidserver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.assignmentandroidserver.network.API
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener(View.OnClickListener {
            hideError()
            showProgress()
            loginRequest(edtUserName.text.toString(),edtPassword.text.toString())
        })
        btnSignUp.setOnClickListener(View.OnClickListener {
            var i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
        })
    }

    private fun showProgress() {
        progressBarLayout.visibility = View.VISIBLE
    }

    private fun hideError() {
        tvErrorMsg.visibility = View.GONE
    }

    private fun loginRequest(username:String, password:String){
        API.service.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                hideProgress()
                if(result.size > 0){
                    Toast.makeText(this,"Đăng nhập thành công !", Toast.LENGTH_SHORT)
                    var i = Intent(this,MainActivity::class.java)
                    i.putExtra("username",result[0].username)
                    startActivity(i)
                }
                else{
                    showError("Error")
                }
            }, { ex ->
                showError(ex.toString())
            })
    }

    private fun hideProgress() {
        progressBarLayout.visibility = View.GONE
    }

    private fun showError(errorMsg: String) {
        tvErrorMsg.visibility = View.VISIBLE
        tvErrorMsg.text = errorMsg
    }
}
