package com.zabava.trelloclone.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.zabava.trelloclone.R
import com.zabava.trelloclone.adapters.CardMemberListItemsAdapter
import com.zabava.trelloclone.databinding.ActivityCardDetailsBinding
import com.zabava.trelloclone.dialogs.LabelColorListDialog
import com.zabava.trelloclone.dialogs.MembersListDialog
import com.zabava.trelloclone.firebase.FirestoreClass
import com.zabava.trelloclone.models.*
import com.zabava.trelloclone.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class CardDetailsActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1

    private var mSelectedColor = ""
    private var mSelectedViewDateMilliSeconds: Long = 0

    private lateinit var mMembersDetailList: ArrayList<User>

    private var binding: ActivityCardDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        getIntentData()
        setupActionBar()


        binding?.etNameCardDetails?.setText(
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name
        )
        binding?.etNameCardDetails?.setSelection(binding?.etNameCardDetails?.text?.toString()!!.length)

        mSelectedColor = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].labelColor
        if (mSelectedColor.isNotEmpty()) {
            setColor()
        }

        binding?.btnUpdateCardDetails?.setOnClickListener {
            if (binding?.etNameCardDetails?.text.toString().isNotEmpty())
                updateCardDetails()
            else
                Toast.makeText(this, "Please enter a card name!", Toast.LENGTH_SHORT).show()
        }
        binding?.tvSelectLabelColor?.setOnClickListener {
            labelColorsListDialog()
        }

        binding?.tvSelectMembers?.setOnClickListener {
            membersListDialog()
        }

        setupSelectedMembersList()

        mSelectedViewDateMilliSeconds =
            mBoardDetails.taskList[mTaskListPosition]
                .cards[mCardPosition].dueDate

        if (mSelectedViewDateMilliSeconds > 0) {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val selectedDate = simpleDateFormat.format(mSelectedViewDateMilliSeconds)
            binding?.tvSelectDueDate?.text = selectedDate
        }

        binding?.tvSelectDueDate?.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(binding?.toolbarCardDetailsActivity)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar?.title = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name

        binding?.toolbarCardDetailsActivity?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun colorsList(): ArrayList<String> {
        return arrayListOf(
            "#43C86F", "#0C90F1", "#F72400", "#7A8089", "#D57C1D", "#FBFF00", "#0022F8"
        )
    }

    private fun setColor() {

        binding?.tvSelectLabelColor?.text = ""
        binding?.tvSelectLabelColor?.setBackgroundColor(Color.parseColor(mSelectedColor))

    }

    private fun labelColorsListDialog() {

        val colorList: ArrayList<String> = colorsList()

        val listDialog = object : LabelColorListDialog(
            this,
            colorList,
            resources.getString(R.string.str_select_label_color),
            mSelectedColor
        ) {
            override fun onItemSelected(color: String) {
                mSelectedColor = color
                setColor()
            }
        }
        listDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_delete_card -> {
                alertDialogForDeleteCard(
                    mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun getIntentData() {

        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)) {
            mCardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)) {
            mTaskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.BOARD_MEMBERS_LIST)) {
            mMembersDetailList = intent.getParcelableArrayListExtra(Constants.BOARD_MEMBERS_LIST)!!
        }
    }

    fun addUpdateTaskListSuccess() {

        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun membersListDialog() {

        val cardAssignedMembersList =
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo

        if (cardAssignedMembersList.size > 0) {
            for (i in mMembersDetailList.indices) {
                for (j in cardAssignedMembersList) {
                    if (mMembersDetailList[i].id == j) {
                        mMembersDetailList[i].selected = true
                    }
                }
            }
        } else {
            for (i in mMembersDetailList.indices) {
                mMembersDetailList[i].selected = false
            }
        }

        val listDialog = object : MembersListDialog(
            this@CardDetailsActivity, mMembersDetailList,
            resources.getString(R.string.str_select_member)
        ) {
            override fun onItemSelected(user: User, action: String) {

                if (action == Constants.SELECT) {
                    if (!mBoardDetails
                            .taskList[mTaskListPosition]
                            .cards[mCardPosition]
                            .assignedTo.contains(user.id)
                    ) {
                        mBoardDetails
                            .taskList[mTaskListPosition]
                            .cards[mCardPosition]
                            .assignedTo.add(user.id)
                    }
                } else {
                    mBoardDetails
                        .taskList[mTaskListPosition]
                        .cards[mCardPosition]
                        .assignedTo.remove(user.id)

                    for (i in mMembersDetailList.indices) {
                        if (mMembersDetailList[i].id == user.id) {
                            mMembersDetailList[i].selected = false
                        }
                    }
                }
                setupSelectedMembersList()
            }
        }
        listDialog.show()
    }

    private fun updateCardDetails() {

        val card = Card(
            binding?.etNameCardDetails?.text.toString(),
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].createdBy,
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo,
            mSelectedColor, mSelectedViewDateMilliSeconds)

        val taskList: ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size - 1)

        mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition] = card

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@CardDetailsActivity, mBoardDetails)
    }

    private fun deleteCard() {

        val cardsList: ArrayList<Card> = mBoardDetails.taskList[mTaskListPosition].cards

        cardsList.removeAt(mCardPosition)

        val taskList: ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size - 1)

        taskList[mTaskListPosition].cards = cardsList

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@CardDetailsActivity, mBoardDetails)
    }

    private fun alertDialogForDeleteCard(cardName: String) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.alert))
        builder.setMessage(
            resources.getString(
                R.string.confirmation_message_to_delete_card,
                cardName
            )
        )
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            dialogInterface.dismiss()
            deleteCard()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun setupSelectedMembersList() {

        val cardAssignedMembersList =
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo

        val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()

        for (i in mMembersDetailList.indices) {
            for (j in cardAssignedMembersList) {
                if (mMembersDetailList[i].id == j) {
                    val selectedMember = SelectedMembers(
                        mMembersDetailList[i].id,
                        mMembersDetailList[i].image
                    )
                    selectedMembersList.add(selectedMember)
                }
            }
        }
        if (selectedMembersList.size > 0) {
            selectedMembersList.add(SelectedMembers("", ""))

            binding?.tvSelectMembers?.visibility = View.GONE
            binding?.rvSelectedMembersList?.visibility = View.VISIBLE

            binding?.rvSelectedMembersList?.layoutManager =
                GridLayoutManager(this@CardDetailsActivity, 6)

            val adapter =
                CardMemberListItemsAdapter(this@CardDetailsActivity, selectedMembersList, true)
            binding?.rvSelectedMembersList?.adapter = adapter
            adapter.setOnClickListener(
                object : CardMemberListItemsAdapter.OnClickListener {
                    override fun onClick() {
                        membersListDialog()
                    }
                }
            )
        } else {
            binding?.tvSelectMembers?.visibility = View.VISIBLE
            binding?.rvSelectedMembersList?.visibility = View.GONE
        }
    }

    private fun showDatePicker() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, { _, mYear, mMonth, mDay ->
            val sDayOfMonth = if (mDay < 10) "0$mDay" else "$mDay"
            val sMonthOfYear = if ((mMonth + 1) < 10) "0${mMonth + 1}" else "${mMonth + 1}"
            val selectedDate = "$sDayOfMonth/$sMonthOfYear/$mYear"
            binding?.tvSelectDueDate?.text = selectedDate

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val theDate = sdf.parse(selectedDate)
            mSelectedViewDateMilliSeconds = theDate!!.time
        }, year, month, day)
        dpd.show()
    }
}