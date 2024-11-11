package org.ktc2.cokaen.wouldyouin.data.entities

import android.os.Parcelable
import androidx.room.*
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import org.ktc2.cokaen.wouldyouin.data.model.Area
import org.ktc2.cokaen.wouldyouin.data.model.CurationCuratorResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationEventResponse
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.data.model.LocalCurationCard
import org.ktc2.cokaen.wouldyouin.data.model.Location

@Entity(tableName = "curation")
@Parcelize
data class CurationEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val content: String,
    val modifiedDate: String,
    val createdTime: String,
    val curator: String, // JSON String for CurationCuratorResponse
    val curationCards: String, // JSON String for List<LocalCurationCard>
    val area: String, // JSON String for Area
    val hashTag: String, // JSON String for List<String>
    val eventsInfo: String // JSON String for List<CurationEventResponse>
): Parcelable