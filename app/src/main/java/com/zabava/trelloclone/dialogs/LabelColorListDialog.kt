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
import com.zabava.trelloclone.adapters.LabelColorAdapter

abstract class LabelColorListDialog(
    context: Context,
    private var list: ArrayList<String>,
    private var title: String = "",
    private var mSelectedColor: String = "",

) : Dialog(context){

    private var adapter: LabelColorAdapter? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list,null)

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)
    }

    private fun setUpRecyclerView(view: View){
        view.findViewById<TextView>(R.id.tv_dialog_Title).text = title
        val colorRv = view.findViewById<RecyclerView>(R.id.rv_color_List)
        colorRv.layoutManager = LinearLayoutManager(context)
        adapter = LabelColorAdapter(context,list,mSelectedColor)
        colorRv.adapter = adapter

        adapter!!.onItemClickListener = object: LabelColorAdapter.OnItemClickListener{
            override fun onClick(position: Int, color: String) {
               dismiss()
                onItemSelected(color)
            }
        }
    }

    protected abstract fun onItemSelected(color: String)
}