package org.ktc2.cokaen.wouldyouin.feat_booking.repository

import org.ktc2.cokaen.wouldyouin.data.model.CurationSliceResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationResponse
import org.ktc2.cokaen.wouldyouin.data.model.ReservationSliceResponse
import org.ktc2.cokaen.wouldyouin.network.repository.EventAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.repository.ReservationAPIRetrofitRepository
import org.ktc2.cokaen.wouldyouin.network.repository.ServerCommonAPIRetrofitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationRepository @Inject constructor(
    private val reservationRepository: ReservationAPIRetrofitRepository
) {
    suspend fun getReservationList(
        page: Int = 0,
        size: Int = 10,
        lastId: Long = Long.MAX_VALUE
    ): ReservationSliceResponse {
        return reservationRepository.getReservationList(page, size, lastId)
    }

    suspend fun getReservation(reservationId : Long): ReservationResponse {
        return reservationRepository.getReservationDetails(reservationId)
    }
}