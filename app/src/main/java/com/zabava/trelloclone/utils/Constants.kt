package com.zabava.trelloclone.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS: String = "Users"
    const val BOARDS: String = "Boards"

    const val ID: String = "id"
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val EMAIL: String = "email"
    const val MOBILE: String = "mobile"
    const val ASSIGNED_TO: String = "assignedTo"
    const val DOCUMENT_ID: String = "documentId"
    const val TASK_LIST: String = "taskList"
    const val BOARD_DETAIL: String = "board_detail"
    const val TASK_LIST_ITEM_POSITION: String = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION: String = "card_list_item_position"
    const val BOARD_MEMBERS_LIST: String = "board_members_list"
    const val SELECT: String = "Select"
    const val UN_SELECT: String = "UnSelect"
    const val TRELLOCLONE_PREFERENCES = "TrelloClone_Preferences"
    const val FCM_TOKEN: String = "fcmToken"
    const val FCM_TOKEN_UPDATED: String = "fcmTokenUpdated"

    const val FCM_BASE_URL: String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION: String = "authorization"
    const val FCM_KEY: String = "key"
    const val FCM_SERVER_KEY: String =
        "AAAAlTNz-1A:APA91bGG-WyQjKQCa1KKRND11Ei21UN-7zBFZMeGCXcGhvX5mOjzdjygCbthFgLR2nfouUCGTmzfdQ9WERtNdTsB2hEYz98WMSRHZHclt8lVAjqZSEp1IUDWjbxgBpw-rJq-JsMlfTyO"
    const val FCM_KEY_TITLE: String = "title"
    const val FCM_KEY_MESSAGE: String = "message"
    const val FCM_KEY_DATA: String = "data"
    const val FCM_KEY_TO: String = "to"

    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val MY_PROFILE_REQUEST_CODE = 11
    const val CREATE_BOARD_REQUEST_CODE = 12
    const val MEMBER_REQUEST_CODE = 13
    const val CARD_DETAIL_REQUEST_CODE = 14

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri))
    }
}