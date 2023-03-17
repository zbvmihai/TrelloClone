package com.zabava.trelloclone.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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

    private fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}