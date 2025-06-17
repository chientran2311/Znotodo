package com.example.znotodo

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private val items: MutableList<TodoItem>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit,
    private val onStatusChanged: (Int, Boolean) -> Unit, // New callback for status changes
    private val fragmentManager: androidx.fragment.app.FragmentManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_TODO = 1
    }

    // Track currently swiped item
    private var currentlySwipedPosition = -1

    // Create display list with headers
    private fun createDisplayList(): List<Any> {
        val displayList = mutableListOf<Any>()

        val activeItems = items.filter { !it.isCompleted }
        val completedItems = items.filter { it.isCompleted }

        // Add active items
        if (activeItems.isNotEmpty()) {
            displayList.add("Active")
            displayList.addAll(activeItems)
        }

        // Add completed items
        if (completedItems.isNotEmpty()) {
            displayList.add("Completed(${completedItems.size})")
            displayList.addAll(completedItems)
        }

        return displayList
    }

    // Get actual item position from adapter position
    private fun getActualPosition(adapterPosition: Int): Int {
        val displayList = createDisplayList()
        if (adapterPosition >= displayList.size) return -1

        val item = displayList[adapterPosition]
        if (item !is TodoItem) return -1

        return items.indexOf(item)
    }

    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.foreground)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
        val title: TextView = view.findViewById(R.id.todoTitle)
        val checkbox: CheckBox = view.findViewById(R.id.checkComplete)
        val foreground: View = view.findViewById(R.id.foreground)
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerText: TextView = view.findViewById(R.id.headerText)
    }

    override fun getItemViewType(position: Int): Int {
        val displayList = createDisplayList()
        return if (displayList[position] is String) TYPE_HEADER else TYPE_TODO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_TODO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_todo, parent, false)
                TodoViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val displayList = createDisplayList()
        val item = displayList[position]

        when (holder) {
            is HeaderViewHolder -> {
                holder.headerText.text = item as String
            }
            is TodoViewHolder -> {
                val todoItem = item as TodoItem
                val actualPosition = getActualPosition(position)

                // Reset checkbox listener
                holder.checkbox.setOnCheckedChangeListener(null)
                holder.checkbox.isChecked = todoItem.isCompleted

                // Update title strike-through and style
                if (todoItem.isCompleted) {
                    holder.title.paintFlags = holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    holder.title.alpha = 0.6f
                    holder.card.alpha = 0.8f
                } else {
                    holder.title.paintFlags = holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    holder.title.alpha = 1.0f
                    holder.card.alpha = 1.0f
                }

                // Set checkbox change listener
                holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
                    if (actualPosition != -1) {
                        todoItem.isCompleted = isChecked
                        onStatusChanged(actualPosition, isChecked)

                        // Update UI immediately
                        if (isChecked) {
                            holder.title.paintFlags = holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            holder.title.alpha = 0.6f
                            holder.card.alpha = 0.8f
                        } else {
                            holder.title.paintFlags = holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                            holder.title.alpha = 1.0f
                            holder.card.alpha = 1.0f
                        }
                    }
                }

                holder.title.text = todoItem.title

                // Reset swipe state logic
                if (position != currentlySwipedPosition) {
                    holder.foreground.clearAnimation()
                    holder.foreground.translationX = 0f
                    holder.foreground.translationZ = 0f
                    holder.btnEdit.visibility = View.GONE
                    holder.btnDelete.visibility = View.GONE
                } else {
                    // Keep swipe state for currently swiped item
                }

                holder.btnEdit.setOnClickListener {
                    if (position == currentlySwipedPosition && actualPosition != -1) {
                        closeSwipe(holder)
                        currentlySwipedPosition = -1
                        EditTaskSheetFragment().show(fragmentManager, "EditTaskSheet")
                    }
                }

                holder.btnDelete.setOnClickListener {
                    if (position == currentlySwipedPosition && actualPosition != -1) {
                        closeSwipe(holder)
                        currentlySwipedPosition = -1

                        val deleteDialog = DeleteConfirmationDialogFragment()
                        deleteDialog.setOnDeleteConfirmedListener {
                            onDelete(actualPosition)
                        }
                        deleteDialog.show(fragmentManager, "DeleteConfirmation")
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return createDisplayList().size
    }

    // Helper method to close swipe animation
    private fun closeSwipe(holder: TodoViewHolder) {
        holder.foreground.animate()
            .translationX(0f)
            .setDuration(200)
            .withEndAction {
                holder.foreground.translationZ = 0f
                holder.btnEdit.visibility = View.GONE
                holder.btnDelete.visibility = View.GONE
            }
            .start()
    }

    // Public method to close any currently swiped item
    fun closeCurrentSwipe(recyclerView: RecyclerView) {
        if (currentlySwipedPosition != -1) {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(currentlySwipedPosition)
            if (viewHolder is TodoViewHolder) {
                closeSwipe(viewHolder)
            }
            currentlySwipedPosition = -1
        }
    }

    // Method to update currently swiped position
    fun setCurrentlySwipedPosition(position: Int) {
        currentlySwipedPosition = position
    }

    fun getCurrentlySwipedPosition(): Int {
        return currentlySwipedPosition
    }

    // Method to get actual position from adapter position (for swipe handling)
    fun getActualPositionForSwipe(adapterPosition: Int): Int {
        return getActualPosition(adapterPosition)
    }

    // Method to refresh the entire list
    fun refreshList() {
        notifyDataSetChanged()
    }
}