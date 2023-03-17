package com.zabava.trelloclone.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.zabava.trelloclone.activities.SignInActivity
import com.zabava.trelloclone.activities.SignUpActivity
import com.zabava.trelloclone.models.User
import com.zabava.trelloclone.utils.Constants


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).set(userInfo,
            SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
        }
    }

    fun signInUser(activity: SignInActivity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
            val loggedInUser = document.toObject(User::class.java)

                activity.signInSuccess(loggedInUser!!)
        }
    }

    fun getCurrentUserId():String{

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser!= null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}