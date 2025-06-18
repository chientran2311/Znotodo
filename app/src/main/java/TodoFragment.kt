package com.example.znotodo.fragment

import TodoMultiEditFragment
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znotodo.R
import com.example.znotodo.TodoAdapter
import com.example.znotodo.TodoItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.abs

class TodoFragment : Fragment(R.layout.todo_edit) {

    private val todoList = mutableListOf(
        TodoItem("To-do 1", "Description 1"),
        TodoItem("To-do 2", "Description 2")
    )
    private lateinit var adapter: TodoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tasksLeftTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val menuButton = view.findViewById<View>(R.id.menuButton)
        menuButton.setOnClickListener { anchor ->
            val popup = PopupMenu(requireContext(), anchor, Gravity.END, 0, R.style.PopupMenuStyle)

            popup.menuInflater.inflate(R.menu.todo_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        // Replace fragment bằng TodoMultiEditFragment
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, TodoMultiEditFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.todoRecyclerView)
        tasksLeftTextView = view.findViewById<TextView>(R.id.tasksLeft)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TodoAdapter(
            todoList,
            onEdit = { pos ->
                // Handle edit action
                adapter.refreshList()
            },
            onDelete = { pos ->
                todoList.removeAt(pos)
                adapter.refreshList()
                updateTasksLeftCounter()
                // Update swipe position tracking
                if (pos <= adapter.getCurrentlySwipedPosition()) {
                    adapter.setCurrentlySwipedPosition(adapter.getCurrentlySwipedPosition() - 1)
                }
            },
            onStatusChanged = { pos, isCompleted ->
                // Handle status change - refresh the entire list to reorganize sections
                adapter.refreshList()
                updateTasksLeftCounter()
            },
            fragmentManager = parentFragmentManager
        )
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    handleSwipeCompletion(viewHolder, position)
                }
            }

            override fun onChildDraw(
                c: android.graphics.Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Only allow swipe on todo items, not headers
                    if (adapter.getItemViewType(viewHolder.adapterPosition) != 1) {
                        return
                    }

                    val foreground = viewHolder.itemView.findViewById<View>(R.id.foreground)
                    val btnEdit = viewHolder.itemView.findViewById<View>(R.id.btnEdit)
                    val btnDelete = viewHolder.itemView.findViewById<View>(R.id.btnDelete)
                    val buttonWidth = 160 * foreground.resources.displayMetrics.density

                    val position = viewHolder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        // Close any other currently swiped item
                        if (adapter.getCurrentlySwipedPosition() != -1 &&
                            adapter.getCurrentlySwipedPosition() != position) {
                            adapter.closeCurrentSwipe(recyclerView)
                        }

                        // Handle left swipe (reveal buttons)
                        val clampedDX = when {
                            dX < 0 -> dX.coerceAtLeast(-buttonWidth).coerceAtMost(0f)
                            dX > 0 -> dX.coerceAtMost(buttonWidth).coerceAtLeast(0f)
                            else -> 0f
                        }

                        foreground.translationX = clampedDX
                        foreground.translationZ = if (clampedDX != 0f) -1f else 0f

                        // Show/hide buttons based on swipe progress
                        val swipeProgress = kotlin.math.abs(clampedDX) / buttonWidth
                        if (swipeProgress > 0.1f) {
                            btnEdit.visibility = View.VISIBLE
                            btnDelete.visibility = View.VISIBLE
                            adapter.setCurrentlySwipedPosition(position)
                        } else {
                            btnEdit.visibility = View.INVISIBLE
                            btnDelete.visibility = View.INVISIBLE
                            if (adapter.getCurrentlySwipedPosition() == position) {
                                adapter.setCurrentlySwipedPosition(-1)
                            }
                        }
                    }
                }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.3f
            }

            override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
                return defaultValue * 2
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            adapter.closeCurrentSwipe(recyclerView)
            val sheet = com.example.znotodo.NewTaskSheetFragment()
            sheet.show(parentFragmentManager, "NewTaskSheet")
        }

        // Add click listener to close swipe when clicking elsewhere
        recyclerView.setOnTouchListener { _, _ ->
            adapter.closeCurrentSwipe(recyclerView)
            false
        }

        // Initialize tasks counter
        updateTasksLeftCounter()
    }

    private fun handleSwipeCompletion(viewHolder: RecyclerView.ViewHolder, position: Int) {
        // Only handle swipe for todo items
        if (adapter.getItemViewType(position) != 1) {
            return
        }

        val foreground = viewHolder.itemView.findViewById<View>(R.id.foreground)
        val btnEdit = viewHolder.itemView.findViewById<View>(R.id.btnEdit)
        val btnDelete = viewHolder.itemView.findViewById<View>(R.id.btnDelete)
        val buttonWidth = 160 * foreground.resources.displayMetrics.density
        val currentTranslation = foreground.translationX

        // Determine whether to snap to open or closed position
        val shouldStayOpen = abs(currentTranslation) > buttonWidth * 0.5f

        if (shouldStayOpen) {
            // Snap to fully open position
            val targetX = if (currentTranslation < 0) -buttonWidth else buttonWidth
            foreground.animate()
                .translationX(targetX)
                .setDuration(150)
                .withEndAction {
                    btnEdit.visibility = View.VISIBLE
                    btnDelete.visibility = View.VISIBLE
                }
                .start()
            adapter.setCurrentlySwipedPosition(position)
        } else {
            // Snap back to closed position
            foreground.animate()
                .translationX(0f)
                .setDuration(150)
                .withEndAction {
                    foreground.translationZ = 0f
                    btnEdit.visibility = View.GONE
                    btnDelete.visibility = View.GONE
                }
                .start()
            adapter.setCurrentlySwipedPosition(-1)
        }
        foreground.isClickable = false
        foreground.isFocusable = false
        btnEdit.translationZ = 10f
        btnDelete.translationZ = 10f
        recyclerView.post {
            adapter.notifyItemChanged(position)
        }

        // Don't notify item changed here to avoid conflicts with section reorganization
    }

    private fun updateTasksLeftCounter() {
        val activeTasks = todoList.count { !it.isCompleted }
        tasksLeftTextView.text = if (activeTasks == 1) {
            "$activeTasks task left"
        } else {
            "$activeTasks tasks left"
        }
    }
}