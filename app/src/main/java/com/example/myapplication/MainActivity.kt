package com.example.fitnessTracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessTracker.databinding.ActivityMainBinding
import com.example.fitnessTracker.data.WorkoutDatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: WorkoutDatabaseHelper
    private lateinit var adapter: WorkoutAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = WorkoutDatabaseHelper(this)

        adapter = WorkoutAdapter(emptyList()) { workout ->
            dbHelper.deleteWorkout(workout.id)
            refreshData()
        }

        recyclerView = binding.rvWorkouts
        recyclerView.adapter = adapter

        binding.btnLogWorkout.setOnClickListener {
            startActivity(Intent(this, LogWorkoutActivity::class.java))
        }

        refreshData()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        val workouts = dbHelper.getAllWorkouts()
        adapter.updateList(workouts)

        val totalWorkouts = workouts.size
        val totalCalories = workouts.sumOf { it.caloriesBurned }

        binding.tvTotalWorkouts.text = "Total Workouts: $totalWorkouts"
        binding.tvTotalCalories.text = "Total Calories Burned: $totalCalories kcal"
    }
}