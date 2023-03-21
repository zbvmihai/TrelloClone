package com.zabava.trelloclone.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zabava.trelloclone.R
import com.zabava.trelloclone.adapters.MembersAdapter
import com.zabava.trelloclone.models.User

abstract class MembersListDialog(
    context: Context,
    private var list: ArrayList<User>,
    private val title: String = ""
) : Dialog(context) {

    private var adapter: MembersAdapter? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)
    }

    @SuppressLint("CutPasteId")
    private fun setUpRecyclerView(view: View) {
        view.findViewById<TextView>(R.id.tv_dialog_Title).text = title

        if (list.size > 0) {

            view.findViewById<RecyclerView>(R.id.rv_List).layoutManager =
                LinearLayoutManager(context)
            adapter = MembersAdapter(context, list)
            view.findViewById<RecyclerView>(R.id.rv_List).adapter = adapter

            adapter!!.setOnClickListener(object :
                MembersAdapter.OnClickListener {
                override fun onClick(position: Int, user: User, action: String) {
                    dismiss()
                    onItemSelected(user, action)
                }
            })
        }
    }

    protected abstract fun onItemSelected(user: User, action: String)
}