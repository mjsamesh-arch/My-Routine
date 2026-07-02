package com.example.routinecheck

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object RoutineStore {

    private const val PREFS_NAME = "routine_prefs"
    private const val KEY_ROUTINES = "routines"
    private const val KEY_LAST_RESET_DATE = "last_reset_date"

    private fun todayString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun loadRoutines(context: Context): MutableList<Routine> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 날짜가 바뀌었으면 전부 체크 해제(초기화)
        val lastReset = prefs.getString(KEY_LAST_RESET_DATE, "")
        val today = todayString()
        val resetNeeded = lastReset != today

        val json = prefs.getString(KEY_ROUTINES, "[]") ?: "[]"
        val array = JSONArray(json)
        val list = mutableListOf<Routine>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                Routine(
                    id = obj.getLong("id"),
                    name = obj.getString("name"),
                    done = if (resetNeeded) false else obj.getBoolean("done")
                )
            )
        }

        if (resetNeeded) {
            saveRoutines(context, list)
            prefs.edit().putString(KEY_LAST_RESET_DATE, today).apply()
        }

        return list
    }

    fun saveRoutines(context: Context, list: List<Routine>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val array = JSONArray()
        for (r in list) {
            val obj = JSONObject()
            obj.put("id", r.id)
            obj.put("name", r.name)
            obj.put("done", r.done)
            array.put(obj)
        }
        prefs.edit().putString(KEY_ROUTINES, array.toString()).apply()
    }
}
