package com.mistry.mithun.autosilent

import android.content.Context
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by Mithun on 7/23/2017.
 */
class ValidationKotlin constructor(private val context: Context){

    fun check_from_to(from:String?, to:String?) : Boolean {
        if (!from.isNullOrBlank() && !to.isNullOrBlank()) {
            val regex = Regex(pattern = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")
            if (regex.matches(from!!) && regex.matches(to!!)) {
                val sdf = SimpleDateFormat("hh:mm")
                try {
                    val time_from = sdf.parse(from)
                    val time_to = sdf.parse(to)
                    if (time_from.before(time_to)) {
                        return true
                    } else {
                        Toast.makeText(this.context, "From time cannot be greater than To time.", Toast.LENGTH_SHORT).show()
                        return false
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            else{
                Toast.makeText(this.context, "Please select proper time", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        Toast.makeText(this.context, "From or To time cannot be null for selected day.", Toast.LENGTH_SHORT).show()
        return false
    }
}
