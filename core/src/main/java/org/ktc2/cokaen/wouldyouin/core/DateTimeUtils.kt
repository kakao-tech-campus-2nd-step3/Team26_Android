package org.ktc2.cokaen.wouldyouin.core

object DateTimeUtils {
    fun formatDateTimeString(dateTimeList: List<String>): String {
        require(dateTimeList.size == 5) { "List must contain exactly 5 elements: year, month, day, hour, minute" }

        val year = dateTimeList[0].padStart(4, '0')
        val month = dateTimeList[1].padStart(2, '0')
        val day = dateTimeList[2].padStart(2, '0')
        val hour = dateTimeList[3].toInt()
        val minute = dateTimeList[4].padStart(2, '0')

        val amPm = if (hour < 12) "AM" else "PM"
        val formattedHour = when {
            hour == 0 -> "12"
            hour > 12 -> (hour - 12).toString().padStart(2, '0')
            else -> hour.toString().padStart(2, '0')
        }

        return "$year.$month.$day, $amPm $formattedHour:$minute"
    }
}