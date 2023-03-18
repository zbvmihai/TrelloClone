package com.zabava.trelloclone.firebase

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.zabava.trelloclone.activities.MainActivity
import com.zabava.trelloclone.activities.MyProfileActivity
import com.zabava.trelloclone.activities.SignInActivity
import com.zabava.trelloclone.activities.SignUpActivity
import com.zabava.trelloclone.models.User
import com.zabava.trelloclone.utils.Constants


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).set(
            userInfo, SetOptions.merge()
        ).addOnSuccessListener {
            activity.userRegisteredSuccess()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun loadUserData(activity: Activity) {
        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)

                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser!!)
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser!!)
                    }
                    is MyProfileActivity -> {
                        activity.setUserDataInUI(loggedInUser!!)
                    }
                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MyProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("SignInUser", "Error writing document", e)
            }
    }

    fun getCurrentUserId(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}